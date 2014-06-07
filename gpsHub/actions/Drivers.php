<?php

require_once('../classes/Drivers.php');
$drivers = new Drivers();

if ($_POST) {
    $id = $_POST['id'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];

    $list = $drivers->setDriver($id, $lat, $lng);
    echo json_encode($list);
} else {
    $list = $drivers->getDrivers();
    echo json_encode($list);
}