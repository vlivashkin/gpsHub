<?php

class Drivers
{
    public function init()
    {
        $this->setDriver(101, '55.75167', '37.61778');
        $this->setDriver(102, '53.75167', '34.61778');
    }

    public function setDriver($id, $lat, $lng)
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "UPDATE `driver` SET
            `lat` = '" . $lat . "',
            `lng` = '" . $lng . "',
            `last_activity` = '" . time() . "'
             WHERE `driver_id` = " . $id;

        $mysqli->query($query);
    }

    public function getDrivers()
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "SELECT * FROM `driver`";
        $drivers = $mysqli->query($query);

        return $drivers;
    }

    public function getDriversLocation()
    {
        require_once('SQLConfig.php');
        $sqlconfig = new SQLConfig();
        $mysqli = $sqlconfig->getMysqli();
        $query = "SELECT `driver_id`, `lat`, `lng`, `last_activity` FROM `driver`";
        $drivers = [];
        if ($result = $mysqli->query($query)) {
            while ($row = $result->fetch_assoc()) {
                array_push($drivers, [
                    "id" => $row["driver_id"],
                    "lat" => $row["lat"],
                    "lng" => $row["lng"],
                    "time" => $row["last_activity"]
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
}