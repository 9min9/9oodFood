function createCaption(jsonData) {
    var caption = $("#user-list-count-caption");
    var captionValue = "조회 결과 : " +jsonData.totalElements;
    caption.append(captionValue);
}

function createUserCountTable(jsonData) {
    var tr = $("#user-count-table-body").find('tr');
    tr.each(function(i, e) {
        var gender = $(this).attr('value');
        var td = $(this).find('td');
        td.each(function(j) {
            td[j].append(jsonData[gender][j]);
        });
    });
}

function createUserListTable(jsonData) {
    var table = document.querySelector('#user-list-table');

    for (var i=0; i<jsonData.content.length; i++) {
        var tr = document.createElement('tr');
        var id = createTdList(jsonData.content[i].loginId);
        var nickName = createTdList(jsonData.content[i].nickName);
        var name = createTdList(jsonData.content[i].name);
        var gender = createTdList(jsonData.content[i].gender);
        var age = createTdAge(jsonData.content[i].age);
        var role = createTdRole(jsonData.content[i]);
        var createDate = createTdList(jsonData.content[i].createDate);
        var deleteBtn = createDeleteBtn(jsonData.content[i]);

        tr.append(id);
        tr.append(nickName);
        tr.append(name);
        tr.append(gender);
        tr.append(age);
        tr.append(createDate);
        tr.append(role);
        tr.append(deleteBtn);
        table.appendChild(tr);

    }
}

function createTdList(data) {
    var td = document.createElement('td');
    td.append(data);
    return td;
}

function createTdAge(age) {
    var td = document.createElement('td');

    var data ="";
    if(age >=10 && age <20) {
        data = "10대"
    } else if (age >=20 && age <30) {
        data = "20대"
    } else if (age >=30 && age <40) {
        data = "30대"
    } else if (age >=40 && age <50) {
        data = "40대"
    }  else if (age >=50) {
        data = "50대 이상"
    }

    td.append(data);
    return td;

}

function createTdRole(jsonData) {
    var td = document.createElement('td');
    var tdId = jsonData.id;
    var select = document.createElement('select');
    var tdClass = "d-flex justify-content-start";
    var selectId = jsonData.loginId;
    var selectName = jsonData.role

    var opt1 = document.createElement('option');
    var opt2 = document.createElement('option');
    var selectClass = "userRole form-select w-50 me-4";
    opt1.append("ADMIN");
    opt2.append("USER");

    select.append(opt1);
    select.append(opt2);
    select.setAttribute('id', selectId)
    select.setAttribute('class', selectClass);
    select.setAttribute('name', selectName);

    td.setAttribute('id', tdId);
    td.setAttribute('class', tdClass);
    td.append(select);


    return td;
}

function createUpdateBtn(userId, originRole, changedRole) {
    var id = userId + "-role-update-btn";
    var btn = document.createElement('a');
    var btnClass = "btn btn-outline-secondary";
    var dataTarget = "#user-update-modal";
    var dataToggle = "modal";

    btn.setAttribute('id', id);
    btn.setAttribute('class', btnClass);
    btn.setAttribute('data-bs-target', dataTarget);
    btn.setAttribute('data-bs-toggle', dataToggle);
    btn.setAttribute('data-user-login-id', userId);
    btn.setAttribute('data-user-origin-role', originRole);
    btn.setAttribute('data-user-changed-role', changedRole);

    btn.append("수정");

    return btn;
}

function createDeleteBtn(jsonData) {
    var td = document.createElement('td');
    var btn = document.createElement('button');
    var btnClass = "btn btn-outline-secondary";
    var dataTarget = "#user-delete-modal";
    var dataToggle = "modal";

    btn.setAttribute('class', btnClass);
    btn.setAttribute('data-bs-target', dataTarget);
    btn.setAttribute('data-bs-toggle', dataToggle);
    btn.setAttribute('data-user-login-id', jsonData.loginId)
    btn.setAttribute('data-user-id', jsonData.id)

    btn.append("삭제");
    td.appendChild(btn);

    return td;

}

$('#user-delete-modal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var modal = $(this);

    var userLoginId = button.data('user-login-id');
    var userId = button.data('user-id');

    modal.find('.modal-title').text(userLoginId);
    modal.find('.modal-body').text("삭제하시겠습니까? (Member ID : " +userId + ")");

    $('#user-delete-chk-btn').on('click', function() {
        $.ajax({
            type:"POST",
            url:"https://www.9oodfood.com/api/admin/user/delete/"+userId,
            success: function(){
                window.location.replace("https://www.9oodfood.com/admin/user/config");
            },
            error:function() {
            }
        });
    });
});

$('#user-update-modal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var modal = $(this);

    var userLoginId = button.data('user-login-id');
    var originRole = button.data('user-origin-role');
    var changedRole = button.data('user-changed-role');

    modal.find('.modal-title').text("ID : "+userLoginId);
    modal.find('.modal-body').html("권한 : " +originRole+ " -> " +changedRole+"<br>수정하시겠습니까?");

    $('#user-update-chk-btn').on('click', function() {
        $.ajax({
            type:"POST",
            url:"https://www.9oodfood.com/api/admin/user/role/update",
            data: {username : userLoginId, changedRole : changedRole},
            success: function(){
                window.location.replace("https://www.9oodfood.com/admin/user/config");
            },
            error:function() {
            }
        });
    });
});

function createSortBtn(i, data, flag) {
    var sortInput = createInput(i, data, flag);
    var sortLabel = createLabel(i, data, flag);

    if(flag == "gender") {
        var genderSortZone = document.querySelector(".gender-sort-btn");
        genderSortZone.appendChild(sortInput);
        genderSortZone.appendChild(sortLabel);

    } else if (flag == "age") {
        var categorySortZone = document.querySelector(".age-sort-btn");
        categorySortZone.appendChild(sortInput);
        categorySortZone.appendChild(sortLabel);
    }
}

function createInput(i, data, flag) {
    var inputType = "checkbox";
    var inputClass;
    var inputName;
    var inputId;

    if(flag == "gender") {
        inputClass = "sort-opt btn-check";
        inputName = "gender-opt[]";
        inputId = "gender-sort-opt" +i;

    } else if (flag == "age") {
        inputClass = "sort-opt btn-check";
        inputName = "age-opt[]";
        inputId = "age-sort-opt" +i;
    }

    var input = document.createElement("input");
    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('name', inputName);
    input.setAttribute('type', inputType);

    return input;
}

function createLabel(i, data, flag) {
    var input = createInput(i, data);
    var labelClass = "btn btn-outline-secondary me-2 mb-2";
    var labelName;
    var inputId;

    if(flag == "gender") {
        inputId = "gender-sort-opt" +i;
        labelName = "gender-data"

    } else if(flag == "age") {
        inputId = "age-sort-opt" +i;
        labelName = "age-data"
    }

    var label = document.createElement("label");
    label.setAttribute('class', labelClass);
    label.setAttribute('name', labelName);
    label.setAttribute('for', inputId);
    label.append(data);

    return label;
}