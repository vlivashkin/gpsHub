<?php
if (isset($_POST) && isset($_POST['action'])) {
    switch ($_POST['action']) {
        case 'create':
            if (isset($_POST['company_hash'])) {
                require_once('../classes/DriverManager.php');
                $driverManager = new DriverManager();
                if ($company_id = $driverManager->getCompanyId($_POST['company_hash']))
                    echo $driverManager->createDriver($company_id);
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
                    $company_id = $user->getCompanyId();
                } else {
                    if (isset($_POST['company_hash'])) {
                        require_once('../classes/DriverManager.php');
                        $driverManager = new DriverManager();
                        if ($company_id = $driverManager->getCompanyId($_POST['company_hash']) &&
                            $driverManager->isUnconfirmed($company_id, $_POST['driver_id'])) {

                        }
                    }
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
                    $driverManager->deleteDriver($user->getCompanyId(), $_POST['driver_id']);
                }
            }
            break;
    }

}