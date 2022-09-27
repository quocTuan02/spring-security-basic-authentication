function parseUrl() {
    // Parse url lấy thông tin
    let vars = [], hash;
    let path = decodeURIComponent((window.location.href).replace(/\+/g, '%20'))
    let hashes = path.slice(path.indexOf('?') + 1).split('&');

    for (let i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }

    return vars;
}

function pagination(totalPages, currentPage) {
    $('#pagination').pagination({
        pages: totalPages,
        currentPage: currentPage,
        cssStyle: '',
        prevText: '<span aria-hidden="true">&laquo;</span>',
        nextText: '<span aria-hidden="true">&raquo;</span>',
        onInit: function () {

        },
        onPageClick: function (page, evt) {
            $('#page').val(page)
            $('#search-form').submit()
        }
    });

    $('#pagination .active .current').addClass('page-link')
    $('#pagination .ellipse.clickable').addClass('page-link')
    $('#pagination .disabled .current.prev').addClass('page-link')
    $('#pagination .disabled .current.next').addClass('page-link')
}

// Sắp xếp
$('.thuy-sort-area').click(function () {
    let newOrder = this.dataset.order
    let direction_input = $(`#search-form input[name='direction']`)
    if (newOrder !== order) {
        direction_input.val("asc")
    } else {
        if (direction === "asc") {
            direction_input.val("desc")
        } else {
            direction_input.val("asc")
        }
    }
    $(`#search-form input[name='order']`).val(newOrder)
    $(`#search-form input[name='page']`).val("1")
    $('#search-form').submit();
});

// Check all
$('input.thuy-check-all').click(function () {
    if ($(this).is(':checked')) {
        $('input.techmaster-check-element').prop('checked', true);
    } else {
        $('input.techmaster-check-element').prop('checked', false);
    }
});

// Tìm kiếm khi ấn enter
$('.thuy-search-input').keypress(function (e) {
    if (e.which === 13) {
        $(`#search-form input[name='page']`).val("1")
        $('#search-form').submit();
    }
});

// Tìm kiếm khi thay đổi select
$('.thuy-search-select').on('change', function () {
    $(`#search-form input[name='page']`).val("1")
    $('#search-form').submit();
});

// Tìm kiếm khi ấn nút tìm kiếm
$('#btn-search').click(function() {
    $(`#search-form input[name='page']`).val("1")
    $('#search-form').submit();
})
