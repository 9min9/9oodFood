var pagination = $('#user-config-pagination');
var genderArray = [];
var ageArray = [];
var flag = true;

$(document).ready(function(){
    getSortTag();
    showUserCount();
    drawPage(1);
});

function drawPage(page){
    if(flag){
        flag = false;
        var listIndex = page -1;
        $.ajax({
            type:"POST",
            url:"https://www.9oodfood.com/api/admin/user/list",
            dataType:"JSON",
            contextType : "application/json",
            data: {genderOpt : genderArray, ageOpt : ageArray, page : listIndex},
            success: function(jsonData){
                setPagination();

                if(jsonData.totalPages != 0) {
                    pagination.twbsPagination("changeTotalPages", jsonData.totalPages , page);
                }

            },
            complete: function(){
                flag = true;
            },
            error:function(){
            }
        });
    }
}

function setPagination() {
    pagination.twbsPagination('destroy');
    var opt = initPagination();
    pagination.twbsPagination(opt);
}

function initPagination() {
    var options = {
        visiblePages: 5,	// 하단에서 한번에 보여지는 페이지 번호 수
        startPage : 1, // 시작시 표시되는 현재 페이지
        initiateStartPageClick: true,	// 플러그인이 시작시 페이지 버튼 클릭 여부 (default : true)

        first : "<<",	// 페이지네이션 버튼중 처음으로 돌아가는 버튼에 쓰여 있는 텍스트
        prev : "<",	// 이전 페이지 버튼에 쓰여있는 텍스트
        next : ">",	// 다음 페이지 버튼에 쓰여있는 텍스트
        last : ">>",	// 페이지네이션 버튼중 마지막으로 가는 버튼에 쓰여있는 텍스트
        nextClass : "page-item next",	// 이전 페이지 CSS class
        prevClass : "page-item prev",	// 다음 페이지 CSS class
        lastClass : "page-item last",	// 마지막 페이지 CSS calss
        firstClass : "page-item first",	// 첫 페이지 CSS class
        pageClass : "page-item",	// 페이지 버튼의 CSS class
        activeClass : "active",	// 클릭된 페이지 버튼의 CSS class
        disabledClass : "disabled",	// 클릭 안된 페이지 버튼의 CSS class
        anchorClass : "page-link",	//버튼 안의 앵커에 대한 CSS class

        onPageClick: function (event, page) {
            var sortOpt = setSortUrl(page);
            drawList(page);
        }
    };
    return options;
}


function drawList(page) {
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

//-----------------------//

function showUserCount() {
    var genderList = ["MALE", "FEMALE", "ALL"];
    var ageList = [];

    var age = $("#user-count-table-head").find('.th-age');

    for(var i=0; i< age.length; i++) {
        var ageData = $(age[i]).text();
        ageList.push(ageData);
    }

    $.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/admin/user/count/list",
        data: {genderOpt : genderList, ageOpt : ageList},
        dataType:"json",
        success: function(jsonData){
            createUserCountTable(jsonData);
        },
        error:function() {
        }
    });
}

function drawUserListTable(jsonData) {
    $("#user-list-table").empty();
    createUserListTable(jsonData);

    $("#user-list-count-caption").empty();
    createCaption(jsonData);
}

function setSortUrl(page) {
    var sortOpt = "";

    if(genderArray.length != 0) { sortOpt += "&gender=" +genderArray; }
    if (ageArray.length != 0) { sortOpt += "&age=" +ageArray; }

    if(sortOpt != "") {
        history.pushState({page, sortOpt} , "sort", location.pathname + "?page=" + page + sortOpt);
    } else {
        history.pushState({page} , "not sort", location.pathname + "?page=" + page);
    }
    return sortOpt;
}

function getSortTag() {
    var gender = $("#user-count-table-body").find('.tr-gender');
    var age = $("#user-count-table-head").find('.th-age');


    for(var i=0; i<gender.length -1; i++) {
        var genderData = $(gender[i]).find('th:eq(0)').text();
        createSortBtn(i, genderData, "gender");
    }

    for(var i=0; i< age.length -1; i++) {
        var ageData = $(age[i]).text();
        createSortBtn(i, ageData, "age");
    }
}

$(window).on('popstate', function(event) {
    var state = event.originalEvent.state;
    if(state.page == null) {
        history.back();
    } else {
        drawPage(state.page);

    }
});

$(document).on("click", "input[name='gender-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();

    if($(this).is(':checked')) {
        genderArray.push(labelValue);
    } else {
        for(let i = 0; i < genderArray.length; i++) {
            if(genderArray[i] == labelValue)  {
                genderArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

$(document).on("click", "input[name='age-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();

    if($(this).is(':checked')) {
        ageArray.push(labelValue);
    } else {
        for(let i = 0; i < ageArray.length; i++) {
            if(ageArray[i] == labelValue)  {
                ageArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

$(document).on("change", "select[class='userRole form-select w-50 me-4']", function(){
    var userId = $(this).attr('id');
    var originRole = $(this).attr('name');
    var clickedRole = $(this).val();

    if(originRole != clickedRole) {
        var btn = createUpdateBtn(userId, originRole, clickedRole);
        var td = $(this).parent().append(btn);

    } else {
        var updateBtnId = userId + "-role-update-btn";
        $('#'+updateBtnId).remove();
    }

});



