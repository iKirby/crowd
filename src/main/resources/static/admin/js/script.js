require(['jquery', 'js-cookie'], function ($, Cookies) {
    $(function () {
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
        Cookies.remove(prefix + "_messageContent", { path: '/admin' });
        Cookies.remove(prefix + "_messageType", { path: '/admin' });
    });
});
