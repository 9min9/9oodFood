
$(document).ready(function(){
    getRecommendStore();
    getNewStore();
});

function getRecommendStore() {
    $.ajax({
        type:"POST",
        url: "https://www.9oodfood.com/api/public/index/recommendStore",
        dataType:"JSON",
        contextType : "application/json",

        success: function(jsonData){
            var flag = "recommend";

            for(var i =0; i<jsonData.length; i++) {
                createCarouselInner(jsonData, i, flag);
            }
        },
        error:function(){
        }
    });
}

function getNewStore() {

    $.ajax({
        type:"POST",
        url: "https://www.9oodfood.com/api/public/index/newStore",
        dataType:"JSON",
        contextType : "application/json",

        success: function(jsonData){
            var flag = "new";

            for(var i =0; i<jsonData.length; i++) {
                createCarouselInner(jsonData, i, flag);
            }
        },
        error:function(error){
        }
    });
}

function createCarouselInner(jsonData, i, flag) {
    var carouselInner;
    if(flag == "recommend") {
        carouselInner = document.querySelector('#recommend-carousel-inner');
    } else if (flag == "new") {
        carouselInner = document.querySelector('#new-carousel-inner');
    }
    var item = document.createElement('div');
    var itemClass;

    if(i == 0) {
        itemClass = "carousel-item active";
    } else {
        itemClass = "carousel-item";
    }

    item.setAttribute('class', itemClass);

    var storeCardZone = createStoreCardZone(jsonData, i, flag);
    var storeDetail = createStoreDetail(jsonData, i, flag);

    item.appendChild(storeCardZone)
    item.appendChild(storeDetail)

    carouselInner.appendChild(item);
}

//Create StoreCard //
function createStoreCardZone(jsonData, i, flag) {
    var cardZone = document.createElement('div');
    var cardZoneId;
    var cardZoneClass = "float-start col-12 col-md-4";

    if(flag == "recommend") {
        cardZoneId = "recommend-index-card-zone" +i;
    } else if (flag == "new") {
        cardZoneId = "new-index-card-zone" +i;
    }

    cardZone.setAttribute('id', cardZoneId);
    cardZone.setAttribute('class', cardZoneClass);

    var storeCard = createStoreCard(jsonData, i, flag);
    cardZone.appendChild(storeCard);

    return cardZone;
}

function createStoreCard(jsonData, i, flag) {
    var storeCard = document.createElement('div');
    var storeCardId;
    var storeCardClass = "index-card card bg-transparent border-secondary";

    if(flag == "recommend") {
        storeCardId = "recommend-index-card" +i;
    } else {
        storeCardId = "new-index-card" +i;
    }

    storeCard.setAttribute('id', storeCardId);
    storeCard.setAttribute('class', storeCardClass);

    var storeImageZone = createStoreImageZone(jsonData, i, flag);
    var storeTitle = createStoreTitle(jsonData, i, flag);
    var storeCardBody = createStoreCardBody(jsonData, i, flag);
    var storeCardFooter = createStoreCardFooter(jsonData, i, flag);


    storeCard.appendChild(storeImageZone);
    storeCard.appendChild(storeTitle);
    storeCard.appendChild(storeCardBody);
    storeCard.appendChild(storeCardFooter);

    return storeCard;
}

function createStoreImageZone(jsonData, i, flag) {
    var imageZone = document.createElement('div');
    var imageZoneId;
    var imageZoneClass = "card-header bg-transparent border-secondary d-flex align-items-center justify-content-center";

    if(flag == "recommend") {
        imageZoneId = "recommend-index-card-image-zone" +i;
    } else {
        imageZoneId = "new-index-card-image-zone" +i;
    }

    var storeImage = createStoreImage(jsonData, i, flag);

    imageZone.setAttribute('id', imageZoneId);
    imageZone.setAttribute('class', imageZoneClass);
    imageZone.appendChild(storeImage);

    return imageZone;
}

function createStoreImage(jsonData, i, flag) {
    var storeImage = document.createElement('img');
    var storeImageClass;
    var storeImageStyle = "height:300px;";
    var storeImageSrc;
//    var h = "80%";
//    var w = "80%";

    //Main Image
    if(jsonData[i].imagePathList[0] == null) {
        if(jsonData[i].category == "RESTAURANT") {
            storeImageSrc = "/images/restaurant.svg"
        } else if (jsonData[i].category == "CAFE") {
            storeImageSrc = "/images/cafe.svg"
        } else if (jsonData[i].category == "BAR") {
            storeImageSrc = "/images/bar.svg"
        }
        storeImageClass = "img-fit";
//        storeImage.setAttribute('width', w);
//        storeImage.setAttribute('height', h);
    } else {
        storeImageSrc = jsonData[i].imagePathList[0];
        storeImageClass = "img-fit w-100";

    }
    storeImage.setAttribute('src', storeImageSrc);
    storeImage.setAttribute('class', storeImageClass);

    storeImage.setAttribute('style', storeImageStyle);


    return storeImage;
}

function createStoreTitle(jsonData, i, flag) {
    var storeTitleRow = document.createElement('div');
    var titleRowId;
    var titleRowClass = "card-header bg-transparent border-secondary align-items-center justify-content-between";

    if(flag == "recommend") {
        titleRowId = "recommend-index-card-title" +i;
    } else {
        titleRowId = "new-index-card-title" +i;
    }

    var storeTitle = document.createElement('div');
    var storeTitleValue = jsonData[i].title;

    storeTitle.append(storeTitleValue);

    storeTitleRow.setAttribute('id', titleRowId);
    storeTitleRow.setAttribute('class', titleRowClass);

    storeTitleRow.appendChild(storeTitle);

    return storeTitleRow;
}

function createStoreCardBody(jsonData, i, flag) {
    var cardBody = document.createElement('div');
    var cardBodyId;
    var cardBodyClass = "card-body bg-transparent border-secondary align-items-center justify-content-between";

    if(flag == "recommend") {
        cardBodyId = "recommend-index-card-body" + i;
    } else {
        cardBodyId = "new-index-card-body" + i;
    }

    cardBody.setAttribute('id', cardBodyId);
    cardBody.setAttribute('class', cardBodyClass);

    var storeAddress = createStoreAddress(jsonData, i);
    var storeMood = createStoreMood(jsonData, i);
    var storeInfo = createStoreInfo(jsonData, i);

    cardBody.appendChild(storeAddress);
    cardBody.appendChild(storeInfo);
    cardBody.appendChild(storeMood);

    return cardBody;
}

function createStoreAddress(jsonData, i) {
    var addressRow = document.createElement('div');
    var addressRowClass = "store-address mb-2"

    var addressLabel = document.createElement('label');
    var labelClass = "btn btn-outline-secondary";
    var labelValue = jsonData[i].address;

    addressLabel.setAttribute('class', labelClass);
    addressLabel.append(labelValue);

    addressRow.setAttribute('class', addressRowClass);
    addressRow.appendChild(addressLabel);

    return addressRow;
}

function createStoreMood(jsonData, i) {
    var moodRow = document.createElement('div');
    var moodRowClass = "store-mood";

    moodRow.setAttribute('class', moodRowClass);

    for (var j=0; j<jsonData[i].storeMoodList.length; j++) {
        var moodLabel = document.createElement('label');
        var labelClass = "btn btn-outline-secondary me-2 mb-2";
        var labelValue = jsonData[i].storeMoodList[j];

        moodLabel.setAttribute('class', labelClass);
        moodLabel.append(labelValue);
        moodRow.appendChild(moodLabel);
    }

    return moodRow;
}

function createStoreInfo(jsonData, i) {
    var infoRow = document.createElement('div');
    var infoRowClass = "store-info";

    var priceLabel = document.createElement('label');
    var categoryLabel = document.createElement('label');
    var statusLabel = document.createElement('label');
    var labelClass = "btn btn-outline-secondary me-2 mb-2";

    var priceValue = jsonData[i].priceRange;
    var categoryValue = jsonData[i].category;
    var statusValue = jsonData[i].status;

    if(categoryValue == "RESTAURANT") {
        categoryValue = "식당";
    } else if (categoryValue == "CAFE") {
        categoryValue = "카페";
    } else if (categoryValue == "BAR") {
        categoryValue = "술집"
    }

    priceLabel.setAttribute('class', labelClass);
    priceLabel.append(priceValue);

    categoryLabel.setAttribute('class', labelClass);
    categoryLabel.append(categoryValue);

    statusLabel.setAttribute('class', labelClass);

    if(statusValue == 0) {
        statusValue = "가 보고 싶은 곳";
    } else {
        statusValue += "점";
    }

    statusLabel.append(statusValue);

    infoRow.setAttribute('class', infoRowClass);
    infoRow.appendChild(priceLabel);
    infoRow.appendChild(categoryLabel);
    infoRow.appendChild(statusLabel);

    return infoRow;
}

function createStoreCardFooter(jsonData, i, flag) {
    var footer = document.createElement('div');
    var footerClass = "card-footer bg-transparent border-secondary d-flex align-items-center justify-content-end";
    var footerId;

    if(flag == "recommend") {
        footerId = "recommend-index-card-footer" +i;
    } else if (flag == "new") {
        footerId = "new-index-card-footer" +i;
    }

    var a = document.createElement('a');
//    var aClass = "btn stretched-link text-secondary";
    var aClass = "btn stretched-link";
    var aHref = "/forum/detail/?forum="+jsonData[i].id;

    a.setAttribute('class', aClass);
    a.setAttribute('href', aHref);
    a.append("자세히 보기");

    var iconDiv = document.createElement("div");
    var iconDivClass = "small";
    var icon = document.createElement("i");
    var iconClass = "fas fa-angle-right";

    icon.setAttribute('class', iconClass);
    iconDiv.setAttribute('class', iconDivClass);
    iconDiv.appendChild(icon);

    footer.setAttribute('id', footerId);
    footer.setAttribute('class', footerClass);
    footer.appendChild(a);
    footer.appendChild(iconDiv)

    return footer;
}

// Create Detail //

function createStoreDetail(jsonData, i, flag) {
    var detailZone = document.createElement('div');
    var detailZoneId;
    var detailZoneClass = "float-end d-none d-md-block col-md-7 my-2";

    if(flag == "recommend") {
        detailZoneId = "recommend-index-detail-zone" +i;
    } else if (flag == "new") {
        detailZoneId = "new-index-detail-zone" +i;
    }

    var imageZone = createDetailImageZone(jsonData, i, flag);
    var mapZone = createMapZone(jsonData, i, flag);

    detailZone.setAttribute('id', detailZoneId);
    detailZone.setAttribute('class', detailZoneClass);
    detailZone.appendChild(imageZone);
    detailZone.appendChild(mapZone);

    return detailZone;
}

function createDetailImageZone(jsonData, i, flag) {
    var imageZone = document.createElement('div');
    var imageZoneId;

    if(flag == "recommend") {
        imageZoneId = "recommend-detail-image-zone" +i;
    } else if (flag == "new") {
        imageZoneId = "new-detail-image-zone" +i;
    }

    var imageTitle = createDetailImageTitle(flag);
    var image = createDetailImage(jsonData, i, flag);

    imageZone.setAttribute('id', imageZoneId);
    imageZone.appendChild(imageTitle);
    imageZone.appendChild(image);

    return imageZone;
}

function createDetailImageTitle(flag) {
    var imageTitle = document.createElement('table');
    var imageTitleId;
    var imageTitleClass = "detail-image-title table table-striped";

    var thead = document.createElement('thead');
    var tr = document.createElement('tr');
    var td = document.createElement('td');

    td.append("사진");
    tr.appendChild(td);
    thead.appendChild(tr);

    imageTitle.setAttribute('id', imageTitleId);
    imageTitle.setAttribute('class', imageTitleClass);
    imageTitle.appendChild(thead);

    return imageTitle;
}

function  createDetailImage(jsonData, i, flag) {
    var detailImage = document.createElement('div');
    var detailImageClass = "recommend-detail-image form-control col-md-4";

    var imageBoxZone = createImageBoxZone(jsonData, i, flag);

    detailImage.setAttribute("class", detailImageClass);
    detailImage.appendChild(imageBoxZone);

    return detailImage;
}

function createImageBoxZone(jsonData, i, flag) {
    var imageBoxZone = document.createElement('div');
    imageBoxZoneClass = "recommend-detail-image-box-zone row justify-content-around align-items-center";

    imageBoxZone.setAttribute('class', imageBoxZoneClass);

    for(var j=1; j<4; j++) {
        var imageBox = createImageBox(jsonData, i, flag, j);
        imageBoxZone.appendChild(imageBox);
    }
    return imageBoxZone;
}

function createImageBox(jsonData, i, flag, boxNum) {
    var imageBox = document.createElement('div');
    var imageBoxId;
    var imageBoxClass = "img-thumbnail px-1 py-1";
    var imageBoxStyle = "width:180px; height:180px;"

    if(flag == "recommend") {
        imageBoxId = "recommend-detail-image-box" +boxNum;
    } else if(flag == "new") {
        imageBoxId = "new-detail-image-box" +boxNum;
    }
    var image = createImage(jsonData, i, flag, boxNum);

    imageBox.setAttribute('id', imageBoxId);
    imageBox.setAttribute('class', imageBoxClass);
    imageBox.setAttribute('style', imageBoxStyle);
    imageBox.appendChild(image);

    return imageBox;
}

function createImage(jsonData, i, flag, boxNum) {
    var image = document.createElement('img');
    var imageId;
    var imageClass = "img-fit w-100 h-100";
    var imageSrc = jsonData[i].imagePathList[boxNum];

    if(flag == "recommend") {
        imageId = "recommend-detail-image" + boxNum;
    } else if (flag == "new") {
        imageId = "new-detail-image" +boxNum;
    }

    //Another Image
    if(imageSrc == null) {
        if(jsonData[i].category == "RESTAURANT") {
                imageSrc = "/images/restaurant.svg"
            } else if (jsonData[i].category == "CAFE") {
                imageSrc = "/images/cafe.svg"
            } else if (jsonData[i].category == "BAR") {
                imageSrc = "/images/bar.svg"
            }
    }

    image.setAttribute('id', imageId);
    image.setAttribute('class', imageClass);
    image.setAttribute('src', imageSrc);

    return image;
}

// Crate Map //
function createMapZone(jsonData, i, flag) {
    var mapZone = document.createElement('div');
    var mapZoneId;

    if(flag == "recommend") {
        mapZoneId = "recommend-detail-map-zone" +i;
    } else if (flag == "new") {
        mapZoneId = "new-detail-map-zone" +i;
    }

    mapZone.setAttribute('id', mapZoneId);

    var mapTitle = createMapTitle();
    var map = createMap(jsonData, i, flag);
    mapZone.appendChild(mapTitle);
    mapZone.appendChild(map);

    return mapZone;
}

function createMapTitle() {
    var mapTitle = document.createElement('table');
//    var mapTitleId;
    var mapTitleClass = "detail-map-title table table-striped";

    var thead = document.createElement('thead');
    var tr = document.createElement('tr');
    var td = document.createElement('td');

    td.append("위치");
    tr.appendChild(td);
    thead.appendChild(tr);

//    mapTitle.setAttribute('id', mapTitleId);
    mapTitle.setAttribute('class', mapTitleClass);
    mapTitle.appendChild(thead);

    return mapTitle;
}

function createMap(jsonData, i, flag) {
    var map = document.createElement('div');
    var mapId;
    var mapClass = "map-div form-control";
    var mapStyle = "width:100%;height:250px;";

    if(flag == "recommend") {
        mapId = "recommend-detail-map" + i;
    } else if (flag == "new") {
        mapId = "new-detail-map" + i;
    }

    map.setAttribute('id', mapId);
    map.setAttribute('class', mapClass);
    map.setAttribute('style', mapStyle);

    searchAddressToCoordinate(jsonData[i].roadAddress, mapId);

    return map;
}

function insertAddress(address, latitude, longitude, mapId) {
	var map = new naver.maps.Map(mapId, {
	    center: new naver.maps.LatLng(longitude, latitude),
	    zoom: 16
	});
    var marker = new naver.maps.Marker({
        map: map,
        position: new naver.maps.LatLng(longitude, latitude),
    });
//    map.setSize(getMapSize());
}

function searchAddressToCoordinate(address, mapId) {
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

        insertAddress(item.roadAddress, item.x, item.y, mapId);
    });
}

// Map 크기 조정 //
$('.carousel').on('slid.bs.carousel', function(e) {
    window.dispatchEvent(new Event('resize'));
});



