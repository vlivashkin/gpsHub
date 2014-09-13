var groups;

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

$(document).ready(function () {
    getAllGroups();

    $driversTable = $('#driversTable');
    $driversTable.dataTable({
        "processing": true,
        "serverSide": true,
        "stateSave": true,
        "ajax": "../actions/driver_manager.php"
    });
    $driversTable.on('draw.dt', function() {
        $driversTable.find('tbody tr').click(function () {
            var id = $(this).children('td:first-child').text();
            buildModal(id);
        });
    });
    $('#add_driver').click(function() {
        createDriver();
    });

    var action = getParameterByName('action');
    var id = getParameterByName('id');
    switch (action) {
        case 'create':
            createDriver();
            break;
        case 'modify':
            if (id != null) {
                buildModal(id);
            }
            break;
    }
});

function buildModal(id) {
    $("#modifyModalLabel").text("Пожалуйста, подождите...");
    $("#modify-name").val("");
    $("#modify-alias").val("");
    $("#modify-phone").val("");
    $("#modify-vehicle-num").val("");
    $("#modify-vehicle-description").val("");
    $('#modify-groups').empty();
    groups.forEach(function(item) {
        $('#modify-groups').append('<div class="checkbox"><label><input id="modify-groups-' + item.group_id + '" group-id="' + item.group_id + '" class="modify-groups-checkbox" type="checkbox">' + item.name + '</label></div>');
    });
    $("#modal-delete").unbind('click');
    $("#modal-save").unbind('click');

    $.ajax({
        url: '../actions/drivers.php',
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
            $("#modify-vehicle-num").val(data.vehicle_num);
            $("#modify-vehicle-description").val(data.vehicle_description);
            $("#modal-delete").click(function() {
                deleteDriver(data.driver_id);
                return false;
            });
            $("#modal-save").click(function() {
                modifyDriver(data.driver_id);
                return false;
            });
        }
    });

    getDriverGroups(id);
    $("#modifyModal").modal("show");
}

function createDriver() {
    $.ajax({
        url: '../actions/driver_manager.php',
        type: 'POST',
        data: {
            action: 'create'
        },
        dataType: 'json',
        success: function (data) {
            if (data && isNumber(data)) {
                buildModal(data);
            }
        }
    });
}

function modifyDriver(id) {
    var groups = [];
    $(".modify-groups-checkbox:checked").each(function() {
        groups.push($(this).attr('group-id'));
    });

    console.log(groups);

    $.ajax({
        url: '../actions/driver_manager.php',
        type: 'POST',
        data: {
            action : "modify",
            driver_id: id,
            name: $("#modify-name").val(),
            alias: $("#modify-alias").val(),
            phone_number: $("#modify-phone").val(),
            vehicle_num: $("#modify-vehicle-num").val(),
            vehicle_description: $("#modify-vehicle-description").val(),
            groups: groups
        },
        dataType: 'json',
        complete: function (data) {
            //location.reload();
            $("#modifyModal").modal("hide");
        }
    });
}

function deleteDriver(driver_id) {
    if (window.confirm("Вы уверены, что хотите удалить аккаунт водителя #" + driver_id + "?")) {
        $.ajax({
            url: '../actions/driver_manager.php',
            type: 'POST',
            data: {
                action : "delete",
                driver_id: driver_id
            },
            dataType: 'json',
            complete: function (data) {
                location.reload();
                $("#modifyModal").modal("hide");
            }
        });
    }
}

function getAllGroups() {
    $.ajax({
        url: '../actions/groups.php',
        type: 'GET',
        data: {
            type : "all"
        },
        dataType: 'json',
        complete: function (data) {
            groups = data.responseJSON.list;
        }
    });
}

function getDriverGroups(driver_id) {
    $.ajax({
        url: '../actions/groups.php',
        type: 'GET',
        data: {
            type : "driver",
            driver_id: driver_id
        },
        dataType: 'json',
        complete: function (data) {
            console.log(data.responseJSON.list);
            data.responseJSON.list.forEach(function(item) {
                $('#modify-groups-' + item.group_id).prop('checked', true);
                console.log(item.name);
            });
        }
    });
}