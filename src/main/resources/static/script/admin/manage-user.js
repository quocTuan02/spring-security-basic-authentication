$(document).ready(function () {
    const  info = JSON.parse(decodeURIComponent(getCookie("info")));

    $("#modal-add-new-user").on("hidden.bs.modal", function () {
        $("#modal-add-new-user form")[0].reset();
        $('.invalid-feedback').hide()
    });

    $(document).on("click", ".btn-add__user--new", function (event) {
        event.preventDefault();
        const form = $(this.form)
        $.ajax({
            url: '/api/admin/users',
            type: 'POST',
            data: JSON.stringify(getFormData(form)),
            processData: false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                let count = $('tbody.list-user > tr').length;
                toastr.success(data.message);
                $('.list-user').append(`
                    <tr data-id="${data.data.id}">
                        <td>${++count}</td>
                        <td>${data.data.id}</td>
                        <td class="${data.data.isDeleted ? "is-deleted " : ""} text-capitalize text-nowrap">${data.data.fullName}</td>
                        <td>${data.data.email}</td>
                        <td>${data.data.phone}</td>
                        <td>${data.data.address}</td>
                        <td class="${data.data.isDeleted ? "is-deleted " : ""} text-capitalize text-nowrap">[${data.data.role}]</td>
                        <td class="text-right">
                            <button type="button" class="btn btn-edit__user" data-id="${data.data.id}">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button type="button" class="btn btn-confirm-delete__user" data-id="${data.data.id}">
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

    $(document).on("click", ".btn-view-edit__user", function (event) {
        let id = $(this).data('id');
        $.ajax({
            url: '/api/admin/users/' + id,
            type: 'GET',
            success: function (data) {
                $("#modal-update-user .modal-body").html(`
                    <form id="form-edit__user" data-id="${id}">
                        <div class="form-group">
                            <label class="required-label" for="full-name--edit">Họ tên</label>
                            <input name="fullName" required value="${data.data.fullName}"
                                   type="text" minlength="2" maxlength="255"
                                   class="form-control" id="full-name--edit">
                            <span class="invalid-feedback" id="invalid-feedback__fullName--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="required-label" for="username--new">Tên tài khoản</label>
                            <input name="username" required type="text" 
                                   value="${data.data.username}"
                                   placeholder="Username" class="form-control"
                                   id="username--new">
                            <span class="invalid-feedback" id="invalid-feedback__username--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="required-label" for="email--edit">Email</label>
                            <input name="email" value="${data.data.email}" required type="email" class="form-control" id="email--edit">
                            <span class="invalid-feedback" id="invalid-feedback__email--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="" for="phone--edit">SĐT</label>
                            <input name="phone" value="${data.data.phone}"
                                   type="tel" pattern="0[1-9][0-9]{8}"
                                   class="form-control"
                                   id="phone--edit">
                            <span class="invalid-feedback" id="invalid-feedback__phone--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="" for="address--edit">Địa chỉ</label>
                            <input name="address" minlength="5" maxlength="511"
                                   type="text" value="${data.data.address}"
                                   class="form-control" id="address--edit">
                            <span class="invalid-feedback" id="invalid-feedback__address--edit"></span>
                        </div>
                        <div class="form-group">
                            <label class="required-label min-w-20 mr-1">role</label>
                            ${info.role === 'ADMIN' ? `
                                <div class="form-check form-check-inline">
                                    <input name="role" value="ADMIN" required ${data.data.role === "ADMIN" ? "checked" : ""}
                                           id="role-admin--edit" class="form-check-input" type="radio">
                                    <label class="form-check-label" for="role-admin--edit">ADMIN</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input name="role" value="EMPLOYEE" required ${data.data.role === "EMPLOYEE" ? "checked" : ""}
                                           id="role-employee--edit" class="form-check-input" type="radio">
                                    <label class="form-check-label" for="role-employee--edit">EMPLOYEE</label>
                                </div>
                            ` : ``}
                            <div class="form-check form-check-inline">
                                <input name="role" value="USER" required ${data.data.role === "USER" ? "checked" : ""}
                                       id="role-user--edit" class="form-check-input" type="radio">
                                <label class="form-check-label" for="role-user--edit">USER</label>
                            </div>
                            <div class=""><span class="invalid-feedback" id="invalid-feedback__role--edit"></span></div>
                        </div>
                        <div class="form-group">
                            ${info.role === 'ADMIN' ? `
                                <div class="">
                                    <label class="required-label min-w-20 mr-1">Is deleted</label>
                                    <div class="form-check form-check-inline">
                                        <input name="isDeleted" value="true" ${data.data.isDeleted ? "checked" : ""}
                                               id="isDeleted--edit" class="form-check-input" type="checkbox">
                                    </div>
                                </div>
                            ` : ``}
                            <div class='${data.data.createdBy == null ? "d-none" : ""}'><label data-id="${data.data.createdBy ? data.data.createdBy.id : ""}">Người tạo:</label> <samp>${data.data.createdBy ? data.data.createdBy.fullName : ""}</samp></div>
                            <div class=""><label>Thời gian tạo:</label> <samp>${data.data.createdAt}</samp></div>
                            <div class='${data.data.modifiedBy == null ? "d-none" : ""}'><label data-id="${data.data.modifiedBy ? data.data.modifiedBy.id : ""}">Cập nhật lần cuối bởi:</label> <samp>${data.data.modifiedBy ? data.data.modifiedBy.fullName : ""}</samp></div>
                            <div class=""><label>Thời gian cập nhật lần cuối:</label> <samp>${data.data.modifiedAt}</samp></div>
                        </div>
                    </form>
                `)

                $("#modal-update-user  .btn-update-user").attr("form", "form-edit__user")
                $('#modal-update-user').modal('show');
            },
            error: function (error) {
                toastr.error(error.responseText);
            }
        });
    })

    $(document).on("click", ".btn-update-user", function (event) {
        event.preventDefault();
        const form = $(this.form)
        let id = form.data("id");
        $.ajax({
            url: "/api/admin/users/" + id,
            type: 'PUT',
            data: JSON.stringify(getFormData(form)),
            processData: false,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success(data.message);
                let count = $(`.list-user tr[data-id="${id}"] > td:nth-child(1)`).html()
                $(`.list-user tr[data-id="${id}"]`).html(`
                    <td>${count}</td>
                    <td>${data.data.id}</td>
                    <td class="${data.data.isDeleted ? "is-deleted " : ""} text-capitalize text-nowrap">${data.data.fullName}</td>
                    <td>${data.data.email}</td>
                    <td>${data.data.phone}</td>
                    <td>${data.data.address}</td>
                    <td class="${data.data.isDeleted ? "is-deleted " : ""} text-capitalize text-nowrap">${data.data.role}</td>
                    <td class="text-right">
                        <button type="button" class="btn btn-view-edit__user" data-id="${data.data.id}">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-confirm-delete__user" data-id="${data.data.id}">
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

    $(document).on("click", ".btn-confirm-delete__user", function (event) {
        let id = $(this).data("id")
        $('#delete-user-id').val(id);
        $('#modal-delete-user').modal('show');
    })

    $(document).on("click", ".btn-delete-user", function (event) {
        let id = $('#delete-user-id').val();
        $.ajax({
            url: '/api/admin/users/' + id,
            type: 'DELETE',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                toastr.success(data.message);
                $(`.list-user tr[data-id=${id}]`).remove();
                $('.modal').modal('hide');
                $('#delete-user-id').val('');
            },
            error: function (data) {
                toastr.error(data.responseText);
            }
        });
    })

})