function getFormData($form) {
    let data_form = $form.serializeArray();
    let indexed_array = {};
    let temp_array = {}

    $.map(data_form, function (n, i) {
        if (n['name'].endsWith('[]')) {
            if (indexed_array[n['name'].replace('[]', '')]) {
                indexed_array[n['name'].replace('[]', '')] = [...indexed_array[n['name'].replace('[]', '')], n['value']]
            } else
                indexed_array[n['name'].replace('[]', '')] = [n['value']]
        } else if (n['name'].split(".").length === 2) {
            let arr = n['name'].split(".")

            if (temp_array[arr[0]])
                temp_array[arr[0]][`${arr[1]}`] = n['value']
            else
                temp_array[arr[0]] = JSON.parse(`{"${arr[1]}": "${n['value']}"}`)
        } else
            indexed_array[n['name']] = n['value'];
    });

    Object.keys(temp_array).forEach(n => {
        let field = n.replace(/\[\d]/i, "")
        if (indexed_array[field])
            indexed_array[field] = [...indexed_array[field], temp_array[n]]
        else indexed_array[field] = [temp_array[n]]
    })

    return indexed_array;
}

function serialize(obj) {
    let str = [];
    for (let p in obj)
        if (obj.hasOwnProperty(p)) {
            str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
    return str.join("&");
}

function populate(frm, data) {
    $.each(data, function (key, value) {
        let ctrl = $('[name=' + key + ']', frm);
        switch (ctrl.prop("type")) {
            case "radio":
            case "checkbox":
                ctrl.each(function () {
                    if ($(this).attr('value') === value) $(this).attr("checked", value);
                });
                break;
            default:
                ctrl.val(value);
        }
    });
}

function convertToSlug(str) {
    return str.trim()
        .toLowerCase()
        //Đổi ký tự có dấu thành không dấu
        .replace(/[áàảạãăắằẳẵặâấầẩẫậ]/gi, 'a')
        .replace(/[éèẻẽẹêếềểễệ]/gi, 'e')
        .replace(/[iíìỉĩị]/gi, 'i')
        .replace(/[óòỏõọôốồổỗộơớờởỡợ]/gi, 'o')
        .replace(/[úùủũụưứừửữự]/gi, 'u')
        .replace(/[ýỳỷỹỵ]/gi, 'y')
        .replace(/đ/gi, 'd')
        //Xóa các ký tự đặt biệt
        .replace(/[`~!@#|$%^&*()+=,.\/?><'":;_]/gi, '')
        //Đổi khoảng trắng thành ký tự gạch ngang
        .replace(/ +/g,'-');
}