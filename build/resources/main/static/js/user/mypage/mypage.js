$(document).ready(function(){
    getUserInfo()

});

function getUserInfo() {
    $.ajax({
        type:"POST",
        url: "https://www.9oodfood.com/api/user/mypage/info",
        dataType:"JSON",
        contextType : "application/json",

        success: function(jsonData){
            $('#login-id').val(jsonData.username);
            $('#nickname').val(jsonData.nickname);
            $('#email').val(jsonData.email);
            $('#name').val(jsonData.name);
            $('#gender').val(jsonData.gender);
            $('#yy').val(jsonData.year);
            $('#mm').val(jsonData.month);
            $('#dd').val(jsonData.day);

            if(jsonData.emailAuth == true) {
                $("#email").attr("readonly", true);
                $("#email-send-btn").attr("disabled", true);
                $('#emailAuth').val(jsonData.emailAuth);

            }
        },
        error:function(){
        }
    });

}

$('#user-update-chk-btn').on('click', function(e) {
  e.preventDefault();
  $('#user-update-modal').modal('hide');
  $('#user-update-form').submit();
});
