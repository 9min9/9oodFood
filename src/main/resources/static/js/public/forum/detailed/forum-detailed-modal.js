function createModalImage(jsonData, i) {
    var modalImage = document.createElement("img");
    var imgSrc;
    var imageAlt = "MODAL-IMAGE "+i;

    if(jsonData.imagePath.length == 0) {
        if(jsonData.category == "RESTAURANT") {
            imgSrc = "/images/restaurant.svg"
        } else if (jsonData.category == "CAFE") {
            imgSrc = "/images/cafe.svg"
        } else if (jsonData.category == "BAR") {
            imgSrc = "/images/bar.svg"
        }
    } else {
        imgSrc = jsonData.imagePath[i];
    }

    modalImage.setAttribute('src', imgSrc);
    modalImage.setAttribute('alt', imageAlt);

    return modalImage;
}


function createModalCarouselPrevBtn() {
    var nextBtn = document.createElement('button');
    var btnId = "carousel-prev-btn";
    var btnClass = "carousel-control-prev";
    var btnType = "button";
    var target = "#store-image-modal-carousel";
    var slide = "prev";

    nextBtn.setAttribute('id', btnId);
    nextBtn.setAttribute('class', btnClass);
    nextBtn.setAttribute('type', btnType);
    nextBtn.setAttribute('data-bs-target', target);
    nextBtn.setAttribute('data-bs-slide', slide);

    var iconSpan = document.createElement('span');
    var iconClass = "carousel-control-prev-icon";

    iconSpan.setAttribute('class', iconClass);
    iconSpan.setAttribute('aria-hidden', "true");

    var textSpan = document.createElement('span');
    var textClass = "visually-hidden";

    textSpan.setAttribute('class', textClass);
    textSpan.append("Prev");

    nextBtn.appendChild(iconSpan);
    nextBtn.appendChild(textSpan);

    return nextBtn;
}

function createModalCarouselNextBtn() {
    var nextBtn = document.createElement('button');
    var btnId = "carousel-next-btn";
    var btnClass = "carousel-control-next";
    var btnType = "button";
    var target = "#store-image-modal-carousel";
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

function createModalCarouselIndicatorBtn(jsonData) {
    var carouselIndicators = document.querySelector("#store-image-modal-carousel-indicator");
    var btnType = "button";
    var dataTarget = "#store-image-modal-carousel";
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
