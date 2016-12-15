global.$ = global.jQuery = require("jquery");
require('jsrender');
var spin = require('spin');
var moment = require("moment");
var Promise = require("bluebird");



var DomSetup = function DomSetup(selector, temaplateFullPathName, appendToSelector) {

    var loadCSSIfNotAlreadyLoaded = function(pathToCss) {
            var ss = document.styleSheets;
            for (var i = 0, max = ss.length; i < max; i++) {
                if (ss[i].href == pathToCss)
                    return;
            }
            var link = document.createElement("link");
            link.rel = "stylesheet";
            link.href = pathToCss;

            document.getElementsByTagName("head")[0].appendChild(link);
        },
        createElement = function(temaplateFullPathName, appendToSelector) {
            return Promise.resolve($.get(temaplateFullPathName))
                .then(function(html) {
                    $(html).appendTo(appendToSelector);

                })
        }

    return {
        createElement: createElement,
        loadCSSIfNotAlreadyLoaded: loadCSSIfNotAlreadyLoaded
    }
}

var AjaxWrap = function AjaxWrap() {
    var ajaxPost = function ajaxPost(url, data) {
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
    }

    var ajaxPostJson = function ajaxPostJson(url, data) {
        return new Promise(function(resolve, reject) {
            $.ajax({
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                type: "POST",
                url: url,
                data: JSON.stringify(data),
                success: function(resp, textStatus, jqXHR) {
                    resolve(resp);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    reject(errorThrown);
                }
            });
        });
    }

    return {
        ajaxPost: ajaxPost,
        ajaxPostJson: ajaxPostJson

    }

}


var TemplateUtils = function TemplateUtils(options) {
    var selector = options.selector;

    var
        getPath = function getPath(name) {
            return options.mountpoint + options.temaplatePath + name + '.tmpl.html';
        },
        getBPPath = function getBPPath(name) {
            return options.mountpoint + options.temaplatePath + name + '.bp.html';
        },
        renderExtTemplate = function renderExtTemplate(item) {

            var file = getPath(item.name);

            console.log("renderExtTemplate with file: " + file + ", has data: " + (item.data ? "YES" : "NO"));

            return Promise.resolve($.get(file))
                .then(function(tmplData) {

                    $.templates({
                        tmpl: tmplData
                    });
                    var html = $.render.tmpl(item.data);

                    if (item.selector) {
                        selector = item.selector;
                    }

                    $(selector).html(html);

                    return html;
                });
        },
        //all it does diff is the template of a template uses {*{:}*} and as a final
        //step it replaces all thoes with a empty string. Templates of templates also have suffix
        //.bp.html to distingish them
        renderTemplateOfTemplate = function renderTemplateOfTemplate(item) {

            var file = getBPPath(item.name);

            console.log("renderTemplateOfTemplate with file: " + file + ", has data: " + (item.data ? "YES" : "NO"));

            return Promise.resolve($.get(file))
                .then(function(tmplData) {

                    $.templates({
                        tmpl: tmplData
                    });
                    var html = $.render.tmpl(item.data);
                    return html;
                }).then(function(markup) {
                    if (item.selector) {
                        selector = item.selector;
                    }
                    var html = String.replace(markup, '*', '');
                    $(selector).html(html);
                    return html;
                });
        }
        //first attempt to use it didn't work
        // linkExtTemplate = function linkExtTemplate(item) {
        //
        //     var file = getPath(item.name);
        //
        //     console.log("linkExtTemplate with file: " + file + ", has data: " + (item.data ? "YES" : "NO"));
        //     // $.templates("myTmpl", "some markup string");
        //     return Promise.resolve($.get(file))
        //         .then(function(tmplData) {
        //
        //             $.templates(item.name, tmplData);
        //
        //             if (item.selector) {
        //                 selector = item.selector;
        //             }
        //             var jqEl = $.link[item.name](selector, item.data, item.helpersOrContext);
        //
        //             return jqEl;
        //         });
        // }

        $.views.converters("dateformat", function(val) {
            //2015-09-02T12:25:33.9500000Z
            return moment(parseInt(val)).format("dddd, h:mm:ss a");
        });


    return {
        getPath: getPath,
        renderExtTemplate: renderExtTemplate,
        //linkExtTemplate: linkExtTemplate,
        renderTemplateOfTemplate: renderTemplateOfTemplate
    }

}


var QuickGrowl = function QuickGrowl() {

    var grrr = function grrr(options) {
            $.notify({
                // options
                title: options.title || "",
                message: options.msg || options.message,
            }, {
                // settings
                element: options.ele || 'body',
                position: null,
                type: options.type,
                allow_dismiss: true,
                newest_on_top: false,
                showProgressbar: false,
                placement: {
                    from: "top",
                    align: "right"
                },
                offset: 50,
                spacing: 10,
                z_index: 1031,
                delay: (options.displaySec * 1000) || 3000,
                animate: {
                    enter: 'animated fadeInDown',
                    exit: 'animated fadeOutUp'
                },
                icon_type: 'class'
            });
        },
        ajaxErr = function ajaxErr(type, name, url, e) {

            var errMsg = "";
            //response text preferred
            if (e.responseText) {
                errMsg = e.responseText;
                var resp = JSON.parse(errMsg);
                if (resp) {
                    errMsg = resp.exception;
                }
            } else if (e.statusText) {
                errMsg = e.statusText;
            }

            console.log("Error: $.ajax:" + type + " " + url + "." + errMsg);
            grrr({
                msg: type + " " + name + " sever error " + errMsg,
                type: "danger"
            });
        }

    return {
        grrr: grrr,
        xerr: ajaxErr
    }

}

var Spinner = function Spinner() {

    var spinner = null,
        start = function start() {
            var target = document.getElementById('spinner');
            console.log("spin target: " + target);
            spinner = new spin({
                lines: 11, // The number of lines to draw
                length: 29, // The length of each line
                width: 12, // The line thickness
                radius: 76, // The radius of the inner circle
                scale: 1, // Scales overall size of the spinner
                corners: 1, // Corner roundness (0..1)
                color: '#000', // #rgb or #rrggbb or array of colors
                opacity: 0.25, // Opacity of the lines
                rotate: 0, // The rotation offset
                direction: 1, // 1: clockwise, -1: counterclockwise
                speed: 1, // Rounds per second
                trail: 60, // Afterglow percentage
                fps: 20, // Frames per second when using setTimeout() as a fallback for CSS
                zIndex: 2e9, // The z-index (defaults to 2000000000)
                className: 'spinner', // The CSS class to assign to the spinner
                top: '50%', // Top position relative to parent
                left: '50%', // Left position relative to parent
                shadow: false, // Whether to render a shadow
                hwaccel: false, // Whether to use hardware acceleration
                position: 'relative', // Element positioning
            }).spin(target);
            console.log("spinner started");


        },
        stop = function stop() {
            setTimeout(function() {
                spinner.stop();
                console.log("spinner stopped");
            }, 1000);
        }

    return {
        start: start,
        stop: stop
    }
}

var FormBuilder = require('./formBuilderPlus');

module.exports.TemplateUtils = TemplateUtils;
module.exports.Spinner = Spinner;
module.exports.QuickGrowl = QuickGrowl;
module.exports.AjaxWrap = AjaxWrap;
module.exports.DomSetup = DomSetup;
module.exports.FormBuilder = FormBuilder;
