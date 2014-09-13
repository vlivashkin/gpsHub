<?php

require_once('../classes/db/Drivers.php');
require_once('../classes/Helper.php');

$drivers = new Drivers();
if ($_GET) {
    if (isset($_GET["company_hash"]) && $_GET["company_hash"] == "qwerty") {
        Helper::checkGetParameter('id');
        $driver = $drivers->getDriver($_GET['id']);
        if ($driver != null) {
            echo "OK";
        } else {
            echo "WRONG USER";
        }
        exit;
    }

    Helper::checkAuthorisation();

    switch ($_GET['type']) {
        case 'id':
            Helper::checkGetParameter('id');

            $driver = $drivers->getDriver($_GET['id']);
            echo json_encode($driver);
            break;
        case 'location':
            $response = [
                'time' => time(),
                'list' => $drivers->getDriversLocation()
            ];
            echo json_encode($response);
            break;
        case 'list':
            $response = [
                'list' => $drivers->getUserGroupDrivers()
            ];
            echo json_encode($response);
            break;
    }
} else if ($_POST) {
    Helper::checkPostParameter('id');
    Helper::checkPostParameter('lat');
    Helper::checkPostParameter('lng');
    Helper::checkPostParameter('busy');

    $id = $_POST['id'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $busy = $_POST['busy'];
    $accuracy = $_POST['accuracy'];

    $drivers->setLocation($id, $lat, $lng, $busy, $accuracy);

}