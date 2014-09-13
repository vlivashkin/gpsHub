function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

$(document).ready(function () {
    $driversTable = $('#driversTable');
    $driversTable.dataTable({
        "processing": true,
        "serverSide": true,
        "stateSave": true,
        "ajax": "../actions/group_manager.php"
    });
    $driversTable.on('draw.dt', function() {
        $driversTable.find('tbody tr').click(function () {
            var id = $(this).children('td:first-child').text();
            buildModal(id);
        });
    });
    $('#add_group').click(function() {
        createGroup();
    })
});

function buildModal(id) {
    $("#modifyModalLabel").text("Пожалуйста, подождите...");
    $("#modify-name").val("");
    $("#modal-delete").unbind('click');
    $("#modal-save").unbind('click');

    $.ajax({
        url: '../actions/groups.php',
        type: 'GET',
        data: {
            type: 'id',
            id : id
        },
        dataType: 'json',
        success: function (data) {
            $("#modifyModalLabel").text("Информация о водителе #" + data.group_id);
            $("#modify-name").val(data.name);
            $("#modal-delete").click(function() {
                deleteGroup(data.group_id);
                return false;
            });
            $("#modal-save").click(function() {
                modifyGroup(data.group_id);
                return false;
            });
        }
    });

    $("#modifyModal").modal("show");
}

function createGroup() {
    $.ajax({
        url: '../actions/group_manager.php',
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

function modifyGroup(id) {
    $.ajax({
        url: '../actions/group_manager.php',
        type: 'POST',
        data: {
            action : "modify",
            group_id: id,
            name: $("#modify-name").val()
        },
        dataType: 'json',
        complete: function (data) {
            location.reload();
            $("#modifyModal").modal("hide");
        }
    });
}

function deleteGroup(id) {
    if (window.confirm("Вы уверены, что хотите удалить группу водителей #" + id + "?")) {
        $.ajax({
            url: '../actions/group_manager.php',
            type: 'POST',
            data: {
                action : "delete",
                group_id: id
            },
            dataType: 'json',
            complete: function (data) {
                location.reload();
                $("#modifyModal").modal("hide");
            }
        });
    }
}