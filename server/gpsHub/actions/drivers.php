<?php

define("TIMEOUT", 25);

require_once('../classes/Drivers.php');
$drivers = new Drivers();

if ($_POST) {
    if (!isset($_POST['id'])) {
        echo 'NEED_ID';
    }
    if (!isset($_POST['lat'])) {
        echo 'NEED_LAT';
    }
    if (!isset($_POST['lng'])) {
        echo 'NEED_LNG';
    }
    if (!isset($_POST['busy'])) {
        echo 'NEED_BUSY';
    }
    $id = $_POST['id'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $busy = $_POST['busy'] == "true" ? "busy" : "free";

    $file = 'log.tmp';
    $current = file_get_contents($file);
    $current .= time(). " id: " . $id . " lat: " . $lat . " lng: " . $lng . " busy: " . $busy . "\n";
    file_put_contents("log.tmp", $current);

    $drivers->setLocation($id, $lat, $lng, $busy);
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
                    'loc_version' => $drivers->getLocationVersion(),
                ];
                echo json_encode($response);
                break;
            case 'list':
                $response = [
                    'list' => $drivers->getDrivers(),
                    'list_version' => $drivers->getLocationVersion(),
                ];
                echo json_encode($response);
                break;
        }
    } else if (isset($_GET['company_hash'])) {
        require_once('../classes/DriverManager.php');

        $driverManager = new DriverManager();
        if (!$driverManager->isTrueHash($_GET['company_hash'])) {
            echo 'NOT_TRUE_HASH';
            exit;
        }
		if (!isset($_GET['id'])) {
            echo 'NEED_ID';
            exit;
        }
        $driver = $drivers->getDriver($_GET['id']);

        if ($driver != NULL)
            echo 'OK';
        else
            echo 'NOT_FOUND';
    }
}