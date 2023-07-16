function createPageTitle() {
    var titleRow = document.querySelector('#page-title-row');

    var title =  document.createElement('h1');
    title.setAttribute('id', 'title');
    title.setAttribute('class', 'mt-4');

    var titleData = "즐겨찾는 ";

    if(urlCategory == "restaurant") {
        titleData += "식당";
    } else if (urlCategory == "cafe") {
        titleData += "카페";
    } else if (urlCategory == "bar") {
        titleData += "술집"
    }

    title.append(titleData);
    titleRow.appendChild(title);

    var breadcrumb = createPageSubTitle();

    titleRow.appendChild(breadcrumb);
}

function createPageSubTitle() {
    var breadcrumb = document.createElement('ol');
    var id = "sub-title";
    var subTitleClass = "breadcrumb mb-4";

    breadcrumb.setAttribute('id', id);
    breadcrumb.setAttribute('class', subTitleClass);
    var subTitle = createBreadcrumbItem();

    breadcrumb.appendChild(subTitle);

    return breadcrumb;
}

function createBreadcrumbItem() {
    var subTitleLi = document.createElement('li');
    var subTitleId = 'sub-title';
    var subTitleClass = 'breadcrumb-item active';
    var subTitleData = "Favorite "

    if(urlCategory == "restaurant") {
        subTitleData += "Restaurant";
    } else if (urlCategory == "cafe") {
        subTitleData += "Cafe";
    } else if (urlCategory == "bar") {
        subTitleData += "Bar"
    }

    subTitleLi.setAttribute('id', subTitleId);
    subTitleLi.setAttribute('class', subTitleClass);

    subTitleLi.append(subTitleData);

    return subTitleLi;
}

//Store Card 생성 로직
function createStoreCardZone(jsonData, i) {
    var storeCardRow = document.querySelector("#store-row");
    var id = "store-card-zone" + i;
    var storeCardZone = document.createElement("div");
    var storeCardZoneClass =  "store-card-zone col-md-4";           //col-md-3 : 4개씩, col-md-4 : 3개씩
    var storeCardZoneName = "storeCardZone"

    storeCardZone.setAttribute('id', id);
    storeCardZone.setAttribute('class', storeCardZoneClass);
    storeCardZone.setAttribute('name', storeCardZoneName);

    var storeCard = createStoreCard(jsonData, i);
    storeCardZone.appendChild(storeCard);

    storeCardRow.appendChild(storeCardZone);
}

function createStoreCard(jsonData, i) {
    var storeCard = document.createElement("div");
    var cardId = "forum-" + jsonData.content[i].id;

    var cardClass = "card bg-transparent border-secondary mb-4";
    var style = "z-index:2";

    var imageZone = createImage(jsonData, i);
    var titleZone = createTitle(jsonData, i);
    var dataZone = createDataZone(jsonData, i);
    var cardFooter = createFooter(jsonData, i);

    storeCard.setAttribute('id', cardId);
    storeCard.setAttribute('class', cardClass);
    storeCard.setAttribute('style', style);

    storeCard.appendChild(imageZone);
    storeCard.appendChild(titleZone);
    storeCard.appendChild(dataZone);
    storeCard.appendChild(cardFooter);

    return storeCard;
}

function createLikeInput(jsonData, i) {
    var likeInput = document.createElement('input');
    var inputId = 'like-input-' +jsonData.content[i].id;
    likeInput.setAttribute('id', inputId);
    likeInput.setAttribute('type', 'checkbox')
    likeInput.setAttribute('hidden', true);
    likeInput.setAttribute('name', 'like');
    likeInput.setAttribute('value', jsonData.content[i].id);

    return likeInput;
}

function createLike(jsonData, i) {
    var like = document.createElement('label');
    var likeId = 'like-label-' +jsonData.content[i].id;
    var likeClass= "btn btn-link btn-lg position-absolute top-0 end-0";
    var forInput = 'like-input-' +jsonData.content[i].id;

    var likeIcon = document.createElement('i');
    var iconId = 'like-icon-' +jsonData.content[i].id;
    var iconClass = "far fa-star";

    likeIcon.setAttribute('id', iconId);
    likeIcon.setAttribute('class', iconClass);

    like.appendChild(likeIcon);

    like.setAttribute('id', likeId);
    like.setAttribute('class', likeClass);
    like.setAttribute('type', 'button');
    like.setAttribute('for', forInput);

    return like;
}

function createImage(jsonData, i) {
    var imageZone = document.createElement("div");
    var imageZoneClass = "store-img-zone card-header bg-transparent border-secondary d-flex justify-content-center ";

    var image = document.createElement('img');
    var imgSrc;
    var imageClass = "img-fit w-100";
    var imageStyle = "height:300px;";
    var alt = "No Photo!";

    if(jsonData.content[i].imagePath) {
        imgSrc = jsonData.content[i].imagePath;
        image.setAttribute('class', imageClass);
    } else {
        if(jsonData.content[i].category == "RESTAURANT") {
            imgSrc = "/images/restaurant.svg"
        } else if (jsonData.content[i].category == "CAFE") {
            imgSrc = "/images/cafe.svg"
        } else if (jsonData.content[i].category == "BAR") {
            imgSrc = "/images/bar.svg"
        }
    }
    imageZone.setAttribute('class', imageZoneClass);

    var likeInput = createLikeInput(jsonData, i);
    var like = createLike(jsonData, i);

    image.setAttribute('src', imgSrc);
    image.setAttribute('style', imageStyle);
    image.setAttribute('alt', alt);

    imageZone.appendChild(image);
    imageZone.appendChild(likeInput);
    imageZone.appendChild(like);

    return imageZone;
}

function createTitle(jsonData, i) {
    var titleZone = document.createElement("div");
    var titleZoneClass = "store-title-zone card-header bg-transparent border-secondary";
    var titleLine = document.createElement("div");
    var titleLineClass = "store-title-line";
    var titleData = jsonData.content[i].title;

    titleZone.setAttribute('class', titleZoneClass);
    titleLine.setAttribute('class', titleLineClass);
    titleLine.append(titleData);

    titleZone.appendChild(titleLine);

    return titleZone;
}

function createDataZone(jsonData, i) {
    var dataZone = document.createElement("div");
    var dataZoneClass = "store-data-zone card-body bg-transparent border-secondary";

    var addressDiv = createAddress(jsonData, i);
    var infoDiv = createInfo(jsonData, i);
    var moodDiv = createStoreMood(jsonData, i);

    dataZone.setAttribute('class', dataZoneClass);
    dataZone.appendChild(addressDiv);
    dataZone.appendChild(infoDiv);
    dataZone.appendChild(moodDiv);

    return dataZone;
}

function createAddress(jsonData, i) {
    var addressZone = document.createElement("div");
    var addressZoneClass = "store-address-line mb-2"

    var addressLabel = document.createElement("label");
    var addressLabelClass = "store-address-label btn btn-outline-secondary";
    var addressData = jsonData.content[i].address;

    addressLabel.setAttribute('class', addressLabelClass);
    addressLabel.append(addressData);

    addressZone.setAttribute('class', addressZoneClass);
    addressZone.appendChild(addressLabel);

    return addressZone;
}

function createSubCategoryDiv(jsonData, i) {
    var subCategoryZone = document.createElement("div");
    var subCategoryZoneClass = "store-subCategory-line"

    subCategoryZone.setAttribute('class', subCategoryZoneClass);

    for(var j=0; j<jsonData.content[i].subCategory.length; j++) {
        var subCategoryLabel = document.createElement("label");
        var subCategoryLabelClass = "store-subcategory-label btn btn-outline-secondary me-1 mb-2";
        subCategoryLabel.setAttribute('class', subCategoryLabelClass);
        subCategoryLabel.append(jsonData.content[i].subCategory[j]);
        subCategoryZone.appendChild(subCategoryLabel);
    }

    return subCategoryZone;
}

function createInfo(jsonData, i) {
    var infoZone = document.createElement("div");
    var infoZoneClass = "store-info-line";

    for(var j=0; j<jsonData.content[i].subCategoryList.length; j++) {
        var subCategoryLabel = document.createElement("label");
        var subCategoryLabelClass = "store-subcategory-label btn btn-outline-secondary me-1 mb-2";

        subCategoryLabel.setAttribute('class', subCategoryLabelClass);
        subCategoryLabel.append(jsonData.content[i].subCategoryList[j]);
        infoZone.appendChild(subCategoryLabel);
    }


    var priceLabel = document.createElement("label");
    var priceLabelClass = "store-price-label btn btn-outline-secondary me-1 mb-2";
    var priceLabelData = jsonData.content[i].priceRange;

    var statusLabel = document.createElement("label");
    var statusLabelClass = "store-status-label btn btn-outline-secondary me-1 mb-2";
    var statusLabelData = statusLabelData = jsonData.content[i].status;

    if(statusLabelData == 0) {
        statusLabelData = "가 보고 싶은 곳";
    } else {
        statusLabelData = statusLabelData = jsonData.content[i].status +"점";
    }

    priceLabel.setAttribute('class', priceLabelClass);
    priceLabel.append(priceLabelData);

    statusLabel.setAttribute('class', statusLabelClass);
    statusLabel.append(statusLabelData);

    infoZone.setAttribute('class', infoZoneClass);
    infoZone.appendChild(priceLabel);
    infoZone.appendChild(statusLabel);

    return infoZone;
}

function createStoreMood(jsonData, i) {
    var moodZone = document.createElement("div");
    var moodZoneClass = "store-mood-line";

    moodZone.setAttribute('class', moodZoneClass);

    for(var j=0; j<jsonData.content[i].storeMoodList.length; j++) {
        var moodLabel = document.createElement("label");
        var moodLabelClass = "store-mood-label btn btn-outline-secondary me-1 mb-2";
        moodLabel.setAttribute('class', moodLabelClass);
        moodLabel.append(jsonData.content[i].storeMoodList[j]);
        moodZone.appendChild(moodLabel);
    }
    return moodZone;
}

function createFooter(jsonData, i) {
    var footerDiv = document.createElement("div");
    var footerClass = "card-footer bg-transparent border-secondary d-flex align-items-center justify-content-end";
    footerDiv.setAttribute('class', footerClass);

    var a = document.createElement("a");
    var aClass = "btn link";
    var aHref = "/forum/detail/?forum="+jsonData.content[i].id;

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

    footerDiv.appendChild(a);
    footerDiv.appendChild(icon);

    return footerDiv;
}

// Create Sort Tag //

function getSortTag() {
    $.ajax({
        type:"get",
        url:"https://www.9oodfood.com/api/public/store/info/sort/tag/" +urlCategory,
        dataType:"json",

        success: function(data){
            moodData = data[0];
            priceData = data[1];
            subCategoryData = data[2];
            locationData = data[3];

            for(var i=0; i<moodData.length; i++) {
                createSortBtn(i, moodData);
            }

            for(var i=0; i<priceData.length; i++) {
                createSortBtn(i, priceData);
            }

            for(var i=0; i<subCategoryData.length; i++) {
                createSortBtn(i, subCategoryData);
            }

            for(var i=0; i<locationData.length; i++) {
                createSortBtn(i, locationData);
            }

        },
        error:function(){
        }
    })
}

function createSortBtn(i, data) {
    var sortInput = createInput(i, data);
    var sortLabel = createLabel(i, data);

    if(data == moodData) {
        var targetSortZone = document.querySelector(".mood-sort-btn");
        targetSortZone.appendChild(sortInput);
        targetSortZone.appendChild(sortLabel);

    } else if (data == priceData) {
        var priceSortZone = document.querySelector(".price-sort-btn");
        priceSortZone.appendChild(sortInput);
        priceSortZone.appendChild(sortLabel);

    } else if (data == subCategoryData) {
        var categorySortZone = document.querySelector(".sub-category-sort-btn");
        categorySortZone.appendChild(sortInput);
        categorySortZone.appendChild(sortLabel);
    } else if (data == locationData) {
        $('#location-sort-zone').attr('hidden', false);
        var locationSortZone = document.querySelector(".location-sort-btn");
        locationSortZone.appendChild(sortInput);
        locationSortZone.appendChild(sortLabel);
    }
}

function createInput(i, data) {
    var inputType = "checkbox";
    var inputClass;
    var inputName;
    var inputId;

    if(data == moodData) {
        inputClass = "sort-opt btn-check";
        inputName = "mood-opt[]";
        inputId = "mood-sort-opt" +i;

    } else if (data == priceData) {
        inputClass = "sort-opt btn-check";
        inputName = "price-opt[]";
        inputId = "price-sort-opt" +i;

    } else if (data == subCategoryData) {
        inputClass = "sort-opt btn-check";
        inputName = "sub-category-opt[]";
        inputId = "sub-category-sort-opt" +i;
    } else if (data == locationData) {
        inputClass = "sort-opt btn-check";
        inputName = "location-opt[]";
        inputId = "location-sort-opt" +i;

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

    if(data == moodData) {
        inputId = "mood-sort-opt" +i;
        labelName = "mood-data"

    } else if(data == priceData) {
        inputId = "price-sort-opt" +i;
        labelName = "price-data"

    } else if (data == subCategoryData) {
        inputId = "sub-category-sort-opt" +i;
        labelName = "sub-category-data"
    } else if (data == locationData) {
        inputId = "location-sort-opt" +i;
        labelName = "location-data"
    }

    var label = document.createElement("label");
    label.setAttribute('class', labelClass);
    label.setAttribute('name', labelName);
    label.setAttribute('for', inputId);
    label.append(data[i]);

    return label;
}