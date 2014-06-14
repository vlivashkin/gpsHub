<?php
require_once('classes/User.php');
$user = new User();
if (!$user->isLoggedIn())
    header("Location: signin.php");
?>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <title>gpsHub</title>

    <script src="js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script src="js/jquery-ui-1.10.4.min.js" type="text/javascript"></script>
    <script src="js/jquery.layout-latest.min.js" type="text/javascript"></script>
    <script src="js/ol.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/moment-with-langs.min.js" type="text/javascript"></script>
    <script src="js/map.js" type="text/javascript"></script>
    <script src="js/user.js" type="text/javascript"></script>

    <link rel="StyleSheet" type="text/css" href="css/ol.css">
    <link rel="StyleSheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="StyleSheet" type="text/css" href="css/main.min.css"/>
</head>
<body>
<div id="container" class="layout-item">
    <div id="list-layout" class="layout-item">
        <div id="list-header">
            <input class="form-control input-sm clearable" placeholder="Поиск машины">
        </div>
        <div id="list-body">
            <div class="panel-group" id="list">
                <?php
                while($row = $user->getDrivers()->fetch_array(MYSQLI_ASSOC)){
                    $driver_id = $row['driver_id'];
                    $name = $row['name'];
                    $phone_number = $row['phone_number'];
                    $vehile_num = $row['vehile_num'];
                    $last_activity = $row['last_activity'];
                    echo <<<EOF
                        <div id="driver-$driver_id-panel" class="panel panel-default">
                            <div class="panel-heading">
                                <div class='circle'></div>
                                <div class='driver-text'>
                                    $name<br>
                                    $vehile_num
                                </div>
                                <div class='right driver-color'></div>
                                <div class='right ext-btn'>
                                    <span class="glyphicon glyphicon-chevron-down"></span>
                                </div>
                            </div>
                            <div id='driver-$driver_id' class="panel-collapse collapse" data-parent="#list">
                                <div class="panel-body">
                                    <table class="table table-striped">
                                    <tr>
                                        <th>Id</th>
                                        <th>$driver_id</th>
                                    </tr>
                                    <tr>
                                        <th>Номер телефона</th>
                                        <th>$phone_number</th>
                                    </tr>
                                    <tr>
                                        <th>Последняя активность</th>
                                        <th>Нет информации</th>
                                    </tr>
                                    </table>
                                </div>
                                Изменить...
                            </div>
                        </div>
EOF;
                }
                ?>
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
                            echo $user->getEmail() . "; " . $user->getName() . "<br>";
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