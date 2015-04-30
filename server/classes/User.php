<?php

class User
{
    private $user;
    private $company;
    private $isLoggedIn = false;

    public function __construct()
    {
        require_once('db/utils/DBHelper.php');
        if (session_id() == "")
            session_start();
        if (isset($_SESSION['login']) && $_SESSION['login']) {
            $this->user = DBHelper::getFirst("SELECT * FROM `users` WHERE `login` = '" . $_SESSION['login'] . "'");
            if ($this->user != NULL) {
                $this->company = DBHelper::getFirst("SELECT `name` FROM `companies` WHERE `company_id` = '" . $this->user['company_id'] . "'");
                $this->isLoggedIn = true;
            }
        }
    }

    public function signIn($login, $hash)
    {
        $hash256 = hash('sha256', $hash . $login);
        $result = DBHelper::getFirst("SELECT * FROM `users` WHERE `login` = '" . $login . "' AND `password` = '" . $hash256 . "'");
        if ($result != NULL) {
            if (session_id() == "")
                session_start();
            $_SESSION['login'] = $login;
            DBHelper::run("UPDATE `users` WHERE `login` = '" . $login . "' SET `last_activity` = " . time());
            return "SUCCESS";
        }

        return "FAILURE";
    }

    public function getLogin()
    {
        return $this->user['login'];
    }

    public function getName()
    {
        return $this->user['name'];
    }

    public function getGroupID()
    {
        return $this->user['group_id'];
    }

    public function getCompanyName()
    {
        return $this->company['name'];
    }

    public function isLoggedIn()
    {
        return $this->isLoggedIn;
    }

    public function isLoggedAsAdmin()
    {
        return $this->isLoggedIn && $this->user['isadmin'] == 'true';
    }
}