var layout;
var map;
var driver = {};
var type = false;

$(document).ready(function () {
    initMap();
    setInterval(getDrivers, 1000);

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
});

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

    map.on("postrender", function () {
        initLayout();
        map.updateSize();
    })
}

function initLayout() {
    layout = $('#container').layout({
        closeable: false,
        slidable: false,
        center__paneSelector: "#map-layout",
        west__paneSelector: "#list-layout",
        center__minWidth: 300,
        west__size: 250,
        spacing_open: 1,
        spacing_closed: 20,
        livePaneResizing: true,
        stateManagement__enabled: true,
        onresize: function () {
            map.updateSize();
        }
    });

    $("#list-layout").show();
}

function getDrivers() {
    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            data.list.forEach(function (row) {
                if (driver[row.id] == undefined) {
                    var color = randomColor();
                    $("#driver-" + row.id + "-panel .driver-color").css('background-color', color);
                    driver[row.id] = addPoint(row.lat, row.lng, row.id, color);
                } else
                    movePoint(row.id, row.lat, row.lng);

                $circle = $("#driver-" + row.id + "-panel .circle");

                if (data.time - row.time < 60*1000)
                    $circle.addClass("online");
                else if (data.time - row.time < 5*60*1000) {
                    $circle.removeClass("online").addClass("wait");
                } else {
                    $circle.removeClass("online").removeClass("wait");
                }

                moment.lang('ru');
                var a = moment(row.time, "X");
                var b = moment(data.time, "X");
                $("#driver-" + row.id + " th:last").text(a.from(b));
            });
        }
    });
}

function addPoint(lat, lon, id, color) {
    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857'))
    });
    iconFeature.id = id;

    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 0.75,
            src: 'actions/taxiimage.php?color=' + encodeURIComponent(color),
            size: [36, 48]
        }))

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
    driver[id].set('geometry', new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857')));
}

// крестик в поиске слева
//
$(document).on('input', '.clearable',function () {
    $(this)[tog(this.value)]('x');
}).on('mousemove', '.x',function (e) {
    $(this)[tog(this.offsetWidth - 18 < e.clientX - this.getBoundingClientRect().left)]('onX');
}).on('click', '.onX', function () {
    $(this).removeClass('x onX').val('');
});

function tog(v) {
    return v ? 'addClass' : 'removeClass';
}

function randomColor() {
    var r = (Math.floor(Math.random() * 16/3)*3).toString(16);
    var g = (Math.floor(Math.random() * 16/3)*3).toString(16);
    var b = (Math.floor(Math.random() * 16/3)*3).toString(16);
    color = "#" + r.toString(16) + g.toString(16) + b.toString(16);
    return color;
}