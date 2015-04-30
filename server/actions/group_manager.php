<?php

require_once('../classes/db/Groups.php');
require_once('../classes/Helper.php');

Helper::checkAuthorisation();

if ($_GET) {
    require_once('../classes/db/utils/SQLConfig.php');
    require_once('../classes/SSP.php');

    $table = 'groups';
    $primaryKey = 'group_id';
    $columns = array(
        array('db' => 'group_id', 'dt' => 0),
        array('db' => 'name', 'dt' => 1)
    );
    $sql_details = SQLConfig::getSqlDetails();
    echo json_encode(
        SSP::simple($_GET, $sql_details, $table, $primaryKey, $columns)
    );
} else if ($_POST) {
    Helper::checkPostParameter('action');

    $groupManager = new Groups();
    switch ($_POST['action']) {
        case 'create':
            $result = $groupManager->createGroup();
            if (isset($result) && $result) {
                echo $result;
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
        case 'modify':
            Helper::checkPostParameter('group_id');

            $result = $groupManager->modifyGroup($_POST['group_id'], $_POST);
            if (isset($result) && $result) {
                echo 'SUCCESS';
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
        case 'delete':
            Helper::checkPostParameter('group_id');

            $result = $groupManager->deleteGroup($_POST['group_id']);
            if (isset($result) && $result) {
                echo 'SUCCESS';
            } else {
                echo 'UNKNOWN_ERROR';
            }
            break;
    }
}