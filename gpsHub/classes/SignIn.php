<?php

require_once('SQLConfig.php');

if ($_GET) {
    $name = $_GET['email'];
    if (!get_magic_quotes_gpc())
        $password = md5($_GET['password']);
    else
        $password = md5(stripslashes($_GET['password']));

    $query = "SELECT * FROM `users` WHERE `email` = '" . $name . "' AND `password` = '" . $password . "'";
    $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
    $result = $mysqli->query($query);

    if ($result->num_rows) {
        session_start();
        $_SESSION['name'] = $name;
        $_SESSION['password'] = $password;
        setcookie('name', $name, time() + 86400 * 30 * 12);
        setcookie('pass', $password, time() + 86400 * 30 * 12);
        return "yes";
    }
}
return "no";