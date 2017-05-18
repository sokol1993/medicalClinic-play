/**
 * Created by sokol on 2017-04-22.
 */

function checkname() {
    var name = document.getElementById("username").value;

    if (name) {
        $.ajax({
            type: "post",
            url: "/check/user/exists",
            data: '{"name":"' + name + '"}',
            contentType: "application/json",
            success: function () {
                $('#subButt').prop('disabled', false);
                $('#user').removeClass('has-error')
                $('#user').addClass('has-success')
                $('#name_statusOk').css('display', 'inline-block');
                $('#name_statusError').hide()
                return true;
            },
            error: function () {
                $('#subButt').prop('disabled', true);
                $('#user').removeClass('has-success')
                $('#user').addClass('has-error')
                $('#name_statusOk').hide()
                $('#name_statusError').css('display', 'inline-block');
                return false;
            }
        });
    }
    else {
        $('#name_status').html("");
        return false;
    }
}