<?php
require_once('classes/User.php');
$user = new User();
?>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <title>Map</title>

    <script src="js/jquery.js" type="text/javascript"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&language=ru&libraries=places,panoramio,drawing"
            type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/map.js" type="text/javascript"></script>

    <link rel="StyleSheet" type="text/css" href="css/map.css"/>
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body onload="initialize();">
<div id="toolbar">
    <div>
        <h3 style="text-align: center; margin: 10px 0">
            <span class="glyphicon glyphicon-road"></span>
        </h3>

        <p>
            <button id="marker" title="place a marker" type="button"
                    class="btn btn-tool glyphicon glyphicon-map-marker"></button>
        </p>
        <p>
            <button id="polyline" title="draw a polyline" type="button"
                    class="btn btn-tool glyphicon glyphicon-flash"></button>
        </p>
        <p>
            <button id="polygon" title="draw a polygon" type="button"
                    class="btn btn-tool glyphicon glyphicon-stop"></button>
        </p>
        <p>
            <button id="rectangle" title="draw a rectangle" type="button"
                    class="btn btn-tool glyphicon glyphicon-retweet"></button>
        </p>
        <p>
            <button id="circle" title="draw a circle" type="button"
                    class="btn btn-tool glyphicon glyphicon-certificate"></button>
        </p>
        <p>
            <button id="infowindow" title="add a description" type="button"
                    class="btn btn-tool glyphicon glyphicon-comment"></button>
        </p>
        <p>
            <button id="photo" title="attach a photo" type="button"
                    class="btn btn-tool glyphicon glyphicon-camera" data-toggle="modal" data-target="#img-window"></button>
        </p>
        <p>
            <button id="kml" title="load gps track" type="button"
                    class="btn btn-tool glyphicon glyphicon-phone" data-toggle="modal" data-target="#gps-window"></button>
        </p>
    </div>
    <hr>
    <div>
        <p>
            <button disabled="disabled" title="calculate the distance" type="button"
                    class="btn btn-warning glyphicon glyphicon-resize-horizontal"></button>
        </p>
        <p>
            <button disabled="disabled" title="get point coordinates" type="button"
                    class="btn btn-warning glyphicon glyphicon-screenshot"></button>
        </p>
        <p>
            <button disabled="disabled" title="attach a photo" type="button"
                    class="btn btn-warning glyphicon glyphicon-camera"></button>
        </p>
    </div>
</div>

<div id="menubar">
    <div class="btn-group">
    <button id="toolBtn" type="button" class="btn btn-danger glyphicon glyphicon-pencil"></button>
        </div>
    <div class="btn-group">
    <button id="panoramio" type="button" class="btn btn-primary glyphicon glyphicon-picture"></button>
        </div>
    <div class="btn-group">
        <button id="zoom-out" type="button" class="btn btn-success glyphicon glyphicon-zoom-out"></button>
        <button id="zoom-in" type="button" class="btn btn-success glyphicon glyphicon-zoom-in"></button>
    </div>
</div>

<div id="signinbar">
    <div class="form-horizontal">
        <?php
        if (!$user->isLoggedIn())
            echo '<button id="sign-in-button" type="button" class="btn btn-warning glyphicon glyphicon-user" data-toggle="modal" data-target="#sign-in-window"></button>';
        ?>
    </div>
</div>

<div id="map-canvas"></div>

<ul id="dropdown-menu" class="dropdown-menu" role="menu">
    <li role="presentation"><a id="dropdown-menu-a" role="menuitem" tabindex="-1" href="#">Delete marker</a></li>
</ul>

<?php if (!$user->isLoggedIn()) { ?>
    <div class="modal fade" id="sign-in-window" tabindex="-1" role="dialog" aria-labelledby="signinLabel" aria-hidden="true">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="signinLabel">Welcome!</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <input id="mail-field" type="text" class="form-control" placeholder="E-mail">
                    </div>
                    <div class="form-group">
                        <input id="pass-field" type="password" class="form-control" placeholder="Password">
                    </div>
                    <div class="sign-in-checkbox-block">
                        <input type="checkbox" id="sign-in-checkbox"/>
                        <label for="sign-in-checkbox">Remember me</label>
                    </div>
                </div>
                <div class="alert alert-danger alert-dismissable" id="sign-in-alert">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    <strong>Warning!</strong> Better check yourself, you're not looking too good.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="sign-in">Sign in</button>
                </div>
            </div>
        </div>
    </div>
<?php } ?>

<div class="modal fade" id="gps-window" tabindex="-1" role="dialog" aria-labelledby="gpsLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="gpsLabel">Add GPS track</h4>
            </div>
            <div class="modal-body">
                <input type="file" id="gps-input">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="sign-in">Add track</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="img-window" tabindex="-1" role="dialog" aria-labelledby="imgLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="imgLabel">Add image</h4>
            </div>
            <div class="modal-body">
                <input type="file" id="img-input">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="sign-in">Add image</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>	