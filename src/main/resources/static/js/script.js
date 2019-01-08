$(function () {
    // process message bar
    Turbolinks.setProgressBarDelay(0);
    document.addEventListener("turbolinks:load", function() {
        showMessageBar();
    });
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
