<?php

if ($_POST) {
    $email = $_POST['email'];
    $hash = $_POST['hash'];
    $hash256 = hash('sha256', $hash . $email);

    require_once('../classes/SQLConfig.php');
    $sqlconfig = new SQLConfig();
    $mysqli = $sqlconfig->getMysqli();
    $query = "SELECT * FROM `user` WHERE `email` = '" . $email . "' AND `password` = '" . $hash256 . "'";
    $result = $mysqli->query($query);

    if ($result->num_rows) {
        session_start();
        $_SESSION['email'] = $email;

        echo "SUCCESS";
        return;
    }

    echo "FAILURE";
}