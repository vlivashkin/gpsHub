<?php

class Users {
    public function __construct()
    {
        require_once('utils/DBHelper.php');
    }

    public function createUser()
    {
        return DBHelper::insert("INSERT INTO `users`");
    }

    public function modifyUser($id, $data)
    {
        return DBHelper::run("UPDATE `users` SET
            `company_id` = '" . $data['company_id'] . "',
            `email` = '" . $data['email'] . "',
            `name` = '" . $data['name'] . "',
            `user_type` = '" . $data['user_type'] . "'
             WHERE `driver_id` = " . $id);
    }

    public function deleteDriver($id)
    {
        return DBHelper::run("DELETE FROM `users` WHERE `user_id` = " . $id);
    }

    public function getAllUsers() {
        return DBHelper::getAssoc("SELECT `user_id`, `company_id`, `email`, `name`, `last_activity`, `user_type` FROM `users`");
    }
} 