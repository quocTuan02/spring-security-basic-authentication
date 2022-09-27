var loader = $('.container-loader')
$.ajaxSetup({
    beforeSend: function () {
        loader.show();
    },
    complete: function () {
        loader.hide();
    }
});
$(document).ready(function () {
    $(window).keydown(function (event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });
});

$(document).ready(function () {
    $("form.search-form").keydown(function (event) {
        if (event.keyCode === 13) {
            if ($("form.search-form input").val())
                $("form.search-form").submit()
            return false;
        }
    });
});

$('body').on('keydown', 'input, select', function (e) {
    if (e.which === 13) {
        var self = $(this), form = self.parents('form:eq(0)'), focusable, next;
        focusable = form.find('input').filter(':visible');
        next = focusable.eq(focusable.index(this) + 1);
        if (next.length) {
            next.focus();
        }
        return false;
    }
});