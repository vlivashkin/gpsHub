<?php

class Drivers
{
    public function init()
    {
        $this->setLocation(101, '55.75167', '37.61778', 'false');
        $this->setLocation(102, '53.75167', '34.61778', 'false');
    }

    public function setLocation($id, $lat, $lng, $busy)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "UPDATE `driver` SET
            `lat` = '" . $lat . "',
            `lng` = '" . $lng . "',
            `busy` = '" . $busy . "',
            `last_activity` = '" . time() . "'
             WHERE `driver_id` = " . $id;

        $mysqli->query($query);

        $this->updateLocationVersion();
    }

    public function getDrivers()
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "SELECT * FROM `driver` ORDER BY `confirmed`";
        $drivers = [];
        if ($result = $mysqli->query($query)) {
            while ($row = $result->fetch_assoc()) {
                array_push($drivers, $row);
            }
            $result->free();
        }

        return $drivers;
    }

    public function getDriversLocation()
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "SELECT `driver_id`, `lat`, `lng`, `busy`, `vehicle_num`, `last_activity` FROM `driver`";
        $drivers = [];
        if ($result = $mysqli->query($query)) {
            while ($row = $result->fetch_assoc()) {
                array_push($drivers, [
                    "id" => $row["driver_id"],
                    "lat" => $row["lat"],
                    "lng" => $row["lng"],
                    "busy" => $row["busy"],
                    "number" => $row["vehicle_num"],
                    "last_activity" => $row["last_activity"]
                ]);
            }
            $result->free();
        }

        return $drivers;
    }

    public function getDriver($id)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "SELECT * FROM `driver` WHERE `driver_id` = " . $id;
        if ($result = $mysqli->query($query)) {
            $driver = $result->fetch_array(MYSQLI_ASSOC);
            if ($result->num_rows > 0) {
                return $driver;
            }
        }
        return NULL;
    }

    public function getListVersion()
    {
        return file_get_contents('./list_version.tmp');
    }

    public function getLocationVersion()
    {
        return file_get_contents('./loc_version.tmp');
    }

    private function updateLocationVersion() {
        $temp_fh = fopen('./loc_version.tmp','wb') or die($php_errormsg);
        fputs($temp_fh, microtime(true));
        fclose($temp_fh) or die($php_errormsg);
    }
}