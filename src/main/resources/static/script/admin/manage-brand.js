$(document).ready(function () {

    $('.modal').on('shown.bs.modal', function() {
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

    $('.btn-choose-img').click(function () {
        let url = $('#list-user-img .grid-item.choosen .grid-item-img').attr('src');
        if (!url) {
            toastr.warning("Bạn chưa chọn ảnh");
            return;
        }
        closeChooseImgModal();
        $('.thumbnail-container').show();
        $('.invalid-feedback-thumbnail').hide();
        $('.preview-img').attr('src', url);
        $('input[name="thumbnail"]').val(url);
    })

    $("#modal-add-new-brand").on("hidden.bs.modal", function () {
        let form = $(this).find("form")[0];
        closeModalInsertUpdate(form);
    });

    $("#modal-update-brand").on("hidden.bs.modal", function () {
        let form = $(this).find("form")[0];
        closeModalInsertUpdate(form);
    });

    function closeModalInsertUpdate(form) {
        $('.thumbnail-container').hide();
        $('.preview-img').attr('src', '');
        $('input[name="thumbnail"]').val("");
        $('.invalid-feedback').hide();

        if (form)
            form.reset()
    }

    $(document).on("click", ".btn-add__brand--new", function (event) {
        event.preventDefault();
        const form = $(this.form)
        $.ajax({
            url: '/api/admin/brands',
            type: 'POST',
            data: JSON.stringify(getFormData(form)),
            processData: false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success(data.message);
                $('.list-brand').append(`
                    <tr data-id="${data.data.id}">
                        <td>${data.data.id}</td>
                        <td class="${data.data.isDeleted ? "is-deleted " : ""} text-capitalize text-nowrap">${data.data.name}</td>
                        <td>${data.data.productCount}</td>
                        <td>
                            <button class="btn btn-view-edit__brand" data-id="${data.data.id}">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn btn-confirm-delete__brand" data-id="${data.data.id}">
                                <i class="fas fa-trash-alt"></i>
                            </button>
                        </td>
                    </tr>
                `);
                $('.modal').modal('hide');
                $('.invalid-feedback').hide()
                form[0].reset()
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

    $(document).on("click", ".btn-confirm-delete__brand", function () {
        let id = $(this).data("id")
        $('#delete-brand-id').val(id);
        $('#modal-delete-brand').modal('show');
    })

    $(document).on("click", ".btn-delete-brand", function () {
        let id = $('#delete-brand-id').val();
        $.ajax({
            url: '/api/admin/brands/' + id,
            type: 'DELETE',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success(data.message);
                $(`.list-brand tr[data-id=${id}]`).remove();
                $('.modal').modal('hide');
                $('#delete-brand-id').val('');
            },
            error: function (data) {
                toastr.error(data.responseText);
            }
        });
    })

    $(document).on("click", ".btn-view-edit__brand", function (event) {
        let id = $(this).data('id');
        $.ajax({
            url: '/api/admin/brands/' + id,
            type: 'GET',
            success: function (data) {
                $("#modal-update-brand .modal-body").html(`
                    <form id="form-edit__brand" data-id="${id}">
                        <div class="form-group">
                            <label class="required-label" for="name--edit">Tên nhãn hiệu</label>
                            <input name="name" value="${data.data.name}" type="text" class="form-control" id="name--edit" required>
                            <span class="invalid-feedback" id="invalid-feedback__name--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="required-label" for="slug--edit">slug</label>
                            <input name="slug" value="${data.data.slug}" type="text" class="form-control" id="slug--edit" required>
                            <span class="invalid-feedback" id="invalid-feedback__slug--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="" for="description--edit">Mô tả</label>
                            <textarea name="description" class="form-control" id="description--edit">${data.data.description}</textarea>
                            <span class="invalid-feedback" id="invalid-feedback__description--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="mr-4">Logo</label>
                            <div class="thumbnail-container">
                                <div class="img-div">
                                    <input type="text" name="thumbnail"  value="${data.data.thumbnail}" hidden>
                                    <img class="preview-img" alt="Thumbnail post" src="${data.data.thumbnail}">
                                </div>
                            </div>
                            <div class="invalid-feedback" id="invalid-feedback__thumbnail--edit"></div>
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
                                    <input name="metaTitle" type="text"  value="${data.data.metaTitle}" 
                                        class="form-control" id="metaTitle--edit" required>
                                    <span class="invalid-feedback" id="invalid-feedback__metaTitle--edit"></span>
                                </div>
                                <div class="form-group">
                                    <label class="" for="metaKeyword--edit">meta keyword</label>
                                    <input name="metaKeyword" type="text" class="form-control" id="metaKeyword--edit"
                                            value="${data.data.metaKeyword}"  required>
                                    <span class="invalid-feedback" id="invalid-feedback__metaKeyword--edit"></span>
                                </div>
                                <div class="form-group">
                                    <label class="" for="metaDescription--edit">meta description</label>
                                    <textarea name="metaDescription" class="form-control"
                                              id="metaDescription--edit">${data.data.metaDescription}</textarea>
                                    <span class="invalid-feedback" id="invalid-feedback__metaDescription--edit"></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="">
                                <label class="required-label min-w-20 mr-1">Is deleted</label>
                                <div class="form-check form-check-inline">
                                    <input name="isDeleted" value="true" ${data.data.isDeleted ? "checked" : ""}
                                           id="isDeleted--edit" class="form-check-input" type="checkbox">
                                </div>
                            </div>
                            <div class='${data.data.createdBy == null ? "d-none" : ""}'><label data-id="${data.data.createdBy ? data.data.createdBy.id : ""}">Người tạo:</label> <samp>${data.data.createdBy ? data.data.createdBy.fullName : ""}</samp></div>
                            <div class=""><label>Thời gian tạo:</label> <samp>${data.data.createdAt}</samp></div>
                            <div class='${data.data.modifiedBy == null ? "d-none" : ""}'><label data-id="${data.data.modifiedBy ? data.data.modifiedBy.id : ""}">Cập nhật lần cuối bởi:</label> <samp>${data.data.modifiedBy ? data.data.modifiedBy.fullName : ""}</samp></div>
                            <div class=""><label>Thời gian cập nhật lần cuối:</label> <samp>${data.data.modifiedAt}</samp></div>
                        </div>
                    </form>
                `)

                $("#modal-update-brand  .btn-update-brand").attr("form", "form-edit__brand")
                $('#modal-update-brand').modal('show');
            },
            error: function (error) {
                toastr.error(error.responseText);
            }
        });
    })

    $(document).on("click", ".btn-update-brand", function (event) {
        event.preventDefault();
        const form = $(this.form)
        let id = form.data("id");
        $.ajax({
            url: "/api/admin/brands/" + id,
            type: 'PUT',
            data: JSON.stringify(getFormData(form)),
            processData: false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success(data.message);
                $(`.list-brand tr[data-id=${id}]`).html(`
                     <td>${data.data.id}</td>
                    <td class="${data.data.isDeleted ? "is-deleted " : ""} text-capitalize text-nowrap">${data.data.name}</td>
                    <td>${data.data.productCount}</td>
                    <td>
                        <button class="btn btn-view-edit__brand" data-id="${data.data.id}">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-confirm-delete__brand" data-id="${data.data.id}">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </td>
                `)
                $('.modal').modal('hide');
                $('.invalid-feedback').hide()
                form[0].reset()
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
})
