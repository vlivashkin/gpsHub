var mapManager;
var list;

function isEmpty(string) {
    return string == undefined || string == null || string == "";
}

$(document).ready(function () {
    moment.lang('ru');
    $.getScript("resources/js/map.js", function(){
        console.log("map.js loaded");
        mapManager = new MapManager();
        mapManager.initMap();
        $.getScript("resources/js/list.js", function(){
            console.log("list.js loaded");
            list = new List();
            list.initList();
        });
    });
});

function initLayout() {
    var layout = $('#container').layout({
        closeable: false,
        slidable: false,
        center__paneSelector: "#map-layout",
        west__paneSelector: "#list-layout",
        center__minWidth: 300,
        west__size: 250,
        west__minSize: 170,
        spacing_open: 1,
        spacing_closed: 20,
        livePaneResizing: true,
        stateManagement__enabled: true,
        onresize: function () {
            mapManager.resizeMap();
            list.resizeList();
        }
    });

    $("#list-layout-resizer").append("<div id='resizer-btn'><span class='glyphicon glyphicon-chevron-right'></span></div>");

    layout.bindButton('#list-toggler', 'close', 'west');
    layout.bindButton('#resizer-btn', 'open', 'west');

    $("#list-layout").show();
}
