
$(document).ready(function () {
    var $url = 'Controller';
    var lang = $('html').attr("lang");
    var participantId = $('#participant-id').text();
    var deleteMsg;
    var errorMsg;

    if (lang === "ru_RU") {
        errorMsg = "Ошибка в процессе выполнения операции";
        deleteMsg = "Удалить?";
    }
    if (lang === "en_EN") {
        errorMsg = "Error during procedure";
        deleteMsg = "Delete?";
    }
    
    $(".delete-participant").on("click", function () {
        $("#delete-participant-modal").modal({})
    });

    $("#delete-participant-modal").find("#delete-participant-btn").on("click", function () {
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'delete-participant',
                participantId: participantId
            },
            success: function (isDeleted) {
                if (isDeleted) {
                    window.location.href = 'Controller?command=show-actors';
                } else if (!isDeleted) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });

    function hideMsg() {
        setTimeout(function () {
            $('#message').empty();
        }, 3000);
    }
});
