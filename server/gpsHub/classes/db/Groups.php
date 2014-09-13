<?php

class Groups {
    public function __construct()
    {
        require_once('utils/DBHelper.php');
    }

    public function createGroup()
    {
        return DBHelper::insert("INSERT INTO `groups` VALUES ()");
    }

    public function modifyGroup($id, $data)
    {
        return DBHelper::run("UPDATE `groups` SET
            `name` = '" . $data['name'] . "'
             WHERE `group_id` = " . $id);
    }

    public function deleteGroup($id)
    {
        return DBHelper::run("DELETE FROM `groups` WHERE `group_id` = " . $id);
    }

    public function getAllGroups()
    {
        return DBHelper::getAssoc("SELECT * FROM `groups`");
    }

    public function getGroup($group_id) {
        return DBHelper::getFirst("SELECT * FROM `groups` WHERE `group_id` = " . $group_id);
    }

    public function getGroups($driver_id)
    {
        return DBHelper::getAssoc("SELECT * FROM `drivers_groups` WHERE `driver_id` = " . $driver_id);
    }

    public function setGroups($driver_id, $groupList)
    {
        DBHelper::run("DELETE FROM `drivers_groups` WHERE `driver_id` = " . $driver_id);
        foreach ($groupList as $group_id) {
            DBHelper::run("INSERT INTO `drivers_groups` SET `group_id` = " . $group_id . ", `driver_id` = " . $driver_id);
        }
    }
} 