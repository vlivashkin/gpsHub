<?php

class DriverManager {
    public function createDriver()
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);

        $random = rand();

        $query = "INSERT INTO `driver` (`name`, `status`) VALUE ('" . $random . "', 'unconfirmed')";
        $mysqli->query($query);

        $query = "SELECT * FROM `driver` WHERE `name` = '" . $random . "'";
        $result = $mysqli->query($query);
        if ($result->num_rows > 0) {
            $driver = $result->fetch_array(MYSQLI_ASSOC);
            return $driver['driver_id'];
        }

        return NULL;
    }

    public function modifyDriver($id, $data)
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "UPDATE `driver` SET `name` = '" . $data['name'] . "', `alias` = '" . $data['alias'] . "',
            `phone_number` = '" . $data['phone_number'] . "', `vehile_num` = '" . $data['vehile_num'] . "',
            `vehile_description` = '" . $data['vehile_description'] . "' WHERE `driver_id` = " . $id;
        $mysqli->query($query);

        return true;
    }

    public function deleteDriver($id)
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "DELETE FROM `driver` WHERE `driver_id` = " . $id;
        $mysqli->query($query);

        return true;
    }

    public function isTrueHash($company_hash)
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "SELECT * FROM `company` WHERE `company_hash` = '" . $company_hash . "'";
        $result = $mysqli->query($query);
        if ($result->num_rows > 0)
            return true;

        return false;
    }

    public function isUnconfirmed($id)
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "SELECT * FROM `driver` WHERE driver_id = " . $id;
        $result = $mysqli->query($query);
        if ($result->num_rows > 0) {
            $driver = $result->fetch_array(MYSQLI_ASSOC);
            return $driver->status == 'unconfirmed';
        }

        return false;
    }
} 