function createMailConfirmForm() {
    $('#email-confirm-zone').empty();

    var input = document.createElement('input');
    var inputId = "token";
    var inputClass = "form-control mt-2"
    var inputName = "token";
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

function createTokenError() {
//    $('#token-errors').empty();

    var div = document.createElement('div');
    var id = "token-errors";
    var divClass = "text-danger small mt-1";

    div.setAttribute('id', id);
    div.setAttribute('class', divClass);

    $('#email-validate-zone').append(div);

}

function createValidateError(target, message) {
    var targetId = target + "-errors";
    var targetDiv = $('#'+targetId);

    var span = document.createElement('span');
    span.append(message);

    targetDiv.append(span);
}
