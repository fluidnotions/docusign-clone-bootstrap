global.$ = global.jQuery = require("jquery");
require('jsrender');

var moment = require("moment");
var Promise = require("bluebird");
var _ = require('lodash');

var debug = true;

// var testOptions = {
//     inputFieldArray: [{
//         name: "firstName",
//         type: "text"
//     }, {
//         name: "lastName",
//         type: "text"
//     }, {
//         name: "email",
//         type: "email"
//     }, {
//         name: "userLoginId",
//         type: "text"
//     }],
//     formId: "addUserForm",
//     formButtonId: "addUserToTenantAccBtn",
//     formButtonName: "Add",
//     formButtonClasses: "btn btn-xl btn-primary pull-right",
//     lookupUrl: "/docusign-component/control/addDocusignUserSuggestFillForm",
//     targetSelector: "formWrapper",
//     templatesFolderPath: "/docusign/assets/main/templates/",
//     otherFormGroupTypesHtml: '<div class="form-group"><div class="col-sm-4"></div><div class="col-sm-8"><label><input class="cb" name="closeDocusignUser" type="checkbox"> close DocuSign User Profile</label></div></div><div class="form-group"><div class="col-sm-4"></div><div class="col-sm-8"><label><input class="cb" name="voidInProgressEnvelopes" type="checkbox"> Void users in-progress envelopes</label></div></div>'
//
//     ------------ NOT YET IMPLEMENTED BACKEND FOR NOW NEED CUSTOM BACKEND FOR EVERY USE -------------
//     tableName: "UserLoginPersonNameAndEmail",
//     whereConditionArray: [{
//         left: "contactMechPurposeTypeId",
//         operator: "EQUALS",
//         right: "PRIMARY_EMAIL",
//         type: "and"
//     }],

// }
module.exports = function FormBuilderPlus() {
    var
        ajaxPost = function ajaxPost(url, data) {
            return new Promise(function(resolve, reject) {
                $.ajax({
                    contentType: "application/x-www-form-urlencoded",
                    dataType: "json",
                    type: "POST",
                    url: url,
                    data: data,
                    success: function(resp, textStatus, jqXHR) {
                        resolve(resp);
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        reject(errorThrown);
                    }
                });
            });
        },
        TemplateUtil = function TemplateUtil(templateFolderPath, selector) {

            var
                getBPPath = function getBPPath(name) {
                    return templateFolderPath + name + '.bp.html';
                },
                getBPFileAsStr = function getBPFileAsStr(name) {
                    var file = getBPPath(name);
                    return Promise.resolve($.get(file))
                        .then(function(formTemplateStr) {
                            return formTemplateStr;
                        });

                },
                renderTemplate = function renderTemplate(formTemplateStr, item) {

                    return new Promise(function(resolve) {

                        $.templates("myTmpl", formTemplateStr);
                        var html = $.render.myTmpl(item.data)
                        if (item.selector) {
                            selector = item.selector;
                        }
                        $(selector).html(html);
                        resolve(html);

                    });
                }

            $.views.converters("dateformat", function(val) {
                //2015-09-02T12:25:33.9500000Z
                return moment(parseInt(val)).format("dddd, h:mm:ss a");
            });

            return {
                renderTemplate: renderTemplate,
                getBPFileAsStr: getBPFileAsStr
            }
        },
        renderDoubleOrderTemplate = function renderDoubleOrderTemplate(secondOrderTemplate, options) {
            $.templates("secondOrderTemplate", secondOrderTemplate);
            var firstOrderTemplateMarkup = $.render.secondOrderTemplate(options);

            //remove new lines before meta template replace
            firstOrderTemplateMarkup = firstOrderTemplateMarkup.replace(/[ \t]*(\r\n|\n|\r)/g, "");
            firstOrderTemplateMarkup = firstOrderTemplateMarkup.replace("  ", " ");

            var firstOrderTemplateString = "";
            for (var i = 0; i < firstOrderTemplateMarkup.length; i = i + 1) {
                var replace = false;
                var char = firstOrderTemplateMarkup.charAt(i);
                if (char === '[' || char === ']') {
                    if (char === '[') {
                        firstOrderTemplateString = firstOrderTemplateString.concat('{{');
                        replace = true;
                    }
                    if (char === ']') {
                        firstOrderTemplateString = firstOrderTemplateString.concat('}}');
                        replace = true;
                    }

                    var nextchar = firstOrderTemplateMarkup.charAt(i + 1);
                    if (nextchar === '@' || nextchar === '|') {

                        if (nextchar === '@') {
                            firstOrderTemplateString = firstOrderTemplateString.concat(':');
                            replace = true;
                        }
                        if (nextchar === '|') {
                            firstOrderTemplateString = firstOrderTemplateString.concat('/');
                            replace = true;
                        }
                        i = i + 1; //skip to next
                    }
                }
                if (replace === false) firstOrderTemplateString = firstOrderTemplateString.concat(char);
            }

            return firstOrderTemplateString
        },
        autocompleteFormBuilder = function autocompleteFormBuilder(options) {

            _.forEach(options.inputFieldArray, function(value) {
                value.fieldDisplyLabel = _.startCase(value.name);
            });
            var url = options.lookupUrl; // "/docusign-component/control/addDocusignUserSuggestFillForm",
            var targetSelector = options.targetSelector;
            var tmpls = TemplateUtil(options.templatesFolderPath, options.targetSelector);
            var formTemplateStr = null;
            var potentialDocuSignUsers = [];
            var currentInput = {};

            tmpls.getBPFileAsStr("formBuilder")
                .then(function(secondOrderTemplate) {
                    if (debug) console.log("secondOrderTemplate str: " + secondOrderTemplate);
                    formTemplateStr = renderDoubleOrderTemplate(secondOrderTemplate, options);
                    if (debug) console.log("formTemplateStr (normal template made from blue print): " + formTemplateStr);

                    return tmpls.renderTemplate(formTemplateStr, {
                        data: {
                            currentInput: currentInput,
                            suggestions: potentialDocuSignUsers
                        }
                    });
                }).then(function() {
                    //create angular2 component - dynamically create the form as well - oss!~@
                    //FIXME not working causes page refresh still with prevent
                    // $('#clearBtn').click(function(event) {
                    //    event.preventDefault();
                    //   _.forEach(options.inputFieldArray, function(value) {
                    //       $('#'+value.name+'-input').val('').att('disable', false);
                    //   });
                    // });


                    $('.adduser-input').keyup(function(event) {

                        //also the id of element so we can extract val() for searchTerm
                        var elId = '#' + event.currentTarget.id;
                        var searchTerm = $(elId).val();
                        console.log("searchTerm: " + searchTerm);
                        //need to get name of elemant used as fieldName
                        var fieldName = event.currentTarget.name;
                        $("#" + fieldName + "-input-sugggest").hide();
                        if (searchTerm.length < 3) return;
                        console.log("addDocusignUserSuggestFillForm: fieldName: " + fieldName + ", searchTerm: " + searchTerm);
                        ajaxPost(url, {
                                "fieldName": fieldName,
                                "searchTerm": searchTerm
                            })
                            .then(function(data) {
                                console.log("lookup objects " + JSON.stringify(data));
                                potentialDocuSignUsers = data;
                                currentInput[fieldName] = searchTerm;
                                return tmpls.renderTemplate(formTemplateStr, {
                                    data: {
                                        currentInput: currentInput,
                                        suggestions: potentialDocuSignUsers
                                    }
                                });
                            }).then(function() {

                                $("#" + fieldName + "-input-sugggest").show();
                                //Bind click event to list elements in results
                                $('.suggestion').click(function(e) {
                                    e.preventDefault();
                                    var arrObjIndex = e.currentTarget.id.split('-')[1];
                                    if (debug) console.log("slected index: " + arrObjIndex);
                                    var selObject = potentialDocuSignUsers[arrObjIndex];
                                    if (debug) console.log("selected object: " + JSON.stringify(selObject));
                                    $(elId).val($(this).text());
                                    _.forEach(options.inputFieldArray, function(value) {
                                        if (value.name !== fieldName) {
                                            var autoFillValue = selObject[value.name];
                                            $('#' + value.name + '-input').val(autoFillValue);
                                        }
                                    });
                                    $("#" + fieldName + "-input-sugggest").hide();

                                });


                            });
                    });
                });
        },
        buildInputFieldArrayFromJsonEntityArrResponse = function buildInputFieldArrayFromJsonEntityArrResponse() {
            var excludeFieldsList = options.excludeFieldsList,
                includeFieldsList = options.includeFieldsList,
                fieldInputTypes = options.fieldInputTypes,
                url = options.entityPropsUrl || '/docusign-component/control/entityStructure'

                if(excludeFieldsList && includeFieldsList){
                  alert("ERROR: either excludeFieldsList or includeFieldsList, can be given not both!");
                }

            ajaxPost(url, {
                    "entityName": options.entityName
                })
                .then(function(object) {
                    var inputFieldPropertyObj = null;
                    //just a json object random values is returned
                    //  {
                    //   "partyId": "28930",
                    //   "partyTypeId": "PERSON",
                    //   "userLoginId": "10170",
                    //   "lastName": "Robinson",
                    //   "firstName": "Justin",
                    //   "contactMechPurposeTypeId": "PRIMARY_EMAIL",
                    //   "purposeFromDate": "Oct 4, 2016 4:40:33 AM",
                    //   "contactMechId": "72392",
                    //   "email": "justin@fluidnotions.com",
                    //   "contactMechTypeId": "EMAIL_ADDRESS"
                    // }
                    if (excludeFieldsList && excludeFieldsList.length>0) { //options are either or not both
                        inputFieldPropertyObj = _.omit(object, excludeFieldsList);
                    } else if (includeFieldsList && includeFieldsList.length>0) {
                        inputFieldPropertyObj = _.pick(object, includeFieldsList);
                    }
                    //     inputFieldArray: [{
                    //         name: "firstName",
                    //         type: "text"
                    //     }, {
                    //         name: "lastName",
                    //         type: "text"
                    //     }, {
                    //         name: "email",
                    //         type: "email"
                    //     }, {
                    //         name: "userLoginId",
                    //         type: "text"
                    //     }]
                    var inputFieldArray = [];
                    _.forOwn(inputFieldPropertyObj, function(value, key) {
                        var type = (fieldInputTypes && fieldInputTypes.length>0)?(_.hasIn(fieldInputTypes, key)?fieldInputTypes[key]:"text"):"text";
                        inputFieldArray.push({"name": key, "type": type});
                    });
                    return inputFieldArray;

                });
        }

    return {
        build: autocompleteFormBuilder
    }

}
