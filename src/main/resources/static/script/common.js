
function isNumeric(value) {
    return /^-?\d+$/.test(value);
}

function isObject(obj) {
    return obj !== null && typeof obj === 'object' && !Array.isArray(obj)
}

function onlyNumbers(array) {
    return array.every(element => {
        return isNumeric(element);
    });
}

function formatObject(obj) {
    if (isObject(obj)) {
        let rs = {};
        const c = onlyNumbers(Object.keys(obj));
        Object.entries(obj).forEach(([field, value]) => {
            if (c) {
                if (!Array.isArray(rs)) {
                    rs = [];
                }
                rs.push(formatObject(value))
            } else rs[field] = formatObject(value)
        })
        return rs;
    }
    return obj;
}

$.fn.getForm2obj = function () {
    const _ = {};
    $.map(this.serializeArray(), function (n) {
        const keys = n.name.match(/[a-zA-Z0-9_]+|(?=\[])/g);
        if (keys.length > 1) {
            let tmp = _;
            let pop = keys.pop();
            for (let i = 0; i < keys.length, j = keys[i]; i++) {
                tmp[j] = (!tmp[j] ? (pop === '') ? [] : {} : tmp[j]), tmp = tmp[j];
            }
            if (pop === '') {
                tmp = (!Array.isArray(tmp) ? [] : tmp), tmp.push(n.value);
            } else tmp[pop] = n.value;
        } else {
            _[keys.pop()] = n.value;
        }
    });

    return formatObject(_);
}

function getFormData($form) {
    return $form.getForm2obj()
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
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