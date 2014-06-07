var layout;
var map;
var driver = {};

$(document).ready(function () {
    initLayout();
    initMap();
    $("#list-layout").show();
    map.updateSize();
    setInterval(getDrivers, 1500);
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
        view: new ol.View2D({
            center: ol.proj.transform([37.61778, 55.75167], 'EPSG:4326', 'EPSG:3857'),
            zoom: 11
        })
    });
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
        spacing_closed: 10,
        livePaneResizing: true,
        stateManagement__enabled: true,
        onresize: function () {
            map.updateSize();
        }
    });
}

function addPoint(lat, lng, id) {
    console.log(driver);

    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.transform([lat, lng], 'EPSG:4326', 'EPSG:3857')),
        name: 'Null Island',
        population: 4000,
        rainfall: 500
    });

    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 0.75,
            src: 'img/marker.png'
        }))
    });

    iconFeature.setStyle(iconStyle);

    map.getLayers().getArray()[1].getSource().addFeature(iconFeature);

    return iconFeature;
}

function movePoint(lat, lng, marker) {
    marker.set('geometry', new ol.geom.Point(ol.proj.transform([lat, lng], 'EPSG:4326', 'EPSG:3857')));
}

function getDrivers() {
    $.ajax({
        url: 'actions/Drivers.php',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            var keys = Object.keys(data);
            keys.forEach(function (id) {
                if (driver[id] == undefined)
                    driver[id] = addPoint(data[id].lat, data[id].lng, id);
                else
                    movePoint(data[id].lat, data[id].lng, driver[id]);
            });
        }
    });
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