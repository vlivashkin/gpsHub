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
            1 => [
                "lat" => 37.61778,
                "lng" => 55.75167,
                "time" => time()
            ],
            2 => [
                "lat" => 34.61778,
                "lng" => 53.75167,
                "time" => time()
            ]
        ];

        $_SESSION['drivers'] = $drivers;
    }

    public function setDriver($id, $lat, $lng)
    {
        $drivers = $_SESSION['drivers'];

        $drivers[$id]["lat"] = $lat;
        $drivers[$id]["lng"] = $lng;
        $drivers[$id]["time"] = time();

        $_SESSION['drivers'] = $drivers;

        return $drivers;
    }

    public function getDrivers()
    {
        $drivers = $_SESSION['drivers'];
        return $drivers;
    }
} 