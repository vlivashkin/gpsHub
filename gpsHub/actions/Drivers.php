<?php

require_once('../classes/Drivers.php');
$drivers = new Drivers();

if ($_POST) {
    $id = $_POST['id'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];

    $drivers->setDriver($id, $lat, $lng);
} else {
    $list = $drivers->getDrivers();
    $time = time();
    $response = [
        'time' => $time,
        'list' => $list
    ];
    echo json_encode($response);
}