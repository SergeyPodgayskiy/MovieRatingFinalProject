/**
 * Created by serge on 18.07.2017.
 */
$(document).ready(function () {
    var $url = 'Controller';
    var lang = $('html').attr("lang");
    var participant;
    var roles = [];
    var languages = [];
    var participantId;
    var addedMsg;
    var errorMsg;
    var editedMsg;
    var selectRole;
    var selectCountries;
    var savePhoto;
    if (lang === "ru_RU") {
        errorMsg = "Ошибка в процессе выполнения операции";
        addedMsg = "Добавлено";
        editedMsg = "Отредактировано";
        selectRole = "Выберите роль";
        selectCountries = "Выберите страну";
        languages = [{id: 'ru_RU', text: 'ru_RU'}, {id: 'en_EN', text: 'en_EN'}]
        savePhoto = "Сохранить"
    }
    if (lang === "en_EN") {
        errorMsg = "Error during procedure";
        addedMsg = "Added";
        editedMsg = "Edited";
        selectRole = "Select role";
        selectCountries = "Select countries";
        languages = [{id: 'en_EN', text: 'en_EN'}, {id: 'ru_RU', text: 'ru_RU'}]
        savePhoto = "Save"
    }
    var countries = [{id: '-1', text: selectCountries}];
    $("#language").select2({
        minimumResultsForSearch: Infinity,
        data: languages
    });
    var contentLanguage = $('#language').select2('data')[0].id;
    var isExistParticipantId = $('#participant-id').text().trim();
    if (isExistParticipantId) { //if is not empty string or whitespaces
        participantId = $('#participant-id').text().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'get-localized-participant-info',
                participantId: participantId,
                contentLanguage: contentLanguage
            },
            success: function (data) {
                participant = data;
                console.log(participant);
                $("#accept-fields-nf, #accept-fields-lf").hide();
                $("#edit-fields-nf, #edit-fields-lf").show();
                // console.log(participant.birthDate);
                $('.datepicker').datepicker({
                    format: 'dd/mm/yyyy',
                    startDate: '01/01/1900',
                    endDate: new Date()
                });
                var birthDate = new Date(participant.birthDate);
                $('.datepicker').datepicker('setDate', birthDate);
                $("#name").val(participant.name);
                $("#surname").val(participant.surname);
            },
            error: function () {
                alert(errorMsg);
            }
        });

    }

    if (!isExistParticipantId) {
        $("#country, #role, #language, #name, #surname, " +
            " #accept-fields-lf, #clear-fields-lf, #clear-fields-nf," +
            "#save-participant-btn, #decline-add-aprticipant-btn").prop("disabled", true);

        $(".frame-nf-step1").css({
            "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)",
            "padding": "5px"
        });
    } else {
        $("#birthdate, #clear-fields-nf, #name, #surname," +
            "  #clear-fields-lf," +
            " #accept-fields-nf, #clear-fields-nf").prop("disabled", true);
    }

    $("#accept-fields-nf").on("click", function () {
        var birthdate = $("#birthdate").val().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'create-participant',
                birthdate: birthdate
            },
            success: function (data) {
                participantId = data;
                $(".frame-nf-step1").css({
                    "box-shadow": "",
                    "padding": ""
                });
                $("#birthdate").prop("disabled", true);
                $("#country, #role, #language, #name, #surname").prop("disabled", false);
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
        $("#birthdate, #clear-fields-nf").prop("disabled", false);
        $("#country, #role, #language, #surname, #name, #accept-fields-lf, #clear-fields-lf," +
            "#accept-fields-nf," +
            "#save-participant-btn, #decline-add-participant-btn").prop("disabled", true);
        $(".frame-nf-step1").css({
            "box-shadow": "inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(74, 86, 224, 0.8)",
            "padding": "5px"
        });
        $("#edit-fields-nf").hide();
        $("#accept-editing-fields-nf").show();
    });

    $("#accept-editing-fields-nf").on("click", function () {
        var birthdate = $("#birthdate").val().trim();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-participant',
                participantId: participantId,
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
                if (isExistParticipantId) {
                    $("#save-participant-btn, #decline-add-participant-btn").prop("disabled", false);
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
    });

//check for language-dependent info
    $('#language').on("select2:select", function (e) {
        var languageCode = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                participantId: participantId,
                contentLanguage: languageCode,
                command: 'check-localized-participant-info'
            },
            success: function (isExist) {
                if (isExist) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: $url,
                        data: {
                            participantId: participantId,
                            contentLanguage: languageCode,
                            command: 'get-localized-participant-info'
                        },
                        success: function (data) {
                            console.log(data);
                            // var movie = JSON.parse(data);
                            $("#name").val(data.name);
                            $("#surname").val(data.surname);
                            $("#name, #surname, #clear-fields-lf").prop("disabled", true);
                            $("#accept-fields-lf").hide();
                            $("#edit-fields-lf").show();
                        },
                        error: function () {
                            alert("Error while loading data");
                        }
                    });
                } else if (!isExist) {
                    $("#name, #surname").val('');
                    $("#name, #surname, #clear-fields-lf").prop("disabled", false);
                    $("#accept-fields-lf").prop("disabled", true);
                    $("#edit-fields-lf").hide();
                    $("#accept-fields-lf").show();
                }
            }
        });
    });
//

    $("#accept-fields-lf").on("click", function () {
        var name = $("#name").val();
        var surname = $("#surname").val();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'add-localized-participant-info',
                name: name,
                surname: surname,
                participantId: participantId,
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
                $("#name, #surname, #clear-fields-lf").prop("disabled", true);
                /*  $("#country, #genre, #actors, #writers, #directors,#accept-fields-lf," +
                 " #accept-fields-participant," +
                 " #clear-fields-participant, #accept-fields-nf" +
                 "#save-movie-btn, #decline-add-movie-btn").prop("disabled", false);*/
                $("#save-participant-btn, #decline-add-participant-btn").prop("disabled", false);
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
        $("#name, #surname, #clear-fields-lf").prop("disabled", false);
        $("#country, #role, #accept-fields-lf," +
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
        var name = $("#name").val();
        var surname = $("#surname").val();
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-localized-participant-info',
                name: name,
                surname: surname,
                participantId: participantId,
                contentLanguage: contentLanguage
            },
            success: function (data) {
                $("#name, #surname, #clear-fields-lf").prop("disabled", true);
                $("#country, #role,  #language, #decline-add-participant-btn, #save-participant-btn").prop("disabled", false);
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
        $("#name, #surname").val('');
    });

    $('#country').on("select2:select", function (e) {
        // var codes = $('#country').val();
        var countryCode = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                command: 'add-country-for-participant',
                countryCode: countryCode,
                participantId: participantId
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
                command: 'delete-country-for-participant',
                countryCode: countryCode,
                participantId: participantId
            },
            success: function (data) {

            }
        });
    });

    $('#role').on("select2:select", function (e) {
        var roleId = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                command: 'add-role-for-participant',
                roleId: roleId,
                participantId: participantId
            },
            success: function (data) {
            }
        });
    });

    $('#role').on("select2:unselect", function (e) {
        var roleId = e.params.data.id;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                command: 'delete-role-for-participant',
                roleId: roleId,
                participantId: participantId
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
            command: 'get-all-movieroles'
        },
        success: function (data) {
            initRolesList(data)
        }
    });

    $("#save-participant-btn").on("click", function () {
        $("#save-participant-modal").modal({})
    });

    $("#save-participant-modal").find("#go-to-the-participant-page").on("click", function () {
        window.location.href = 'Controller?command=show-participant-page&participantId=' + participantId;
    });

    $("#decline-add-participant-btn").on("click", function () {
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
                    window.location.href = 'Controller?command=redirect&redirectPage=addParticipantPage';
                } else if (!isDeleted) {
                    alert(errorMsg);
                }
            },
            error: function () {
                alert(errorMsg);
            }
        });
    });

    $('#upload-poster').on('click', function () {
        $('#upload-poster').hide();
        $('#poster-form-wrapper').html(
            "<div class='row row__margin_0'>" + "<form method='post'" +
            "action='UploadServlet'" +
            "enctype='multipart/form-data'>" +
            "<div class='col-sm-7'><input id='image-path' name='data' type='file'></div> " +

            "</form>" +
            "<div id='for-click' class='col-sm-5'><button id='save-participant-photo-btn' class='btn btn-primary btn-sm pull-right'></button></div></div>");
        $('#save-participant-photo-btn').text(savePhoto);
    });


    $('#poster-form-wrapper').on('click', '#save-participant-photo-btn', function (e) {
        var fileInput = document.getElementById('image-path');
        var file = fileInput.files[0];
        var formData = new FormData();
        formData.append('image-path', file);
        $.ajax({
            type: 'post',
            data: formData,
            url: 'UploadServlet?command=upload-participant-photo&participantId=' + participantId,
            processData: false,
            contentType: false,
            success: function (data) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: $url,
                    data: {
                        participantId: participantId,
                        command: 'get-participant-photo'
                    },
                    success: function (posterPath) {
                        $('.participant-poster').attr({
                            'width': 430,
                            'height': 465,
                            'src': '/' + posterPath
                        });
                        $('#save-participant-photo-btn').hide();
                    }
                });
            },
            error: function (data) {
                alert(errorMsg);
            }
        });
    });

    if(isExistParticipantId){
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: $url,
            data: {
                participantId: participantId,
                command: 'get-participant-photo'
            },
            success: function (photoPath) {
                $('.participant-poster').attr({
                    'width': 430,
                    'height': 465,
                    'src': '/' + photoPath
                });
                $('#save-participant-photo-btn').hide();
            }
        });
    }

    $('#poster-form-wrapper').on('click', '#image-path', function () {
        if ($('#save-participant-photo-btn').css('display') === 'none') {
            $('#save-participant-photo-btn').show();
        }
    });

    function initCountriesList(data) {

        for (var i = 0; i < data.length; i++) {
            countries.push({id: data[i].code, text: data[i].name});
        }
        $("#country").select2({
            data: countries,
            placeholder: {
                id: '-1', // the value of the option
                text: selectCountries
            },
            allowClear: true
        });
        if (isExistParticipantId) {
            var selected = [];
            console.log(participant.country.code);
            selected.push(participant.country.code);
            $("#country").val(selected).trigger('change');
        }
    }

    function initRolesList(data) {

        for (var i = 0; i < data.length; i++) {
            roles.push({id: data[i].id, text: data[i].name});
        }
        $("#role").select2({
            placeholder: selectRole,
            data: roles
        });
        if (isExistParticipantId) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: $url,
                data: {
                    command: 'get-participant-roles',
                    participantId: participantId
                },
                success: function (data) {
                    var selected = [];
                    for (var i = 0; i < data.length; i++) {
                        selected.push(data[i].id);
                    }
                    $("#role").val(selected).trigger('change');
                }
            });
        }
    }


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

    if (!isExistParticipantId) {
        $('.datepicker').datepicker({
            format: 'mm/dd/yyyy'
        });
    }
})
;
