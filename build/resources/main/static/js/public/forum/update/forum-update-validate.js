
$('#updateForum-form').on("submit",function (e) {
    e.preventDefault();

    var text = $('#store-content').val();
    text = text.replace(/(\r\n|\r|\n)/g, '<br>');
    $('#store-content').val(text);

    onSubmitForm();
});


function onSubmitForm() {
    var formData = new FormData($('#updateForum-form')[0]);
    $('div[id*="-errors"]').empty();

    var data = {};
    formData.forEach(function(value, key){
        data[key] = value;
    });

    $.ajax({
        type:"POST",
        url:"https://www.9oodfood.com/api/public/forum/update",
        contentType: false,
        processData: false,
        data: formData,

        success: function(jsonData){
            alert('수정이 완료되었습니다.');
            window.location.href = '/';

        },
        error:function(error) {
            if(error.responseJSON) {
                var errors = error.responseJSON;

                for(var i=0; i< errors.length; i++) {
                    var errorField = errors[i].field;
                    var errorCode = errors[i].code;
                    var errorMessage = errors[i].message;
                    createValidateError(errorField, errorMessage);
                }
            }
        }
    });

}

function removeHtml(text) {
    text = text.replace(/<br\/>/ig, "\n");
    text = text.replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, "");
    return text;
}
