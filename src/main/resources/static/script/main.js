$(function () {
    formatMoney();
    resetModal();
    configToastr();

    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.back-to-top').fadeIn();
        } else {
            $('.back-to-top').fadeOut();
        }
    });
    $('.back-to-top').click(function () {
        $("html, body").animate({scrollTop: 0}, 600);
        return false;
    });
})

$(document).on('click', function (e) {
    let target = e.target;

    if (target.closest('.product-link')) {
        localStorage.setItem('sessionsProduct', $(this).attr('id'));

        if (target !== $('.product-link')) {
            let productId = $(target).parents('.product-link').attr('id');
            localStorage.setItem('sessionsProduct', productId);
        }
    }
})

function convertPrice(currency) {
    let number = Number(currency);
    if (isNaN(number))
        return currency;

    return new Intl.NumberFormat('vn-VN', {
        minimumFractionDigits: 0,
        maximumFractionDigits: 1
    }).format(number);
}

function configToastr() {
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-top-center",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "2000",
        "hideDuration": "1000",
        "timeOut": "1000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    }
}

function formatMoney() {
    $('.text-price').each(function (index, element) {
        let money = $(element).text();
        $(element).text(convertPrice(money));
    });

    $('.text-price-input').each(function (index, element) {
        let money = $(element).val();
        $(element).val(convertPrice(money));
    });
}

// Reset form after close modal

function resetModal() {
    $('.modal').on('hidden.bs.modal', function () {
        $(this).find('form').trigger('reset');
        $('.invalid-feedback').css('display', 'none');
    })
}

$(document).on('keyup', function (e) {
    let target = e.target;

    // if (target.closest('.search-input')) {
    //     var keycode = (e.keyCode ? e.keyCode : e.which);
    //     if (keycode == '13') {
    //         searchProductByKeyword();
    //     }
    // }
})


$('.search-button').click(function () {
    searchProductByKeyword();
})

function searchProductByKeyword() {
    let keyword = $('.search-input').val();
    if (keyword.length === 0) {
        toastr.warning("Vui lòng nhập từ khóa tìm kiếm");
        return
    }
    location.href = "/api/tim-kiem?keyword=" + keyword;
}


$(document).on('click', ".btn-hide-loader i", function () {
    loader.hide();
})

$('.btn-back').click(function () {
    if (document.referrer.startsWith(window.location.origin))
        history.back();
    else window.location.href = '/'
})