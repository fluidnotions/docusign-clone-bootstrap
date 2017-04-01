//NO CONFLICT EDITED -- this is the root statement
//here the jquery needed for docusign is placed on the global as
//$$ and other components have been imported to source and edited
//so that they link up via $$ rather then $/jQuery so that conflicts
//with eariler versions of jquery do not occure
var $ = global.ds$ = require("jquery");
require("./noConflictEditedDeps/bootstrap-notify");
require('./noConflictEditedDeps/jsrender');
var spin = require('spin');
var moment = require("moment");
var Promise = require("bluebird");
var JSON2 = require('JSON2');
var DomSetup = function DomSetup(selector, temaplateFullPathName, appendToSelector) {
    var loadCSSIfNotAlreadyLoaded = function(pathToCss) {
            var ss = document.styleSheets;
            for (var i = 0, max = ss.length; i < max; i++) {
                if (ss[i].href == pathToCss) return;
            }
            var link = document.createElement("link");
            link.rel = "stylesheet";
            link.href = pathToCss;
            document.getElementsByTagName("head")[0].appendChild(link);
        },
        createElement = function(temaplateFullPathName, appendToSelector) {
            return Promise.resolve($.get(temaplateFullPathName)).then(function(html) {
                $(html).appendTo(appendToSelector);
            })
        }
    return {
        createElement: createElement,
        loadCSSIfNotAlreadyLoaded: loadCSSIfNotAlreadyLoaded
    }
}
var CoolTools = function CoolTools() {
    var traverse = function traverse(o, func) {
        for (var i in o) {
            func.apply(this, [i, o[i]]);
            if (o[i] !== null && typeof(o[i]) == "object") {
                //going on step down in the object tree!!
                traverse(o[i], func);
            }
        }
    }
    return {
        traverse: traverse
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
    var ajaxGet = function ajaxGet(url, data) {
        return new Promise(function(resolve, reject) {
            $.ajax({
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                type: "GET",
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
                data: JSON2.stringify(data),
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
        ajaxPostJson: ajaxPostJson,
        ajaxGet: ajaxGet
    }
}
var TemplateUtils = function TemplateUtils(options) {
    var selector = options.selector;
    var getPath = function getPath(name) {
            return options.mountpoint + options.temaplatePath + name + '.tmpl.html';
        },
        renderExtTemplate = function renderExtTemplate(item) {
            var file = getPath(item.name);
            console.log("renderExtTemplate with file: " + file + ", has data: " + (item.data ? "YES" : "NO"));
            return Promise.resolve($.get(file)).then(function(tmplData) {
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
        renderExtTemplateStr = function renderExtTemplateStr(item) {
            var file = getPath(item.name);
            return Promise.resolve($.get(file)).then(function(tmplData) {
                $.templates({
                    tmpl: tmplData
                });
                var html = $.render.tmpl(item.data);
                //replace all newlines
                html = html.replace(/(?:\r\n|\r|\n)/g, '');
                return html;
            });
        }
    $.views.converters("dateformat", function(val) {
        //2015-09-02T12:25:33.9500000Z
        return moment(parseInt(val)).format("dddd, h:mm:ss a");
    });
    return {
        getPath: getPath,
        renderExtTemplate: renderExtTemplate,
        renderExtTemplateStr: renderExtTemplateStr
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
                element: 'body',
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
                icon_type: 'class',
                template: '<div data-notify="container" class="col-xs-11 col-sm-4 alert alert-{0}" role="alert"><button type="button" aria-hidden="true" class="close" data-notify="dismiss">&times;</button><span data-notify="icon"></span> <span data-notify="title">{1}</span> <span data-notify="message">{2}</span><div class="progress" data-notify="progressbar"><div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div></div><a href="{3}" target="{4}" data-notify="url"></a></div>'
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
                scale: 1.5, // Scales overall size of the spinner
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