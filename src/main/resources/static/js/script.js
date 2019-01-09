$(function () {
    // process message bar
    Turbolinks.setProgressBarDelay(0);
    document.addEventListener("turbolinks:load", function () {
        bindForm();
        showMessageBar();
    });
    bindForm();
    showMessageBar();
});

// bind button confirm action
function showConfirm(text, url) {
    $("#confirm-modal-body").text(text);
    $("#confirm-modal-btn").attr("href", url);
    $("#confirm-modal").modal("show");
}

function showMessageBar() {
    var prefix = "user";
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
    Cookies.remove(prefix + "_messageContent");
    Cookies.remove(prefix + "_messageType");
}

function bindForm() {
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
