//Create Store Info
function createStoreTitle(jsonData) {
    var storeTitleRow = document.querySelector("#store-title-row");
    var storeTitle = document.createElement("h1");
    var storeTitleId = "store-title"
    var storeTitleClass = "mt-4";

    storeTitle.setAttribute('id', storeTitleId);
    storeTitle.setAttribute('class', storeTitleClass);
    storeTitle.append(jsonData.title);

    storeTitleRow.appendChild(storeTitle);

    return storeTitleRow;
}

function createStoreInfo(jsonData) {
    createAddress(jsonData);
    createMood(jsonData);
    createPrice(jsonData);
    createWaiting(jsonData);
    createCategory(jsonData);
    createStatus(jsonData);
}

function createAddress(jsonData) {
    var storeAddressRow = document.querySelector("#store-address-label-row");

    var storeAddress = document.createElement("a");
    var addressId = "store-address";
    var addressClass = "btn btn-outline-secondary col-auto";
    var addressHref = "https://map.naver.com/v5/search/" + jsonData.title;
    var target = "_blank";

    storeAddress.setAttribute('id', addressId);
    storeAddress.setAttribute('class', addressClass);
    storeAddress.setAttribute('href', addressHref);
    storeAddress.setAttribute('target', target);
    storeAddress.append(jsonData.address);

    storeAddressRow.appendChild(storeAddress);

    return storeAddressRow;
}

function createMood(jsonData) {
    var storeMoodRow = document.querySelector("#store-mood-label-row");

    for(var i=0; i<jsonData.storeMoodList.length; i++) {
        var storeMood = document.createElement("label");
        var moodId = "store-mood"+i;
        var moodClass = "btn btn-outline-secondary col-auto me-2 mb-2";

        storeMood.setAttribute('id', moodId);
        storeMood.setAttribute('class', moodClass);
        storeMood.append(jsonData.storeMoodList[i]);
        storeMoodRow.appendChild(storeMood);
    }
    return storeMood;
}

function createPrice(jsonData) {
    var storePriceRow = document.querySelector("#store-price-label-row");

    var storePrice = document.createElement("label");
    var priceId = "store-price";
    var priceClass = "btn btn-outline-secondary";

    storePrice.setAttribute('id', priceId);
    storePrice.setAttribute('class', priceClass);
    storePrice.append(jsonData.priceRange);
    storePriceRow.appendChild(storePrice);

    return storePriceRow;
}

function createCategory(jsonData) {
    var storeCategoryRow = document.querySelector("#store-category-label-row");

    var storeCategory = document.createElement("label");
    var categoryId = "store-category";
    var categoryClass = "btn btn-outline-secondary col-auto me-2 mb-2";
    var categoryData = "";

    storeCategory.setAttribute('id', categoryId);
    storeCategory.setAttribute('class', categoryClass);

    if(jsonData.category == "RESTAURANT") {
        categoryData = "식당";
    } else if (jsonData.category == "CAFE") {
        categoryData = "카페";
    } else if (jsonData.category == "BAR") {
        categoryData = "술집";
    }

    storeCategory.append(categoryData);
    storeCategoryRow.appendChild(storeCategory);

    var storeSubCategory;
    var subCategoryId;
    var subCategoryClass = "btn btn-outline-secondary col-auto me-2 mb-2";

    for(var i=0; i<jsonData.subCategoryList.length; i++) {
        storeSubCategory = document.createElement("label");
        subCategoryId = "store-subcategory"+i;

        storeSubCategory.setAttribute('id', subCategoryId);
        storeSubCategory.setAttribute('class', subCategoryClass);
        storeSubCategory.append(jsonData.subCategoryList[i]);

        storeCategoryRow.appendChild(storeSubCategory);
    }

    return storeCategoryRow;
}

function createWaiting(jsonData) {
    var storeWaitingRow = document.querySelector("#store-waiting-label-row");

    var storeWaiting = document.createElement("label");
    var waitingId = "store-waiting";
    var waitingClass = "btn btn-outline-secondary col-xs-12";

    storeWaiting.setAttribute('id', waitingId);
    storeWaiting.setAttribute('class', waitingClass);
    storeWaiting.append(jsonData.waiting);

    storeWaitingRow.appendChild(storeWaiting);

    return storeWaitingRow;
}

function createStatus(jsonData) {
    var storeStatusRow = document.querySelector("#store-status-label-row");
    var storeStatus = document.createElement("label");
    var statusId = "store-status";
    var statusClass = "btn btn-outline-secondary";
    var statusData =jsonData.status;

    if(statusData == 0) {
        statusData = "가 보고 싶은 곳";
    } else {
        statusData = statusData + "점";
    }

    storeStatus.setAttribute('id', statusId);
    storeStatus.setAttribute('class', statusClass);

    storeStatus.append(statusData);
    storeStatusRow.appendChild(storeStatus);

    return storeStatusRow;

}

/** Create Store Image */
function createImageCarousel(jsonData) {
    createCarouselItem(jsonData);
    createCarouselIndicatorBtn(jsonData);
}

function createCarouselItem(jsonData, flag) {
    var imageCarouselInner = document.querySelector("#store-image-carousel-inner");
//    var modalCarouselInner = document.querySelector("#store-image-modal-carousel-inner");
    var i=0;

    do {
        var imageCarouselItem = document.createElement("div");
        var itemId = "carousel-item"+i;
        var itemClass;

//        var modalCarouselItem = document.createElement("div");
//        var modalItemId = "modal-carousel-item"+i;
//        var modalItemClass;

        if(i == 0) {
            itemClass = "carousel-item text-center active";
//            modalItemClass ="carousel-item text-center active";
        } else {
            itemClass = "carousel-item text-center";
//            modalItemClass ="carousel-item text-center";
        }

        imageCarouselItem.setAttribute('id', itemId);
        imageCarouselItem.setAttribute('class', itemClass);

//        modalCarouselItem.setAttribute('id', modalItemId);
//        modalCarouselItem.setAttribute('class', modalItemClass);
//        modalCarouselItem.setAttribute('name', i);

        var image = createStoreImage(jsonData, i);
//        var modalImage = createModalImage(jsonData, i);

        imageCarouselItem.appendChild(image);
        imageCarouselInner.appendChild(imageCarouselItem);

//        modalCarouselItem.appendChild(modalImage);
//        modalCarouselInner.appendChild(modalCarouselItem);

        i++;
    } while (i < jsonData.imagePath.length);

    if(jsonData.imagePath.length != 0 && jsonData.imagePath.length != 1) {
        var nextBtn = createCarouselNextBtn();
        var prevBtn = createCarouselPrevBtn();

        imageCarouselInner.appendChild(prevBtn);
        imageCarouselInner.appendChild(nextBtn);
    }
}

function createStoreImage(jsonData, i) {
    var storeImage = document.createElement("img");
    var imgSrc;
    var imageClass = "store-image";
    var imageStyle = "height:356px;";
    var imageAlt = "IMAGE "+i;

    if(jsonData.imagePath.length == 0) {
        if(jsonData.category == "RESTAURANT") {
            imgSrc = "/images/restaurant.svg"
        } else if (jsonData.category == "CAFE") {
            imgSrc = "/images/cafe.svg"
        } else if (jsonData.category == "BAR") {
            imgSrc = "/images/bar.svg"
        }
        imageClass += " w-100";
    } else {
        imgSrc = jsonData.imagePath[i];
        imageClass += " img-fit";
    }

    storeImage.setAttribute('src', imgSrc);
    storeImage.setAttribute('class', imageClass);
    storeImage.setAttribute('style', imageStyle);
    storeImage.setAttribute('alt', imageAlt);

    return storeImage;
}

function createCarouselPrevBtn() {
    var prevBtn = document.createElement('button');
    var btnId = "carousel-prev-btn";
    var btnClass = "carousel-control-prev";
    var btnType = "button";
    var target = "#store-image-carousel";
    var slide = "prev";

    prevBtn.setAttribute('id', btnId);
    prevBtn.setAttribute('class', btnClass);
    prevBtn.setAttribute('type', btnType);
    prevBtn.setAttribute('data-bs-target', target);
    prevBtn.setAttribute('data-bs-slide', slide);

    var iconSpan = document.createElement('span');
    var iconClass = "carousel-control-prev-icon";

    iconSpan.setAttribute('class', iconClass);
    iconSpan.setAttribute('aria-hidden', "true");

    var textSpan = document.createElement('span');
    var textClass = "visually-hidden";

    textSpan.setAttribute('class', textClass);
    textSpan.append("Prev");

    prevBtn.appendChild(iconSpan);
    prevBtn.appendChild(textSpan);

    return prevBtn;
}

function createCarouselNextBtn() {
    var nextBtn = document.createElement('button');
    var btnId = "carousel-next-btn";
    var btnClass = "carousel-control-next";
    var btnType = "button";
    var target = "#store-image-carousel";
    var slide = "next";

    nextBtn.setAttribute('id', btnId);
    nextBtn.setAttribute('class', btnClass);
    nextBtn.setAttribute('type', btnType);
    nextBtn.setAttribute('data-bs-target', target);
    nextBtn.setAttribute('data-bs-slide', slide);

    var iconSpan = document.createElement('span');
    var iconClass = "carousel-control-next-icon";

    iconSpan.setAttribute('class', iconClass);
    iconSpan.setAttribute('aria-hidden', "true");

    var textSpan = document.createElement('span');
    var textClass = "visually-hidden";

    textSpan.setAttribute('class', textClass);
    textSpan.append("Next");

    nextBtn.appendChild(iconSpan);
    nextBtn.appendChild(textSpan);

    return nextBtn;
}

function createCarouselIndicatorBtn(jsonData) {
    var carouselIndicators = document.querySelector("#store-image-carousel-indicator");
    var btnType = "button";
    var dataTarget = "#store-image-carousel";
    var btnClass = "active";
    var ariaCurrent = "true";

    for(var i=0; i<jsonData.imagePath.length; i++) {
        var carouselIndicatorBtn = document.createElement("button");
        var slideTo = i;
        var ariaLabel = "Slide " +i;

        if(i==0) {
            carouselIndicatorBtn.setAttribute('class', btnClass);
        }

        carouselIndicatorBtn.setAttribute('type', btnType);
        carouselIndicatorBtn.setAttribute('data-bs-target', dataTarget);
        carouselIndicatorBtn.setAttribute('data-bs-slide-to', slideTo);
        carouselIndicatorBtn.setAttribute('aria-current', ariaCurrent);
        carouselIndicatorBtn.setAttribute('aria-label', ariaLabel);

        carouselIndicators.appendChild(carouselIndicatorBtn);
    }
}

function createStoreComment(jsonData) {
    var storeContentsZone = document.querySelector("#store-contents-zone");

    var storeContents = document.createElement("textarea");
    var contentsId = "store-contents-area"
    var contentsClass = "form-control bg-white";
    var contentsStyle = "height:384px;";

    storeContents.setAttribute('id', contentsId);
    storeContents.setAttribute('class', contentsClass);
    storeContents.setAttribute('style', contentsStyle);
    storeContents.setAttribute('readonly', 'true');
    storeContents.setAttribute('disabled', 'true');
    storeContents.append(jsonData.contents);

    storeContentsZone.appendChild(storeContents);

    return storeContentsZone;
}

/** Create Map */
function createStoreMap(jsonData) {
    searchAddressToCoordinate(jsonData.address);
}

function insertAddress(address, latitude, longitude) {
	var map = new naver.maps.Map('store-map', {
	    center: new naver.maps.LatLng(longitude, latitude),
	    zoom: 16
	});
    var marker = new naver.maps.Marker({
        map: map,
        position: new naver.maps.LatLng(longitude, latitude),
    });
}

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
