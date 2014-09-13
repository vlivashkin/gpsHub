<?php

class Helper {
    public static function checkGetParameter($name) {
        if (!isset($_GET[$name])) {
            echo 'NEED_' . strtoupper($name);
            exit;
        }
    }

    public static function checkPostParameter($name) {
        if (!isset($_POST[$name])) {
            echo 'NEED_' . strtoupper($name);
            exit;
        }
    }

    public static function checkAuthorisation() {
        require_once('../classes/User.php');
        $login = new User();
        if (!$login->isLoggedIn()) {
            echo "YOU ARE NOT AUTHORISED";
            exit;
        }
    }
} 