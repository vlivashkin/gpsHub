var layout;
var map;
var driver = {};
var type = false;

$(document).ready(function () {
    initMap();
    setInterval(getDrivers, 1000);

    $(".driver-item").click(function () {
        var id = $(this).attr('id').substring(7);
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
        west__minSize: 200,
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
            data.forEach(function (row) {
                if (driver[row.id] == undefined) {
                    var color = randomColor();
                    $("#driver-" + row.id + " .circle").addClass("online");
                    $("#driver-" + row.id + " .driver-color").css('background-color', color);
                    driver[row.id] = addPoint(row.lat, row.lng, row.id, color);
                } else
                    movePoint(row.id, row.lat, row.lng);
            });
        }
    });
}

function addPoint(lat, lon, id, color) {
    console.log(driver);

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
    console.log(id + " " + lat + " " + lon);
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
    var color = Math.floor(Math.random() * 16777215).toString(16);
    color = "#" + ("000000" + color).slice(-6);
    return color;
}