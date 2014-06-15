<?php

require_once('../classes/Drivers.php');
$drivers = new Drivers();

if ($_POST) {
    $id = $_POST['id'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];

    $drivers->setDriver($id, $lat, $lng);
} else {
    require_once('../classes/User.php');
    $user = new User();
    if ($user->isLoggedIn()) {
        if (isset($_GET['id'])) {
            $driver = $drivers->getDriver($_GET['id']);
            echo json_encode($driver);
        } else {
            $list = $drivers->getDriversLocation();
            $time = time();
            $response = [
                'time' => $time,
                'list' => $list
            ];
            echo json_encode($response);
        }
    }
}