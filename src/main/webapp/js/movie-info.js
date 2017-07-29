
$(document).ready(function () {
    var $url = 'Controller';
    var lang = $('html').attr("lang");
    var movieId = $('#movie-id').text();
    var userId = $('#user-id').text();
    var notRatedEn = "Not Rated";
    var deleteMsg;
    var yetReviewedMsg;
    var needAuthMsgForRate;
    var rateFirstMsg;
    var errorMsg;
    var bannedUser;
    var yourRatingMsg;
    var isRated;
    var isBanned;
    var isUser = true;

    if (lang === "ru_RU") {
        yetReviewedMsg = "Вы уже оставили отзыв на этот фильм";
        needAuthMsgForRate = "Войдите в систему, чтобы поставить оценку";
        errorMsg = "Ошибка в процессе выполнения операции";
        bannedUser = "Вы не можете оценить фильм, так как забанены";
        deleteMsg = "Удалить?";
        yourRatingMsg = "Твой рейтинг:";
        // rate="Оценить";
    }
    if (lang === "en_EN") {
        yetReviewedMsg = "You have reviewed this movie yet";
        needAuthMsgForRate = "Log in to rate movie";
        errorMsg = "Error during procedure";
        bannedUser = "You can't rate movie because you are banned";
        deleteMsg = "Delete?";
        yourRatingMsg = "Your rating:";
        // rate ="Rate";
    }


    function hideMsg() {
        setTimeout(function () {
            $('#message').empty();
        }, 3000);
    }

    $('#input-movie-rating').rating({
        min: 0, max: 10, step: 0.1,
        stars: 10, size: 'xs', showClear: false, displayOnly: true
    });
    $('#input-user-movie-rating').rating({
        min: 0, max: 10, step: 0.5, stars: 10, size: 'xs'
    });


    $.ajax({
        type: 'POST',
        url: $url,
        data: {
            command: 'check-rate-opportunity'
        },
        success: function (data) {
            if (data === "isBanned") {
                $('#input-user-movie-rating').rating('refresh', {
                    disabled: true
                });
                isBanned = true;
            } else if (data === "false") {
                $('#input-user-movie-rating').rating('refresh', {
                    disabled: true
                });
                isUser = false;
            }
        }
    });

    $(".users-movie-rating").hover(function () {
        if (!isUser) {
            $('#message').html('<div class="alert alert-info fade in  ">' +
                '<button type="button" class="close close-alert" data-dismiss="alert"' +
                ' aria-hidden="true">×</button>' + needAuthMsgForRate +
                '</div>');
        } else if (isBanned) {
            $('#message').html('<div class="alert alert-danger fade in ">' +
                '<button type="button" class="close close-alert" data-dismiss="alert"' +
                ' aria-hidden="true">×</button>' + bannedUser +
                '</div>');
        }
    });

    if ($('.users-movie-rating').find('.caption').text().trim() === notRatedEn) {
        isRated = false;
    } else {
        isRated = true;
    }


    $(".users-movie-rating").on("click", ".rating-container .rating-stars", function () {
        var rating = $('.users-movie-rating').find('.caption').text().trim();
    });

    $('#input-user-movie-rating')
        .on('rating.change', function (event, value, caption) {
            var rating = $('.users-movie-rating').find('.caption').text().trim();
            if (!isRated) {
                $.ajax({
                    type: 'POST',
                    url: $url,
                    data: {
                        command: 'rate-movie',
                        movieId: movieId,
                        mark: rating
                    },
                    success: function (data) {
                        isRated = true;
                        alert("Rated");
                    }
                });
            } else if (isRated) {
                $.ajax({
                    type: 'POST',
                    url: $url,
                    data: {
                        command: 'update-movie-rating',
                        movieId: movieId,
                        mark: rating
                    },
                    success: function (data) {
                        isRated = true;
                        alert("Updated");
                    }
                });
            }
        })
        .on('rating.clear', function (event) {
            var rating = $('.users-movie-rating').find('.caption').text().trim();
            if (isRated) {
                if (confirm(deleteMsg)) {
                    $.ajax({
                        type: 'POST',
                        url: $url,
                        data: {
                            command: 'delete-movie-rating',
                            movieId: movieId
                        },
                        success: function (data) {
                            isRated = false;
                            alert("Deleted");
                        }
                    });
                }
            }
        });
    
    $(".delete-movie").on("click", function () {
        $("#delete-movie-modal").modal({})
    });

    $("#delete-movie-modal").find("#delete-movie-btn").on("click", function () {
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'delete-movie',
                movieId: movieId
            },
            success: function (isDeleted) {
                if(isDeleted){
                    window.location.href = 'Controller?command=show-top-movies';
                } else if(!isDeleted){
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });
});


