function tog(v) {
    return v ? 'addClass' : 'removeClass';
}

var layout;
var map;

$(document).on('input', '.clearable',function () {
    $(this)[tog(this.value)]('x');
}).on('mousemove', '.x',function (e) {
    $(this)[tog(this.offsetWidth - 18 < e.clientX - this.getBoundingClientRect().left)]('onX');
}).on('click', '.onX', function () {
    $(this).removeClass('x onX').val('');
});

$(document).ready(function () {
    initMap();
});

window.onresize = function (event) {
    mapResize();
};

function initMap() {
    var options = {
        projection: new OpenLayers.Projection("EPSG:900913"),
        displayProjection: new OpenLayers.Projection("EPSG:4326"),
        numZoomLevels: 18,
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
            })
        ]
    };
    map = new OpenLayers.Map('map-canvas', options);

    var center = new OpenLayers.Geometry.Point(37.61778, 55.75167);
    center.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
    map.setCenter(new OpenLayers.LonLat(center.x, center.y), 11);

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