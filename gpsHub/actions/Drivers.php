<?php
require_once('../classes/User.php');
$user = new User();
if ($user->isLoggedIn()) {
    require_once('../classes/Drivers.php');
    $drivers = new Drivers();

    if ($_POST) {
        $id = $_POST['id'];
        $lat = $_POST['lat'];
        $lon = $_POST['lon'];

        $list = $drivers->setDriver($id, $lat, $lon);
        echo json_encode($list);
    } else {
        $list = $drivers->getDrivers();
        echo json_encode($list);
    }
} else {
    echo "NOT_LOGGED_IN";
}



