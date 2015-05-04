<?php

class SQLConfig {
    const SERVERNAME = "";
    const USER = "";
    const PASSWORD = "";
    const DATABASE = "";
    private $mysqli;

    public function __construct() {
        $this->mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);

        if (mysqli_connect_errno()) {
            exit();
        }

        $this->mysqli->set_charset("utf8");
    }

    public static function getSqlDetails() {
        return array(
            'user' => SQLConfig::USER,
            'pass' => SQLConfig::PASSWORD,
            'db' => SQLConfig::DATABASE,
            'host' => SQLConfig::SERVERNAME
        );
    }

    public function __destruct() {
        $this->mysqli->close();
    }

    public function getMysqli() {
        return $this->mysqli;
    }
}
