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
                        <li class="active"><a href="#">Управление</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <h1>Управление</h1>
    <ul class="nav nav-pills nav-stacked">
        <li><a href="./drivers.php">Управление списком водителей</a></li>
        <li><a href="./groups.php">Управление списком групп водителей</a></li>
    </ul>
</body>
</html>	