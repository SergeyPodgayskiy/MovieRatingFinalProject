/**
 * Created by serge on 27.07.2017.
 */
$(document).ready(function () {
    $(".loader").fadeOut("slow");
})
    .ajaxStart(function () {
        $(".loader").show();
    })
    .ajaxStop(function () {
    $(".loader").fadeOut("slow");
});
