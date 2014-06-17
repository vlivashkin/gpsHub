var map;
var drivers = {};

$(document).ready(function () {
    initMap();
    bindListEvents();
    resizeList();
    moment.lang('ru');

    setInterval(getDriversLocation, 1000);
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

function getDriversLocation() {
    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        data: {
            type: 'location'
        },
        dataType: 'json',
        success: function (data) {
            data.list.forEach(function (driver) {
                if (driver.id == null || driver.lat == null|| driver.lng == null) {
                    $("#driver-" + driver.id + "-panel .panel-heading").addClass('striped unknown');
                    return;
                }

                if (drivers[driver.id] == undefined)
                    drivers[driver.id] = addPoint(driver.id, driver.lat, driver.lng);
                else
                    movePoint(driver.id, driver.lat, driver.lng);

                drivers[driver.id].last_activity = driver.last_activity;
            });
            updateOnlineStatuses(data.time);
        }
    });
}

function updateOnlineStatuses(now) {
    Object.keys(drivers).forEach(function (id) {
        var diff = now - drivers[id].last_activity;
        var status;
        if (diff < 60)
            status = 'online';
        else if (diff < 5 * 60) {
            status = 'wait';
        } else {
            status = 'offline';
        }
        if (drivers[id].status !== status) {
            $circle = $("#driver-" + id + "-panel .circle");
            switch (status) {
                case 'online':
                    $circle.removeClass("wait").addClass("online");
                    break;
                case 'wait':
                    $circle.removeClass("online").addClass("wait");
                    break;
                case 'offline':
                    $circle.removeClass("online").removeClass("wait");
                    break;
            }
        }
        var a = moment(drivers[id].last_activity, "X");
        var b = moment(now, "X");
        $("#driver-" + id + " .last-activity").text(a.from(b));
    });
}

function addPoint(id, lat, lon) {
    var color = randomColor();
    $("#driver-" + id + "-panel .driver-color").css('background-color', color);

    if (typeof(lat) == "string")
        lat = parseFloat(lat);
    if (typeof(lon) == "string")
        lon = parseFloat(lon);

    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857'))
    });
    iconFeature.id = id;

    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 0.75,
            src: 'img/marker.php?color=' + encodeURIComponent(color),
            size: [36, 48]
        })
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
    drivers[id].set('geometry', point);
}

function randomColor() {
    var r = (Math.floor(Math.random() * 16 / 3) * 3).toString(16);
    var g = (Math.floor(Math.random() * 16 / 3) * 3).toString(16);
    var b = (Math.floor(Math.random() * 16 / 3) * 3).toString(16);
    color = "#" + r.toString(16) + g.toString(16) + b.toString(16);

    return color;
}