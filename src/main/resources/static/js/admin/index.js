var storePagination = $('#store-config-pagination');
var userPagination = $('#user-config-pagination');
var locationArray;
var categoryArray;
var genderArray;
var ageArray;
var flag = true;

$(document).ready(function(){
    drawStoreList(1);
    drawUserList(1);
});

function drawStoreList(page) {
    var listIndex = page -1;
    $.ajax({
        type:"GET",
        url:"https://www.9oodfood.com/api/admin/forum/list",
        dataType:"json",
        data: {locationOpt : locationArray, categoryOpt : categoryArray, page : listIndex},
        success: function(jsonData){
            drawStoreListTable(jsonData);

        },
        error:function() {
        }
    });
}

function drawStoreListTable(jsonData) {
    $("#store-list-table").empty();
    createStoreListTable(jsonData);

    $("#store-list-count-caption").empty();
    createCaption(jsonData, "store");
}

function drawUserList(page) {
    var listIndex = page -1;
    $.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/admin/user/list",
        dataType:"json",
        data: {genderOpt : genderArray, ageOpt : ageArray, page : listIndex},
        success: function(jsonData){
            drawUserListTable(jsonData);

            for(var i=0; i<jsonData.content.length; i++) {
                $('#'+ jsonData.content[i].loginId +' option:contains('+ jsonData.content[i].role +')').attr('selected', true);
            }

        },
        error:function() {
        }
    });
}

function drawUserListTable(jsonData) {
    $("#user-list-table").empty();
    createUserListTable(jsonData);

    $("#user-list-count-caption").empty();
    createCaption(jsonData, "user");
}

$(window).on('popstate', function(event) {
    var state = event.originalEvent.state;

    if(state.page == null) {
        history.back();
    } else {
        if(state.sortOpt == null) {
            drawStorePage(state.page);
        } else {
            drawSortList(state.page);
        }
    }
});

$('#forum-delete-modal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var modal = $(this);

    var storeTitle = button.data('store-title');
    var forumId = button.data('forum-id');

    modal.find('.modal-title').text(storeTitle);
    modal.find('.modal-body').text("삭제하시겠습니까? (게시글 ID : " +forumId + ")");

    $('#forum-delete-chk-btn').on('click', function() {
        $.ajax({
            type:"POST",
            url:"https://www.9oodfood.com/api/user/forum/delete/"+forumId,
            success: function(){
                window.location.replace("https://www.9oodfood.com/admin");
            },
            error:function() {
            }
        });
    });
});

$(document).on("change", "select[class='userRole form-select w-50 me-4']", function(){
    var userId = $(this).attr('id');
    var originRole = $(this).attr('name');
    var clickedRole = $(this).val();

    if(originRole != clickedRole) {
        var btn = createUserUpdateBtn(userId, originRole, clickedRole);
        var td = $(this).parent().append(btn);

    } else {
        var updateBtnId = userId + "-role-update-btn";
        $('#'+updateBtnId).remove();
    }

});

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
                window.location.replace("https://www.9oodfood.com/admin");
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
                window.location.replace("https://www.9oodfood.com/admin");
            },
            error:function() {
            }
        });
    });
});

//-----------------------//

function createTdList(data) {
    var td = document.createElement('td');
    td.append(data);
    return td;
}

function createStoreListTable(jsonData) {
    var table = document.querySelector('#store-list-table');

    for(var i=0; i<jsonData.content.length; i++) {
        var tr = document.createElement('tr');
        var id = createTdList(jsonData.content[i].id);
        var title = createTitleList(jsonData.content[i].title, jsonData.content[i].id);
        var location = createTdList(jsonData.content[i].location);
        var category = createTdList(jsonData.content[i].category);
        var createDate = createTdList(jsonData.content[i].createDate);
        var updateBtn = createStoreUpdateBtn(jsonData.content[i].id, jsonData.content[i].title);
        var deleteBtn = createStoreDeleteBtn(jsonData.content[i].id, jsonData.content[i].title);

        tr.appendChild(id);
        tr.appendChild(title);
        tr.appendChild(location);
        tr.appendChild(category);
        tr.appendChild(createDate);
        tr.appendChild(updateBtn);
        tr.appendChild(deleteBtn);

        table.appendChild(tr);
    }

}

function createTitleList(title, id) {
    var th = document.createElement('td');

    var btn = document.createElement('a');
    var aHref = "/forum/detail/?forum="+id;
    var btnClass = "link-secondary";

    btn.setAttribute('class', btnClass)
    btn.setAttribute('href', aHref);
    btn.append(title);
    th.appendChild(btn);

    return th;
}

function createStoreUpdateBtn(forumId) {
    var td = document.createElement('td');
    var btn = document.createElement('a');
    var btnClass = "btn btn-outline-secondary";
    var href = "/forum/update/?forum=" + forumId;

    btn.setAttribute('class', btnClass);
    btn.setAttribute('href', href);

    btn.append("수정");
    td.appendChild(btn);

    return td;
}

function createStoreDeleteBtn(forumId, storeTitle) {
    var td = document.createElement('td');
    var btn = document.createElement('button');
    var btnClass = "btn btn-outline-secondary";

    var dataTarget = "#forum-delete-modal";
    var dataToggle = "modal";

    btn.setAttribute('class', btnClass);
    btn.setAttribute('data-bs-target', dataTarget);
    btn.setAttribute('data-bs-toggle', dataToggle);
    btn.setAttribute('data-store-title', storeTitle)
    btn.setAttribute('data-forum-id', forumId)

    btn.append("삭제");
    td.appendChild(btn);

    return td;
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
        var createDate = createTdList(jsonData.content[i].createDate);
        var role = createTdRole(jsonData.content[i]);
        var deleteBtn = createUserDeleteBtn(jsonData.content[i]);

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

function createUserUpdateBtn(userId, originRole, changedRole) {
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

function createUserDeleteBtn(jsonData) {
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

function createCaption(jsonData, flag) {
    var a = document.createElement('a');
    var aClass = "text-secondary";

    if(flag == "store") {
        var caption = $("#store-list-count-caption");
        var captionValue = "조회 결과 : " +jsonData.totalElements;
        var href = "/admin/store/config";

        a.setAttribute('href', href);
        a.setAttribute('class', aClass);
        a.append(captionValue+ "  | 더 보기");

        caption.append(a);
    }

    if(flag == "user") {
        var caption = $("#user-list-count-caption");
        var captionValue = "조회 결과 : " +jsonData.totalElements;
        var href = "/admin/user/config";

        a.setAttribute('href', href);
        a.setAttribute('class', aClass);
        a.append(captionValue+ "  | 더 보기");

        caption.append(a);
    }

}


