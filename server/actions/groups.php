<?php

require_once('../classes/db/Groups.php');
require_once('../classes/Helper.php');

Helper::checkAuthorisation();

if ($_GET) {
    $groups = new Groups();
    switch ($_GET['type']) {
        case 'id':
            Helper::checkGetParameter('id');

            $group = $groups->getGroup($_GET['id']);
            echo json_encode($group);
            break;
        case 'driver':
            Helper::checkGetParameter('driver_id');

            $response = [
                'list' => $groups->getGroups($_GET['driver_id'])
            ];
            echo json_encode($response);
            break;
        case 'all':
            $response = [
                'list' => $groups->getAllGroups()
            ];
            echo json_encode($response);
            break;
    }
}