var moodTagData;
var priceTagData;
var categoryTagData;
var imageIdArray = [];
var imageNameArray = [];

$(document).ready(function() {
    const searchParams = new URLSearchParams(location.search);      //URL의 param (?ooo=) 추출
    const forumId = searchParams.get('forum');                     //?forum= value value 추출
    getForum(forumId);
    selectMapList();
    createImageBox();
    createStatus();

    //점수 슬라이더
    $('#status-slider').on("input", function() {
        var statusValue = $('#status-value');
        $('#status-slider').attr('value', this.value);

        statusValue.text(this.value);

        if (statusValue.text() == 0) {
            $('#status-value').hide();
            $('#status-value-text').text("가 보고 싶은 곳");

        } else {
            $('#status-value').show();
            $('#status-value-text').text("점");
        }
    });
});

function getForum(forumId) {
$.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/public/forum/update/find/"+forumId,
        dataType:"json",

        success: function(jsonData){
            getNaverSearchResult(jsonData.title);   //Set Store Table
            setStoreInfoTag(convertCategory(jsonData.category));                //선택된 가게의 카테고리를 함수에 전달하여 Tag Button을 생성
            searchAddressToCoordinate(jsonData.address);                        //선택된 가게의 주소를 함수에 전달하여 Naver MAP API를 작동
        },
        complete: function(jsonData) {
            setForumId(jsonData.responseJSON.id);
            setStoreImage(jsonData.responseJSON.id);
            setStoreTitle(jsonData.responseJSON.title);
            setForumContent(jsonData.responseJSON.content);
            setStoreMood(jsonData.responseJSON.mood);
            setStorePrice(jsonData.responseJSON.price);
            setStoreSubCategory(jsonData.responseJSON.subCategory);
            setStoreWaiting(jsonData.responseJSON.waiting);
            setStoreStatus(jsonData.responseJSON.status);
        },

        error:function(){
            console.log("ERR ");
        }
    })
}

function setForumId(forumId) {
    var input =  document.querySelector('#forum-id');
    input.setAttribute('value', forumId);
}

function setStoreTitle(title) {
    var input =  document.querySelector('#inputStoreName');
    input.setAttribute('value', title);
}

function setForumContent(content) {
    var textArea =  document.querySelector('#store-content');

    if(content == "작성된 내용이 없습니다.") {
        textArea.append("");
    } else {
        textArea.append(content);
    }
}

function setStoreMood(mood) {
    for(var i=0; i<mood.length; i++) {
        var findInput = $('#store-mood-tag-row').find('input[value="' +mood[i] + '"]');
        findInput.prop('checked', true);
    }
}

function setStorePrice(price) {
    var findInput = $('#store-price-tag-row').find('input[value="' + price + '"]');
    findInput.prop('checked', true);
}

function setStoreSubCategory(subCategory) {
    for(var i=0; i<subCategory.length; i++) {
        var findInput = $('#store-subCategory-tag-row').find('input[value="' +subCategory[i] + '"]');
        findInput.prop('checked', true);
    }
}

function setStoreWaiting(waiting) {
    var findInput = $('#store-waiting-tag-row').find('input[value="' + waiting + '"]');
    findInput.prop('checked', true);
}

function setStoreStatus(status) {
    $('#status-value').text(status);
    $('#status-slider').attr('value', status);

    $('#status-value').show();
    $('#status-value-text').text("점");
}


function setStoreImage(forumId) {
    $.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/public/forum/update/find/file/"+forumId,
        contentType: "application/json",
        dataType:"json",

        success: function(jsonData){

            for(var i=0; i<jsonData.length; i++) {
//                var thumbnailImage = setImageThumbnail(i, jsonData[i].imagePath, jsonData[i].originalFileName);
                var thumbnailImage = setImageThumbnail(i, jsonData[i].gcsPath, jsonData[i].originalFileName);
                setSelectImageBox(thumbnailImage);
                setImageIdInput(i, jsonData[i].imageId);
                setImageNameInput(i, jsonData[i].originalFileName);
                setImageFile(i, jsonData[i].bytes, jsonData[i].originalFileName, jsonData[i].contentType);
            }
        },

        error:function(){
        }
    })
}

function setImageThumbnail(imageNum ,imagePath, imageOriginalName) {
        var imageId = "store-image" +imageNum;
        var image = document.querySelector('#'+imageId);
        var imgClass = 'select-img img-fit w-100 h-100 position-relative';
        image.setAttribute('src', imagePath);
        image.setAttribute('class', imgClass);
        image.setAttribute('name', imageOriginalName);

        return image;
}

function setImageIdInput(imageNum, imageId) {
    var input = document.querySelector('#image-id-input'+imageNum);
//    input.setAttribute('name', 'image['+imageNum+'].id');
    input.setAttribute('name', 'imageId');
//    imageIdArray.push(imageId);
    input.setAttribute('value', imageId);
}

function setImageNameInput(imageNum, imageName) {
    var input = document.querySelector('#image-name-input'+imageNum);
//    input.setAttribute('name', "image["+imageNum+"].originalName");
    input.setAttribute('name', "imageName");
    input.setAttribute('value', imageName);
}

function setImageFile(imageNum, imageBytes, originalFileName, contentType) {
    const decodeBytes = atob(imageBytes);

    let buffer = new ArrayBuffer(decodeBytes.length);
    let view = new Uint8Array(buffer);

    while(buffer--) {
        view[buffer] = decodeBytes.charCodeAt(buffer);
    }

    const file = new File([view], originalFileName, {type: contentType});

    filesArr.push(file);
    fileInputDataTransfer();
}


// 2. getJSON()을 호출하고 Table을 통해 List //
function getNaverSearchResult(title) {
    $.ajax({
        type:"get",
        url:"https://www.9oodfood.com/api/public/server/naver/search/"+title,
        dataType:"json",

        success: function(jsonData){
            createTable(jsonData)

        },
        error:function(){
        }
    })
}

// 4. 선택된 주소를 API에 적용 //
// 지도에 주소 입력 //
function insertAddress(address, latitude, longitude) {
	var map = new naver.maps.Map('store-map', {
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
	var map = new naver.maps.Map('store-map', {
	    center: new naver.maps.LatLng(37.3595704, 127.105399),
	    zoom: 14
	});
}

//6. Tag 생성 로직 //
// 선택한 가게의 Category에 따른 SubCategory를 Controller에서 Json으로 받아 Tag Button을 생성
function setStoreInfoTag(category) {
    $.ajax({
        type:"get",
        url:"https://www.9oodfood.com/api/public/store/info/"+category,
        dataType:"json",
        async : false,

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
            $('#store-subCategory-tag-row').children().remove();
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

function removeHtml(text) {
    text = text.replace(/<br\/>/ig, "\n");
    text = text.replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, "");
    return text;
}

//Target, SubCategory의 선택 개수를 각각 4개, 3개로 제한
$(document).on("click", "input[name='storeMoodList[]']:checkbox", function(){
    var cnt = $("input[name='storeMoodList[]']:checked").length;
    if(cnt > 3) {
//        alert("최대 4개 선택가능합니다.")
        $("input[name='storeMoodList[]']:not(:checked)").attr("disabled", "disabled");
    } else {
        $("input[name='storeMoodList[]']").removeAttr("disabled");
    }
});

$(document).on("click", "input[name='subCategoryList[]']:checkbox", function(){
    var cnt = $("input[name='subCategoryList[]']:checked").length;
    if(cnt > 2) {
//        alert("최대 3개 선택가능합니다.")
        $("input[name='subCategoryList[]']:not(:checked)").attr("disabled", "disabled");
    } else {
        $("input[name='subCategoryList[]']").removeAttr("disabled");
    }
});

function convertCategory(category) {
    var convertCategory;

    if(category == "RESTAURANT") {
        convertCategory = "음식점";
    } else if (category == "CAFE") {
        convertCategory = "카페";
    } else if (category == "BAR") {
        convertCategory = "술집"
    }

    return convertCategory;
}

// Submit text 처리
$('#update-form').submit(function(e) {
    e.preventDefault();
    var text = $('#store-comment').val();
    text = text.replace(/(\r\n|\r|\n)/g, '<br>');
    $('#store-content').val(text);

    this.submit();
});
