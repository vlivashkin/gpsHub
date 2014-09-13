<?php

require_once('../classes/User.php');
$user = new User();
if (!$user->isLoggedAsAdmin()) {
    header("Location: ../index.php");
}

?>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <title>gpsHub</title>

    <script src="../resources/js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script src="../resources/js/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="../resources/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/drivers.js" type="text/javascript"></script>


    <link rel="StyleSheet" type="text/css" href="../resources/css/bootstrap.min.css">
    <link rel="StyleSheet" type="text/css" href="../resources/css/main.css">
    <link rel="StyleSheet" type="text/css" href="../resources/css/jquery.dataTables.min.css">

    <style>
        #driversTable tbody tr {
            cursor: pointer;
        }
    </style>
</head>
<body>

<div class="navbar-sm">
    <div class="navbar-primary">
        <div class="navbar navbar-default" role="navigation" style="margin-bottom: 0">
            <div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="../index.php">gpsHub</a>
                </div>
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-8">
                    <ul class="nav navbar-nav">
                        <li><a href="index.php">Управление</a></li>
                        <li class="active"><a href="#">Cписок водителей</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <h1>
        Cписок водителей
        <button id="add_driver" class="btn btn-success">
            <span class="glyphicon glyphicon-plus"></span> Добавить водителя
        </button>
    </h1>

    <table id="driversTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Позывной</th>
            <th>Номер телефона</th>
            <th>Номер машины</th>
            <th>Описание машины</th>
        </tr>
        </thead>
    </table>
</div>

<div class="modal fade" id="modifyModal" tabindex="-1" role="dialog" aria-labelledby="modifyModalLabel"
     aria-hidden="true">
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
                        <label class="col-sm-3 control-label" for="modify-vehicle-num">Номер машины:</label>

                        <div class="col-sm-9">
                            <input id="modify-vehicle-num" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-vehicle-description">Описание машины:</label>

                        <div class="col-sm-9">
                            <textarea id="modify-vehicle-description" class="form-control"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-vehicle-description">Группы:</label>

                        <div class="col-sm-9">
                            <div id="modify-groups" style="margin-left: 17px"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a id="modal-delete" href="#">Удалить водителя</a>
                <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                <button id="modal-save" type="button" class="btn btn-primary" data-dismiss="modal">Сохранить изменения</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>	