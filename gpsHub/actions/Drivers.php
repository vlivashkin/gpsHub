<?php

require_once('../classes/Drivers.php');
$drivers = Drivers::getInstance();

if ($_POST) {
    $id = $_GET['id'];
    $lat = $_GET['lat'];
    $lng = $_GET['lng'];

    $drivers->setDriver($id, $lat, $lng);
} else if ($_GET) {
    $list = $drivers->getDrivers();
    echo json_encode($list);
}