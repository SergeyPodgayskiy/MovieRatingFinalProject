/**
 * Created by serge on 13.07.2017.
 */
$(document).ready(function () {
    var $url = 'Controller';
    var isAdmin = 'true';
    var isBanned = 'true';
    var adminId = $('#admin-id').text();
    var currentAdminStatus;
    var currentBanStatus;
    var userId;
    var thisButton;
    var adminCell;
    var banCell;
    var deleteButton;
    var selectedRow;

    $("[name='admin-switch']").bootstrapSwitch({
        size: 'small', onText: 'Да', offText: 'Нет', handleWidth: 43,
    });

    $("[name='ban-switch']").bootstrapSwitch({
        size: 'small', onText: 'Да', offText: 'Нет'
    });

    //each row initialization
    $('#users-table tr:not(#header-row)').each(function () {
        var currentRow = $(this);
        adminCell = $(this).find('#is-admin-column');
        banCell = $(this).find('#is-banned-column');
        deleteButton = $(this).find('#delete-button-column').find("[name='delete-user-btn']");
        var adminStatus = adminCell.find('#admin-status').text().trim();
        var banStatus = banCell.find('#ban-status').text().trim();
        userId = $(this).find("#number-column").find("#user-id").text().trim();

        if (adminStatus === isAdmin) {
            $(adminCell.find('#admin-switch')).bootstrapSwitch('state', true, false);
            $(banCell.find('#ban-switch')).bootstrapSwitch('readonly', true);
            currentRow.addClass("success")
        } else {
            $(adminCell.find('#admin-switch')).bootstrapSwitch('state', false, true)
        }

        if (banStatus === isBanned) {
            $(banCell.find('#ban-switch')).bootstrapSwitch('state', true, false);
            $(adminCell.find('#admin-switch')).bootstrapSwitch('readonly', true);
            // currentRow.addClass("warning")
        } else {
            $(banCell.find('#ban-switch')).bootstrapSwitch('state', false, true)
        }
        if (userId === adminId) {
            $(banCell.find('#ban-switch')).bootstrapSwitch('readonly', true);
            $(adminCell.find('#admin-switch')).bootstrapSwitch('readonly', true);
            currentRow.addClass("info");
            deleteButton.addClass("disabled");
        }
    });


    $("[name='admin-switch']").on('switchChange.bootstrapSwitch', function (e, data) {
        selectedRow = $(this).closest('tr');
        adminCell = $(selectedRow).find('#is-admin-column');
        banCell = $(selectedRow).find('#is-banned-column');
        currentAdminStatus = !data;
        userId = $(this).closest('tr').find("#number-column").find("#user-id").text().trim();
        thisButton = $(this);

        $(this).bootstrapSwitch('state', currentAdminStatus, true);
        $('#admin-modal').modal({});
    });

    $("#admin-modal").find(".modal-footer .btn-primary").click(function () {
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-admin-status',
                userId: userId,
                adminStatus: currentAdminStatus
            },
            success: function (data) {
                if (data !== currentAdminStatus) {
                    $(thisButton).bootstrapSwitch('toggleState', true, true);
                    if (currentAdminStatus) {
                        $(banCell.find('#ban-switch')).bootstrapSwitch('readonly', false);
                        selectedRow.removeClass("success")
                    } else {
                        $(banCell.find('#ban-switch')).bootstrapSwitch('readonly', true);
                        selectedRow.addClass("success")
                    }
                } else {
                    alert("Error");
                }
            }
        });
        $('#admin-modal').modal('hide')
    });


    $("[name='ban-switch']").on('switchChange.bootstrapSwitch', function (e, data) {
        selectedRow = $(this).closest('tr');
        adminCell = $(selectedRow).find('#is-admin-column');
        banCell = $(selectedRow).find('#is-banned-column');
        currentBanStatus = !data;
        userId = $(this).closest('tr').find("#number-column").find("#user-id").text().trim();
        thisButton = $(this);

        $(this).bootstrapSwitch('state', currentBanStatus, true);
        $('#ban-modal').modal({});
    });

    $("#ban-modal").find(".modal-footer .btn-primary").click(function () {
        $.ajax({
            type: 'POST',
            url: $url,
            data: {
                command: 'update-ban-status',
                userId: userId,
                banStatus: currentBanStatus
            },
            success: function (data) {
                if (data !== currentBanStatus) {
                    $(thisButton).bootstrapSwitch('toggleState', true, true);
                    if (currentBanStatus) {
                        $(adminCell.find('#admin-switch')).bootstrapSwitch('readonly', false);
                    } else {
                        $(adminCell.find('#admin-switch')).bootstrapSwitch('readonly', true);
                    }
                } else {
                    alert("Error");
                }
            }
        });
        $('#ban-modal').modal('hide')
    });

    $("[name='delete-user-btn']:not(.disabled)").click(function () {
        selectedRow = $(this).closest('tr');
        userId = $(this).closest('tr').find("#number-column").find("#user-id").text().trim();
        thisButton = $(this);
        $('#delete-modal').modal({});
        /*$.ajax({
            type: 'POST',
            url: $url,
            dataType: 'html',
            data: {
                command: 'delete-user',
                userId: userId
            },
            success: function (data) {
                selectedRow.remove();
            }
        });*/
    });

    $("#delete-modal").find(".modal-footer .btn-primary").click(function () {
        $.ajax({
            type: 'POST',
            url: $url,
            dataType: 'html',
            data: {
                command: 'delete-user',
                userId: userId
            },
            success: function (data) {
                selectedRow.remove();
            }
        });
        $('#delete-modal').modal('hide')
    });
});