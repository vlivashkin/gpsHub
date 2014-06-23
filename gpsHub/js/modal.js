function buildModal(id) {
    $("#modifyModalLabel").text("Пожалуйста, подождите...");
    $("#modify-name").val("");
    $("#modify-alias").val("");
    $("#modify-phone").val("");
    $("#modify-vehile-num").val("");
    $("#modify-vehile-description").val("");
    $("#modal-delete").unbind('click');
    $("#modal-save").unbind('click');
    $("#modal-confirm-msg").hide();
    $("#modal-confirm-btn").unbind('click');

    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        data: {
            type: 'id',
            id : id
        },
        dataType: 'json',
        success: function (data) {
            $("#modifyModalLabel").text("Информация о водителе #" + data.driver_id);
            $("#modify-name").val(data.name);
            $("#modify-alias").val(data.alias);
            $("#modify-phone").val(data.phone_number);
            $("#modify-vehile-num").val(data.vehile_num);
            $("#modify-vehile-description").val(data.vehile_description);
            $("#modal-delete").click(function() {
                deleteDriver(data.driver_id);
                return false;
            });
            $("#modal-save").click(function() {
                modifyDriver(data.driver_id);
                return false;
            });
            if (data.confirmed == 0)
                $("#modal-confirm-msg").show();
            $("#modal-confirm-btn").click(function() {
                confirmDriver(data.driver_id);
                return false;
            });
        }
    });

    $("#modifyModal").modal("show");
}

function modifyDriver(id) {
    $.ajax({
        url: 'actions/manager.php',
        type: 'POST',
        data: {
            action : "modify",
            driver_id: id,
            name: $("#modify-name").val(),
            alias: $("#modify-alias").val(),
            phone_number: $("#modify-phone").val(),
            vehile_num: $("#modify-vehile-num").val(),
            vehile_description: $("#modify-vehile-description").val()
        },
        dataType: 'json',
        success: function (data) {
            getList();
        }
    });
}
function confirmDriver(id) {
    $.ajax({
        url: 'actions/manager.php',
        type: 'POST',
        data: {
            action : "confirm",
            driver_id: id
        },
        dataType: 'json',
        success: function (data) {
            getList();
            $("#modal-confirm-msg").hide();
        }
    });
}

function deleteDriver(id) {
    if (window.confirm("Вы уверены, что хотите удалить аккаунт водителя #" + id + "?")) {
        $.ajax({
            url: 'actions/manager.php',
            type: 'POST',
            data: {
                action : "delete",
                driver_id: id
            },
            dataType: 'json',
            success: function (data) {
                getList();
            }
        });
    }
}