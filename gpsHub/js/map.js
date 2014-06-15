var map;
var driver = {};

$(document).ready(function () {
    initMap();
    bindListEvents();
    resizeList();
    moment.lang('ru');

    setInterval(getDrivers, 1000);
});

function bindListEvents() {
    $list = $("#list");

    $list.find(".panel-heading").click(function () {
        var id = $(this).closest(".panel").attr('id').substring(7, 10);
        var features = map.getLayers().getArray()[1].getSource().getFeatures();
        var point;
        features.forEach(function (item) {
            if (item.id == id) {
                point = item.getGeometry().getExtent();
            }
        });

        if (point != undefined) {
            var view = map.getView();
            var pan = ol.animation.pan({
                duration: 400,
                source: (view.getCenter())
            });
            map.beforeRender(pan);
            view.setCenter([point[0], point[1]]);
        }
    });

    $list.find(".ext-btn").click(function () {
        $collapse = $(this).closest(".panel").find(".panel-collapse");
        $collapse.collapse('toggle');

        return false;
    });

    $panel_collapse = $(".panel-collapse");

    $panel_collapse.on('show.bs.collapse', function () {
        $span = $(this).closest(".panel").find(".ext-btn").find("span");
        $span.removeClass();
        $span.addClass("glyphicon glyphicon-chevron-up");
    });

    $panel_collapse.on('hide.bs.collapse', function () {
        $span = $(this).closest(".panel").find(".ext-btn").find("span");
        $span.removeClass();
        $span.addClass("glyphicon glyphicon-chevron-down");
    });
}

function initMap() {
    map = new ol.Map({
        target: 'map-canvas',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
            }),
            new ol.layer.Vector({
                source: new ol.source.Vector()
            })
        ],
        projection: 'EPSG:3857',
        view: new ol.View2D({
            center: ol.proj.transform([37.61778, 55.75167], 'EPSG:4326', 'EPSG:3857'),
            zoom: 11
        })
    });

    map.once("postrender", function () {
        initLayout();
        map.updateSize();
    })
}

function initLayout() {
    var layout = $('#container').layout({
        closeable: false,
        slidable: false,
        center__paneSelector: "#map-layout",
        west__paneSelector: "#list-layout",
        center__minWidth: 300,
        west__size: 250,
        west__minSize: 150,
        spacing_open: 1,
        spacing_closed: 20,
        livePaneResizing: true,
        stateManagement__enabled: true,
        onresize: function () {
            map.updateSize();
            resizeList();
        }
    });

    $("#list-layout-resizer").append(
        "<div id='resizer-btn'>" +
            "<span class='glyphicon glyphicon-chevron-right'></span>" +
        "</div>"
    );

    layout.bindButton('#list-toggler', 'close', 'west');
    layout.bindButton('#resizer-btn', 'open', 'west');
    $("#list-layout").show();
}

function resizeList() {
    if ($("#list").width() < 360)
        $(".panel-collapse .col-xs-6").width("70%");
    else
        $(".panel-collapse .col-xs-6").width("20%");
}

function getDrivers() {
    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            data.list.forEach(function (row) {
                if (driver[row.id] == undefined)
                    driver[row.id] = addPoint(row.id, row.lat, row.lng);
                else
                    movePoint(row.id, row.lat, row.lng);

                updateDriverStatus(row.id, row.time, data.time);
            });
        }
    });
}

function updateDriverStatus(id, last_activity, now) {
    $circle = $("#driver-" + id + "-panel .circle");

    var diff = now - last_activity;
    if (diff < 60)
        $circle.addClass("online");
    else if (diff < 5 * 60) {
        $circle.removeClass("online").addClass("wait");
    } else {
        $circle.removeClass("online").removeClass("wait");
    }

    var a = moment(last_activity, "X");
    var b = moment(now, "X");
    $("#driver-" + id + " .last-activity").text(a.from(b));
}

function addPoint(id, lat, lon) {
    var color = randomColor();
    $("#driver-" + id + "-panel .driver-color").css('background-color', color);

    var point = new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857'));
    var iconFeature = new ol.Feature({
        geometry: point
    });
    iconFeature.id = id;

    var icon = new ol.style.Icon({
        anchor: [0.5, 46],
        anchorXUnits: 'fraction',
        anchorYUnits: 'pixels',
        opacity: 0.75,
        src: 'actions/taxiimage.php?color=' + encodeURIComponent(color),
        size: [36, 48]
    });

    var iconStyle = new ol.style.Style({
        image: icon
    });
    iconFeature.setStyle(iconStyle);

    map.getLayers().getArray()[1].getSource().addFeature(iconFeature);

    return iconFeature;
}

function movePoint(id, lat, lon) {
    if (typeof(lat) == "string")
        lat = parseFloat(lat);
    if (typeof(lon) == "string")
        lon = parseFloat(lon);

    var point = new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857'));
    driver[id].set('geometry', point);
}

function randomColor() {
    var r = (Math.floor(Math.random() * 16 / 3) * 3).toString(16);
    var g = (Math.floor(Math.random() * 16 / 3) * 3).toString(16);
    var b = (Math.floor(Math.random() * 16 / 3) * 3).toString(16);
    color = "#" + r.toString(16) + g.toString(16) + b.toString(16);

    return color;
}

function buildModal(id) {
    $("#modifyModalLabel").text("Пожалуйста, подождите...");
    $("#modify-name").val("");
    $("#modify-alias").val("");
    $("#modify-phone").val("");
    $("#modify-vehile-num").val("");
    $("#modify-vehile-description").val("");
    $("#modal-delete").unbind('click');


    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        data: {
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
        }
    });

    $("#modifyModal").modal("show");
}

function deleteDriver(id) {
    if (confirm("Вы уверены, что хотите удалить аккаунт водителя #" + id + "?")) {
        $.ajax({
            url: 'actions/drivermanager.php',
            type: 'POST',
            data: {
                action : "delete",
                driver_id: id
            },
            dataType: 'json',
            success: function (data) {
                console.log(data);
                location.reload();
            }
        });
    }
}