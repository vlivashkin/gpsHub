<?php

if (isset($_POST) && isset($_POST['action'])) {
    switch ($_POST['action']) {
        case 'create':
            require_once('../classes/DriverManager.php');
            $driverManager = new DriverManager();
            $result = $driverManager->createDriver();
            if (isset($result) && $result) {
                echo $result;
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
        case 'modify':
            if (!isset($_POST['driver_id'])) {
                echo 'NEED_ID';
                exit;
            }
            require_once('../classes/User.php');
            $user = new User();
            if ($user->isLoggedIn()) {
                require_once('../classes/DriverManager.php');
                $driverManager = new DriverManager();
                $result = $driverManager->modifyDriver($_POST['driver_id'], $_POST);
            } else {
                if (!isset($_POST['company_hash'])) {
                    echo 'NEED_COMPANY_HASH';
                    exit;
                }
                require_once('../classes/DriverManager.php');
                $driverManager = new DriverManager();
                if (!$driverManager->isTrueHash($_POST['company_hash'])) {
                    echo 'NOT_TRUE_HASH';
                    exit;
                }
                if (!$driverManager->isUnconfirmed($_POST['driver_id'])) {
                    echo 'DRIVER_IS_CONFIRMED';
                    exit;
                }
                $result = $driverManager->modifyDriver($_POST['driver_id'], $_POST);
            }
            if (isset($result) && $result) {
                echo 'SUCCESS';
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
        case 'delete':
            if (!isset($_POST['driver_id'])) {
                echo 'NEED_ID';
                exit;
            }
            require_once('../classes/User.php');
            $user = new User();
            if ($user->isLoggedIn()) {
                require_once('../classes/DriverManager.php');
                $driverManager = new DriverManager();
                $result = $driverManager->deleteDriver($_POST['driver_id']);
            }
            if (isset($result) && $result) {
                echo 'SUCCESS';
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
        case 'confirm':
            if (!isset($_POST['driver_id'])) {
                echo 'NEED_ID';
                exit;
            }
            require_once('../classes/User.php');
            $user = new User();
            if ($user->isLoggedIn()) {
                require_once('../classes/DriverManager.php');
                $driverManager = new DriverManager();
                $result = $driverManager->confirmDriver($_POST['driver_id']);
            }
            if (isset($result) && $result) {
                echo 'SUCCESS';
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
    }
}