<?php

class DriverManager {
    public function createDriver($company_id)
    {
        $random = rand();

        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "INSERT INTO `driver` (`company_id`, `name`, `status`) VALUE (" . $company_id . ", '" . $random . "', 'unconfirmed')";
        $mysqli->query($query);

        $query = "SELECT * FROM `driver` WHERE `company_id` = " . $company_id . " AND `name` = '" . $random . "'";
        $result = $mysqli->query($query);
        if ($result->num_rows > 0) {
            $driver = $result->fetch_array(MYSQLI_ASSOC);
            return $driver['driver_id'];
        }

        return NULL;
    }

    public function modifyDriver($company_id, $id, $data)
    {

    }

    public function deleteDriver($company_id, $id)
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "DELETE FROM `drivers` WHERE `company_id` = " . $company_id . "AND `driver_id` = '" . $id . "'";
        $mysqli->query($query);

        $query = "SELECT * FROM `drivers` WHERE `company_id` = " . $company_id . "AND `driver_id` = '" . $id . "'";
        $result = $mysqli->query($query);
        if ($result->num_rows == 0) {
            return true;
        }

        return false;
    }

    public function getCompanyId($company_hash)
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "SELECT * FROM `company` WHERE `company_hash` = '" . $company_hash . "'";
        $result = $mysqli->query($query);
        if ($result->num_rows > 0) {
            $company = $result->fetch_array(MYSQLI_ASSOC);
            return $company['company_id'];
        }

        return NULL;
    }

    public function isUnconfirmed($company_id, $id)
    {
        require_once('SQLConfig.php');
        $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
        $query = "SELECT * FROM `driver` WHERE `company_id` = " . $company_id . " AND driver_id = " . $id;
        $result = $mysqli->query($query);
        if ($result->num_rows > 0) {
            $driver = $result->fetch_array(MYSQLI_ASSOC);
            return $driver->status == 'unconfirmed';
        }

        return false;
    }
} 