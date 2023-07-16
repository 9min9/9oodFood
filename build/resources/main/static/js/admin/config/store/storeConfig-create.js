function createCaption(jsonData) {
    var caption = $("#store-list-count-caption");
    var captionValue = "조회 결과 : " +jsonData.totalElements;
    caption.append(captionValue);
}

function createStoreCountTable(jsonData) {
    var tr = $("#store-count-table-body").find('tr');
    tr.each(function(i, e) {
        var location = $(this).attr('value');
        var td = $(this).find('td');

        td.each(function(j) {
            td[j].append(jsonData[location][j]);
        });
    });
}

function createStoreListTable(jsonData) {
    var table = document.querySelector('#store-list-table');

    for(var i=0; i<jsonData.content.length; i++) {
        var tr = document.createElement('tr');
        var id = createIdList(jsonData.content[i].id);
        var title = createTitleList(jsonData.content[i].title, jsonData.content[i].id);
        var location = createLocationList(jsonData.content[i].location);
        var category = createCategoryList(jsonData.content[i].category);
        var createDate = createDateList(jsonData.content[i].createDate);
        var updateBtn = createUpdateBtn(jsonData.content[i].id, jsonData.content[i].title);
        var deleteBtn = createDeleteBtn(jsonData.content[i].id, jsonData.content[i].title);

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

function createIdList(id) {
    var td = document.createElement('td');
    td.append(id);
    return td;
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
//    th.append(title);
    return th;
}

function createLocationList(location) {
    var td = document.createElement('td');
    td.append(location);
    return td;
}

function createCategoryList(category) {
    var td = document.createElement('td');
    td.append(category);
    return td;
}

function createDateList(createDate) {
    var td = document.createElement('td');
    td.append(createDate);
    return td;
}

function createUpdateBtn(forumId) {
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

function createDeleteBtn(forumId, storeTitle) {
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

// Forum Delete Check Modal
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
                window.location.replace("https://www.9oodfood.com/admin/store/config");
            },
            error:function() {
            }
        });
    });
});

function createSortBtn(i, data) {
    var sortInput = createInput(i, data);
    var sortLabel = createLabel(i, data);

    if(data == locationData) {
        var locationSortZone = document.querySelector(".location-sort-btn");
        locationSortZone.appendChild(sortInput);
        locationSortZone.appendChild(sortLabel);

    } else if (data == categoryData) {
        var categorySortZone = document.querySelector(".category-sort-btn");
        categorySortZone.appendChild(sortInput);
        categorySortZone.appendChild(sortLabel);
    }
}

function createInput(i, data) {
    var inputType = "checkbox";
    var inputClass;
    var inputName;
    var inputId;

    if(data == locationData) {
        inputClass = "sort-opt btn-check";
        inputName = "location-opt[]";
        inputId = "location-sort-opt" +i;

    } else if (data == categoryData) {
        inputClass = "sort-opt btn-check";
        inputName = "category-opt[]";
        inputId = "category-sort-opt" +i;
    }

    var input = document.createElement("input");
    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('name', inputName);
    input.setAttribute('type', inputType);

    return input;
}

function createLabel(i, data) {
    var input = createInput(i, data);
    var labelClass = "btn btn-outline-secondary me-2 mb-2";
    var labelName;
    var inputId;

    if(data == locationData) {
        inputId = "location-sort-opt" +i;
        labelName = "location-data"

    } else if(data == categoryData) {
        inputId = "category-sort-opt" +i;
        labelName = "category-data"
    }

    var label = document.createElement("label");
    label.setAttribute('class', labelClass);
    label.setAttribute('name', labelName);
    label.setAttribute('for', inputId);
    label.append(data[i]);

    return label;
}