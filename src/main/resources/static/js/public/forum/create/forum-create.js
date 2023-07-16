var moodTagData;
var priceTagData;
var categoryTagData;

var moodArray=[];
var subCategoryArray=[];


$(document).ready(function() {
    selectMapList();
    createStatus();

    var statusText = $('#status-value-text').text();



    //점수 슬라이더

    $('#status-slider').on("input", function() {
        var statusValue = $('#status-value');
        var statusText = $('#status-value-text');
        statusValue.text(this.value);

        $('#checkStatus').attr('value', true);

        if (statusValue.text() == 0) {
            $('#status-value').hide();
            $('#status-value-text').text("가 보고 싶은 곳");

        } else {
            $('#status-value').show();
            $('#status-value-text').text("점");
        }

    });
});

// 1. 주소 입력 및 주소 입력 버튼 Event //
// 주소 검색 Enter //
$('#inputStoreName').on('keydown', function(e) {
    var keyCode = e.which;
    if (keyCode === 13) { // Enter Key
        var inputStoreName = $('#inputStoreName').val();
        searchStore(inputStoreName);
    }
});

// 주소 검색 Click //
$('#submitBtn-addon').on('click', function(e) {
    e.preventDefault();
    var inputStoreName = $('#inputStoreName').val();
    searchStore(inputStoreName);
});

// 2. naverSearchApi()을 호출하고 Table을 통해 List //
function searchStore(inputStoreName) {
    $.ajax({
        type:"get",
        url:"https://www.9oodfood.com/api/public/server/naver/search/"+inputStoreName,
        dataType:"json",

        success: function(jsonData){
            createTable(jsonData);

        },
        error:function(){
            alert("다시 입력해주세요.")
        }
    })
}

// 3.테이블의 btn을 클릭 시 발생하는 Event //
//btn 클릭 시 해당 주소와 카테고리를 전달//
$(document).on('click','.selectBtn', function() {
    var selectRow = $(this).closest('tr');
    var selectStoreAddress = selectRow.children("td.storeAddress").children().val();
    var selectStoreCategory = selectRow.children("td.storeCategory").children().val();

    setSelectStore(selectRow);                          //선택된 가게의 HTML 태그를 수정하여 InsertStoreForm에 전달
    searchAddressToCoordinate(selectStoreAddress);      //선택된 가게의 주소를 함수에 전달하여 Naver MAP API를 작동
    getStoreInfo(selectStoreCategory);            //선택된 가게의 카테고리를 함수에 전달하여 Tag Button을 생성
});

function setSelectStore(selectRow) {
    //1. <input>의 클래스를 조회
    var storeTitle= document.querySelectorAll(".title");
    var storeAddress = document.querySelectorAll(".address");
    var storeRoadAddress = document.querySelectorAll(".roadAddress");
    var storeCategory = document.querySelectorAll(".category");

    //2. <input>의 클래스를 조회하여 name을 초기화
    for(var i=0; i<storeTitle.length; i++) {
        storeTitle[i].setAttribute('name', "")
        storeAddress[i].setAttribute('name', "");
        storeRoadAddress[i].setAttribute('name', "");
        storeCategory[i].setAttribute('name', "");
    }

    //3. 선택된 <input>의 name 설정
    var selectTitle = selectRow.children('td.storeTitle').children('input');
    var selectAddress = selectRow.children('td.storeAddress').children('input');
    var selectRoadAddress = selectRow.children('td.storeRoadAddress').children('input');
    var selectCategory = selectRow.children('td.storeCategory').children('input');

    selectTitle.attr('name', 'title');
    selectAddress.attr('name', 'address');
    selectRoadAddress.attr('name', 'roadAddress');
    selectCategory.attr('name', 'category');
}

/** Map */
// 4. 선택된 주소를 API에 적용 //
// 지도에 주소 입력 //
function insertAddress(address, latitude, longitude) {
	var map = new naver.maps.Map('selectStoreMap', {
	    center: new naver.maps.LatLng(longitude, latitude),
	    zoom: 14
	});
    var marker = new naver.maps.Marker({
        map: map,
        position: new naver.maps.LatLng(longitude, latitude),
    });
}

//검색한 주소의 정보를 insertAddress 함수로 넘겨준다.
function searchAddressToCoordinate(address) {
    naver.maps.Service.geocode({
        query: address
    }, function(status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
            return alert('Something Wrong!');
        }
        if (response.v2.meta.totalCount === 0) {
            return alert('올바른 주소를 입력해주세요.');
        }
        var htmlAddresses = [],
            item = response.v2.addresses[0],
            point = new naver.maps.Point(item.x, item.y);
        if (item.roadAddress) {
            htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
        }
        if (item.jibunAddress) {
            htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
        }
        if (item.englishAddress) {
            htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
        }

        insertAddress(item.roadAddress, item.x, item.y);
    });
}

// 5. Map과 Marker를 생성 //
//지도를 그려주는 함수
function selectMapList() {
	var map = new naver.maps.Map('selectStoreMap', {
	    center: new naver.maps.LatLng(37.3595704, 127.105399),
	    zoom: 14
	});
}

//6. Tag 생성 로직 //
// 선택한 가게의 Category에 따른 SubCategory를 Controller에서 Json으로 받아 Tag Button을 생성
function getStoreInfo(selectCategory) {
    $.ajax({
        type:"get",
        url:"https://www.9oodfood.com/api/public/store/info/"+selectCategory,
        dataType:"json",

        success: function(data){
            moodTagData = data[0];
            priceTagData = data[1];
            categoryTagData = data[2];
            waitingTagData = data[3];

            if(categoryTagData.length == 0) {
                alert("음식점만 선택 가능합니다");
                $('.table_body').empty();
            }

            $('#store-mood-tag-row').children().remove();
            $('#store-price-tag-row').children().remove();
            $('#store-category-tag-row').children().remove();
            $('#store-waiting-tag-row').children().remove();

            for(var i=0; i<moodTagData.length; i++) {
                createTagBtn(i, moodTagData);
            }

            for(var i=0; i<priceTagData.length; i++) {
                createTagBtn(i, priceTagData);
            }

            for(var i=0; i<categoryTagData.length; i++) {
                createTagBtn(i, categoryTagData);
            }

            for(var i=0; i<waitingTagData.length; i++) {
                createTagBtn(i, waitingTagData);
            }

        },
        error:function(){
        }
    })
}

$(document).on("click", "input[name='moodList[]']:checkbox", function(){
    var cnt = $("input[name='storeMoodList[]']:checked").length;
    if(cnt > 3) {
        $("input[name='storeMoodList[]']:not(:checked)").attr("disabled", "disabled");
    } else {
        $("input[name='storeMoodList[]']").removeAttr("disabled");
    }
});

$(document).on("click", "input[name='subCategoryList[]']:checkbox", function(){
    var cnt = $("input[name='subCategoryList[]']:checked").length;
    if(cnt > 2) {
        $("input[name='subCategoryList[]']:not(:checked)").attr("disabled", "disabled");
    } else {
        $("input[name='subCategoryList[]']").removeAttr("disabled");
    }
});

$(document).on("click", "input[name='moodList[]']:checkbox", function(){
    var labelValue = $(this).next().text();

    if($(this).is(':checked')) {
        moodArray.push(labelValue);
    } else {
        for(let i = 0; i < moodArray.length; i++) {
            if(moodArray[i] == labelValue)  {
                moodArray.splice(i, 1);
            }
        }
    }
});
