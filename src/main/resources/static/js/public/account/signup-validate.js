
//Check Duplicate LoginId
$(document).on("click", "button[id='username-confirm-btn']", function(){
    var loginId = $('#username').val();
    $('#username-errors').empty();

    if(!loginId) {
        createValidateError("username", "아이디를 입력해주세요.");

    } else {

        $.ajax({
            type:"POST",
            url:"https://www.9oodfood.com/api/public/duplicate/check",
            contentType: "application/json",
            data: JSON.stringify({username : loginId}),
            success: function(jsonData){
                createValidateError("username", "사용할 수 있는 이름입니다.");
                $('#username-errors').attr('class', 'text-success small mt-1');

            },
            error:function(error) {
                if(error.responseJSON) {
                    var errors = error.responseJSON;
                    var errorCode = errors[0].code;
                    var errorMessage = errors[0].defaultMessage;
                    $('#username-errors').attr('class', 'text-danger small mt-1');

                    if(errorCode == "duplicate.username") {
                        createValidateError("username", errorMessage);
                    }

                    if(errorCode == "Size.username") {
                        createValidateError("username", errorMessage);
                    }

                    if(errorCode == "Pattern.username") {
                        createValidateError("username", errorMessage);
                    }
                }
            }
        });
    }
});

//Send Email
$(document).on("click", "button[id='email-send-btn']", function(){
    var email = $('#email').val();
    var message = "9ood Food 회원가입 본인인증 메일"
    $('#email-errors').empty();

    if(!email) {
        createValidateError("email", "이메일을 입력해주세요.");

    } else {
        createMailConfirmForm();
        createTokenError();

        $.ajax({
            type:"POST",
            url:"https://www.9oodfood.com/api/public/signup/mail/send",
            contentType: "application/json",
            data: JSON.stringify({email : email, message : message }),
            success: function(){
                $('#email-errors').empty();

            },
            error: function(error) {
                if(error.responseJSON) {
                    var errors = error.responseJSON;
                    var errorField = errors[0].field;
                    var errorCode = errors[0].code;
                    var errorMessage = errors[0].defaultMessage;

                    if(errorCode == "duplicate.email") {
                        createValidateError(errorField, errorMessage);
                    }

                    if(errorCode == "Email") {
                        createValidateError(errorField, errorMessage);
                    }
                }
            }
        });
    }
});

//Check Validate Email
$(document).on("click", "button[id='email-confirm-btn']", function(){
    var email = $('#email').val();
    var token = $('#token').val();

    if(!token) {
        createValidateError('token', "인증번호를 입력해주세요.")

    } else {
        $.ajax({
            type:"POST",
            url:"https://www.9oodfood.com/api/public/mail/validate",
            contentType: "application/json",
            data: JSON.stringify({ email: email, token:token }),
            success: function(jsonData){
                alert("인증에 성공했습니다.");
                $('#token-errors').empty();
                $('#emailAuth').attr('value', true);

                $('#email').attr("readonly", true);
                $('#token').attr("readonly", true);
                $('#email-send-btn').attr("disabled", true);
                $('#email-confirm-btn').attr("disabled", true);

            },
            error:function(error) {

                if(error.responseJSON) {
                    var errors = error.responseJSON;
                    var errorField = errors[0].field;
                    var errorCode = errors[0].code;
                    var errorMessage = errors[0].defaultMessage;

                    if(errorCode == "misMatch.token") {
                        alert("인증번호가 일치하지 않습니다.");
                        createValidateError(errorField, errorMessage);
                    }

                    if(errorCode == "expired.token") {
                        alert("이미 만료된 인증번호입니다.");
                        createValidateError(errorField, errorMessage);
                    }
                }
            }
        });
    }
});

$('#signup-form').on("submit",function (e) {
    e.preventDefault(); // 폼 기본 동작 방지
    onSubmitForm();
});

function onSubmitForm() {
    var formData = new FormData($('#signup-form')[0]);
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
        url:"https://www.9oodfood.com/api/public/signup/confirm",
        contentType: "application/json",
        data: JSON.stringify(data),

        success: function(jsonData){
            alert('회원가입이 완료되었습니다.');
            window.location.href = '/login';

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



