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
    <script src="js/groups.js" type="text/javascript"></script>


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
                        <li class="active"><a href="#">Cписок групп водителей</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <h1>
        Cписок групп водителей
        <button id="add_group" class="btn btn-success">
            <span class="glyphicon glyphicon-plus"></span> Добавить группу
        </button>
    </h1>

    <table id="driversTable" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th>ID</th>
            <th>Название</th>
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
                        <label class="col-sm-3 control-label" for="modify-name">Название:</label>

                        <div class="col-sm-9">
                            <input id="modify-name" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="modify-participants">Участники:</label>

                        <div class="col-sm-9">
                            <textarea id="modify-participants" class="form-control"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a id="modal-delete" href="#">Удалить группу</a>
                <button type="button" class="btn btn-default" data-dismiss="modal">Отмена</button>
                <button id="modal-save" type="button" class="btn btn-primary" data-dismiss="modal">Сохранить изменения
                </button>
            </div>
        </div>
    </div>
</div>

</body>
</html>	