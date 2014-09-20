var mapManager;

function List() {
    this.drivers = {};
}

List.prototype.initList = function() {
    this.bindListBtns();
    this.updateList();
};

List.prototype.bindListBtns = function() {
    var $panel_collapse = $(".panel-collapse");

    $panel_collapse.on('show.bs.collapse', function () {
        var $span = $(this).closest(".panel").find(".ext-btn").find("span");
        $span.removeClass();
        $span.addClass("glyphicon glyphicon-chevron-up");
    });

    $panel_collapse.on('hide.bs.collapse', function () {
        var $span = $(this).closest(".panel").find(".ext-btn").find("span");
        $span.removeClass();
        $span.addClass("glyphicon glyphicon-chevron-down");
    });

    $("#settingsBtn").click(function() {
        window.open('management/','_blank');
    });
};

List.prototype.bindListItemsBtns = function () {
    $(".list-item").find(".ext-btn").click(function () {
        var $item = $(this).closest(".list-item");
        var id = $item.attr('driver_id');
        $item.find(".panel-collapse").collapse('toggle');

        return false;
    });
};

List.prototype.bindListMapping = function() {
    var _this = this;
    var $list_item = $(".list-item");
    $list_item.find(".panel-heading").click(function () {
        var id = $(this).closest(".list-item").attr('driver_id');
        var feature = _this.drivers[id].feature;
        var point = feature.getGeometry().getExtent();
        var view = mapManager.map.getView();
        var pan = ol.animation.pan({
            duration: 400,
            source: view.getCenter()
        });
        mapManager.map.beforeRender(pan);
        view.setCenter([point[0], point[1]]);
    });
};

List.prototype.resizeList = function() {
    if ($("#list").width() < 400) {
        $(".panel-collapse .col-xs-6").width("70%");
    } else {
        $(".panel-collapse .col-xs-6").width("20%");
    }
};

List.prototype.updateList = function() {
    var _this = this;

    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        data: {
            type: 'list'
        },
        dataType: 'json',
        success: function (data) {
            if (data && data.list) {
                _this.clearList();
                data.list.forEach(function (driver) {
                    _this.addDriverToList(driver);
                });
                _this.bindListItemsBtns();
                _this.resizeList();
                mapManager.initLocationUpdates();
            }
        }
    });
};

List.prototype.clearList = function() {
    $("#list").empty();
    this.drivers = {};
};

List.prototype.addDriverToList = function(driver) {
    this.drivers[driver.driver_id] = driver;

    var heading_text = '<div class="driver-header">' + (!isEmpty(driver.name) ? driver.name : 'ID: ' + driver.driver_id) + '</div>' +
        '<br>' + driver.vehicle_num;

    var properties = [{name: "ID водителя", value: driver.driver_id}];
    if (!isEmpty(driver.alias)) {
        properties.push({name: 'Позывной', value: driver.alias});
    }
    if (!isEmpty(driver.phone_number)) {
        properties.push({name: 'Номер телефона', value: driver.phone_number});
    }
    if (!isEmpty(driver.vehicle_description)) {
        properties.push({name: 'Описание машины', value: driver.vehicle_description});
    }
    if (!isEmpty(driver.busy)) {
        properties.push({name: 'Водитель занят', value: driver.busy == "true" || driver.busy == "busy" ? "Занят" : "Свободен", classname: "busy"});
    }
    properties.push({name: "Последняя активность", value: "Нет информации", classname: "last-activity"});

    var prop_text = "";
    properties.forEach(function (property) {
        prop_text +=
            '<div class="row">' +
                '<div class="col-xs-6">' + property.name + '</div>' +
                '<div class="col-xs-6 ' + (property.classname ? property.classname : '') + '">' + property.value + '</div>' +
            '</div>'
    });

    $("#list").append(
        '<div id="driver-' + driver.driver_id + '" driver_id="' + driver.driver_id + '"  class="list-item panel panel-default">' +
            '<div class="panel-heading">' +
                '<div class="status"></div>' +
                '<div class="driver-text">' + heading_text + '</div>' +
                '<div class="right ext-btn">' +
                    '<span class="glyphicon glyphicon-chevron-down"></span>' +
                '</div>' +
            '</div>' +
            '<div class="panel-collapse collapse" data-parent="#list">' +
                '<div class="panel-body">' + prop_text + '</div>' +
            '</div>' +
            '</div>'
    );
};

List.prototype.updateStatuses = function(now) {
    var _this = this;

    Object.keys(this.drivers).forEach(function (id) {
        var $item = $(".list-item[driver_id='" + id + "']");
        var $status = $item.find(".status");
        var $busy = $item.find(".busy");
        var $last_activity = $item.find(".last-activity");

        var diff = now - _this.drivers[id].last_activity;

        if (diff < 60) {
            $status.removeClass("wait").addClass("online");
        } else if (diff < 5 * 60) {
            $status.removeClass("online").addClass("wait");
        } else {
            $status.removeClass("online").removeClass("wait");
        }

        var busy = _this.drivers[id].busy == "true" || _this.drivers[id].busy == "busy" ? "Занят" : "Свободен";
        $busy.text(busy);

        var time = moment(_this.drivers[id].last_activity, "X").format("dd, DD MMM, hh:mm");
        $last_activity.text(time);
    });
};