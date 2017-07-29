/**
 * Created by serge on 03.07.2017.
 */
$(document).ready(function () {
    $('#movies-table').dataTable({
        "order": [],
        "columnDefs": [{
            "targets": 'no-sort',
            "orderable": false
        }],
        "pageLength": 50
    });
    $('#users-table').dataTable({
        "order": [],
        "columnDefs": [{
            "targets": 'no-sort',
            "orderable": false
        }],
        "pageLength": 50
    });
});