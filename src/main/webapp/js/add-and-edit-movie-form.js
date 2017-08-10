/**
 * Created by serge on 18.07.2017.
 */
$(document).ready(function () {
    var $url = 'Controller';
    var lang = $('html').attr("lang");
    var movie;
    var idRoleActor;
    var idRoleDirector;
    var idRoleWriter;
    var RoleActorEn = 'actor';
    var RoleActorRu = 'актер';
    var RoleDirectorEn = 'director';
    var RoleDirectorRu = 'режиссер';
    var RoleWriterEn = 'writer';
    var RoleWriterRu = 'сценарист';
    var actors = [];
    var directors = [];
    var writers = [];
    var countries = [];
    var genres = [];
    var languages = [];
    var ageLimits = [{id: '0+', text: '0+'}, {id: '6+', text: '6+'}, {id: '12+', text: '12+'},
        {id: '16+', text: '16+'}, {id: '18+', text: '18+'}];

    $("#age-limit").select2({
        minimumResultsForSearch: Infinity,
        data: ageLimits
    });
    var movieId;
    var addedMsg;
    var errorMsg;
    var editedMsg;
    var selectGenre;
    var selectCountries;
    var selectActors;
    var selectWriters;
    var selectDirectors;
    var savePoster;
    if (lang === "ru_RU") {
        errorMsg = "Ошибка в процессе выполнения операции";
        addedMsg = "Добавлено";
        editedMsg = "Отредактировано";
        selectGenre = "Выберите жанр";
        selectCountries = "Выберите страну";
        selectActors = "Выберите актеров";
        selectWriters = "Выберите сценариста";
        selectDirectors = "Выберите продюсера";
        savePoster = "Сохранить";
        languages = [{id: 'ru_RU', text: 'ru_RU'}, {id: 'en_EN', text: 'en_EN'}]
    }
    if (lang === "en_EN") {
        errorMsg = "Error during procedure";
        addedMsg = "Added";
        editedMsg = "Edited";
        selectGenre = "Select genre";
        selectCountries = "Select countries";
        selectActors = "Select actors";
        selectWriters = "Select writers";
        selectDirectors = "Select directors";
        savePoster = "Save";
        languages = [{id: 'en_EN', text: 'en_EN'}, {id: 'ru_RU', text: 'ru_RU'}]
    }

    $("#language").select2({
        minimumResultsForSearch: Infinity,
        data: languages
    });
    var contentLanguage = $('#language').select2('data')[0].id;

    var isExistMovieId = $('#movie-id').text().trim();
    if (isExistMovieId) { //if is not empty string or whitespaces
        movieId = $('#movie-id').text().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'get-neutral-movie-information',
                movieId: movieId
            },
            success: function (data) {
                movie = data;
                console.log(movie);
                $("#accept-fields-nf, #accept-fields-lf").hide();
                $("#edit-fields-nf, #edit-fields-lf").show();
                $("#release-year").val(movie.releaseYear);
                $("#duration").val(movie.duration);
                $("#name-movie").val(movie.title);
                $("#slogan").val(movie.slogan);
                $("#description").val(movie.description);
                $("#age-limit").val(movie.ageLimit).trigger('change');
            },
            error: function () {
                alert(errorMsg);
            }
        });

    }

    function hideMsg() {
        setTimeout(function () {
            $('.message-nf, .message-lf').empty();
        }, 3000);
    }

    if (!isExistMovieId) {
        $("#country, #genre, #actors, #writers, #directors, #language, #name-movie," +
            " #slogan, #description," +
            " #accept-fields-lf, #clear-fields-lf," +
            " #accept-fields-participant," +
            " #clear-fields-participant, #accept-fields-nf, #clear-fields-nf," +
            "#save-movie-btn, #decline-add-movie-btn").prop("disabled", true);

        $(".frame-nf-step1").css({
            "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)",
            "padding": "5px"
        });
    } else {
        $("#release-year, #age-limit, #duration, #clear-fields-nf," +
            "#name-movie," +
            " #slogan, #description," +
            " #accept-fields-lf, #clear-fields-lf," +
            " #accept-fields-nf, #clear-fields-nf").prop("disabled", true);
    }

    $("#accept-fields-nf").on("click", function () {
        var releaseYear = $("#release-year").val();
        var ageLimit = $("#age-limit").val();
        var duration = $("#duration").val();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'create-movie',
                releaseYear: releaseYear,
                ageLimit: ageLimit,
                duration: duration
            },
            success: function (data) {
                movieId = data;
                $(".frame-nf-step1").css({
                    "box-shadow": "",
                    "padding": ""
                });
                $("#release-year, #age-limit, #duration").prop("disabled", true);
                $("#country, #genre, #actors, #writers, #directors, #language, #name-movie," +
                    " #slogan, #description").prop("disabled", false);
                $("#accept-fields-nf").hide();
                $("#edit-fields-nf").show();
                $('.message-nf').html('<div class="alert alert-info fade in new-size-alert">' +
                    '<button type="button" class="close close-info" data-dismiss="alert"' +
                    ' aria-hidden="true">×</button>' + addedMsg +
                    '</div>');
                hideMsg();
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

    $("#edit-fields-nf").on("click", function () {
        $("#release-year, #age-limit, #duration, #clear-fields-nf").prop("disabled", false);
        $("#country, #genre, #actors, #writers, #directors, #language, #name-movie," +
            " #slogan, #description," +
            " #accept-fields-lf, #clear-fields-lf," +
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
        var releaseYear = $("#release-year").val();
        var ageLimit = $("#age-limit").val();
        var duration = $("#duration").val();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-movie',
                movieId: movieId,
                releaseYear: releaseYear,
                ageLimit: ageLimit,
                duration: duration
            },
            success: function (data) {
                $("#release-year, #age-limit, #duration, #clear-fields-nf").prop("disabled", true);
                $("#country, #genre, #actors, #writers, #directors, #language, #name-movie," +
                    " #slogan, #description").prop("disabled", false);
                $("#accept-editing-fields-nf").hide();
                $("#edit-fields-nf").show();
                $(".frame-nf-step1").css({
                    "box-shadow": "",
                    "padding": ""
                });
                if (isExistMovieId) {
                    $("#save-movie-btn, #decline-add-movie-btn").prop("disabled", false);
                    $("#name-movie, #slogan, #description").prop("disabled", true);
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
        $("#release-year, #age-limit, #duration").val('');
    });

//check for language info
    $('#language').on("select2:select", function (e) {
        // var codes = $('#country').val();
        /*var participantId = e.params.data.id;
         var movieroleId = e.params.data.roleId;*/
        var languageCode = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                movieId: movieId,
                contentLanguage: languageCode,
                command: 'check-localized-movie-info'
            },
            success: function (isExist) {
                if (isExist) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: $url,
                        data: {
                            movieId: movieId,
                            contentLanguage: languageCode,
                            command: 'get-localized-movie-info'
                        },
                        success: function (data) {
                            console.log(data);
                            // var movie = JSON.parse(data);
                            $("#name-movie").val(data.title);
                            $("#slogan").val(data.slogan);
                            $("#description").val(data.description);
                            $("#name-movie, #slogan, #description, #clear-fields-lf").prop("disabled", true);
                            $("#accept-fields-lf").hide();
                            $("#edit-fields-lf").show();
                        },
                        error: function () {
                            alert("Error while loading data");
                        }
                    });
                } else if (!isExist) {
                    $("#name-movie, #slogan, #description").val('');
                    $("#name-movie, #slogan, #description, #clear-fields-lf").prop("disabled", false);
                    $("#accept-fields-lf").prop("disabled", true);
                    $("#edit-fields-lf").hide();
                    $("#accept-fields-lf").show();
                }
            }
        });
    });
//


    $("#accept-fields-lf").on("click", function () {
        var name = $("#name-movie").val();
        var slogan = $("#slogan").val();
        var description = $("#description").val();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'add-localized-movie-info',
                movieName: name,
                slogan: slogan,
                description: description,
                movieId: movieId,
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
                $("#name-movie, #slogan, #description, #clear-fields-lf").prop("disabled", true);
                /*  $("#country, #genre, #actors, #writers, #directors,#accept-fields-lf," +
                 " #accept-fields-participant," +
                 " #clear-fields-participant, #accept-fields-nf" +
                 "#save-movie-btn, #decline-add-movie-btn").prop("disabled", false);*/
                $("#save-movie-btn, #decline-add-movie-btn").prop("disabled", false);
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

    $("#edit-fields-lf").on("click", function () {
        $("#name-movie, #slogan, #description, #clear-fields-lf").prop("disabled", false);
        $("#country, #genre, #actors, #writers, #directors,#accept-fields-lf," +
            " #accept-fields-participant," +
            " #clear-fields-participant, #accept-fields-nf" +
            "#save-movie-btn, #decline-add-movie-btn, #language").prop("disabled", true);
        $(".frame-step-3").css({
            "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)"
        });
        $("#edit-fields-lf").hide();
        $("#accept-editing-fields-lf").show();
    });

    $("#accept-editing-fields-lf").on("click", function () { //update lang dependent info
        var name = $("#name-movie").val();
        var slogan = $("#slogan").val();
        var description = $("#description").val();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-localized-movie-info',
                movieName: name,
                slogan: slogan,
                description: description,
                movieId: movieId,
                contentLanguage: contentLanguage
            },
            success: function (data) {
                $("#name-movie, #slogan, #description, #clear-fields-lf").prop("disabled", true);
                $("#country, #genre, #actors, #writers, #directors, #language, #language, #decline-add-movie-btn, #save-movie-btn").prop("disabled", false);
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
        $("#name-movie, #slogan, #description").val('');
    });

    $('#actors, #directors, #writers').on("select2:select", function (e) {
        // var codes = $('#country').val();
        var participantId = e.params.data.id;
        var movieroleId = e.params.data.roleId;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                movieId: movieId,
                participantId: participantId,
                roleId: movieroleId,
                command: 'add-participant-for-movie'
            },
            success: function (data) {

            }
        });
    });

    $('#actors, #directors, #writers').on("select2:unselect", function (e) {
        var participantId = e.params.data.id;
        var movieroleId = e.params.data.roleId;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                movieId: movieId,
                participantId: participantId,
                roleId: movieroleId,
                command: 'delete-participant-for-movie'
            },
            success: function (data) {

            }
        });
    });

    $('#country').on("select2:select", function (e) {
        // var codes = $('#country').val();
        var countryCode = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                command: 'add-country-for-movie',
                countryCode: countryCode,
                movieId: movieId
            },
            success: function (data) {
            }
        });
    });

    $('#country').on("select2:unselect", function (e) {
        var countryCode = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                command: 'delete-country-for-movie',
                countryCode: countryCode,
                movieId: movieId
            },
            success: function (data) {

            }
        });
    });

    $('#genre').on("select2:select", function (e) {
        // var codes = $('#country').val();
        var genreId = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                command: 'add-genre-for-movie',
                genreId: genreId,
                movieId: movieId
            },
            success: function (data) {
            }
        });
    });

    $('#genre').on("select2:unselect", function (e) {
        var genreId = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                command: 'delete-genre-for-movie',
                genreId: genreId,
                movieId: movieId
            },
            success: function (data) {
            }
        });
    });

    $('#language').on("select2:select", function (e) {
        contentLanguage = e.params.data.id;
    });

    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: $url,
        data: {
            command: 'get-all-countries'
        },
        success: function (data) {
            initCountriesList(data)
        }
    });

    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: $url,
        data: {
            command: 'get-all-genres'
        },
        success: function (data) {
            initGenresList(data)
        }
    });

    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: $url,
        data: {
            command: 'get-all-movieroles'
        },
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var roleName = data[i].name;
                var idRole = data[i].id;

                if ((roleName === RoleActorEn) || (roleName === RoleActorRu)) {
                    idRoleActor = idRole;
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: $url,
                        data: {
                            command: 'get-all-participants-by-role-id',
                            roleId: idRoleActor
                        },
                        success: function (data) {

                            initActorsList(data);

                        }
                    });
                } else if ((roleName === RoleDirectorEn) || (roleName === RoleDirectorRu)) {
                    idRoleDirector = idRole;
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: $url,
                        data: {
                            command: 'get-all-participants-by-role-id',
                            roleId: idRoleDirector
                        },
                        success: function (data) {
                            initDirectorsList(data)
                        }
                    });
                } else if ((roleName === RoleWriterEn) || (roleName === RoleWriterRu)) {
                    idRoleWriter = idRole;
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: $url,
                        data: {
                            command: 'get-all-participants-by-role-id',
                            roleId: idRoleWriter
                        },
                        success: function (data) {
                            initWritersList(data)
                        }
                    });
                }
            }
        }
    });


    $("#save-movie-btn").on("click", function () {
        $("#save-movie-modal").modal({})
    });

    $("#save-movie-modal").find("#go-to-the-movie-page").on("click", function () {
        window.location.href = 'Controller?command=show-movie-page&movieId=' + movieId;
    });

    $("#decline-add-movie-btn").on("click", function () {
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
                    window.location.href = 'Controller?command=redirect&redirectPage=addMoviePage';
                } else if (!isDeleted) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });

    function initActorsList(data) {

        for (var i = 0; i < data.length; i++) {
            actors.push({id: data[i].id, text: data[i].name + " " + data[i].surname, roleId: idRoleActor});
        }
        $("#actors").select2({
            placeholder: selectActors,
            data: actors
        });
        if (isExistMovieId) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: $url,
                data: {
                    command: 'get-participants-in-movie',
                    roleId: idRoleActor,
                    movieId: movieId
                },
                success: function (data) {
                    var selected = [];
                    for (var i = 0; i < data.length; i++) {
                        selected.push(data[i].id);
                    }
                    $("#actors").val(selected).trigger('change');
                }
            });
        }
    }

    function initDirectorsList(data) {

        for (var i = 0; i < data.length; i++) {
            directors.push({id: data[i].id, text: data[i].name + " " + data[i].surname, roleId: idRoleDirector});
        }
        $("#directors").select2({
            placeholder: selectDirectors,
            data: directors
        });
        if (isExistMovieId) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: $url,
                data: {
                    command: 'get-participants-in-movie',
                    roleId: idRoleDirector,
                    movieId: movieId
                },
                success: function (data) {
                    var selected = [];
                    for (var i = 0; i < data.length; i++) {
                        selected.push(data[i].id);
                    }
                    $("#directors").val(selected).trigger('change');
                }
            });
        }
    }

    function initWritersList(data) {

        for (var i = 0; i < data.length; i++) {
            writers.push({id: data[i].id, text: data[i].name + " " + data[i].surname, roleId: idRoleWriter});
        }
        $("#writers").select2({
            placeholder: selectWriters,
            data: writers
        });

        if (isExistMovieId) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: $url,
                data: {
                    command: 'get-participants-in-movie',
                    roleId: idRoleWriter,
                    movieId: movieId
                },
                success: function (data) {
                    var selected = [];
                    for (var i = 0; i < data.length; i++) {
                        selected.push(data[i].id);
                    }
                    $("#writers").val(selected).trigger('change');
                }
            });
        }
    }

    function initCountriesList(data) {

        for (var i = 0; i < data.length; i++) {
            countries.push({id: data[i].code, text: data[i].name});
        }
        $("#country").select2({
            placeholder: selectCountries,
            data: countries
        });
        if (isExistMovieId) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: $url,
                data: {
                    command: 'get-countries-in-movie',
                    movieId: movieId
                },
                success: function (data) {
                    var selected = [];
                    for (var i = 0; i < data.length; i++) {
                        selected.push(data[i].code);
                    }
                    $("#country").val(selected).trigger('change');
                }
            });
        }
    }

    function initGenresList(data) {

        for (var i = 0; i < data.length; i++) {
            genres.push({id: data[i].id, text: data[i].name});
        }
        $("#genre").select2({
            placeholder: selectGenre,
            data: genres
        });
        if (isExistMovieId) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: $url,
                data: {
                    command: 'get-genres-in-movie',
                    movieId: movieId
                },
                success: function (data) {
                    var selected = [];
                    for (var i = 0; i < data.length; i++) {
                        selected.push(data[i].id);
                    }
                    $("#genre").val(selected).trigger('change');
                }
            });
        }
    }

    (function () {
        $('.frame-nf-step1 input').keyup(function () {
            var empty = false;
            $('.frame-nf-step1 input').each(function () {
                if ($(this).val() == '') {
                    empty = true;
                }
            });

            if (empty) {
                $('#accept-fields-nf, #clear-fields-nf').prop('disabled', true);
            } else {
                $('#accept-fields-nf, #clear-fields-nf').prop('disabled', false);
            }
        });
    })();

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

    $('#upload-poster').on('click', function () {
        $('#upload-poster').hide();
        $('#poster-form-wrapper').html(
            "<div class='row row__margin_0'>" + "<form method='post'" +
            "action='UploadServlet'" +
            "enctype='multipart/form-data'>" +
            "<div class='col-sm-7'><input id='image-path' name='data' type='file'></div> " +

            "</form>" +
            "<div id='for-click' class='col-sm-5'><button id='save-movie-poster-btn' class='btn btn-primary btn-sm pull-right'></button></div></div>");
        $('#save-movie-poster-btn').text(savePoster);
    });


    $('#poster-form-wrapper').on('click', '#save-movie-poster-btn', function (e) {
        var fileInput = document.getElementById('image-path');
        var file = fileInput.files[0];
        var formData = new FormData();
        formData.append('image-path', file);
        formData.append('movieId', movieId);
        $.ajax({
            type: 'post',
            data: formData,
            url: 'UploadServlet?command=upload-movie-poster&movieId=' + movieId,
            processData: false,
            contentType: false,
            success: function (data) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: $url,
                    data: {
                        movieId: movieId,
                        command: 'get-movie-poster'
                    },
                    success: function (posterPath) {
                        $('.movie-poster').attr({
                            'width': 430,
                            'height': 465,
                            'src': '/' + posterPath
                        });
                        $('#save-movie-poster-btn').hide();
                    }
                });
            },
            error: function (data) {
                alert(errorMsg);
            }
        });
    });

    if(isExistMovieId){
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                movieId: movieId,
                command: 'get-movie-poster'
            },
            success: function (posterPath) {
                $('.movie-poster').attr({
                    'width': 430,
                    'height': 465,
                    'src': '/' + posterPath
                });
                $('#save-movie-poster-btn').hide();
            }
        });
    }

    $('#poster-form-wrapper').on('click', '#image-path', function () {
        if ($('#save-movie-poster-btn').css('display') === 'none') {
            $('#save-movie-poster-btn').show();
        }
    });

    /* $('#poster-form-wrapper').find('form').ajaxForm({
     success: function(msg) {
     alert("File has been uploaded successfully");
     },
     error: function(msg) {
     alert(errorMsg);
     }
     });*/

    /*function checkLanguageMovieInfo() {
     var languageCode = e.params.data.id;
     $.ajax({
     type: 'POST',
     dataType: 'json',
     url: $url,
     data: {
     movieId: movieId,
     contentLanguage: languageCode,
     command: 'check-localized-info-by-code'
     },
     success: function (data) {
     return data;
     }
     });
     }*/
})
;