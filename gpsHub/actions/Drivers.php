<?php

require_once('../classes/Drivers.php');
$drivers = new Drivers();

if ($_POST) {
    if (!isset($_POST['id'])) {
        echo 'NEED_ID';
        exit;
    }
    if (!isset($_POST['lat'])) {
        echo 'NEED_LAT';
        exit;
    }
    if (!isset($_POST['lng'])) {
        echo 'NEED_LNG';
        exit;
    }
    $id = $_POST['id'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];

    $drivers->setDriver($id, $lat, $lng);

    $random = rand(0, 9999);
    session_start();
    $_SESSION['locversion'] = $random;
} else if ($_GET) {
    require_once('../classes/User.php');
    $user = new User();
    if ($user->isLoggedIn()) {
        switch ($_GET['type']) {
            case 'id':
                if (!isset($_GET['id'])) {
                    echo 'NEED_ID';
                    exit;
                }
                $driver = $drivers->getDriver($_GET['id']);
                echo json_encode($driver);
                break;
            case 'location':
                $response = [
                    'time' => time(),
                    'list' => $drivers->getDriversLocation(),
                ];
                echo json_encode($response);
                break;
            case 'all':
                $list = $drivers->getDrivers();
                echo json_encode($list);
                break;
        }
    } else if (isset($_GET['company_hash'])) {
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
        if (!isset($_GET['id'])) {
            echo 'NEED_ID';
            exit;
        }
        $driver = $drivers->getDriver($_GET['id']);
        echo json_encode($driver);
    }
}