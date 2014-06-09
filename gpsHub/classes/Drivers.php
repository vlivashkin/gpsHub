<?php

class Drivers
{
    public function __construct()
    {
        if (!isset($_SESSION))
            session_start();
    }

    public function init()
    {
        $drivers = [
            101 => [
                "lat" => 37.61778,
                "lon" => 55.75167,
                "time" => time()
            ],
            102 => [
                "lat" => 34.61778,
                "lon" => 53.75167,
                "time" => time()
            ]
        ];

        $_SESSION['drivers'] = $drivers;
    }

    public function setDriver($id, $lat, $lon)
    {

            $drivers = $_SESSION['drivers'];

            $drivers[$id]["lat"] = $lat;
            $drivers[$id]["lon"] = $lon;
            $drivers[$id]["time"] = time();

            $_SESSION['drivers'] = $drivers;
    }

    public function getDrivers()
    {
        $drivers = $_SESSION['drivers'];
        return $drivers;
    }
} 