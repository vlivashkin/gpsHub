<?php

require_once('../classes/db/Drivers.php');
require_once('../classes/Helper.php');

Helper::checkAuthorisation();

if ($_GET) {
    require_once('../classes/SSP.php');
    require_once('../classes/db/utils/SQLConfig.php');

    $table = 'drivers';
    $primaryKey = 'driver_id';
    $columns = array(
        array('db' => 'driver_id', 'dt' => 0),
        array('db' => 'name', 'dt' => 1),
        array('db' => 'alias', 'dt' => 2),
        array('db' => 'phone_number', 'dt' => 3),
        array('db' => 'vehicle_num', 'dt' => 4),
        array('db' => 'vehicle_description', 'dt' => 5)
    );
    $sql_details = SQLConfig::getSqlDetails();
    echo json_encode(
        SSP::simple($_GET, $sql_details, $table, $primaryKey, $columns)
    );
} else if ($_POST) {
    Helper::checkPostParameter('action');

    $driverManager = new Drivers();
    switch ($_POST['action']) {
        case 'create':
            $result = $driverManager->createDriver();
            if (isset($result) && $result) {
                echo $result;
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
        case 'modify':
            Helper::checkPostParameter('driver_id');
            Helper::checkPostParameter('groups');

            require_once('../classes/db/Groups.php');
            $groupManager = new Groups();

            $result = $driverManager->modifyDriver($_POST['driver_id'], $_POST);
            $groupManager->setGroups($_POST['driver_id'], $_POST['groups']);
            if (isset($result) && $result) {
                echo 'SUCCESS';
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
        case 'delete':
            Helper::checkPostParameter('driver_id');

            $result = $driverManager->deleteDriver($_POST['driver_id']);
            if (isset($result) && $result) {
                echo 'SUCCESS';
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
    }
}