require(['jquery', 'js-cookie'], function ($, Cookies) {
    $(function () {
        Turbolinks.setProgressBarDelay(0);
        document.addEventListener("turbolinks:load", function () {
            bindForm($);
            showMessageBar($, Cookies);
        });
        bindForm($);
        showMessageBar($, Cookies);
    });
});

function showMessageBar($, Cookies) {
    var prefix = "admin";
    var msgBar = $("#message-bar");
    if (msgBar.length > 0) {
        var content = Cookies.get(prefix + "_messageContent");
        var type = Cookies.get(prefix + "_messageType");
        if (content !== undefined && type !== undefined) {
            $("#message-content").text(content);
            msgBar.addClass(type);
            msgBar.collapse("show");
        }
    }
    Cookies.remove(prefix + "_messageContent", {path: '/admin'});
    Cookies.remove(prefix + "_messageType", {path: '/admin'});
}

function bindForm($) {
    $("form").submit(function (e) {
        e.preventDefault();
        if ($(this).attr("method") === "post") {
            Turbolinks.controller.adapter.progressBar.setValue(0);
            Turbolinks.controller.adapter.progressBar.show();
            if ($(this).attr("enctype") === "multipart/form-data") {
                $.ajax({
                    type: "POST",
                    url: $(this).attr("action"),
                    data: new FormData(this),
                    success: function (data, textStatus, request) {
                        Turbolinks.visit(request.getResponseHeader("Turbolinks-Location"));
                    },
                    cache: false,
                    contentType: false,
                    processData: false
                });
            } else {
                $.ajax({
                    type: "POST",
                    url: $(this).attr("action"),
                    data: $(this).serialize(),
                    success: function (data, textStatus, request) {
                        Turbolinks.visit(request.getResponseHeader("Turbolinks-Location"));
                    }
                });
            }
        } else {
            var url = $(this).attr("action") + "?" + $(this).serialize();
            Turbolinks.visit(url);
        }
    })
}

