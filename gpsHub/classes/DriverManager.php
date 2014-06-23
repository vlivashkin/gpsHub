<?php

class DriverManager {
    public function createDriver()
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $random = rand(0, 9999);

        $query = "INSERT INTO `driver` (`confirmed`, `rand`) VALUE (0, " . $random . ")";
        $mysqli->query($query);

        $query = "SELECT * FROM `driver` WHERE `rand` = " . $random;
        $result = $mysqli->query($query);
        if ($result->num_rows > 0) {
            $driver = $result->fetch_array(MYSQLI_ASSOC);
            $this->updateListVersion();
            return $driver['driver_id'];
        }

        return NULL;
    }

    public function modifyDriver($id, $data)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "UPDATE `driver` SET
            `name` = '" . $data['name'] . "',
            `alias` = '" . $data['alias'] . "',
            `phone_number` = '" . $data['phone_number'] . "',
            `vehile_num` = '" . $data['vehile_num'] . "',
            `vehile_description` = '" . $data['vehile_description'] . "'
             WHERE `driver_id` = " . $id;
        $mysqli->query($query);
        $this->updateListVersion();
        return true;
    }

    public function deleteDriver($id)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "DELETE FROM `driver` WHERE `driver_id` = " . $id;
        $mysqli->query($query);
        $this->updateListVersion();
        return true;
    }

    public function confirmDriver($id)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "UPDATE `driver` SET `confirmed` = 1 WHERE `driver_id` = " . $id;
        $mysqli->query($query);
        $this->updateListVersion();
        return true;
    }

    public function isTrueHash($company_hash)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "SELECT * FROM `company` WHERE `company_hash` = '" . $company_hash . "'";
        $result = $mysqli->query($query);

        return $result->num_rows > 0;
    }

    public function isUnconfirmed($id)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "SELECT * FROM `driver` WHERE driver_id = " . $id;
        $result = $mysqli->query($query);
        if ($result->num_rows > 0) {
            $driver = $result->fetch_array(MYSQLI_ASSOC);
            return $driver->confirmed == 0;
        }

        return false;
    }

    private function updateListVersion() {
        $temp_fh = fopen('./loc_version.tmp','wb') or die($php_errormsg);
        fputs($temp_fh, microtime(true));
        fclose($temp_fh) or die($php_errormsg);
    }
} 