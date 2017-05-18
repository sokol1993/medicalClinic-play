/**
 * Created by sokol on 2017-03-15.
 */


$(document).ready(function(){

    function generate_handler(j) {
        return function (event) {
            $('#addr' + j).addClass('active');
            $('#inputName' + j).prop('disabled', false);
            $('#edit' + j).prop('disabled', false);
            $('#edit' + j)[0].addEventListener("click", function () {
                var input = $('#inputName' + j).val();
                var id = $('#idSpec' + j).html();
                $.ajax({
                    type: "POST",
                    url: "/admin/specializations/edit",
                    data: '{"name":"' + input + '","id":"' + id + '"}',
                    contentType: "application/json",
                    success: function () {
                        window.location.href = "/admin/specializations"
                    }
                });
            });
        };
    }

    var d = document.getElementById("specialization_table").rows.length;
    for (var z = 1; z < d; z++) {
        $('#addr' + z).click(generate_handler(z));
    }

    $("#add_row").click(function(){
        var i = document.getElementById("specialization_table").rows.length;
        $('#specialization_table')
            .append("<tr id='addr" + i + "'><td>" + i + "</td><td><input class='form-control' id = 'name" + i + "' name='name" + i + "' type='text' placeholder='Specializacja' /></td><td><button class='btn btn-primary' type='submit' id='addSpec" + i + "'>Zapisz</button></td></tr>");
        $('#addSpec'+i)[0].addEventListener("click", function()
        {
            var input = $('#name'+i).val();
            $.ajax({
                type: "POST",
                url: "/admin/specializations/add",
                data: '{"name":"' + input + '"}',
                contentType: "application/json",
                success: function () {
                    window.location.href = "/admin/specializations"
                }
            });
        });
    });

});

