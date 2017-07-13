$(document).ready(function () {
    var $url = 'Controller';
    var movieId = $('#movie-id').text();
    var notRatedEn = "Not Rated";
    var deleteMsg = "Delete?";
    var isRated;
    $('#input-movie-rating').rating({
        min: 0, max: 10, step: 0.1,
        stars: 10, size: 'xs', showClear: false, displayOnly: true
    });
    $('#input-user-movie-rating').rating({
        min: 0, max: 10, step: 0.5, stars: 10, size: 'xs'
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
            }
            else if (isRated) {
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
});
