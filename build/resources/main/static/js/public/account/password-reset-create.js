function createMailConfirmForm() {
    $('#email-confirm-zone').empty();

    var input = document.createElement('input');
    var inputId = "token";
    var inputClass = "form-control mt-2"
    var inputName = "emailTokenConfirm";
    var inputType = "text";

    var btn = document.createElement('button');
    var btnId = "email-confirm-btn";
    var btnClass = "btn btn-outline-secondary";
    var btnType = "button";
    var btnValue = "인증하기";

    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('name', inputName);
    input.setAttribute('type', inputType);

    btn.setAttribute('id', btnId);
    btn.setAttribute('class', btnClass);
    btn.setAttribute('type', btnType);
    btn.append(btnValue);

    $('#email-confirm-zone').append(input);
    $('#email-confirm-zone').append(btn);


}

function createChangePasswordForm() {
    $('#change-password-zone').empty();
    $('#confirm-password-zone').empty();

    var passwordLabel = createLabel("password");
    var passwordInput = createInput("password");

    var confirmLabel = createLabel("confirm");
    var confirmInput = createInput("confirm");
    var submitBtn = createSubmitBtn();

    $('#change-password-zone').append(passwordLabel);
    $('#change-password-zone').append(passwordInput);

    $('#confirm-password-zone').append(confirmLabel);
    $('#confirm-password-zone').append(confirmInput);
    $('#password-change-btn-zone').append(submitBtn);

}

function createInput(flag) {
    var input = document.createElement('input');
    var inputId;
    var inputClass;
    var inputName;
    var inputType;

    if(flag == "password") {
        inputId = "changePassword";
        inputClass = "form-control"
        inputName = "changePassword";
        inputType = "password";
    }

    if(flag == "confirm") {
        inputId = "confirmPassword";
        inputClass = "form-control"
        inputName = "confirmPassword";
        inputType = "password";
    }

    input.setAttribute('id', inputId);
    input.setAttribute('class', inputClass);
    input.setAttribute('name', inputName);
    input.setAttribute('type', inputType);

    return input;
}

function createLabel(flag) {
    var label = document.createElement('label');

    var labelId;
    var labelClass;
    var labelName;
    var labelValue;

    if(flag == "password") {
        labelId = "change-password-label";
        labelClass = "form-label my-3"
        labelName = "changePassword";
        labelValue = "변경 할 비밀번호";
    }

    if(flag == "confirm") {
        labelId = "confirm-password-label";
        labelClass = "form-label my-3"
        labelName = "confirmPassword";
        labelValue = "비밀번호 확인";
    }

    label.setAttribute('id', labelId);
    label.setAttribute('class',  labelClass);
    label.setAttribute('name',  labelName);
    label.append(labelValue);

    return label;


}

function createSubmitBtn() {
    var input = document.createElement('input');
    var id = "submit-btn";
    var inputClass = "btn btn-secondary btn-block my-3"
    var type = "submit";
    var value = "변경하기";

    input.setAttribute('id', id);
    input.setAttribute('class', inputClass);
    input.setAttribute('type', type);
    input.setAttribute('value', value);

    return input;
}

function createValidateError(target, message) {
    var targetId = target + "-errors";
    var targetDiv = $('#'+targetId);

    targetDiv.empty();
    var span = document.createElement('span');
    span.append(message);

    targetDiv.append(span);
}
