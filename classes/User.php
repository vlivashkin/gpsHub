<?php

/**
 * User class
 */
class User
{
    private $id;
    private $mail;
    private $fullname;
    private $login = false;

    public function __construct()
    {
        require_once('SQLConfig.php');
        session_start();
        if (isset($_SESSION['name']) && isset($_SESSION['password'])) {
            $query = "SELECT `user_id`, `e-mail`, `fullname` FROM `users` WHERE `e-mail` = '" . $_SESSION['name'] . "' AND `pass` = '" . $_SESSION['password'] . "'";
            $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
            $result = $mysqli->query($query);

            $user = $result->fetch_array(MYSQLI_ASSOC);

            if ($result->num_rows > 0) {
                $this->login = true;
                $this->id = $user['user_id'];
                $this->mail = $user['e-mail'];
                $this->fullname = $user['fullname'];
            }
        }
    }

    public function __destruct()
    {

    }

    public function isLoggedIn()
    {
        return $this->login;
    }

    public function hasName()
    {
        if (!empty($this->fullname))
            return true;
        return false;
    }

    public function getId()
    {
        return (int)$this->id;
    }

    public function getFullname()
    {
        return $this->fullname;
    }

    public function getEmail()
    {
        return $this->mail;
    }
}

?>