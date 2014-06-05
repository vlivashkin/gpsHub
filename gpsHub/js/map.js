function tog(v) {
    return v ? 'addClass' : 'removeClass';
}

var layout;
var map;
var drawingManager;

var mapOptions = {
    center: new google.maps.LatLng(55.75167, 37.61778),
    zoom: 10,
    mapTypeControl: true,
    mapTypeControlOptions: {
        style: google.maps.MapTypeControlStyle.DROPDOWN_MENU,
        position: google.maps.ControlPosition.RIGHT_TOP
    },
    disableDefaultUI: true,
    overviewMapControl: true
};

window.onresize = function(event) {
    google.maps.event.trigger(map, 'resize');
}

$(document).on('input', '.clearable',function () {
    $(this)[tog(this.value)]('x');
}).on('mousemove', '.x',function (e) {
    $(this)[tog(this.offsetWidth - 18 < e.clientX - this.getBoundingClientRect().left)]('onX');
}).on('click', '.onX', function () {
    $(this).removeClass('x onX').val('');
});

$(document).ready(function () {
    initMap();

    $("#zoom-in").click(function () {
        map.setZoom(map.getZoom() + 1);
    });
    $("#zoom-out").click(function () {
        map.setZoom(map.getZoom() - 1);
    });
});

function initMap() {
    map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

    var control;
    control = document.getElementById("userbar");
    console.log(control);
    map.controls[google.maps.ControlPosition.TOP_RIGHT].push(control);
    control = document.getElementById("menubar");
    console.log(control);
    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(control);

    drawingManager = new google.maps.drawing.DrawingManager({
        drawingControl: false,
        markerOptions: {
            draggable: true,
            zIndex: 2,
            flat: false
        }
    });
    drawingManager.setMap(map);

    google.maps.event.addListenerOnce(map, 'idle', function(){
        initLayout();
        $("#list-layout").show();
        mapResize();
        $("#userbar").show();
        $("#menubar").show();
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
        onresize: function() {
            mapResize();
        }
    });
}

function mapResize() {
    var center = map.getCenter();
    google.maps.event.trigger(map, "resize");
    map.setCenter(center);
}