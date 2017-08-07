/**
 * Created by serge on 18.07.2017.
 */
$(document).ready(function () {
    var $url = 'Controller';
    var lang = $('html').attr("lang");
    var genre;
    var languages = [];
    var genreId;
    var addedMsg;
    var errorMsg;
    var editedMsg;
    if (lang === "ru_RU") {
        errorMsg = "Ошибка в процессе выполнения операции";
        addedMsg = "Добавлено";
        editedMsg = "Отредактировано";
        languages = [{id: 'ru_RU', text: 'ru_RU'}, {id: 'en_EN', text: 'en_EN'}]
    }
    if (lang === "en_EN") {
        errorMsg = "Error during procedure";
        addedMsg = "Added";
        editedMsg = "Edited";
        languages = [{id: 'en_EN', text: 'en_EN'}, {id: 'ru_RU', text: 'ru_RU'}]
    }
    $("#language").select2({
        minimumResultsForSearch: Infinity,
        data: languages
    });
    var contentLanguage = $('#language').select2('data')[0].id;
    var isExistGenreId = $('#genre-id').text().trim();
    if (isExistGenreId) { //if is not empty string or whitespaces
        genreId = $('#genre-id').text().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'get-localized-genre-info-by-code',
                genreId: genreId
            },
            success: function (data) {
                genre = data;
                console.log(genre);
                $("#accept-fields-nf, #accept-fields-lf").hide();
                $("#edit-fields-nf, #edit-fields-lf").show();
                $("#name").val(genre.name);
                $("#description").val(genre.description);
            },
            error: function () {
                alert(errorMsg);
            }
        });

    }

    if (!isExistGenreId) {
        $("#accept-fields-lf, #clear-fields-lf, #save-genre-btn, #decline-add-genre-btn").prop("disabled", true);

        $(".frame-step-3").css({
            "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)",
            "padding": "5px"
        });
    } else {
        $("#clear-fields-lf").prop("disabled", true);
    }

    /* $("#accept-fields-lf").on("click", function () {
     var name = $("#name").val().trim();
     var description = $("#description").val().trim();
     $.ajax({
     type: 'POST',
     url: $url,
     data: {
     command: 'create-genre',
     name: name,
     description: description
     },
     success: function (data) {
     genreId = data;
     /!* $(".frame-nf-step1").css({
     "box-shadow": "",
     "padding": ""
     });*!/
     $("#name, #description").prop("disabled", true);
     $("#language").prop("disabled", false);
     $("#accept-fields-lf").hide();
     $("#edit-fields-lf").show();
     $('.message-lf').html('<div class="alert alert-info fade in new-size-alert">' +
     '<button type="button" class="close close-info" data-dismiss="alert"' +
     ' aria-hidden="true">×</button>' + addedMsg +
     '</div>');
     hideMsg();
     },
     error: function () {
     $('.message-lf').html('<div class="alert alert-danger fade in new-size-alert">' +
     '<button type="button" class="close close-danger" data-dismiss="alert"' +
     ' aria-hidden="true">×</button>' + errorMsg +
     '</div>');
     hideMsg()
     }
     });
     });

     $("#edit-fields-nf").on("click", function () {
     $("#birthdate, #clear-fields-nf").prop("disabled", false);
     $("#country, #role, #language, #surname, #name, #accept-fields-lf, #clear-fields-lf," +
     "#accept-fields-nf," +
     "#save-movie-btn, #decline-add-movie-btn").prop("disabled", true);
     $(".frame-nf-step1").css({
     "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)",
     "padding": "5px"
     });
     $("#edit-fields-nf").hide();
     $("#accept-editing-fields-nf").show();
     });

     $("#accept-editing-fields-nf").on("click", function () {
     var birthdate = $("#birthdate").val();
     $.ajax({
     type: 'POST',
     url: $url,
     data: {
     command: 'update-participant',
     participantId: genreId,
     birthdate: birthdate
     },
     success: function (data) {
     $("#birthdate, #clear-fields-nf").prop("disabled", true);
     $("#country, #role, #language, #name, #surname").prop("disabled", false);
     $("#accept-editing-fields-nf").hide();
     $("#edit-fields-nf").show();
     $(".frame-nf-step1").css({
     "box-shadow": "",
     "padding": ""
     });
     if (isExistGenreId) {
     $("#save-genre-btn, #decline-add-genre-btn").prop("disabled", false);
     $("#name, #surname").prop("disabled", true);
     }
     $('.message-nf').html('<div class="alert alert-info fade in new-size-alert">' +
     '<button type="button" class="close close-info" data-dismiss="alert"' +
     ' aria-hidden="true">×</button>' + editedMsg +
     '</div>');
     hideMsg();
     if (data === "false") {
     $('.message-nf').html('<div class="alert alert-danger fade in new-size-alert">' +
     '<button type="button" class="close close-danger" data-dismiss="alert"' +
     ' aria-hidden="true">×</button>' + errorMsg +
     '</div>');
     hideMsg()
     }
     },
     error: function () {
     $('.message-nf').html('<div class="alert alert-danger fade in new-size-alert">' +
     '<button type="button" class="close close-danger" data-dismiss="alert"' +
     ' aria-hidden="true">×</button>' + errorMsg +
     '</div>');
     hideMsg()
     }
     });
     });

     $("#clear-fields-nf").on("click", function (e) {
     $("#birthdate").val('');
     });*/

//check for language-dependent info
    $('#language').on("select2:select", function (e) {
        var languageCode = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                genreId: genreId,
                contentLanguage: languageCode,
                command: 'check-localized-genre-info-by-code'
            },
            success: function (isExist) {
                if (isExist) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: $url,
                        data: {
                            genreId: genreId,
                            contentLanguage: languageCode,
                            command: 'get-localized-genre-info-by-code'
                        },
                        success: function (data) {
                            $("#name").val(data.name);
                            $("#description").val(data.description);
                            $("#name, #description, #clear-fields-lf").prop("disabled", true);
                            $("#accept-fields-lf").hide();
                            $("#edit-fields-lf").show();
                        },
                        error: function () {
                            alert("Error while loading data");
                        }
                    });
                } else if (!isExist) {
                    $("#name, #description").val('');
                    $("#name, #description, #clear-fields-lf").prop("disabled", false);
                    $("#accept-fields-lf").prop("disabled", true);
                    $("#edit-fields-lf").hide();
                    $("#accept-fields-lf").show();
                }
            }
        });
    });
//

    $("#accept-fields-lf").on("click", function () {
        var name = $("#name").val().trim();
        var description = $("#description").val().trim();
        if (!(genreId)) {
            $.ajax({
                type: 'POST',
                url: $url,
                data: {
                    command: 'create-genre',
                    name: name,
                    description: description,
                    contentLanguage: contentLanguage
                },
                success: function (data) {
                    genreId = data;
                    $("#accept-fields-lf").hide();
                    $("#edit-fields-lf").show();
                    $('.message-lf').html('<div class="alert alert-info fade in new-size-alert">' +
                        '<button type="button" class="close close-info" data-dismiss="alert"' +
                        ' aria-hidden="true">×</button>' + addedMsg +
                        '</div>');
                    hideMsg();
                    $("#name, #description, #clear-fields-lf").prop("disabled", true);
                    /*  $("#country, #genre, #actors, #writers, #directors,#accept-fields-lf," +
                     " #accept-fields-participant," +
                     " #clear-fields-participant, #accept-fields-nf" +
                     "#save-movie-btn, #decline-add-movie-btn").prop("disabled", false);*/
                    $("#save-genre-btn, #decline-add-genre-btn").prop("disabled", false);
                },
                error: function () {
                    $('.message-lf').html('<div class="alert alert-danger fade in new-size-alert">' +
                        '<button type="button" class="close close-danger" data-dismiss="alert"' +
                        ' aria-hidden="true">×</button>' + errorMsg +
                        '</div>');
                    hideMsg()
                }
            });
        } else if(genreId) {
            $.ajax({
                type: 'POST',
                url: $url,
                data: {
                    command: 'add-localized-genre-info',
                    name: name,
                    description: description,
                    genreId: genreId,
                    contentLanguage: contentLanguage
                },
                success: function (data) {
                    $("#accept-fields-lf").hide();
                    $("#edit-fields-lf").show();
                    $('.message-lf').html('<div class="alert alert-info fade in new-size-alert">' +
                        '<button type="button" class="close close-info" data-dismiss="alert"' +
                        ' aria-hidden="true">×</button>' + addedMsg +
                        '</div>');
                    hideMsg();
                    $("#name, #description, #clear-fields-lf").prop("disabled", true);
                    /*  $("#country, #genre, #actors, #writers, #directors,#accept-fields-lf," +
                     " #accept-fields-participant," +
                     " #clear-fields-participant, #accept-fields-nf" +
                     "#save-movie-btn, #decline-add-movie-btn").prop("disabled", false);*/
                    $("#save-genre-btn, #decline-add-genre-btn").prop("disabled", false);
                },
                error: function () {
                    $('.message-lf').html('<div class="alert alert-danger fade in new-size-alert">' +
                        '<button type="button" class="close close-danger" data-dismiss="alert"' +
                        ' aria-hidden="true">×</button>' + errorMsg +
                        '</div>');
                    hideMsg()
                }
            });
        }
    });

    $("#edit-fields-lf").on("click", function () {
        $("#name, #description, #clear-fields-lf").prop("disabled", false);
        $("#accept-fields-lf, #save-genre-btn, #decline-add-genre-btn, #language").prop("disabled", true);
        $(".frame-step-3").css({
            "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)"
        });
        $("#edit-fields-lf").hide();
        $("#accept-editing-fields-lf").show();
    });

    $("#accept-editing-fields-lf").on("click", function () { //update lang dependent info
        var name = $("#name").val().trim();
        var description = $("#description").val().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-localized-genre-info',
                name: name,
                description: description,
                genreId: genreId,
                contentLanguage: contentLanguage
            },
            success: function (data) {
                $("#name, #description, #clear-fields-lf").prop("disabled", true);
                $("#language, #decline-add-genre-btn, #save-genre-btn").prop("disabled", false);
                $("#accept-editing-fields-lf").hide();
                $("#edit-fields-lf").show();
                $(".frame-step-3").css({
                    "box-shadow": "",
                    "padding": ""
                });
                $('.message-lf').html('<div class="alert alert-info fade in new-size-alert">' +
                    '<button type="button" class="close close-info" data-dismiss="alert"' +
                    ' aria-hidden="true">×</button>' + editedMsg +
                    '</div>');
                hideMsg();
                if (data === "false") {
                    $('.message-lf').html('<div class="alert alert-danger fade in new-size-alert">' +
                        '<button type="button" class="close close-danger" data-dismiss="alert"' +
                        ' aria-hidden="true">×</button>' + errorMsg +
                        '</div>');
                    hideMsg()
                }
            },
            error: function () {
                $('.message-lf').html('<div class="alert alert-danger fade in new-size-alert">' +
                    '<button type="button" class="close close-danger" data-dismiss="alert"' +
                    ' aria-hidden="true">×</button>' + errorMsg +
                    '</div>');
                hideMsg()
            }
        });
    });

    $("#clear-fields-lf").on("click", function (e) {
        $("#name, #description").val('');
    });


    $('#language').on("select2:select", function (e) {
        contentLanguage = e.params.data.id;
    });

    $("#save-genre-btn").on("click", function () {
        $("#save-genre-modal").modal({})
    });

    $("#save-genre-modal").find("#go-to-the-genre-page").on("click", function () {
        window.location.href = 'Controller?command=show-genre-page&genreId=' + genreId;
    });

    $("#decline-add-genre-btn").on("click", function () {
        $("#delete-genre-modal").modal({})
    });

    $("#delete-genre-modal").find("#delete-genre-btn").on("click", function () {
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'delete-genre',
                genreId: genreId
            },
            success: function (isDeleted) {
                if (isDeleted) {
                    window.location.href = 'Controller?command=redirect&redirectPage=addGenrePage';
                } else if (!isDeleted) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });


    (function () {
        $('.frame-step-3 input').keyup(function () {

            var empty = false;
            $('.frame-step-3 input').each(function () {
                if ($(this).val() == '') {
                    empty = true;
                }
            });

            if (empty) {
                $('#accept-fields-lf, #clear-fields-lf').prop('disabled', true);
            } else {
                $('#accept-fields-lf, #clear-fields-lf').prop('disabled', false);
            }
        });
    })();

    function hideMsg() {
        setTimeout(function () {
            $('.message-nf, .message-lf').empty();
        }, 3000);
    }

})
;
