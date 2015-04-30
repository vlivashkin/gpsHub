var list;

function MapManager() {

}

MapManager.prototype.initMap = function() {
    var _this = this;

    this.map = new ol.Map({
        target: 'map-canvas',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.TileJSON({
                    url: 'http://api.tiles.mapbox.com/v3/vkmaps.map-an1xcr4f.jsonp'
                })
            }),
            new ol.layer.Vector({
                source: new ol.source.Vector()
            })
        ],
        projection: 'EPSG:3857',
        view: new ol.View({
            center: ol.proj.transform([38.457470, 55.786079], 'EPSG:4326', 'EPSG:3857'),
            zoom: 12
        })
    });

    this.map.once("postrender", function () {
        initLayout();
        _this.resizeMap();
    });
};

MapManager.prototype.resizeMap = function() {
    this.map.updateSize();
};

MapManager.prototype.initLocationUpdates = function() {
    this.addDriversToMap();
};

MapManager.prototype.addDriversToMap = function() {
    var _this = this;

    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        data: {
            type: 'location'
        },
        dataType: 'json',
        success: function (data) {
            if (data && data.list) {
                data.list.forEach(function (driver) {
                    _this.addPoint(driver.driver_id, driver.lat, driver.lng);
                    list.drivers[driver.driver_id].last_activity = driver.last_activity;
                });
                list.bindListMapping();
                setInterval((function(self) {
                    return function() {
                        self.updateDriversLocation();
                    }
                })(_this), 1500);
            }
        }
    });
};

MapManager.prototype.updateDriversLocation = function() {
    var _this = this;

    $.ajax({
        url: 'actions/drivers.php',
        type: 'GET',
        data: {
            type: 'location'
        },
        dataType: 'json',
        success: function (data) {
            if (data && data.list) {
                data.list.forEach(function (driver) {
                    if (isEmpty(driver.driver_id) || isEmpty(driver.lat) || isEmpty(driver.lng) || isEmpty(driver.busy))
                        return;
                    _this.movePoint(driver.driver_id, driver.lat, driver.lng);
                    list.drivers[driver.driver_id].lat = driver.lat;
                    list.drivers[driver.driver_id].lon = driver.lon;
                    list.drivers[driver.driver_id].busy = driver.busy;
                    list.drivers[driver.driver_id].accuracy = driver.accuracy;
                    list.drivers[driver.driver_id].last_activity = driver.last_activity;
                });

                _this.updateStatuses(data.time);
                list.updateStatuses(data.time);
            }
        }
    });
};

MapManager.prototype.addPoint = function(id, lat, lon) {
    if (typeof(lat) === "string")
        lat = parseFloat(lat);
    if (typeof(lon) === "string")
        lon = parseFloat(lon);

    var iconFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857'))
    });

    this.map.getLayers().getArray()[1].getSource().addFeature(iconFeature);
    list.drivers[id].feature = iconFeature;
};

MapManager.prototype.movePoint = function(id, lat, lon) {
    lat = parseFloat(lat);
    lon = parseFloat(lon);

    var point = new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857'));
    var driver = list.drivers[id];
    if (driver) {
        var feature = list.drivers[id].feature;
        feature.set('geometry', point);
    }

};

MapManager.prototype.updateStatuses = function(now) {
    var _this = this;

    Object.keys(list.drivers).forEach(function (id) {
        var diff = now - list.drivers[id].last_activity;
        var busy = list.drivers[id].busy;
        var accuracy = parseFloat(list.drivers[id].accuracy);

        var iconStyle = new ol.style.Style({
            image: _this.icon(diff, busy, accuracy),
            text: new ol.style.Text({
                font: '12px Calibri,sans-serif',
                text: list.drivers[id].vehicle_num,
                fill: new ol.style.Fill({color: '#333'}),
                stroke: new ol.style.Stroke({color: '#eee', width: 3}),
                offsetY: "7"
            })
        });

        list.drivers[id].feature.setStyle(iconStyle);
    });
};

MapManager.prototype.icon = function(diffTime, busyStatus, accuracyStatus) {
    var status = 'red';
    if (diffTime < 60) {
        status = 'green';
    } else if (diffTime < 5 * 60) {
        status = 'yellow';
    }

    var busy = busyStatus == "true" || busyStatus == "busy" ? "yellow" : "white";

    var accuracy = 'red';
    if (accuracyStatus < 15) {
        accuracy = 'green';
    } else if (accuracyStatus < 50) {
        accuracy = 'yellow';
    } else if (isNaN(accuracyStatus)) {
        accuracy = 'grey';
    }

    return new ol.style.Icon({
        anchor: [0.5, 35],
        anchorXUnits: 'fraction',
        anchorYUnits: 'pixels',
        opacity: 0.95,
        src: 'resources/images/taxi.php?status=' + status + "&busy=" + busy + "&accuracy=" + accuracy
    })
};