<?php

if ($_POST) {
    $email = $_POST['email'];
    $hash = $_POST['hash'];
    $hash256 = hash('sha256', $hash . $email);

    require_once('../classes/SQLConfig.php');
    $query = "SELECT * FROM `user` WHERE `email` = '" . $email . "' AND `password` = '" . $hash256 . "'";
    $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
    $result = $mysqli->query($query);

    if ($result->num_rows) {
        session_start();
        $_SESSION['email'] = $email;

        require_once('../classes/Drivers.php');
        $drivers = new Drivers();
        $drivers->init();

        echo "yes";
        return;
    }

    echo "no";
}