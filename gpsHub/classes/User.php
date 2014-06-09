<?php
class User
{
    private $login = false;
    private $email;
    private $name;
    private $drivers;

    public function __construct()
    {
        require_once('SQLConfig.php');
        session_start();
        if (isset($_SESSION['email'])) {
            $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
            $query = "SELECT `name`, `company_id` FROM `user` WHERE `email` = '" . $_SESSION['email'] . "'";
            $result = $mysqli->query($query);
            $user = $result->fetch_array(MYSQLI_ASSOC);
            if ($result->num_rows > 0) {
                $this->login = true;
                $this->email = $_SESSION['email'];
                $this->name = $user['name'];

                $query = "SELECT `name` FROM `company` WHERE `company_id` = '" . $user['company_id'] . "'";
                $result = $mysqli->query($query);
                $organization = $result->fetch_array(MYSQLI_ASSOC);
                $this->companyName = $organization['name'];

                $query = "SELECT * FROM `driver` WHERE `company_id` = '" . $user['company_id'] . "'";
                $result = $mysqli->query($query);
                $this->drivers = $result;
            }
        }
    }

    public function isLoggedIn()
    {
        return $this->login;
    }

    public function getEmail()
    {
        return $this->email;
    }

    public function getName()
    {
        return $this->name;
    }

    public function getCompanyName()
    {
        return $this->companyName;
    }

    public function getDrivers()
    {
        return $this->drivers;
    }

}