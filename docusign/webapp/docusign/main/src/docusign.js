var utils = require('./utils');
require('./noConflictEditedDeps/bootstrap');
var eModal = require('./eModal-hacked')();
var JSON2 = require('JSON2');
var $ = global.ds$ || alert("FATAL: global.ds$ is null!");
var domSetup = utils.DomSetup();
var moment = require("moment");
var Docusign = function Docusign(options) {
    var publicInterface;
    var depBasePath = "/main/";
    var targetDiv = options.targetDiv || "#docusign";
    var iframeTargetDiv = options.iframeTargetDiv || $(targetDiv).parent().parent();
    var spinnerTargetDiv = "#spinner";
    var loginUrlKey = options.loginUrlKey || alert("ERROR: loginUrlKey missing");
    var userLoginId = options.userLoginId;
    //var loginUrlKey = options.loginUrlKey || '111111';
    var debugging = options.debug || false;
    if (debugging === 'true') {
        debugging = true;
    }
    var modeAdmin = options.modeAdmin || false;
    if (modeAdmin === 'true') {
        modeAdmin = true;
    }
    var ajx = utils.AjaxWrap();
    var gwl = utils.QuickGrowl();
    var spin = utils.Spinner();
    var tenantKey = null;
    var name = null;
    var userEmail = null;
    var afterSendRedirectUrl = "/docusign/afterSentIFrameClose.html";
    var embeddedAltWrapperTargetId = null;
    var mode = null;
    //identity is a activated docusign user which is originally gotten in init() with call to getDocuSignUserEmail
    //renamed to getDocuSignUserObject
    var docuSignOnBehalfOfUser = null;
    var authenicatingUser;
    var hostBaseUrl = null;
    var isDocSUser = true;
    var iframeModalClose = null;
    var signAndSendNewEnvelopeId = null;
    var tmpls = utils.TemplateUtils({
        mountpoint: "/docusign/",
        temaplatePath: "main/templates/"
    });
    var getEndPoint = function getEndPoint(base) {
            return "/docusign/";
        },
        checkUid = function checkUid(debug) {
            return ajx.ajaxPost("/docusign-component/control/lookupUidInfo", {
                loginUrlKey: loginUrlKey,
                userLoginId: userLoginId
            }).then(function(uid) {
                if (!uid.tenantId) {
                    gwl.grrr({
                        message: "<b>Misconfigured!</b><br/>No uid found in local storage.",
                        type: 'danger',
                        displaySec: 15
                    });
                } else {
                    tenantKey = uid.tenantId;
                    userLoginId = uid.userLoginId;
                    name = uid.name;
                    userEmail = uid.email;
                    return true;
                }
                if (!tenantKey || !userLoginId) {
                    if (!tenantKey) {
                        gwl.grrr({
                            message: "<b>Misconfigured!</b><br/>No tenantKey was in found, required by the script! Try logging in again.",
                            type: 'danger',
                            displaySec: 15
                        });
                    }
                    if (!userLoginId) {
                        gwl.grrr({
                            message: "<b>Misconfigured!</b><br/>No userLoginId was found, required by the script! Try logging in again.",
                            type: 'danger',
                            displaySec: 15
                        });
                    }
                    return false;
                }
            });
        },
        getGUID = function getGUID() {
            function s4() {
                return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
            }
            return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
        },
        init = function init() {
            $("#main_body_section").css("height:800px");
            spin.start();
            checkUid().then(function() {
                return ajx.ajaxPost(getEndPoint() + "getDocuSignUser", {
                    userLoginId: userLoginId,
                    tenantKey: tenantKey
                });
            }).then(function(userData) {
                console.log("init getDocuSignUser response: " + JSON.stringify(userData, null, 2));
                var docuSignUsersAuthAndAssociated = JSON.parse(userData.otherJson);
                //FIXME name is not present server side issue
                authenicatingUser = docuSignUsersAuthAndAssociated.authenticatingUser;
                if (userData.status === "success") {
                    spin.stop();
                    gwl.grrr({
                        msg: "Got DocuSignUser for login, ready to use DocuSign.",
                        type: "success",
                        displaySec: 3
                    });
                    docuSignOnBehalfOfUser = docuSignUsersAuthAndAssociated.docuSignUser;
                } else {
                    spin.stop();
                    gwl.grrr({
                        msg: "No DocuSignUser found for currently logged in user.",
                        type: "danger",
                        displaySec: 3
                    });
                    isDocSUser = false;
                }
                return Promise.resolve();
            }).then(function() {
                setupIframeMessageEventListener();
                if (modeAdmin !== true) {
                    tmpls.renderExtTemplate({
                        name: "signHere",
                        selector: "docusign",
                        data: {
                            debugging: debugging
                        }
                    }).then(function() {
                        //Examples: ViewInvoice.ftl
                        // <a data-dynamicDocUrl="<@ofbizUrl>invoice.pdf?invoiceId=${invoice.invoiceId}&amp;reportId=FININVOICE&amp;reportType=application/pdf</@ofbizUrl>" data-mime="application/pdf" data-title="invoice-${invoice.invoiceId}"  data-emailSubject="Please sign asap!" data-mode="signAndSend" data-emailBody="Please complete tags and sign the document" class="signhere subMenuButton" target="_blank">Sign And Send</a>
                        $('.signhere').click(function(event) {
                            event.preventDefault();
                            if (!docuSignOnBehalfOfUser) {
                                gwl.grrr({
                                    title: "<b>Unable To Perform Action</b><br/>",
                                    msg: "No DocuSignUser found for currently logged in user.",
                                    type: "danger"
                                });
                                return;
                            }
                            var mydata = $(event.target).closest('a').data();
                            signhereAction(mydata);
                        });
                    });
                } else {
                    tmpls.renderExtTemplate({
                        name: "adminDashboard",
                        selector: "docusign",
                        data: {
                            debugging: debugging
                        }
                    }).then(function() {
                        return showAddUserToTenantAccForm();
                    }).then(function() {
                        $(".nav").click(function(e) {
                            var show = e.target.dataset.show;
                            $(".nav-pills > li.active").removeClass("active");
                            $(e.target.parentElement).addClass("active");
                            console.log("show: " + show);
                            publicInterface[show]();
                        });
                        setTimeout(function() {
                            $("#ds-ui-wrapper").css("display", "block");
                        }, 2000);
                    });
                }
            });
        },
        signhereAction = function signhereAction(mydata) {
            //  <a 
            //     data-title="invoice-00000" 
            //     data-emailSubject="invoice due 00/00/00" 
            //     data-emailBody="bla bla bla"
            //     data-dynamicDocUrl="../financials/control/invoice.pdf?invoiceId=15120&reportId=FININVOICE&reportType=application/pdf" 
            //     data-mime="application/pdf"
            //     data-mode="signsend" 
            //     class="signhere btn btn-default pull-left">
            //         Customize And Send
            // </a>
            mode = mydata.mode;
            var dynamicDocUrl = null;
            //if there is no dynamicDocUrl then there must be a dynamicDocFormName else we throw an error
            if (!mydata.dynamicdocurl) {
                dynamicDocUrl = formInputToUrlWithQueryStr(mydata.dynamicdocformname, mydata.ofbizurlprefix);
            } else {
                dynamicDocUrl = mydata.dynamicdocurl;
            }
            if (dynamicDocUrl) {
                spin.start();
                if (mode && mode === "custom") {
                    setupDocumentSendEmbeddedView(mydata.title, mydata.emailsubject, mydata.emailbody, dynamicDocUrl, mydata.mime);
                } else {
                    mode = "sns";
                    showSignAndSendModal(mydata.title, mydata.emailsubject, mydata.emailbody, dynamicDocUrl, mydata.mime);
                }
            }
        },
        showOtherTests = function showOtherTests() {
            return tmpls.renderExtTemplate({
                name: 'otherTests',
                selector: targetDiv
            }).then(function() {
                //testing block
                $('#otherTestsPanel').on('click', '#test1', function(e) {
                    e.preventDefault();
                    setupEmbeddedView($('#snsUrl').val(), "Sign And Send");
                });
                $('#otherTestsPanel').on('click', '#test2', function(e) {
                    e.preventDefault();
                    setupEmbeddedView($('#customizeUrl').val(), "Customize And Send");
                });
            });
        },
        setupIframeMessageEventListener = function setupIframeMessageEventListener() {
            // Create IE + others compatible event handler
            var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
            var eventer = window[eventMethod];
            var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";
            // Listen to message from child window
            eventer(messageEvent, function(e) {
                var payload = e.data;
                if (debugging) console.log("window msg: " + JSON.stringify(payload));
                if (payload && payload.type && payload.type === "docusignAfterSend") {
                    if (payload.envelopeId) {
                        showEnvelopeSummary(payload.envelopeId);
                    } else {
                        //use closure var signAndSendNewEnvelopeId stored in startSignAndSend
                        if (signAndSendNewEnvelopeId) {
                            showEnvelopeSummary(signAndSendNewEnvelopeId);
                        } else {
                            gwl.grrr({
                                msg: "Error: After send event without envelope id available",
                                type: "danger",
                                displaySec: 3
                            });
                        }
                    }
                }
                // if (isEnvelopeId === true) {
                //     if (debugging) console.log('parent received message! envelopeId:  ', envelopeId);
                //     showEnvelopeSummary(envelopeId);
                // } else {
                //     if (debugging) console.log('message from parent frame json:  ', JSON2.stringify(payload));
                //     signhereAction(payload);
                // }
            }, false);
            // PYM plugin uses this therefore we would need to be very careful with the conditional logic to avoid interference
        },
        showEnvelopeSummary = function showEnvelopeSummary(envelopeId) {
            if (debugging) {
                gwl.grrr({
                    msg: "Showing envelope summary for " + envelopeId,
                    type: "success",
                    displaySec: 3
                });
            }
            spin.stop();
            return ajx.ajaxGet(getEndPoint() + "getEnvelopeSummary", {
                userLoginId: docuSignOnBehalfOfUser.userLoginId,
                docuSignUserEmail: docuSignOnBehalfOfUser.email,
                tenantKey: tenantKey,
                envelopeId: envelopeId
            }).then(function(data) {
                if (debugging) console.log("response: " + JSON2.stringify(data, null, 2));
                //use moment here to transform date strings since jsrender via jquery seems not to work
                data.envelopeInformation.sentDateTime = transformDateString(data.envelopeInformation.sentDateTime);
                return tmpls.renderExtTemplateStr({
                    name: 'envelopeSummary',
                    data: data
                });
            }).then(function(envelopeSummaryHtml) {
                eModal.alert({
                    message: envelopeSummaryHtml,
                    buttons: false
                }, "New envelope Sent");
                return Promise.resolve();
            }).then(function() {
                setTimeout(function() {
                    finishSigningSession();
                }, 3500);
            }).then(function() {})
            if (debugging) console.log("showEnvelopeSummary called!");
        },
        transformDateString = function transformDateString(val){
            return moment(parseInt(val)).format("dddd, h:mm:ss a");
        },
        startSignAndSend = function startSignAndSend(argsObj) {
            //NOTE: can't set  "afterSendRedirectUrl": afterSendRedirectUrl,
            //because we are creating in sent status so the callback happens before the window opens
            //which is why we need to rely on alt mechanism
            //however now the auto pos tags are not showing in this view check docs and forum
            //what status and if callback has any other details on it there was something in the docs 
            //about this
            var request = {
                    "tenantKey": tenantKey,
                    "dynamicDocUrl": argsObj.docUrlOrPath,
                    "title": argsObj.docName,
                    "emailSubject": argsObj.emailSubject,
                    "emailblurb": argsObj.emailBody,
                    "afterSendRedirectUrl": afterSendRedirectUrl,
                    "docuSignUserEmail": docuSignOnBehalfOfUser.email,
                };
               //var RecipientModels = [];
            // Must set |clientUserId| for embedded recipients and provide the same
            // value when requesting the recipient view URL
            // therefore |clientUserId| must be set for sender, left blank for
            // Recipient
            //"signerEmail", "signerName", "recipientId", "routingOrder", "clientUserId"
            var sname = $("#senderSignUserName").val();
            var semail = $("#senderSignUserEmail").val();
            var guid = getGUID();
            var sender = {
                signerName: sname,
                signerEmail: semail,
                routingOrder: "1",
                clientUserId: guid,
                recipientId: "1",
                embeddedSigner: true
            }
            //RecipientModels.push(sender);
            request.firstRecipient = JSON2.stringify(sender);
            var rname = $("#recipientSignUserName").val();
            var remail = $("#recipientSignUserEmail").val();
            var recipient = {
                signerName: rname,
                signerEmail: remail,
                routingOrder: "2",
                recipientId: "2",
                embeddedSigner: false
            }
            request.secondRecipient = JSON2.stringify(recipient);
            //RecipientModels.push(recipient);
            //request.recipientModelString = JSON2.stringify(RecipientModels);
            spin.start();
            if (debugging) console.log("startSignAndSend request " + JSON2.stringify(request))
            return ajx.ajaxPostJson(getEndPoint() + 'autoPositionedSigner', request).then(function(data) {
                if (debugging) console.log("setupDocumentSend response " + JSON2.stringify(data))
                if (data.status === "success") {
                    spin.stop();
                    var senderResult = JSON.parse(data.response);
                    //for sign and send mode we need to capture the envelopeId here so that we can use it later
                    signAndSendNewEnvelopeId = senderResult.envelopeId;
                    return setupEmbeddedView(senderResult.senderRecipientUrl, "Sender Sign Before Send");
                } else {
                    spin.stop();
                    gwl.grrr({
                        msg: "Cannot load DocuSign Send View. Reason: " + data.response,
                        type: "danger",
                        displaySec: 30
                    });
                    console.error("failed with cause: " + data.response);
                    return null;
                }
                if (debugging) console.log("signAndSendNewEnvelopeId: " + signAndSendNewEnvelopeId);
            });
        },
        showSignAndSendModal = function showSignAndSendModal() {
            var args = [].slice.call(arguments);
            var argsObj = {
                docName: args[0],
                emailSubject: args[1],
                emailBody: args[2],
                docUrlOrPath: args[3]
            }
            if (args.length === 5) {
                argsObj.mime = args[4];
            }
            return tmpls.renderExtTemplateStr({
                name: "signAndSendModalBody",
                data: docuSignOnBehalfOfUser
            }).then(function(bodyHtml) {
                signAndSendNewEnvelopeId = null;
                eModal.startSignAndSendDialogCustom({
                    title: 'Recipient Details',
                    html: bodyHtml,
                    btnLabel: "Sign And Send",
                    submitActionArgs: argsObj,
                    submitAction: function(args) {
                        console.log("dialog button pressed calling startSignAndSend");
                        startSignAndSend(args);
                    }
                });
                spin.stop();
            });
        },
        showSetupDocumentForm = function showSetupDocumentForm() {
            return tmpls.renderExtTemplate({
                name: 'setupDocumentForm',
                selector: targetDiv
            }).then(function() {
                //single use plugin not required
                //$('#docusign #showSetupDocumentPOCform').find('.selectpicker').selectpicker();
                $('#setupDocumentSendEmbeddedViewBtn').click(function() {
                    if (debugging) console.log('setupDocumentSendEmbeddedViewBtn clicked!');
                    var mydata = {
                        title: $("#docName").val(),
                        emailbody: $("#emailBody").val(),
                        dynamicdocurl: $("#docUrl").val(),
                        mode: $("#mode").val(),
                        mime: "application/pdf",
                        emailsubject: $("#emailSubject").val(),
                        dynamicdocformname: $("#dynamicDocFormName").val(),
                        ofbizurlprefix: $("#ofbizUrlPrefix").val()
                    }
                    signhereAction(mydata);
                });
            });
        },
        setupDocumentSendEmbeddedView = function setupDocumentSendEmbeddedView(docName, emailSubject, emailBody, docUrlOrPath, mime) {
            //mark 1
            var request = {
                docUrl: docUrlOrPath,
                docName: docName,
                emailSubject: emailSubject,
                emailBody: emailBody,
                docuSignUserEmail: docuSignOnBehalfOfUser.email,
                docuSignUserId: docuSignOnBehalfOfUser.docuSignUserId,
                tenantKey: tenantKey,
                isDynamic: "true",
                afterSendRedirectUrl: afterSendRedirectUrl,
                mime: mime
            }
            return ajx.ajaxPostJson(getEndPoint() + 'setupDocument', request).then(function(data) {
                if (debugging) console.log("setupDocumentSend response " + JSON2.stringify(data))
                $("#showSetupDocumentForm").hide("slow");
                if (data.status === "success") {
                    return setupEmbeddedView(data.response, "Customize And Send");
                } else {
                    spin.stop();
                    gwl.grrr({
                        msg: "Cannot load DocuSign Send View. Reason: " + data.response,
                        type: "danger",
                        displaySec: 30
                    });
                    console.error("failed with cause: " + data.response);
                }
            });
        },
        setupEmbeddedView = function setupEmbeddedView(url, modeltitle) {
            if (debugging) console.log("signAndSendNewEnvelopeId: " + signAndSendNewEnvelopeId);
            return Promise.resolve(eModal.iframe(url, modeltitle)).then(function(modalCloseFuction) {
                spin.stop();
                if (debugging) console.log("signAndSendNewEnvelopeId: " + signAndSendNewEnvelopeId);
                iframeModalClose = modalCloseFuction;
                // var loadNum = 0;
                // $("#emodal-hacked-iframe").on('load', function(e) {
                //     loadNum++;
                //     if (loadNum > 1) {
                //         console.log("iframe second load event ... closing modal");
                //         finishSigningSession();
                //     } else {
                //         console.log("iframe load event");
                //     }
                // });
            });
        },
        finishSigningSession = function finishSigningSession() {
            iframeModalClose();
            setTimeout(function() {
                gwl.grrr({
                    msg: "DocuSign Session complete.",
                    type: "success",
                    displaySec: 3
                });
                spin.stop();
            }, 2500);
        },
        // showAddUserToTenantAccForm = function showAddUserToTenantAccForm() {
        //     var actioned = false;
        //     return tmpls.renderExtTemplate({
        //         name: 'addUserToAccountWrapper',
        //         selector: targetDiv
        //     }).then(function() {
        //         if (debugging) console.log("top of showAddUserToTenantAccForm func. actioned: " + actioned);
        //         //suggest and fill all form fields
        //         return tmpls.renderExtTemplate({
        //             name: 'addUserToAccountForm',
        //             selector: "#addNewUserForm"
        //         })
        //     }).then(function() {
        //         $('docusign').on('click', '#addUserBtn', function(e) {
        //             e.preventDefault();
        //             var nameValues = $("#addDocusignUserForm").serializeArray();;
        //             var jsonReq = {};
        //             $.each(nameValues, function(index, pairs) {
        //                 jsonReq[pairs.name] = pairs.value;
        //             });
        //             if (debugging) console.log(JSON2.stringify(jsonReq));
        //             var evt = e;
        //             //FIXME seems to get called twice, casing errors duplicate user
        //             if (actioned === false) {
        //                 if (debugging) console.log("actioned: " + actioned + ", addUserToTenantAcc(jsonReq) about to be called")
        //                 addUserToTenantAcc(jsonReq);
        //                 actioned = true;
        //                 if (debugging) console.log("after call to addUserToTenantAcc(jsonReq). actioned is now " + actioned);
        //             }
        //         });
        //         $('docusign').on('click', '#addUserBtn-clear', function(e) {
        //             e.preventDefault();
        //             showAddUserToTenantAccForm();
        //             spin.stop();
        //         });
        //     });
        // },
        showAddUserToTenantAccForm = function showAddUserToTenantAccForm() {
            var actioned = false;
            return tmpls.renderExtTemplate({
                name: 'addUserToAccountWrapper',
                selector: targetDiv
            }).then(function() {
                if (debugging) console.log("top of showAddUserToTenantAccForm func. actioned: " + actioned);
                //suggest and fill all form fields
                return utils.FormBuilder().build({
                    inputFieldArray: [{
                        name: "firstName",
                        type: "text"
                    }, {
                        name: "lastName",
                        type: "text"
                    }, {
                        name: "email",
                        type: "email"
                    }, {
                        name: "userLoginId",
                        type: "text"
                    }],
                    formId: "addNewUserForm",
                    formButtonId: "addUserBtn",
                    formButtonName: "Add",
                    formButtonClasses: "btn btn-xl btn-primary pull-right",
                    lookupUrl: "/docusign-component/control/addDocusignUserSuggestFillForm",
                    targetSelector: "#addNewUserTarget",
                    templatesFolderPath: "/docusign/main/templates/"
                });
            }).then(function() {
                $("#addNewUserTarget").on('click', '#addUserBtn', function(e) {
                    e.preventDefault();
                    var nameValues = $("#addNewUserForm").serializeArray();
                    var jsonReq = {};
                    $.each(nameValues, function(index, pairs) {
                        jsonReq[pairs.name] = pairs.value;
                    });
                    if (debugging) console.log(JSON2.stringify(jsonReq));
                    var evt = e;
                    //FIXME seems to get called twice, casing errors duplicate user
                    if (actioned === false) {
                        if (debugging) console.log("actioned: " + actioned + ", addUserToTenantAcc(jsonReq) about to be called")
                        addUserToTenantAcc(jsonReq);
                        actioned = true;
                        if (debugging) console.log("after call to addUserToTenantAcc(jsonReq). actioned is now " + actioned);
                    }
                });
                $("#addNewUserTarget").on('click', '#addUserBtn-clear', function(e) {
                    e.preventDefault();
                    showAddUserToTenantAccForm();
                    spin.stop();
                });
            });
        },
        addUserToTenantAcc = function addUserToTenantAcc(jsonRequestData) {
            //add tenant key
            jsonRequestData.tenantKey = tenantKey;
            spin.start();
            return ajx.ajaxPost(getEndPoint() + 'addNewUsers', jsonRequestData).then(function(data) {
                if (debugging) console.log("addNewUsers response " + JSON2.stringify(data))
                spin.stop();
                if (data.newUsers[0].errorDetails) {
                    gwl.grrr({
                        msg: "Error new user not added. Reason: " + data.newUsers[0].errorDetails.message,
                        type: "danger",
                        displaySec: 30,
                        ele: "#addUserForm"
                    });
                } else if (data.newUsers[0].userId) {
                    gwl.grrr({
                        msg: "New user " + data.newUsers[0].userName + " was added!<br/>An activation email was sent for confirmation.<br/>Please remind the new user to confirm before attempting to use docusign.",
                        type: "success",
                        displaySec: 5,
                        ele: "#addUserForm"
                    });
                } else {
                    gwl.grrr({
                        msg: "Error new user not added. Reason: Unknown",
                        type: "danger",
                        displaySec: 20,
                        ele: "#addUserForm"
                    });
                }
                return Promise.resolve();
            }).catch(function(e) {
                spin.stop();
            });
        },
        showDisableUserForm = function showDisableUserForm() {
            if (debugging) console.log("showDisableUserForm called.");
            var actioned = false;
            tmpls.renderExtTemplate({
                name: 'disableUserFormWrapper',
                selector: targetDiv
            }).then(function() {
                //suggest and fill all form fields
                return utils.FormBuilder().build({
                    inputFieldArray: [{
                        name: "firstName",
                        type: "text"
                    }, {
                        name: "lastName",
                        type: "text"
                    }, {
                        name: "email",
                        type: "email"
                    }, {
                        name: "userLoginId",
                        type: "text"
                    }],
                    formId: "disableUserForm",
                    formButtonId: "disableUserBtn",
                    formButtonName: "Disable",
                    formButtonClasses: "btn btn-xl btn-primary pull-right",
                    lookupUrl: "/docusign-component/control/disableDocusignUserSuggestFillForm",
                    targetSelector: "#disableUserTagret",
                    templatesFolderPath: "/docusign/main/templates/",
                    otherFormGroupTypesHtml: '<div class="form-check"><div class="col-sm-4"></div><label class="col-sm-8 form-check-label"><input id="cb-close" class="cb form-check-input" type="checkbox">close DocuSign User Profile</label></div><div class="form-check"><div class="col-sm-4"></div><label class="col-sm-8 form-check-label"><input id="cb-void" class="cb form-check-input" type="checkbox">Void users in-progress envelopes</label></div>'
                });
            }).then(function() {
                $("#disableUserTagret").on('click', '#disableUserBtn', function(e) {
                    e.preventDefault();
                    if (debugging) console.log('disableUserBtn clicked!');
                    var nameValues = $("#disableUserForm").serializeArray();
                    var jsonReq = {};
                    $.each(nameValues, function(index, pairs) {
                        jsonReq[pairs.name] = pairs.value;
                    });
                    if ($('#cb-close').is(":checked")) {
                        jsonReq.close = "on";
                    } else {
                        jsonReq.close = "off";
                    }
                    if ($('#cb-void').is(":checked")) {
                        jsonReq.void = "on";
                    } else {
                        jsonReq.void = "off";
                    }
                    //ref 123
                    if (debugging) console.log(JSON2.stringify(jsonReq));
                    //FIXME seems to get called twice, casing errors duplicate user
                    if (actioned === false) {
                        disableUser(jsonReq);
                        actioned = true;
                    }
                });
                $("#disableUserTagret").on('click', '#disableUserBtn-clear', function(e) {
                    e.preventDefault();
                    //alert("mark");
                    showDisableUserForm();
                    spin.stop();
                });
                $('#disableUserBtn').prop('disabled', true);
                $("#disableUserTagret").on('click', '.cb', function(e) {
                    $('#disableUserBtn').prop('disabled', false);
                });
            });
        },
        showEnvelopeStatusTable = function showEnvelopeStatusTable() {
            return tmpls.renderExtTemplate({
                name: 'dataTables',
                selector: targetDiv
            })
        },
        disableUser = function disableUser(jsonRequestData) {
            //add tenant key
            jsonRequestData.tenantKey = tenantKey;
            spin.start();
            return ajx.ajaxPost(getEndPoint() + 'disableUser', jsonRequestData).
            then(function(data) {
                if (debugging) console.log("disableUser response " + JSON2.stringify(data));
                // $('#disableUserBtn').attr('disabled', true);
                spin.stop();
                //{"status":"failed","response":"1000 could not be closed. Error Message: No enabaled DocuSignUser found for userLoginId: 1000"}
                if (data.status === 'success') {
                    gwl.grrr({
                        msg: data.response,
                        type: "success",
                        ele: "#disableUserForm"
                    });
                } else {
                    gwl.grrr({
                        msg: data.response,
                        type: "danger",
                        ele: "#disableUserForm"
                    });
                }
            });
        },
        formInputToUrlWithQueryStr = function formInputToUrlWithQueryStr(formName, ofbizUrlPrefix) {
            var form = null;
            if (formName) {
                form = $('form[name="' + formName + '"]');
            }
            if (form === null) {
                gwl.grrr({
                    title: "<b>Unable To Perform Action</b><br/>",
                    msg: "Sign Here button incorrectly configured! No dynamic document details found.",
                    type: "danger"
                });
                return;
            }
            var serial = form.serialize();
            //console.log("serialized: " + serial);
            var actionUrl = "/" + form.attr("action");
            //console.log("actionUrl: " + actionUrl);
            var url = (ofbizUrlPrefix ? ofbizUrlPrefix : "") + actionUrl + "?" + serial;
            //console.log("url: " + url);
            return url;
        }
    publicInterface = {
        init: init,
        //action methods
        setupDocumentSendEmbeddedView: setupDocumentSendEmbeddedView,
        disableUser: disableUser,
        addUserToTenantAcc: addUserToTenantAcc,
        formInputToUrlWithQueryStr: formInputToUrlWithQueryStr,
        //poc form show methods
        showSetupDocumentForm: showSetupDocumentForm,
        showAddUserToTenantAccForm: showAddUserToTenantAccForm,
        showDisableUserForm: showDisableUserForm,
        showEnvelopeStatusTable: showEnvelopeStatusTable,
        showOtherTests: showOtherTests
    }
    return publicInterface;
}
$(function() {
    window.ds = window.ds || {};
    var getParameterByName = function(name) {
        var url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }
    var dsscript = $('script[data-loginUrlKey]'); //ends with
    var loginUrlKey = dsscript.attr('data-loginUrlKey');
    if (!loginUrlKey) {
        //in testing through an ifrm params are used
        loginUrlKey = getParameterByName("loginUrlKey");
    }
    //at this stage unsure where this came from var is not exposed on ds anyway
    // else {
    //     loginUrlKey = window.ds.loginUrlKey;
    // }
    var userLoginId = dsscript.attr('data-userLoginId');
    if (!userLoginId) {
        //in testing through an ifrm params are used
        userLoginId = getParameterByName("userLoginId");
    }
    var debug = dsscript.attr('data-debug');
    if (!debug) {
        //in testing through an ifrm params are used
        debug = getParameterByName("debug");
    }
    var modeAdmin = dsscript.attr('data-admin');
    if (!modeAdmin) {
        //in testing through an ifrm params are used
        modeAdmin = getParameterByName("admin");
    }
    var docusigninthandler = setInterval(function() {
        if (Docusign) {
            clearInterval(docusigninthandler);
            domSetup.loadCSSIfNotAlreadyLoaded("/docusign/main/global/bootstrap/css/bootstrap.min.css", "bootstrap");
            domSetup.loadCSSIfNotAlreadyLoaded("/docusign/main/global/css/google-fonts.css", "fonts.googleapis.com");
            //domSetup.loadCSSIfNotAlreadyLoaded("/docusign/main/dst/docusign-boot-styles.min.css")
            var ds = Docusign({
                loginUrlKey: loginUrlKey,
                userLoginId: userLoginId,
                modeAdmin: modeAdmin,
                debug: debug
            });
            ds.init();
        }
    }, 800);
});