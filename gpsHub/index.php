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
            <h4 class="logo">gpsHub</h4>
            <div id="list-toggler">
                <span class="glyphicon glyphicon-chevron-left" style=""></span>
            </div>
        </div>
        <div id="list-body">
            <div class="panel-group" id="list">
                <?php
                require_once('classes/Drivers.php');
                $drivers = new Drivers();
                $drivers_array = $drivers->getDrivers();
                while ($row = $drivers_array->fetch_array(MYSQLI_ASSOC)) {
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
                                    <div class="row">
                                        <div class="col-xs-6">Id</div>
                                        <div class="col-xs-6">$driver_id</div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-6">Номер телефона</div>
                                        <div class="col-xs-6">$phone_number</div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-6">Последняя активность</div>
                                        <div class="col-xs-6 last-activity">Нет информации</div>
                                    </div>
                                    <a href="#" onclick="buildModal($driver_id);">Изменить...</a>
                                </div>
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

<div class="modal fade" id="modifyModal" tabindex="-1" role="dialog" aria-labelledby="modifyModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modifyModalLabel">Пожалуйста, подождите...</h4>
            </div>
            <div class="modal-body">
                <div class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-name">Полное имя:</label>
                        <div class="col-sm-9">
                            <input id="modify-name" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-alias">Позывной:</label>
                        <div class="col-sm-9">
                            <input id="modify-alias" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-phone">Номер телефона:</label>
                        <div class="col-sm-9">
                            <input id="modify-phone" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-vehile-num">Номер машины:</label>
                        <div class="col-sm-9">
                            <input id="modify-vehile-num" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-vehile-description">Описание машины:</label>
                        <div class="col-sm-9">
                            <textarea id="modify-vehile-description" class="form-control"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a id="modal-delete" href="#">Удалить водителя</a>
                <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                <button id="modal-save" type="button" class="btn btn-primary">Сохранить изменения</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>	