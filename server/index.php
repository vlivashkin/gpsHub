<?php

require_once('classes/User.php');
$user = new User();
if (!$user->isLoggedIn()) {
    header("Location: signin.php");
}

?>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <title>gpsHub</title>

    <script src="resources/js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script src="resources/js/jquery-ui.min.js" type="text/javascript"></script>
    <script src="resources/js/jquery.layout-latest.min.js" type="text/javascript"></script>
    <script src="resources/js/ol.js" type="text/javascript"></script>
    <script src="resources/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="resources/js/moment-with-langs.min.js" type="text/javascript"></script>
    <script src="resources/js/main.js" type="text/javascript"></script>
    <script src="resources/js/user.js" type="text/javascript"></script>

    <link rel="StyleSheet" type="text/css" href="resources/css/ol.css">
    <link rel="StyleSheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="StyleSheet" type="text/css" href="resources/css/main.css"/>
</head>
<body>
<div id="container" class="layout-item">
    <div id="list-layout" class="layout-item">
        <div id="list-header" class="list-header">
            <h4 class="logo">gpsHub</h4>

            <div id="list-toggler">
                <span class="glyphicon glyphicon-chevron-left"></span>
            </div>
            <?php
                if ($user->isLoggedAsAdmin()) {
                    echo "<div id='settingsBtn'><span class='glyphicon glyphicon-wrench'></span></div>";
                }
            ?>
        </div>
        <div id="list-body">
            <div class="panel-group" id="list">

            </div>
        </div>
    </div>
    <div id="map-layout" class="layout-item">
        <div id="map-canvas"></div>

        <div id="userbar">
            <div class="btn-group">
                <button id="sign-in-button" type="button" class="btn btn-warning" onclick="signOut()">
                    <span id="userinfo" role="tooltip">
                        <?php
                        echo $user->getLogin() . "; " . $user->getName() . "<br>";
                        echo $user->getCompanyName();
                        ?>
                    </span>
                    <span class="glyphicon glyphicon glyphicon-log-out"></span>
                </button>
            </div>
        </div>
    </div>
</div>

</body>
</html>	