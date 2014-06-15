<?php

class User
{
    private $login = false;
    private $email;
    private $name;
    private $companyId;
    private $companyName;

    public function __construct()
    {
        session_start();
        if (isset($_SESSION['email'])) {
            require_once('SQLConfig.php');
            $sqlconfig = new SQLConfig();
            $mysqli = $sqlconfig->getMysqli();
            $query = "SELECT * FROM `user` WHERE `email` = '" . $_SESSION['email'] . "'";
            $result = $mysqli->query($query);
            if ($result->num_rows > 0) {
                $user = $result->fetch_array(MYSQLI_ASSOC);

                $this->login = true;
                $this->email = $user['email'];
                $this->name = $user['name'];
                $this->companyId = $user['company_id'];

                $query = "SELECT `name` FROM `company` WHERE `company_id` = '" . $this->companyId . "'";
                $result = $mysqli->query($query);
                $company = $result->fetch_array(MYSQLI_ASSOC);
                $this->companyName = $company['name'];
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

    public function getCompanyId()
    {
        return $this->companyId;
    }

    public function getCompanyName()
    {
        return $this->companyName;
    }
}