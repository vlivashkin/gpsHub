var map;
var panoramio;
var drawingManager;

var figureList = {};
var selected = [];

var isCtrl = false;
var infowindowMode = false;
var infowindowcurrent;

var tools = [
    {name: "marker", class: google.maps.drawing.OverlayType.MARKER},
    {name: "polyline", class: google.maps.drawing.OverlayType.POLYLINE},
    {name: "polygon", class: google.maps.drawing.OverlayType.POLYGON},
    {name: "rectangle", class: google.maps.drawing.OverlayType.RECTANGLE},
    {name: "circle", class: google.maps.drawing.OverlayType.CIRCLE}
];

var selectedFigure = {
    editable: true,
    draggable: true,
    strokeOpacity: 1,
    fillOpacity: 0.30
};

var unselectedFigure = {
    editable: false,
    draggable: false,
    strokeOpacity: 0.7,
    fillOpacity: 0.20
};

var mapOptions = {
    center: new google.maps.LatLng(-34.397, 150.644),
    zoom: 8,

    mapTypeControl: true,
    mapTypeControlOptions: {
        style: google.maps.MapTypeControlStyle.DROPDOWN_MENU,
        position: google.maps.ControlPosition.TOP_RIGHT
    },
    streetViewControl: true,
    streetViewControlOptions: {
        position: google.maps.ControlPosition.RIGHT_BOTTOM
    },
    disableDefaultUI: true,
    overviewMapControl: true
};

var contentString =
    '<div>' +
    '<textarea id="carea" class="form-control" placeholder="Write here"></textarea>' +
    '<button type="button" class="btn btn-default">Cancel</button>' +
    '<button type="button" class="btn btn-primary" onclick="saveInfoWindow();">Save</button>' +
    '</div>';

/**
 * Function to get user layer id from the url string
 * like map.php?lid={id}
 * @returns {id}
 */
function getLayerId() {
    return getParameterByName('lid');
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

/**
 * Get user objects from database
 */

function displayObjectsFromDB() {
    if(getLayerId())
        $.ajax({
            url: 'classes/getLayerObjects.php',
            method: 'GET',
            dataType: 'json',
            data: {layer: getLayerId()},
            success: function(json) {
                console.log(json);
            }
        });
}

/**
 * Sign in function
 */
function signIn() {
    $.ajax({
        url: 'action.php',
        type: 'POST',
        data: {signin: true, email: $("#mail-field").val(), password: $("#pass-field").val()},
        success: function(data) {
            if(data.length == 4)
                location.reload();
        }
    });
}


function initialize() {
    map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
    panoramio = new google.maps.panoramio.PanoramioLayer();

    var menu = document.getElementById("signinbar");
    map.controls[google.maps.ControlPosition.TOP_RIGHT].push(menu);
    menu = document.getElementById("menubar");
    map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(menu);
    var toolbar = document.getElementById("toolbar");
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(toolbar);

    var ctaLayer = new google.maps.KmlLayer({
        url: 'http://gmaps-samples.googlecode.com/svn/trunk/ggeoxml/cta.kml'
    });
    ctaLayer.setMap(map);

    var defaultMarkerOptions = {
        draggable: true,
        zIndex: 2,
        flat: false
    };

    var defaultOptions = {
        strokeColor: '#FF0000',
        strokeOpacity: 0.7,
        strokeWeight: 2,
        fillColor: "#FF0000",
        fillOpacity: 0.20,
        clickable: false,
        draggable: false,
        zIndex: 1,
        editable: false
    };

    var defaultPolylineOptions = jQuery.extend({}, defaultOptions);
    defaultPolylineOptions.strokeWeight = 3;

    drawingManager = new google.maps.drawing.DrawingManager({
        drawingControl: false,
        markerOptions: defaultMarkerOptions,
        polylineOptions: defaultPolylineOptions,
        polygonOptions: defaultOptions,
        rectangleOptions: defaultOptions,
        circleOptions: defaultOptions
    });
    drawingManager.setMap(map);

    createListeners();

    setTimeout(function() {
        $("#signinbar").show();
        $("#menubar").show();
        $("#toolbar").show();
    }, 300);

    displayObjectsFromDB();
}

/**
 * Listeners
 */

function createListeners() {
    listenMap();
    listenMenu();
    listenKeyboard();
    tools.forEach(listenToolBtn);
    listenFigureOnComplete();
    listenInfoTool();
}

function listenMap() {
    google.maps.event.addListener(map, 'click', function () {
        $("#dropdown-menu").hide();
        unselectAll();
    });
    google.maps.event.addListener(map, 'rightclick', function () {
        closeTool();
        $("#dropdown-menu").hide();
        unselectAll();
    });
}

function listenMenu() {
    $("#zoom-in").click(function () {
        map.setZoom(map.getZoom() + 1);
    });
    $("#zoom-out").click(function () {
        map.setZoom(map.getZoom() - 1);
    });
    $("#panoramio").click(function () {
        panoramio.setMap(panoramio.getMap() == null ? map : null);
    });
    $("#toolBtn").click(function () {
        $("#toolbar").toggle();
    });
    $("#sign-in").click(function () {
        signIn();
    });
}

function listenKeyboard() {
    $(document).keyup(function (e) {
        if(e.keyCode == 17)
            isCtrl = false;
    }).keydown(function (e) {
        if(e.keyCode == 17)
            isCtrl = true;
        if(e.keyCode == 27)
            closeTool();
        if(e.keyCode == 46)
            delFigure();
        if((e.keyCode == 65 || e.keyCode == 97)&& isCtrl == true) {
            selectAll();
            e.preventDefault();
        }
    });
}

function listenToolBtn(tool) {
    var $btn = $("#" + tool.name);

    $btn.click(function () {
        if (!$btn.hasClass("btn-primary")) {
            closeTool();
            drawingManager.setOptions({
                drawingMode: tool.class
            });
            unselectAll();
            setClickableAll(false);
            $btn.addClass("btn-primary");
        } else {
            closeTool();
        }
    });
}

function listenInfoTool() {
    var $btn = $("#infowindow");

    $btn.click(function () {
        if (!$btn.hasClass("btn-primary")) {
            closeTool();
            map.setOptions({draggableCursor: 'crosshair'});
            infowindowMode = true;
            $btn.addClass("btn-primary");
        } else {
            closeTool();
        }
    });
}

function closeTool() {
    map.setOptions({draggableCursor: null});
    drawingManager.setOptions({drawingMode: null});
    $(".btn-tool").removeClass("btn-primary");
    $("#dropdown-menu").hide();
    infowindowMode = false;
    setClickableAll(true);
}

function listenFigureOnComplete() {
    google.maps.event.addListener(drawingManager, 'overlaycomplete', function (event) {
        var figure = event.overlay;
        figure.type = event.type;
        var toolName = event.type;
        var dragold;

        tools.forEach(function (tool) {
            if (event.type == tool.class) {
                toolName = tool.class;
            }
        });

        google.maps.event.addListener(figure, "click", function () {
            if (figure.type == google.maps.drawing.OverlayType.MARKER && infowindowMode) {
                figure.infowindow = new google.maps.InfoWindow({content: contentString});
            }
            if (figure.infowindow) {
                figure.infowindow.open(map, figure);
                infowindowcurrent = figure.infowindow;
            }
            if (!isCtrl)
                unselectAll();
            selected.indexOf(figure) >= 0 ? unselectFigure(figure) : selectFigure(figure);
        });

        google.maps.event.addListener(figure, "rightclick", function (event) {
            if (selected.length > 1) {
                if (selected.indexOf(figure) >= 0) {
                    showContextMenu(event.latLng, "this objects: " + selected.length);
                }
            } else {
                showContextMenu(event.latLng, toolName, figure.__gm_id);
            }
        });

        google.maps.event.addListener(figure, "dragstart", function (event) {
            dragold = event.latLng;
            console.log("dragstart");
        });

        google.maps.event.addListener(figure, "drag", function (event) {
            if (selected.length > 1) {
                if (dragold) {
                    var dragnew = event.latLng;
                    var dlat = dragnew.lat() - dragold.lat();
                    var dlng = dragnew.lng() - dragold.lng();

                    selected.forEach(function(sel) {
                        if (figure.__gm_id != sel.__gm_id) {
                            switch (sel.type) {
                                case google.maps.drawing.OverlayType.MARKER :
                                    sel.setPosition(new google.maps.LatLng(sel.getPosition().lat() + dlat, sel.getPosition().lng() + dlng));
                                    break;
                                case google.maps.drawing.OverlayType.POLYLINE :
                                case google.maps.drawing.OverlayType.POLYGON :
                                    var path = sel.getPath();
                                    path.forEach(function(point, index) {
                                        path.setAt(index, new google.maps.LatLng(point.lat() + dlat, point.lng() + dlng));
                                    });
                                    break;
                                case google.maps.drawing.OverlayType.RECTANGLE :
                                    var bounds = sel.getBounds();
                                    var sw = new google.maps.LatLng(bounds.getSouthWest().lat() + dlat, bounds.getSouthWest().lng() + dlng);
                                    var ne = new google.maps.LatLng(bounds.getNorthEast().lat() + dlat, bounds.getNorthEast().lng() + dlng);
                                    sel.setBounds(new google.maps.LatLngBounds(sw, ne));
                                    break;
                                case google.maps.drawing.OverlayType.CIRCLE :
                                    sel.setCenter(new google.maps.LatLng(sel.getCenter().lat() + dlat, sel.getCenter().lng() + dlng));
                                    break;
                            }
                        }
                    });
                    dragold = dragnew;
                }
            }
        });

        google.maps.event.addListener(figure, "dragend", function (event) {
            console.log("dragend");
        });

        figureList[figure.__gm_id] = figure;
        saveObjectToDB(figure);
    });
}

function delFigure(id) {
    if (arguments.length == 1 && id != undefined) {
        console.log(id);
        figureList[id].setMap(null);
    } else {
        selected.forEach(function (figure) {
            figure.setMap(null);
        });
    }
}

function setClickableAll(clickable) {
    if (arguments.length == 0)
        clickable = true;
    Object.keys(figureList).forEach(function (temp) {
        figureList[temp].setOptions({
            clickable: clickable
        });
    });
}

function selectFigure(figure) {
    figure.setOptions(selectedFigure);
    selected.push(figure);
}

function unselectFigure(figure) {
    figure.setOptions(unselectedFigure);
    selected.splice(selected.indexOf(figure), 1);
}

function selectAll() {
    Object.keys(figureList).forEach(function (temp) {
        figureList[temp].setOptions(selectedFigure);
        selected.push(temp);
    });
}

function unselectAll() {
    Object.keys(figureList).forEach(function (temp) {
        figureList[temp].setOptions(unselectedFigure);
    });
    selected = [];
}

/**
 * Save to DataBase functions
 */

function saveObjectToDB(figure) {
    var object = {
        id: figure.__gm_id,
        type: figure.type
    };
    switch (object.type) {
        case google.maps.drawing.OverlayType.MARKER :
            object.position = figure.getPosition();
            break;
        case google.maps.drawing.OverlayType.POLYLINE :
        case google.maps.drawing.OverlayType.POLYGON :
            object.path = figure.getPath().getArray();
            break;
        case google.maps.drawing.OverlayType.RECTANGLE :
            object.bounds = figure.getBounds();
            break;
        case google.maps.drawing.OverlayType.CIRCLE :
            object.center = figure.getCenter();
            object.radius = figure.getRadius();
            break;
    }

    console.log(JSON.stringify(object));

    $.ajax({
        url: 'classes/saveObjects.php',
        type: 'POST',
        data: {object: JSON.stringify(object), layer_id: getLayerId()},
        dataType: 'json',
        success: function (data) {
            console.log(data);
        }
    });
}

/**
 * Context menu
 */

function showContextMenu(currentLatLng, name, id) {
    setMenuXY(currentLatLng);
    $("#dropdown-menu").show();
    var $dropdown = $('#dropdown-menu-a');
    $dropdown.text("Delete " + name);
    $dropdown.click(function () {
        delFigure(id);
        $("#dropdown-menu").hide();
    });
}

function getCanvasXY(currentLatLng) {
    var scale = Math.pow(2, map.getZoom());
    var nw = new google.maps.LatLng(
        map.getBounds().getNorthEast().lat(),
        map.getBounds().getSouthWest().lng()
    );
    var worldCoordinateNW = map.getProjection().fromLatLngToPoint(nw);
    var worldCoordinate = map.getProjection().fromLatLngToPoint(currentLatLng);
    return new google.maps.Point(
        Math.floor((worldCoordinate.x - worldCoordinateNW.x) * scale),
        Math.floor((worldCoordinate.y - worldCoordinateNW.y) * scale)
    );
}

function setMenuXY(currentLatLng) {
    var clickedPosition = getCanvasXY(currentLatLng);

    var $dropdown = $('#dropdown-menu');
    $dropdown.css('left', clickedPosition.x);
    $dropdown.css('top', clickedPosition.y);
}

function saveInfoWindow() {
    var text = '<div id="ccontainer">' + $("#carea").val() + "<br><a onclick='editInfoWindow()'>Edit...</a></div>";
    infowindowcurrent.setContent(text);
}

function editInfoWindow() {
    var text = infowindowcurrent.getContent();
    $("#ccontainer").html(contentString);
    $("#carea").val(text);
}