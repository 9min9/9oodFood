$(document).ready(function() {
    const searchParams = new URLSearchParams(location.search);      //URL의 param (?ooo=) 추출
    const forumId = searchParams.get('forum');                     //?forum= value value 추출
    showStore(forumId);
});

function showStore(id) {
    $.ajax({
        type:"GET",
        url:"https://www.9oodfood.com/api/public/forum/detail/"+id,
        dataType:"json",
        success: function(jsonData){
            createStoreTitle(jsonData);
            createStoreInfo(jsonData);
            createImageCarousel(jsonData);
            createStoreComment(jsonData);
            createStoreMap(jsonData);
        },
        error:function() {
        }
    });
}

$(document).on("click", ".store-image", function(e){
    var src = $(this).attr("src");
    var modal = $("#store-image-modal");
    var child = modal.children();
    var find = modal.find('img');
    var src = find.attr("src")
    var findModalSrc = $("img[src='" +src+ "']");
    var parent = findModalSrc.parent().attr("class", "changed");
    var modalImage = $("#store-image-modal").find("img[src='" +src+ "']");
    var index = $("#store-image-modal").find("img[src='" +src+ "']").parent().attr('name');

    $("#store-image-modal").show();
});

$("#store-image-modal").on("shown.bs.modal", function () {
    $("#store-image-modal").focus();
});


$(document).on("click", "#store-image-modal", function(e){
    if (e.target.id != "store-image-modal") {
        return false;
    } else {
        $("#store-image-modal").hide();
    }
});

$(document).on("click", "#image-modal-btn-close", function(e){
    $("#store-image-modal").hide();
});