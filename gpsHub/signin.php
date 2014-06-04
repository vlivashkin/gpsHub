<?php
?>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <title>gpsHub: Sign In</title>

    <script src="js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/user.min.js" type="text/javascript"></script>

    <link rel="StyleSheet" type="text/css" href="css/bootstrap.min.css">

    <style>
        body {
            background: url(img/bg.jpg) no-repeat center center fixed;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
        }

        h1 {
            font-size: 4.5em;
            font-weight: 700;
        }

        .form-control {
            border-radius: 0;
        }
    </style>
</head>
<body onload="signInInit();">
<div class="container" style="padding-top:30px">
    <h1>gpsHub</h1>

    <div class="col-md-4 col-md-offset-4" style="margin-top: 50px">
        <div class="panel panel-default">
            <div class="panel-heading"><h3 class="panel-title"><strong>Войти</strong></h3></div>
            <div class="panel-body">
                <div class="form-group">
                    <input type="email" class="form-control" id="signin-email"
                           placeholder="Логин или Email">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" id="signin-password"
                           placeholder="Пароль">
                </div>
                <div style="text-align: right">
                    <button class="btn btn-primary" onclick="signIn()">Войти</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>