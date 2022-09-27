var options = {
    valueNames: [
        {name: 'grid-item-img', attr: 'src'}
    ],
    item: '<div class="grid-item" onclick="chooseImg(this)"><div class="img-container"><div class="img-div"><img class="grid-item-img" src=""></div></div></div>',
    pagination: true,
    page: 10
};

var values = [];
var imgList = new List('list-user-img', options);

function initListImg(arr) {
    values = arr;
    imgList.add(values);
}

function closeChooseImgModal() {
    $('#choose-img-modal').modal('hide');
    $('#list-user-img .grid-item.choosen').removeClass('choosen');
    $('.btn-delete-img').prop('disabled', true);
    $('.btn-choose-img').prop('disabled', true);
}

function chooseImg(img) {
    if ($(img).hasClass('choosen')) {
        $(img).toggleClass('choosen');
        $('.btn-delete-img').prop('disabled', true);
        $('.btn-choose-img').prop('disabled', true);
    } else {
        $('.grid-item.choosen').toggleClass('choosen');
        $(img).toggleClass('choosen');
        $('.btn-delete-img').prop('disabled', false);
        $('.btn-choose-img').prop('disabled', false);
    }
}

$("#upload-thumbnail").change(function () {
    const extensionList = ["jpg", "png", "svg", "jpeg", "gif"]
    let fd = new FormData();
    let file = $(this)[0].files[0];
    let fileName = file.name;
    let extension = fileName.substr((fileName.lastIndexOf('.') + 1));
    if (!extensionList.includes(extension)) {
        $(this).val('');
        toastr.warning("Chỉ hỗ trợ các định dạng ảnh: jpg, png, svg, jpeg");
        return;
    }
    if (file.size > 1000000) {
        toastr.warning("Chỉ hỗ trợ file ảnh có kích thước lớn nhất 1MB");
        $(this).val('');
        return;
    }
    fd.append('file', file);

    $.ajax({
        url: '/api/upload-file',
        type: 'post',
        data: fd,
        contentType: false,
        processData: false,
        success: function (data) {
            values.unshift({"grid-item-img": data})
            imgList.clear();
            imgList.add(values);
        },
        error: function (data) {
            toastr.warning(data.responseJSON.message);
        }
    });
});

$('.btn-delete-img').click(function () {
    let url = $('.grid-item.choosen .grid-item-img').attr('src');
    if (url === "" || url == null) {
        toastr.warning("Vui lòng chọn ảnh cần xóa");
        return;
    }
    let filename = url.replace('/media/static/', '');
    // Confirm
    let click = confirm("Bạn chắc chắn muốn xóa ảnh này?");
    if (click === true) {
        // Send api delete
        $.ajax({
            url: '/api/delete-image/' + filename,
            type: 'DELETE',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                // Remove from list
                let index = -1;
                let i = 0;
                for (i = 0; i < values.length; i++) {
                    if (JSON.stringify(values[i]) === JSON.stringify({"grid-item-img": url})) {
                        index = i;
                        break;
                    }
                }
                if (index !== -1) {
                    values.splice(index, 1);
                }
                imgList.remove('grid-item-img', url);
            },
            error: function (data) {
                toastr.warning(data.responseJSON.message);
            }
        });
    }
})