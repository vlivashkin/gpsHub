<?php

class Drivers {
    protected static $_instance;
    private $drivers;

    private function __construct() {
        $this->drivers = [
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
    }

    private function __clone(){
    }

    public static function getInstance() {
        if (null === self::$_instance) {
            self::$_instance = new self();
        }
        return self::$_instance;
    }

    public function setDriver($id, $lat, $lng) {
        $this->drivers[$id]->lat = $lat;
        $this->drivers[$id]->lng = $lng;
        $this->drivers[$id]->time = time();
    }

    public function getDrivers() {
        return $this->drivers;
    }
} 