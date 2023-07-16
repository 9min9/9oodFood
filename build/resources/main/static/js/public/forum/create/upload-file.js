var filesArr = new Array();

/* 첨부파일 추가 */
function addFile(obj){
    var maxFileCnt = 4;                                                 // 첨부파일 최대 개수
    var attFileCnt = document.querySelectorAll('.select-img').length;   // 기존 추가된 첨부파일 개수
    var remainFileCnt = maxFileCnt - attFileCnt;                        // 추가로 첨부가능한 개수
    var curFileCnt = obj.files.length;                                  // 현재 선택된 첨부파일 개수

    // Btn 생성
    for(var i=0; i<maxFileCnt; i++) {
        createBtn(i);
    }

    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
    }

    for (var i = 0; i < Math.min(curFileCnt, remainFileCnt); i++) {
        const file = obj.files[i];

        // 첨부파일 검증
        if (validation(file)) {
            // 파일 배열에 담기
            filesArr.push(file);
            const imgUrl = URL.createObjectURL(file);               //Img URL 추출
            createImg(i, imgUrl, file.name);                       //미리보기 Img 생성

        } else {
            continue;
        }
    }

    const dataTransfer = new DataTransfer();
    filesArr.forEach(file => { dataTransfer.items.add(file); });
    $('#upload_btn')[0].files = dataTransfer.files;
}

function createImg(indexNo, imgUrl, fileName) {
    var img = document.querySelector('.non-select-img');

    var imgClass = 'select-img img-fit position-relative';
    var imgStyle = 'width:110px; height:110px;';
    var imgName = fileName;

    img.setAttribute('class', imgClass);
    img.setAttribute('name', imgName);
    img.setAttribute('src', imgUrl);
    img.setAttribute('style', imgStyle);

    return img;
}

function createBtn(indexNo) {
    var btnId = 'img-btn' +indexNo;
    var btn = document.getElementById(btnId);
    var btnClass = 'delete position-absolute top-0 end-0 px-1';
    var btnOnclick = 'deleteFile(' +indexNo+ ')';

    btn.setAttribute('class', btnClass);
    btn.setAttribute('onclick', btnOnclick);

    return btn;
}

//1. img의 이름을 가져온다
//2. filesArr에서 img의 이름을 찾는다
//3. 2의 결과의 인덱스를 삭제
/* 첨부파일 삭제 */
function deleteFile(num) {
    var findImg = document.querySelector("#testImg" + num);
    var findImgName = findImg.getAttribute('name');     //1. img의 이름을 가져옴
    var fileIndex = filesArr.findIndex(obj => obj.name == findImgName); //2. 1의 결과를 통해 filesArr의 index 반환

    findImg.setAttribute('src', '/images/picture.svg');
    findImg.setAttribute('class', 'non-select-img img-fit position-relative');
    findImg.setAttribute('name', '');

    filesArr.splice(fileIndex, 1) ; //3. 2의 결과로 받은 index의 파일을 삭제

    //성공한 제거된 파일을 삭제하는 로직
    const dataTransfer = new DataTransfer();
    filesArr.forEach(file => { dataTransfer.items.add(file); });
    $('#upload_btn')[0].files = dataTransfer.files;	//제거 처리된 FileList를 돌려줌
}

/* 첨부파일 검증 */
function validation(obj){
    const fileTypes = ['application/pdf', 'image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif', 'application/haansofthwp', 'application/x-hwp'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (500 * 1024 * 1024)) {
        alert("최대 파일 용량인 500MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}