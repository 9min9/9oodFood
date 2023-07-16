
$(document).on("click", "button[id='email-send-btn']", function(){
    var loginId = $('#loginId').val();
    var email = $('#email').val();

    if(!loginId) {
        createValidateError("username", "아이디를 입력해주세요.");
    } else {
        $('#username-errors').empty();
    }

    if(!email) {
        createValidateError("email", "이메일을 입력해주세요.");
    } else {
        $('#email-errors').empty();
    }

    if(loginId && email) {
        $.ajax({
                type:"POST",
                url:"https://www.9oodfood.com/api/public/password/reset/mail/send",
                contentType: "application/json",
//                dataType : "json",
                data: JSON.stringify({ loginId: loginId, email: email }),
                success: function(){
                    alert("이메일이 확인되었습니다.");
                    createMailConfirmForm();

                },
                error: function(error) {
                    var errors = error.responseJSON;
                    var errorMessage = errors[0].defaultMessage;
                    var errorCode = errors[0].code;
                    if(errorCode.indexOf('email') != -1) {
                        createValidateError('email', errorMessage);
                    }
                    if(errorCode.indexOf('username') != -1) {
                        createValidateError('username', errorMessage);
                    }
                }
            });
    }
});

//Check Validate Email (Sign up)
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

                createChangePasswordForm();

            },
            error:function(error) {
                if(error.responseJSON) {
                    var errors = error.responseJSON;
                    var errorField = errors[0].field;
                    var errorCode = errors[0].code;
                    var errorMessage = errors[0].defaultMessage;

                    if(errorCode == "misMatch.token") {
                        createValidateError(errorField, errorMessage)
                    }

                    if(errorCode == "expired.token") {
                        createValidateError(errorField, errorMessage)
                    }
                }
            }
        });
    }
});


$('#password-reset-form').on("submit",function (e) {
    e.preventDefault(); // 폼 기본 동작 방지
    onSubmitForm();
});

function onSubmitForm() {
    var formData = new FormData($('#password-reset-form')[0]);
    var data = {};
    $('div[id*="-errors"]').empty();

    formData.forEach(function(value, key){
        data[key] = value;
    });

    var changePassword = $('#changePassword').val();
    var confirmPassword = $('#confirmPassword').val();

    if(!changePassword) {
        createValidateError('changePassword', "비밀번호를 입력해주세요.")
    }

    if(!confirmPassword) {
        createValidateError('confirmPassword', "비밀번호 확인을 입력해주세요.")
    }

    if (changePassword && confirmPassword) {
        $('#changePassword-errors').empty();
        $('#confirmPassword-errors').empty();

        $.ajax({
            type:"POST",
            url:"https://wwww.9oodfood.com/api/public/password/reset",
            contentType: "application/json",
            data: JSON.stringify(data),

            success: function(jsonData){
                alert('비밀번호가 변경되었습니다.');
                window.location.href = '/login';

            },
            error:function(error) {
                if(error.responseJSON) {
                    var errors = error.responseJSON;

                    for(var i=0; i< errors.length; i++) {
                        var errorField = errors[i].field;
                        var errorCode = errors[i].code;
                        var errorMessage = errors[i].message;

                        if(errorCode.indexOf("AssertTrue") !== -1) {
                            createValidateError("token", errorMessage);
                        } else {
                            createValidateError(errorField, errorMessage);
                        }
                    }
                }
            }
        });
    }
}

