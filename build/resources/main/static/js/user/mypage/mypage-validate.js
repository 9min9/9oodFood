$('#user-update-form').on("submit",function (e) {
    e.preventDefault(); // 폼 기본 동작 방지
    onSubmitForm();
});

function onSubmitForm() {
    var formData = new FormData($('#user-update-form')[0]);
    var data = {};
    $('div[id*="-errors"]').empty();

    formData.forEach(function(value, key){
        if(value == "true") {
            data[key] = true
        } else {
            data[key] = value;
        }
    });

    $.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/user/update/confirm",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function(jsonData){
            alert('회원 수정이 완료되었습니다.');
            window.location.href = '/';

        },
        error:function(error) {
            if(error.responseJSON) {
                var errors = error.responseJSON;

                for(var i=0; i< errors.length; i++) {
                    var errorField = errors[i].field;
                    var errorCode = errors[i].code;
                    var errorMessage = errors[i].message;
                    $('#'+errorField+'-errors').empty();

                    if(errorCode.indexOf("AssertTrue") !== -1) {
                        createValidateError("token", errorMessage);
                    } else {
                        createValidateError(errorField, errorMessage);
                    }
                }

                errors.forEach(function(error) {
                    var errorCode = error.code;
                    var errorField = error.field;
                    var errorMessage = error.message;

                    if(errorCode.indexOf("NotBlank") !== -1) {
                        $('#'+errorField+'-errors').empty();
                        createValidateError(errorField, errorMessage);
                    }
                });
            }
        }
    });

}