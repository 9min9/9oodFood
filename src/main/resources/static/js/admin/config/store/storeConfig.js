var pagination = $('#store-config-pagination');
var locationArray = [];
var categoryArray = [];
var flag = true;

$(document).ready(function(){
    getSortTag();
    showStoreCount();
    drawPage(1);
});

function drawPage(page){
    if(flag){
        flag = false;
        var listIndex = page -1;

        $.ajax({
            type:"GET",
            url:"https://www.9oodfood.com/api/admin/forum/list",
            dataType:"JSON",
            contextType : "application/json",
            data: {locationOpt : locationArray, categoryOpt : categoryArray, page : listIndex},
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

function showStoreCount() {
    var locationList = [];
    var categoryList = ["RESTAURANT", "CAFE", "BAR", "ALL"];

    var tr = $("#store-count-table-body").find('tr');

    tr.each(function(i, e) {
        var location = $(this).attr('value');
        locationList.push(location);
    });

    $.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/admin/store/count/list",
        data: {location : locationList, category : categoryList},
        dataType:"json",
        success: function(jsonData){
            createStoreCountTable(jsonData);
        },
        error:function() {
        }
    });
}

function drawStoreListTable(jsonData) {
    $("#store-list-table").empty();
    createStoreListTable(jsonData);

    $("#store-list-count-caption").empty();
    createCaption(jsonData);
}

function setSortUrl(page) {
    var sortOpt = "";
    if(locationArray.length != 0) { sortOpt += "&locationOpt=" +locationArray; }
    if (categoryArray.length != 0) { sortOpt += "&categoryOpt=" +categoryArray; }

    if(sortOpt != "") {
        history.pushState({page, sortOpt} , "sort", location.pathname + "?page=" + page + sortOpt);
    } else {
        history.pushState({page} , "not sort", location.pathname + "?page=" + page);
    }
    return sortOpt;
}

$(window).on('popstate', function(event) {
    var state = event.originalEvent.state;

    if(state.page == null) {
        history.back();
    }
    drawPage(state.page);

});

$(document).on("click", "input[name='location-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();

    if($(this).is(':checked')) {
        locationArray.push(labelValue);
    } else {
        for(let i = 0; i < locationArray.length; i++) {
            if(locationArray[i] == labelValue)  {
                locationArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

$(document).on("click", "input[name='category-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();

    if($(this).is(':checked')) {
        categoryArray.push(labelValue);
    } else {
        for(let i = 0; i < categoryArray.length; i++) {
            if(categoryArray[i] == labelValue)  {
                categoryArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

function getSortTag() {
    $.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/admin/store/sort/tag",
        dataType:"json",

        success: function(data){
            locationData = data[0];
            categoryData = data[1];

            for(var i=0; i<locationData.length; i++) {
                createSortBtn(i, locationData);
            }

            for(var i=0; i<categoryData.length; i++) {
                createSortBtn(i, categoryData);
            }
        },
        error:function(){
        }
    })
}