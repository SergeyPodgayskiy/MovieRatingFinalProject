$(document).ready(function () {
    var $url = 'Controller';
    var lang = $('html').attr("lang");
    var movieId = $('#movie-id').text();
    var userId = $('#user-id').text().trim();
    var reviewIdUser = $('#review-id-user').text().trim();
    var reviewText;
    var reviewTitle;
    var reviewType;
    var notRatedEn = "Not Rated";
    var deleteMsg;
    var yetReviewedMsg;
    var needAuthMsgForRate;
    var needAuthMsgForReview;
    var errorMsg;
    var bannedUserRate;
    var bannedUserReview;
    var yourRatingMsg;
    var isRated;
    var isBanned;
    var isUser = true;

    if (lang === "ru_RU") {
        yetReviewedMsg = "Вы уже оставили отзыв на этот фильм";
        needAuthMsgForRate = "Войдите в систему, чтобы поставить оценку";
        needAuthMsgForReview = "Войдите в систему, чтобы оставить отзыв";
        errorMsg = "Ошибка в процессе выполнения операции";
        bannedUserRate = "Вы не можете оценить фильм, так как забанены";
        bannedUserReview = "Вы не можете оставить отзыв, так как забанены";
        deleteMsg = "Удалить?";
        yourRatingMsg = "Твой рейтинг:";
    }
    if (lang === "en_EN") {
        yetReviewedMsg = "You have reviewed this movie yet";
        needAuthMsgForRate = "Log in to rate movie";
        needAuthMsgForReview = "Log in to review movie";
        errorMsg = "Error during procedure";
        bannedUserRate = "You can't rate movie because you are banned";
        bannedUserReview = "You can't review movie because you are banned";
        deleteMsg = "Delete?";
        yourRatingMsg = "Your rating:";
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

    $.ajax({
        type: 'POST',
        url: $url,
        data: {
            command: 'check-review-opportunity',
            movieId: movieId
        },
        success: function (result) {
            if (result === 'guest') {
                $('#review-message').html('<div class="alert alert-info fade in ">' + needAuthMsgForReview + '</div>');
            } else if (result === 'isBanned') {
                $('#review-message').html('<div class="alert alert-danger fade in ">' + bannedUserReview + '</div>');
            } else if (result) {
                $('#review-message').html('<div class="alert alert-info fade in ">' + yetReviewedMsg + '</div>');
                /*$(".review").css({
                 "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)"
                 });*/
            } else if (!result) {
                $('#open-review-box').show();
            } else {
                $('#review-message').html('<div class="alert alert-danger fade in ">' + errorMsg + '</div>');
            }
        },
        error: function () {
            alert(errorMsg);
            $('#review-message').html('<div class="alert alert-danger fade in ">' + errorMsg + '</div>');
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
                ' aria-hidden="true">×</button>' + bannedUserRate +
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
                if (isDeleted) {
                    window.location.href = 'Controller?command=show-top-movies';
                } else if (!isDeleted) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });

    $("#delete-review-btn").on("click", function () {
        $("#delete-review-modal").modal({})
    });

    $("#delete-review-modal").find("#accept-delete-review-btn").on("click", function () {
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'delete-review',
                movieId: movieId
            },
            success: function (isDeleted) {
                if (isDeleted) {
                    window.location.reload(true);
                } else if (!isDeleted) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });

    $("#edit-review-btn").on("click", function () {
        if ($('.review').hasClass('user-review')) {
            $("#edit-review-btn").hide();
            reviewText = $('.user-review').find(".review-text").text().trim();
            reviewTitle =$('.user-review').find(".review-title").find('b').text().trim();
            reviewType = $('.user-review').find(".review-type").val().trim();
            $('.user-review').find(".review-content-form").hide();
            $('.user-review').find(".review-title").find('b').hide();
            $("#accept-edit-rev-btn").show();
            $('.user-review').find(".review-textarea").val(reviewText);
            $('.user-review').find(".review-type").val(reviewType);
            $('.user-review').find("#edit-review-input").val(reviewTitle);
            $('.user-review').find(".edit-review-input-wrapper, .edit-review-form").show();
        }
    });

    $("#accept-edit-rev-btn").on("click", function () {
        var oldReviewType = reviewType;
        reviewText = $('.user-review').find(".review-textarea").val().trim();
        reviewType = $('.user-review').find(".review-type").val().trim();
        reviewTitle = $('.user-review').find("#edit-review-input").val().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-review',
                movieId: movieId,
                reviewTitle: reviewTitle,
                reviewText: reviewText,
                reviewType: reviewType
            },
            success: function (isUpdated) {
                if (isUpdated) {
                    $("#accept-edit-rev-btn").hide();
                    $("#edit-review-btn").show();
                    $('.user-review').find(".review-content-form").find(".review-text").text(reviewText);
                    $('.user-review').find(".review-title").find('b').text(reviewTitle);
                    $('.user-review').find(".review-content-form").show();
                    $('.user-review').find(".review-title").find('b').show();
                    if(oldReviewType === 'negative'){
                        $('.user-review').removeClass('review-negative')
                    } else if(oldReviewType === 'positive') {
                        $('.user-review').removeClass('review-positive')
                    } if(oldReviewType === 'neutral') {
                        $('.user-review').removeClass('review-neutral')
                    }
                    if(reviewType === 'positive'){
                        $('.user-review').addClass('review-positive')
                    } else if (reviewType === 'negative'){
                        $('.user-review').addClass('review-negative')
                    } else {
                        $('.user-review').addClass('review-neutral')
                    }
                    $('.user-review').find(".edit-review-input-wrapper, .edit-review-form").hide();
                } else if (!isUpdated) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });

    $('#save-review').prop('disabled', true);

    $('#save-review').on('click', function (e) {
        reviewTitle = $('#review-title').val().trim();
        reviewText = $('#new-review').val().trim();
        reviewType = $('#review-type').val().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'review-movie',
                movieId: movieId,
                reviewTitle: reviewTitle,
                reviewText: reviewText,
                reviewType: reviewType
            },
            success: function (isAdded) {
                if (isAdded) {
                    $('#open-review-box').hide();
                    window.location.reload(true);
                } else if (!isAdded) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });

    (function (e) {
        var t, o = {
            className: "autosizejs",
            append: "",
            callback: !1,
            resizeDelay: 10
        }, i = '<textarea tabindex="-1" style="position:absolute; top:-999px; left:0; right:auto; bottom:auto; border:0; padding: 0; -moz-box-sizing:content-box; -webkit-box-sizing:content-box; box-sizing:content-box; word-wrap:break-word; height:0 !important; min-height:0 !important; overflow:hidden; transition:none; -webkit-transition:none; -moz-transition:none;"/>', n = ["fontFamily", "fontSize", "fontWeight", "fontStyle", "letterSpacing", "textTransform", "wordSpacing", "textIndent"], s = e(i).data("autosize", !0)[0];
        s.style.lineHeight = "99px", "99px" === e(s).css("lineHeight") && n.push("lineHeight"), s.style.lineHeight = "", e.fn.autosize = function (i) {
            return this.length ? (i = e.extend({}, o, i || {}), s.parentNode !== document.body && e(document.body).append(s), this.each(function () {
                function o() {
                    var t, o;
                    "getComputedStyle" in window ? (t = window.getComputedStyle(u, null), o = u.getBoundingClientRect().width, e.each(["paddingLeft", "paddingRight", "borderLeftWidth", "borderRightWidth"], function (e, i) {
                        o -= parseInt(t[i], 10)
                    }), s.style.width = o + "px") : s.style.width = Math.max(p.width(), 0) + "px"
                }

                function a() {
                    var a = {};
                    if (t = u, s.className = i.className, d = parseInt(p.css("maxHeight"), 10), e.each(n, function (e, t) {
                            a[t] = p.css(t)
                        }), e(s).css(a), o(), window.chrome) {
                        var r = u.style.width;
                        u.style.width = "0px", u.offsetWidth, u.style.width = r
                    }
                }

                function r() {
                    var e, n;
                    t !== u ? a() : o(), s.value = u.value + i.append, s.style.overflowY = u.style.overflowY, n = parseInt(u.style.height, 10), s.scrollTop = 0, s.scrollTop = 9e4, e = s.scrollTop, d && e > d ? (u.style.overflowY = "scroll", e = d) : (u.style.overflowY = "hidden", c > e && (e = c)), e += w, n !== e && (u.style.height = e + "px", f && i.callback.call(u, u))
                }

                function l() {
                    clearTimeout(h), h = setTimeout(function () {
                        var e = p.width();
                        e !== g && (g = e, r())
                    }, parseInt(i.resizeDelay, 10))
                }

                var d, c, h, u = this, p = e(u), w = 0, f = e.isFunction(i.callback), z = {
                    height: u.style.height,
                    overflow: u.style.overflow,
                    overflowY: u.style.overflowY,
                    wordWrap: u.style.wordWrap,
                    resize: u.style.resize
                }, g = p.width();
                p.data("autosize") || (p.data("autosize", !0), ("border-box" === p.css("box-sizing") || "border-box" === p.css("-moz-box-sizing") || "border-box" === p.css("-webkit-box-sizing")) && (w = p.outerHeight() - p.height()), c = Math.max(parseInt(p.css("minHeight"), 10) - w || 0, p.height()), p.css({
                    overflow: "hidden",
                    overflowY: "hidden",
                    wordWrap: "break-word",
                    resize: "none" === p.css("resize") || "vertical" === p.css("resize") ? "none" : "horizontal"
                }), "onpropertychange" in u ? "oninput" in u ? p.on("input.autosize keyup.autosize", r) : p.on("propertychange.autosize", function () {
                    "value" === event.propertyName && r()
                }) : p.on("input.autosize", r), i.resizeDelay !== !1 && e(window).on("resize.autosize", l), p.on("autosize.resize", r), p.on("autosize.resizeIncludeStyle", function () {
                    t = null, r()
                }), p.on("autosize.destroy", function () {
                    t = null, clearTimeout(h), e(window).off("resize", l), p.off("autosize").off(".autosize").css(z).removeData("autosize")
                }), r())
            })) : this
        }
    })(window.jQuery || window.$);

    $(function () {

        $('#new-review').autosize({append: "\n"});

        var reviewBox = $('#post-review-box');
        var newReview = $('#new-review');
        var openReviewBtn = $('#open-review-box');
        var closeReviewBtn = $('#close-review-box');

        openReviewBtn.click(function (e) {
            reviewBox.slideDown(400, function () {
                $('#new-review').trigger('autosize.resize');
                newReview.focus();
            });
            openReviewBtn.fadeOut(100);
            closeReviewBtn.show();
        });

        closeReviewBtn.click(function (e) {
            e.preventDefault();
            reviewBox.slideUp(300, function () {
                newReview.focus();
                openReviewBtn.fadeIn(200);
            });
            closeReviewBtn.hide();

        });
    });

    (function () {
        $('.review-form input, .review-form textarea').keyup(function () {

            var empty = false;
            $('.review-form input,.review-form textarea').each(function () {
                if ($(this).val() == '') {
                    empty = true;
                }
            });

            if (empty) {
                $('#save-review').prop('disabled', true);
            } else {
                $('#save-review').prop('disabled', false);
            }
        });
    })();
});


