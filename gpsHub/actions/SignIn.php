<?php

require_once('../classes/SQLConfig.php');

if ($_GET) {
    $name = $_GET['email'];
    $password = md5($_GET['password']);

    $query = "SELECT * FROM `user` WHERE `email` = '" . $name . "' AND `password` = '" . $password . "'";
    $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
    $result = $mysqli->query($query);

    if ($result->num_rows) {
        session_start();
        $_SESSION['name'] = $name;
        $_SESSION['password'] = $password;
        setcookie('name', $name, time() + 86400 * 30 * 12);
        setcookie('pass', $password, time() + 86400 * 30 * 12);
        echo "yes";
        return;
    }

    echo "no";
}