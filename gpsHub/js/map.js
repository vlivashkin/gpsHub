var layout;
var map;
var driver = {};

$(document).ready(function () {
    initMap();
    setInterval(getDrivers, 1500);
});

window.onresize = function (event) {
    mapResize();
};

function initMap() {
    var options = {
        projection: new OpenLayers.Projection("EPSG:900913"),
        displayProjection: new OpenLayers.Projection("EPSG:4326"),
        layers: [
            new OpenLayers.Layer.OSM()
        ],
        controls: [
            new OpenLayers.Control.Navigation({
                dragPanOptions: {
                    enableKinetic: true
                }
            }),
            new OpenLayers.Control.Attribution(),
            new OpenLayers.Control.Zoom({
                zoomInId: "zoom-in",
                zoomOutId: "zoom-out"
            }),
            new OpenLayers.Control.ScaleLine(),
            new OpenLayers.Control.MousePosition()
        ]
    };
    map = new OpenLayers.Map('map-canvas', options);
    map.setCenter(coord(37.61778, 55.75167), 11);
    markers = new OpenLayers.Layer.Markers("Markers");
    map.addLayer(markers);

    initLayout();
    $("#list-layout").show();
    mapResize();
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
            mapResize();
        }
    });
}

function mapResize() {
    var center = map.getCenter();
    map.updateSize();
    map.setCenter(center);
}

function getDrivers() {
    $.ajax({
        url: 'actions/Drivers.php',
        type: 'GET',
        data: 'yes',
        dataType: 'json',
        success: function (data) {
            var keys = Object.keys(data);
            keys.forEach(function (id) {
                // if (driver[id] == undefined)
                driver[id] = addPoint(data[id].lat, data[id].lng);
                // else
                // movePoint(data[id].lat, data[id].lng, driver[id]);
            });
        }
    })
}

function addPoint(lat, lng) {
    var size = new OpenLayers.Size(21,25);
    var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
    var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png',size,offset);
    var marker = new OpenLayers.Marker(coord(lat, lng),icon.clone())
    markers.addMarker(marker);

    return marker;
}

function movePoint(lat, lng, marker) {
    marker.moveTo(coord(lat, lng));
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

function coord(lat, lng) {
    var position = new OpenLayers.Geometry.Point(lat, lng);
    position.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
    return new OpenLayers.LonLat(position.x, position.y);
}