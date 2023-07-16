var searchParams = new URLSearchParams(location.search);
const searchPath = location.pathname.split('/');
const urlCategory = searchPath[3];

var pagination = $('#store-pagination');
var flag = true;
var moodArray = [];
var priceArray = [];
var subCategoryArray = [];
var locationArray = [];


$(document).ready(function(){
    getSortTag();
    drawPage(1);
});

function drawPage(page){
    if(flag){
        flag = false;
        var storeIndex = page -1;
        var username = $('#login-user-name').text();

        $.ajax({
            type:"post",
            async : false,
            url: "https://www.9oodfood.com/api/user/like/forum/list/sort/" + urlCategory,
            data: {username : username, moodOpt : moodArray, priceOpt : priceArray, subCategoryOpt : subCategoryArray, locationOpt : locationArray, page : storeIndex},
            success: function(jsonData) {
                setPagination();
                pagination.twbsPagination("changeTotalPages", jsonData.totalPages, page);
            },
            complete: function(){
                flag = true;
            },
            error: function() {
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
            drawStore(page);
        }
    };
    return options;
}

function drawStore(page) {
    var storeIndex = page -1;
    var username = $('#login-user-name').text();
    $.ajax({
        type:"post",
        async : false,
        url: "https://www.9oodfood.com/api/user/like/forum/list/sort/" + urlCategory,
        data: {username : username, moodOpt : moodArray, priceOpt : priceArray, subCategoryOpt : subCategoryArray, locationOpt : locationArray, page : storeIndex},
        success: function(jsonData) {
            if(jsonData.totalPages == 0) {
                alert("검색 결과가 없습니다.");
            }
            drawContents(jsonData);
            checkedFavorite();
        },
        complete: function(){
            flag = true;
        },
        error: function() {
        }
    });
}

function drawContents(jsonData) {
    $("#page-title-row").empty();
    createPageTitle();

    $("#store-row").empty();
    for(var i=0; i<jsonData.content.length; i++) {
        createStoreCardZone(jsonData, i);
    }
}

function setSortUrl(page) {
    var sortOpt = "";
    if(moodArray.length != 0) { sortOpt += "&moodOpt=" +moodArray; }
    if (priceArray.length != 0) { sortOpt += "&priceOpt=" +priceArray; }
    if (subCategoryArray.length != 0) { sortOpt += "&subcategoryOpt=" +subCategoryArray; }
    if (locationArray.length != 0) { sortOpt += "&locationOpt=" +locationArray; }

    if(sortOpt != "") {
        history.pushState({page, sortOpt} , "sort", location.pathname + "?page=" + page + sortOpt);
    } else {
        history.pushState({page} , "not sort", location.pathname + "?page=" + page);
    }
    return sortOpt;
}

// Sort Tag Btn  로직 //
$(document).on("click", "input[name='mood-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();
    if($(this).is(':checked')) {
        moodArray.push(labelValue);
    } else {    //체크가 해제되었을 때 해당 요소를 배열에서 삭제
        for(let i = 0; i < moodArray.length; i++) {
            if(moodArray[i] == labelValue)  {
                moodArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

$(document).on("click", "input[name='price-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();
    if($(this).is(':checked')) {
        priceArray.push(labelValue);
    } else {    //체크가 해제되었을 때 해당 요소를 배열에서 삭제
        for(let i = 0; i < priceArray.length; i++) {
            if(priceArray[i] == labelValue)  {
                priceArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

$(document).on("click", "input[name='sub-category-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();
    if($(this).is(':checked')) {
        subCategoryArray.push(labelValue);
    } else {    //체크가 해제되었을 때 해당 요소를 배열에서 삭제
        for(let i = 0; i < subCategoryArray.length; i++) {
            if(subCategoryArray[i] == labelValue)  {
                subCategoryArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

$(document).on("click", "input[name='location-opt[]']:checkbox", function(){
    var labelValue = $(this).next().text();
    if($(this).is(':checked')) {
        locationArray.push(labelValue);
    } else {    //체크가 해제되었을 때 해당 요소를 배열에서 삭제
        for(let i = 0; i < locationArray.length; i++) {
            if(locationArray[i] == labelValue)  {
                locationArray.splice(i, 1);
            }
        }
    }
    drawPage(1);
});

// 뒤로 가기 로직 //
$(window).on('popstate', function(event) {
    var state = event.originalEvent.state;
    if(state.page == null) {
        history.back();
    } else {
        drawPage(state.page)
    }
});

$(document).on("click", "input[name='like']:checkbox", function(){
    var forumId = $(this).val();
    var userId = $('#login-user-name').text();
    var iconId = "#like-icon-" +forumId;

    if($(this).is(':checked')) {
        $(iconId).attr('data-prefix', 'fas');
         $.ajax({
            type:"post",
            url: "https://www.9oodfood.com/api/user/like",
            contextType : "application/json",
            data: {userId : userId, forumId : forumId},
            success: function(jsonData){
            },
            error:function(){
            }
        });

    } else {
        $(iconId).attr('data-prefix', 'far');

        $.ajax({
            type:"post",
            url: "https://www.9oodfood.com/api/user/unlike",
            contextType : "application/json",
            data: {userId : userId, forumId : forumId},

            success: function(jsonData){
            },
            error:function(){
            }
        });
    }

});

function checkedFavorite() {
    var card = $(".store-card-zone");
    var cardLength = card.length;
    var userId = $('#login-user-name').text();
    var forumCard = card.find('.card');
    var forumIds = [];

    forumCard.each(function() {
        var forumId = $(this).attr('id').slice(6);
        forumIds.push(forumId);
    });

    $.ajax({
        type:"POST",
        url: "https://www.9oodfood.com/api/user/like/check",
        dataType:"JSON",
        contextType : "application/json",
        data: {userId : userId, forumIds : forumIds},

        success: function(jsonData){
            for(var i=0; i<jsonData.favoriteAndForumIds.length; i++) {
                var iconId = "#like-icon-"+jsonData.favoriteAndForumIds[i].forumId;
                var inputId = "#like-input-"+jsonData.favoriteAndForumIds[i].forumId;
                $(iconId).attr('data-prefix', 'fas');
                $(inputId).attr("checked", true);
            }
        },
        error:function(error){
         }
    });
}