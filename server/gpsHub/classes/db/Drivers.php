<?php

class Drivers {
    public function __construct()
    {

        require_once('utils/DBHelper.php');
    }

    public function createDriver()
    {
        return DBHelper::insert("INSERT INTO `drivers` VALUES ()");
    }

    public function modifyDriver($id, $data)
    {
        return DBHelper::run("UPDATE `drivers` SET
            `name` = '" . $data['name'] . "',
            `alias` = '" . $data['alias'] . "',
            `phone_number` = '" . $data['phone_number'] . "',
            `vehicle_num` = '" . $data['vehicle_num'] . "',
            `vehicle_description` = '" . $data['vehicle_description'] . "'
             WHERE `driver_id` = " . $id);
    }

    public function deleteDriver($id)
    {
        return DBHelper::run("DELETE FROM `drivers` WHERE `driver_id` = " . $id);
    }

    public function getUserGroupDrivers()
    {
        $user = new User();
        $group_id = $user->getGroupID();
        if ($group_id > 0) {
            return DBHelper::getAssoc("
            SELECT d.*,
              if(now() - d.last_activity < 60, 1, 0) as online,
              if(now() - d.last_activity < 5*60, 1, 0) as wait
            FROM `drivers` d, `drivers_groups` g
            WHERE d.driver_id = g.driver_id AND g.`group_id` = " . $group_id . "
            ORDER BY online DESC, wait DESC;");
        } else {
            return DBHelper::getAssoc("
            SELECT *,
                if(now() - d.last_activity < 60, 1, 0) online,
                if(now() - d.last_activity < 5*60, 1, 0) wait
            FROM `drivers` d
            ORDER BY online DESC, wait DESC");
        }
    }

    public function getDriversLocation()
    {
        $user = new User();
        $group_id = $user->getGroupID();
        if ($group_id > 0) {
            return DBHelper::getAssoc("
            SELECT d.`driver_id`, d.`lat`, d.`lng`, d.`busy`, d.`accuracy`, d.`last_activity`
            FROM `drivers` d, `drivers_groups` g
            WHERE d.driver_id = g.driver_id AND g.`group_id` = " . $group_id);
        } else {
            return DBHelper::getAssoc("
            SELECT `driver_id`, `lat`, `lng`, `busy`, `accuracy`, `last_activity`
            FROM `drivers`");
        }

    }

    public function getDriver($id)
    {
        return DBHelper::getFirst("SELECT * FROM `drivers` WHERE `driver_id` = " . $id);
    }

    public function setLocation($id, $lat, $lng, $busy, $accuracy)
    {
        return DBHelper::run("UPDATE `drivers` SET
            `lat` = '" . $lat . "',
            `lng` = '" . $lng . "',
            `busy` = '" . $busy . "',
            `accuracy` = '" . $accuracy . "',
            `last_activity` = '" . time() . "'
             WHERE `driver_id` = " . $id);
    }
}