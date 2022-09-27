var listCategory = []

function initListCategories(categories) {
    listCategory = categories
}

$('.modal').on('shown.bs.modal', function () {
    const input_name = $('input[name="name"]')
    input_name.keyup(function () {
        let slug = $('input[name="slug"]')
        if (slug)
            slug.val(convertToSlug($(this).val()));
    });

    input_name.bind("paste", function () {
        let slug = $('input[name="slug"]')
        if (slug)
            slug.val(convertToSlug($(this).val()));
    });
});

$('.category-list-content .btn-choose-img').click(function () {
    let url = $('#list-user-img .grid-item.choosen .grid-item-img').attr('src');
    if (!url) {
        toastr.warning("Bạn chưa chọn ảnh");
        return;
    }
    closeChooseImgModal();
    $('.banner-container').show();
    $('.invalid-feedback-banner').hide();
    $('.preview-img').attr('src', url);
    $('input[name="banner"]').val(url);
})

$("#modal-add-new-brand").on("hidden.bs.modal", function () {
    let form = $(this).find("form")[0];
    closeModalInsertUpdate(form);
});

$("#modal-update-category").on("hidden.bs.modal", function () {
    let form = $(this).find("form")[0];
    closeModalInsertUpdate(form);
});

function closeModalInsertUpdate(form) {
    $('.banner-container').hide();
    $('.preview-img').attr('src', '');
    $('input[name="banner"]').val("");
    $('.invalid-feedback').hide();

    if (form)
        form.reset()
}

function loadCategories() {
    $.get("/api/admin/categories/all", function () {
    }).done(function (data) {
        listCategory = data.data
    });
}

$(document).on("click", ".btn-view-add__category", function () {
    let categoryParentId = $(this).data('id');
    let col = $(this).parent().parent().parent().data("cols") | 0
    $("#modal-add-new-category .modal-body").html(`
            <form id="form-add__category" data-id="${categoryParentId}" data-cols="${col}">
                <div class="d-none" hidden >
                    <input name="categoryParentId" value="${categoryParentId}" type="number" class="form-control" id="categoryParentId--new">
                    <span class="invalid-feedback" id="invalid-feedback__categoryParentId--new"></span>
                </div>
                <div class="form-group">
                    <label class="required-label" for="name--new">Tên danh mục</label>
                    <input name="name" type="text" class="form-control" id="name--new" required>
                    <span class="invalid-feedback" id="invalid-feedback__name--new"></span>
                </div>
                <div class="form-group">
                    <label class="required-label" for="slug--new">slug</label>
                    <input name="slug" type="text" class="form-control" id="slug--new" required>
                    <span class="invalid-feedback" id="invalid-feedback__slug--new"></span>
                </div>
                <div class="form-group">
                    <label class="" for="description--new">Mô tả</label>
                    <textarea name="description" class="form-control" id="description--new"></textarea>
                    <span class="invalid-feedback" id="invalid-feedback__description--new"></span>
                </div>
                <div class="form-group">
                    <label class="mr-4">Banner</label>
                    <div class="banner-container" style="display:none">
                        <div class="img-div">
                            <input type="text" name="banner" hidden>
                            <img class="preview-img" alt="banner" src="#">
                        </div>
                    </div>
                    <div class="invalid-feedback" id="invalid-feedback__banner--new"></div>
                    <button type="button" class="btn btn-info" data-toggle="modal"
                            data-target="#choose-img-modal">
                        Chọn ảnh
                    </button>
                </div>
                <div>
                    <div class="d-flex flex-row-reverse"
                         data-toggle="collapse" data-target="#setting-seo"
                         aria-controls="setting-seo" aria-expanded="false">
                        <i class="fas fa-chevron-down"></i>
                    </div>
                    <div class="collapse" id="setting-seo">
                        <div class="form-group">
                            <label class="" for="metaTitle--new">meta title</label>
                            <input name="metaTitle" type="text"  
                                class="form-control" id="metaTitle--new" >
                            <span class="invalid-feedback" id="invalid-feedback__metaTitle--new"></span>
                        </div>
                        <div class="form-group">
                            <label class="" for="metaKeyword--new">meta keyword</label>
                            <input name="metaKeyword" type="text" class="form-control" id="metaKeyword--new">
                            <span class="invalid-feedback" id="invalid-feedback__metaKeyword--new"></span>
                        </div>
                        <div class="form-group">
                            <label class="" for="metaDescription--new">meta description</label>
                            <textarea name="metaDescription" class="form-control"
                                      id="metaDescription--new"></textarea>
                            <span class="invalid-feedback" id="invalid-feedback__metaDescription--new"></span>
                        </div>
                    </div>
                </div>     
            </form>
        `)

    $("#modal-add-new-category  .btn-add__category--new").attr("form", "form-add__category")
    $('#modal-add-new-category').modal('show');
})

$(document).on("click", ".btn-add__category--new", function (event) {
    event.preventDefault();
    const form = $(this.form)
    let col = form.data("cols") | 0
    $.ajax({
        url: '/api/admin/categories',
        type: 'POST',
        data: JSON.stringify(getFormData(form)),
        processData: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            toastr.success(data.message);
            let category = data.data
            $(`div.category-list > ul.scroll-item[data-cols="${col + 1}"]`).append(`
                <li class="category-item" id='${"category--id_" + category.id}' data-id="${category.id}" data-parent-id="${category.categoryParentId}">
                    <p>${category.name}</p>
                    ${category.categoryChild.length > 0 && col < 3 ? '<i class="ml-2 fas fa-chevron-right"></i>' : ''}
                    <div class="item-action shadow">
                        ${col < 3 ? `<button class="btn btn-view-add__category" data-id="${category.id}"><i class="fas fa-plus"></i></button>` : ''}
                        <button class="btn btn-view-edit__category"
                                data-id="${category.id}">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-confirm-delete__category"
                                data-id="${category.id}">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                </li>
            `)
            let categoryParent = $(`#category--id_${category.categoryParentId}`)
            if (categoryParent.find(".fas.fa-chevron-right").length === 0)
                categoryParent.append('<i class="ml-2 fas fa-chevron-right"></i>')
            $('.modal').modal('hide');
            $('.invalid-feedback').hide()
            form[0].reset()
            loadCategories()
        },
        error: function (error) {
            let data = error.responseJSON
            if (data.errors instanceof Array) {
                toastr.error(data.message);
                $('.invalid-feedback').hide()
                $.map(data.errors, function (e) {
                    let obj = $(`#invalid-feedback__${e.field}--new`)
                    obj.show();
                    obj.text(e.message)
                })
            } else {
                toastr.error(error.responseText);
            }

        }
    });
});

$(document).on("click", ".btn-confirm-delete__category", function () {
    let id = $(this).data("id")
    $('#delete-category-id').val(id);
    $('#modal-delete-category').modal('show');
})

$(document).on("click", ".btn-delete-category", function () {
    let id = $('#delete-category-id').val();
    $.ajax({
        url: '/api/admin/categories/' + id,
        type: 'DELETE',
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            toastr.success(data.message);
            data.data.forEach(i => {
                $(`#category--id_${i}`).remove();
            })

            $('.modal').modal('hide');
            $('#delete-brand-id').val('');
            loadCategories()
        },
        error: function (data) {
            toastr.error(data.responseText);
        }
    });
})

$(document).on("click", ".btn-view-edit__category", function () {
    let id = $(this).data('id');
    $.ajax({
        url: '/api/admin/categories/' + id,
        type: 'GET',
        success: function (data) {
            let category = data.data
            $("#modal-update-category .modal-body").html(`
                <form id="form-edit__category" data-id="${id}">
                    <div class="form-group">
                        <label class="required-label" for="name--edit">Tên danh mục</label>
                        <input name="name" value="${category.name}" type="text" class="form-control" id="name--edit" required>
                        <span class="invalid-feedback" id="invalid-feedback__name--edit"></span>
                    </div>
                    <div class="form-group">
                        <label class="required-label" for="slug--edit">slug</label>
                        <input name="slug" value="${category.slug}" type="text" class="form-control" id="slug--edit" required>
                        <span class="invalid-feedback" id="invalid-feedback__slug--edit"></span>
                    </div>
                    <div class="form-group">
                        <label class="" for="description--edit">Mô tả</label>
                        <textarea name="description" class="form-control" id="description--edit">${category.description}</textarea>
                        <span class="invalid-feedback" id="invalid-feedback__description--edit"></span>
                    </div>
                    <div class="form-group">
                        <label class="mr-4">Banner</label>
                        <div class="banner-container" ${category.banner ? "" : 'style="display:none"'}>
                            <div class="img-div">
                                <input type="text" name="banner" hidden>
                                <img class="preview-img" alt="banner" src="${category.banner}">
                            </div>
                        </div>
                        <div class="invalid-feedback" id="invalid-feedback__banner--edit"></div>
                        <button type="button" class="btn btn-info" data-toggle="modal"
                                data-target="#choose-img-modal">
                            Chọn ảnh
                        </button>
                    </div>
                    <div>
                        <div class="d-flex flex-row-reverse"
                             data-toggle="collapse" data-target="#setting-seo"
                             aria-controls="setting-seo" aria-expanded="false">
                            <i class="fas fa-chevron-down"></i>
                        </div>
                        <div class="collapse" id="setting-seo">
                            <div class="form-group">
                                <label class="" for="metaTitle--edit">meta title</label>
                                <input name="metaTitle" type="text"  
                                    value="${category.metaTitle}"
                                    class="form-control" id="metaTitle--edit" >
                                <span class="invalid-feedback" id="invalid-feedback__metaTitle--edit"></span>
                            </div>
                            <div class="form-group">
                                <label class="" for="metaKeyword--edit">meta keyword</label>
                                <input name="metaKeyword"  value="${category.metaKeyword}" type="text" class="form-control" id="metaKeyword--edit">
                                <span class="invalid-feedback" id="invalid-feedback__metaKeyword--edit"></span>
                            </div>
                            <div class="form-group">
                                <label class="" for="metaDescription--edit">meta description</label>
                                <textarea name="metaDescription" class="form-control"
                                          id="metaDescription--edit">${category.metaDescription}</textarea>
                                <span class="invalid-feedback" id="invalid-feedback__metaDescription--edit"></span>
                            </div>
                        </div>
                    </div>    
                    <div class="form-group">
                        <div class='${data.data.createdBy == null ? "d-none" : ""}'><label data-id="${data.data.createdBy ? data.data.createdBy.id : ""}">Người tạo:</label> <samp>${data.data.createdBy ? data.data.createdBy.fullName : ""}</samp></div>
                        <div class=""><label>Thời gian tạo:</label> <samp>${data.data.createdAt}</samp></div>
                        <div class='${data.data.modifiedBy == null ? "d-none" : ""}'><label data-id="${data.data.modifiedBy ? data.data.modifiedBy.id : ""}">Cập nhật lần cuối bởi:</label> <samp>${data.data.modifiedBy ? data.data.modifiedBy.fullName : ""}</samp></div>
                        <div class=""><label>Thời gian cập nhật lần cuối:</label> <samp>${data.data.modifiedAt}</samp></div>
                    </div> 
                </form>
            `)

            $("#modal-update-category  .btn-update-category").attr("form", "form-edit__category")
            $('#modal-update-category').modal('show');
        },
        error: function (error) {
            toastr.error(error.responseText);
        }
    });
})

$(document).on("click", ".btn-update-category", function (event) {
    event.preventDefault();
    const form = $(this.form)
    let id = form.data("id");
    $.ajax({
        url: "/api/admin/categories/" + id,
        type: 'PUT',
        data: JSON.stringify(getFormData(form)),
        processData: false,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            toastr.success(data.message);
            let category = data.data
            $(`#category--id_${category.id} > p`).html(`${category.name}`)

            $('.modal').modal('hide');
            $('.invalid-feedback').hide()
            form[0].reset()
            loadCategories()
        },
        error: function (error) {
            let data = error.responseJSON
            if (data.errors instanceof Array) {
                toastr.error(data.message);
                $('.invalid-feedback').hide()
                $.map(data.errors, function (e) {
                    let obj = $(`#invalid-feedback__${e.field}--edit`)
                    obj.show();
                    obj.text(e.message)
                })
            } else {
                toastr.error(error.responseText);
            }

        }
    });
})


$(document).on("click", ".category-list .scroll-item .category-item", function (e) {
    if ($(this).hasClass("active")) return;

    $(this).parent().find('li.category-item').removeClass('active')
    $(this).addClass("active")
    let categoryId = $(this).data("id")
    let col = $(this).parent().data("cols")
    let col_2 = $(`div.category-list > ul.scroll-item[data-cols="2"]`);
    let col_3 = $(`div.category-list > ul.scroll-item[data-cols="3"]`);
    let col_4 = $(`div.category-list > ul.scroll-item[data-cols="4"]`);
    switch (col) {
        case 1:
            col_2.html("")
            col_3.html("")
            col_4.html("")
            break
        case 2:
            col_3.html("")
            col_4.html("")
            break
        case 3:
            col_4.html("")
            break
        case 4:
            return
    }

    let sc = listCategory.filter(i => i.id === categoryId)[0]

    let subCategory = sc ? sc.categoryChild : []
    if (subCategory.length > 0)
        $(`div.category-list > ul.scroll-item[data-cols="${col + 1}"]`).html(`
            ${subCategory.map(category => `
                <li class="category-item" id='${"category--id_" + category.id}' data-id="${category.id}" data-parent-id="${category.categoryParentId}">
                    <p>${category.name}</p>
                    ${category.categoryChild.length > 0 && col < 3 ? '<i class="ml-2 fas fa-chevron-right"></i>' : ''}
                    <div class="item-action shadow">
                        ${col < 3 ? `<button class="btn btn-view-add__category" data-id="${category.id}"><i class="fas fa-plus"></i></button>` : ''}
                        <button class="btn btn-view-edit__category"
                                data-id="${category.id}">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-confirm-delete__category"
                                data-id="${category.id}">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                </li>
            `).reduce((a, b) => a + b)}
        `)
})
