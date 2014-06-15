<?php
if (isset($_POST) && isset($_POST['action'])) {
    switch ($_POST['action']) {
        case 'create':
            if (isset($_POST['company_hash'])) {
                require_once('../classes/DriverManager.php');
                $driverManager = new DriverManager();
                if ($driverManager->isTrueHash($_POST['company_hash']))
                    echo $driverManager->createDriver();
                else {
                    echo 'NOT_VALID_COMPANY_HASH';
                }
            } else {
                echo 'NEED_COMPANY_HASH';
            }
            break;
        case 'modify':
            if (isset($_POST['driver_id'])) {
                require_once('../classes/User.php');
                $user = new User();
                if ($user->isLoggedIn()) {
                    require_once('../classes/DriverManager.php');
                    $driverManager = new DriverManager();
                    $result = $driverManager->modifyDriver($_POST['driver_id'], $_POST);
                } else {
                    if (isset($_POST['company_hash'])) {
                        require_once('../classes/DriverManager.php');
                        $driverManager = new DriverManager();
                        if ($driverManager->isTrueHash($_POST['company_hash']) &&
                            $driverManager->isUnconfirmed($_POST['driver_id'])) {
                            $result = $driverManager->modifyDriver($_POST['driver_id'], $_POST);
                        }
                    }
                }
                if (isset($result) && $result) {
                    echo 'SUCCESS';
                } else {
                    echo 'ERROR';
                }
            }
            break;
        case 'delete':
            if (isset($_POST['driver_id'])) {
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
                    echo 'ERROR';
                }
            }
            break;
    }
}