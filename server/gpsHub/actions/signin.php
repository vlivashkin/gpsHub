<?php

require_once('../classes/User.php');
require_once('../classes/Helper.php');

if ($_POST) {
    Helper::checkPostParameter('login');
    Helper::checkPostParameter('hash');

    $login = new User();
    echo $login->signIn($_POST['login'], $_POST['hash']);
}