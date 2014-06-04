function tog(v) {
    return v ? 'addClass' : 'removeClass';
}

var layout;
var map;
var drawingManager;

var mapOptions = {
    center: new google.maps.LatLng(-34.397, 150.644),
    zoom: 8,
    mapTypeControl: true,
    mapTypeControlOptions: {
        style: google.maps.MapTypeControlStyle.DROPDOWN_MENU,
        position: google.maps.ControlPosition.LEFT_TOP
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
    control = document.getElementById("signinbar");
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
        google.maps.event.trigger(map, 'resize');
        $("#signinbar").show();
        $("#menubar").show();
    });
}

function initLayout() {
    layout = $('#container').layout({
        center__paneSelector: "#map-layout",
        west__paneSelector: "#list-layout",
        center__minWidth: 200,
        west__size: 250,
        west__minSize: 150,
        spacing_open: 5,
        spacing_closed: 10,
        livePaneResizing: true,
        stateManagement__enabled: true,
        onresize: function() {
            google.maps.event.trigger(map, 'resize');
        }
    });
}