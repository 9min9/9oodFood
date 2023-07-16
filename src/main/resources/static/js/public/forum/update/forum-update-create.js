
/** Store Table */

function createTable(jsonData) {
    $('.table_body').empty();

    var table = document.querySelector('#store-table-body');

    for(var i=0; i<jsonData.items.length; i++) {
        var tr = document.createElement('tr');
        var title = createTableTitle(jsonData.items[i].title, i);
        var address = createTableAddress(jsonData.items[i].address, i);
        var roadAddress = createTableRoadAddress(jsonData.items[i].roadAddress, i);
        var category = createTableCategory(jsonData.items[i].category, i);

        var selectBtn = createTableBtn();

        tr.appendChild(title);
        tr.appendChild(address);
        tr.appendChild(roadAddress);
        tr.appendChild(category);
        tr.appendChild(selectBtn);
        table.appendChild(tr);
    }
}

function createTableTitle(title, i) {
    var storeTitle = removeHtml(title);

    var td = document.createElement('td');
    var tdClass = "storeTitle col-9";

    var input = document.createElement('input');
    var inputId = "search-store-title" + i;
    var inputClass = "title";
    var inputType = "hidden";
    var inputValue = storeTitle;

    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('type', inputType);
    input.setAttribute('value', inputValue);

    td.setAttribute('class', tdClass);
    td.appendChild(input);
    td.append(storeTitle);

    return td;
}

function createTableAddress(address, i) {
    var td = document.createElement('td');
    var tdClass = "storeAddress";

    var input = document.createElement('input');
    var inputId = "search-store-address" + i;
    var inputClass = "address";
    var inputType = "hidden";
    var inputValue = address;
    var inputName = "address";

    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('type', inputType);
    input.setAttribute('value', inputValue);
    input.setAttribute('name', inputName);

    td.setAttribute('class', tdClass);
    td.appendChild(input);

    return td;
}

function createTableRoadAddress(roadAddress, i) {
    var td = document.createElement('td');
    var tdClass = "storeRoadAddress";

    var input = document.createElement('input');
    var inputId = "search-store-roadAddress" + i;
    var inputClass = "roadAddress"
    var inputType = "hidden";
    var inputValue = roadAddress;
    var inputName = "roadAddress";

    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('type', inputType);
    input.setAttribute('value', inputValue);
    input.setAttribute('name', inputName);

    td.setAttribute('class', tdClass);
    td.appendChild(input);

    return td;

}

function createTableCategory(category, i) {
    var td = document.createElement('td');
    var tdClass = "storeCategory";

    var input = document.createElement('input');
    var inputId = "search-store-category" + i;
    var inputClass = "category";
    var inputType = "hidden";
    var inputValue = category;
    var inputName = "category";

    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('type', inputType);
    input.setAttribute('value', inputValue);
    input.setAttribute('name', inputName);


    td.setAttribute('class', tdClass);
    td.appendChild(input);

    return td;
}

function createTableBtn() {
    var td = document.createElement('td');
    var tdClass = "col-3";

    var btn = document.createElement('button');
    var btnClass = "selectBtn btn btn-outline-secondary btn-sm float-end";
    var type = "button";

    btn.setAttribute('class', btnClass);
    btn.setAttribute('disabled', true);
    btn.append("선택");

    td.setAttribute('class', tdClass);
    td.appendChild(btn);

    return td;
}

/** */

function createTagBtn(i, jsonData) {
    var input = createInput(i, jsonData);
    var label = createLabel(i, jsonData, input);

    if(jsonData == moodTagData) {
        $('#store-mood-tag-row').append(input);
        $('#store-mood-tag-row').append(label);
        $('#'+label.id).append(jsonData[i]);

    } else if(jsonData == priceTagData) {
        $('#store-price-tag-row').append(input);
        $('#store-price-tag-row').append(label);
        $('#'+label.id).append(jsonData[i]);

    } else if(jsonData == categoryTagData) {
        $('#store-subCategory-tag-row').append(input);
        $('#store-subCategory-tag-row').append(label);
        $('#'+label.id).append(jsonData[i]);

    } else if (jsonData == waitingTagData) {
        $('#store-waiting-tag-row').append(input);
        $('#store-waiting-tag-row').append(label);
        $('#'+label.id).append(jsonData[i]);
    }
}

function createInput(i, jsonData) {
    var inputId;
    var inputClass;
    var inputName;
    var inputType;
    var inputValue;

    if(jsonData == moodTagData) {
        inputId = 'mood-tag-opt' +i ;
        inputClass = 'mood-tag-btn btn-check';
        inputName = 'moodList[]';
        inputType = 'checkbox';
        inputValue = jsonData[i]

    } else if(jsonData == priceTagData) {
        inputId = 'price-tag-opt' +i ;
        inputClass = 'price-tag btn-check';
        inputName = 'priceRange';
        inputType = 'radio';
        inputValue = jsonData[i]

    } else if (jsonData == categoryTagData) {
        inputId = 'category-tag-opt' +i ;
        inputClass = 'category-tag-btn btn-check';
        inputName = 'subCategoryList[]';
        inputType = 'checkbox';
        inputValue = jsonData[i]

    } else if (jsonData == waitingTagData) {
        inputId = 'waiting-tag-opt' +i;
        inputClass = 'waiting-tag btn-check';
        inputName = 'waiting';
        inputType = 'radio';
        inputValue = jsonData[i]
    }

    var input = document.createElement('input');
    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('name', inputName);
    input.setAttribute('type', inputType);
    input.setAttribute('value', inputValue);

    return input;
}

function createLabel(i, jsonData, input) {
    var labelId;
    var labelClass;
    var labelFor;

    if(jsonData == moodTagData) {
        labelId = 'store-mood-tag-opt-label' +i;
        labelClass = 'btn btn-outline-secondary me-2 mb-2'
        labelFor = input.id;

    } else if(jsonData == priceTagData) {
        labelId = 'price-tag-opt-label' +i;
        labelClass = 'btn btn-outline-secondary me-2 mb-2'
        labelFor = input.id;

    } else if (jsonData == categoryTagData) {
        labelId = 'category-tag-opt-label' +i;
        labelClass = 'btn btn-outline-secondary me-2 mb-2'
        labelFor = input.id;

    } else if (jsonData == waitingTagData) {
        labelId = 'waiting-tag-opt-label' +i;
        labelClass = 'btn btn-outline-secondary me-2 mb-2'
        labelFor = input.id;
    }

    var label = document.createElement('label');
    label.setAttribute('id', labelId);
    label.setAttribute('class', labelClass);
    label.setAttribute('for', labelFor);

    return label;
}

function createStatus() {
    var statusSlider = createStatusSlider();
    var statusValue = createStatusValue();

    $('#store-status-row').append(statusSlider);
    $('#store-status-row').append(statusValue);
}

function createStatusSlider() {
    var sliderRow = document.createElement('div');
    var sliderRowId = 'store-status-slider-row';
    var sliderRowClass = 'col-8 me-2';

    sliderRow.setAttribute('id', sliderRowId);
    sliderRow.setAttribute('class', sliderRowClass);

    var slider = document.createElement('input');
    var sliderId = 'status-slider';
    var sliderClass = 'form-range'
    var sliderName = 'status';
    var sliderType = 'range';
    var sliderMin = "0";
    var sliderMax = "10";

    slider.setAttribute('id', sliderId);
    slider.setAttribute('class', sliderClass);
    slider.setAttribute('name', sliderName);
    slider.setAttribute('type', sliderType);
    slider.setAttribute('min', sliderMin);
    slider.setAttribute('max', sliderMax);

    sliderRow.appendChild(slider);

    return sliderRow;
}

function createStatusValue() {
    var valueRow = document.createElement('div');
    var valueRowId = 'store-status-value-row';

    var textSpan = document.createElement('span');
    var textSpanId = 'status-value-text';
    var textSpanClass = 'text-secondary';

    var valueSpan = document.createElement('span');
    var valueSpanId = 'status-value';
    var valueSpanClass = 'text-secondary';

    valueRow.setAttribute('id', valueRowId);
    textSpan.setAttribute('id', textSpanId);
    textSpan.setAttribute('class', textSpanClass);
    textSpan.append("점수를 선택해 주세요");

    valueSpan.setAttribute('id', valueSpanId);
    valueSpan.setAttribute('class', valueSpanClass);

    valueRow.appendChild(valueSpan);
    valueRow.appendChild(textSpan);

    return valueRow;
}

function createImageBox() {
    var imageZone = document.querySelector('#image-zone');

    var boxClass = "non-select-img-box img-thumbnail px-1 py-1 position-relative";
    var boxStyle =  "width:120px; height:120px;";

    for(var imageNum=0; imageNum<4; imageNum++) {
        var imageBox = document.createElement('div');
        var boxId = "image-box" +imageNum;

        imageBox.setAttribute('id', boxId);
        imageBox.setAttribute('class', boxClass);
        imageBox.setAttribute('style', boxStyle);

        var image = createImage(imageNum);
        var deleteBtn = createDeleteBtn(imageNum);
        var idInput = createIdInput(imageNum);
        var nameInput = createNameInput(imageNum);
        var sizeInput = createSizeInput(imageNum);

        imageBox.appendChild(deleteBtn);
        imageBox.appendChild(image);
        imageBox.appendChild(idInput);
        imageBox.appendChild(nameInput);
        imageBox.appendChild(sizeInput);

        imageZone.appendChild(imageBox);
    }
}

function createImage(imageNum) {
    var img = document.createElement('img');
    var id = "store-image" +imageNum;
    var src = "/images/picture.svg";
    var imageClass = "non-select-img img-fit w-100 h-100";

    img.setAttribute('id', id);
    img.setAttribute('src', src);
    img.setAttribute('class', imageClass);

    return img;
}

function createDeleteBtn(imageNum) {
    var a = document.createElement('a');
    var aId = "image-delete-btn" + imageNum;
    var aClass = "delete position-absolute top-0 end-0 px-1";
    var aStyle = "z-index:1";
    var btnOnclick = 'deleteFile(' +imageNum+ ')';

    var icon = document.createElement('i');
    var iconClass = "img-btn-icon far fa-minus-square";

    a.setAttribute('id', aId);
    a.setAttribute('class', aClass);
    a.setAttribute('style', aStyle);
    a.setAttribute('onclick', btnOnclick);
    icon.setAttribute('class', iconClass);

    a.appendChild(icon);

    return a;
}

function createIdInput(indexNo) {
    var input = document.createElement('input');
    var inputId = 'image-id-input' +indexNo;
    var inputType = 'hidden';

    input.setAttribute('id', inputId);
    input.setAttribute('type', inputType);

    return input;
}

function createNameInput(indexNo) {
    var input = document.createElement('input');
    var inputId = 'image-name-input' +indexNo;
    var inputType = 'hidden';

    input.setAttribute('id', inputId);
    input.setAttribute('type', inputType);

    return input;
}

function createSizeInput(indexNo) {
    var input = document.createElement('input');
    var inputId = 'image-size-input' +indexNo;
    var inputType = 'hidden';

    input.setAttribute('id', inputId);
    input.setAttribute('type', inputType);

    return input;
}

function createValidateError(target, message) {
    var targetId = target + "-errors";
    var targetDiv = $('#'+targetId);

    var span = document.createElement('span');
    span.append(message);

    targetDiv.append(span);
}