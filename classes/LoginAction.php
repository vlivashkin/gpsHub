<?php

class LoginAction
{
    private $post;

    public function __construct($post)
    {
        $this->post = $post;
        require_once('SQLConfig.php');
    }

    public function signIn() {
        if ($this->post) {
            $name = $this->post['email'];
            if (!get_magic_quotes_gpc())
                $password = md5($this->post['password']);
            else
                $password = md5(stripslashes($this->post['password']));


            $query = "SELECT * FROM `users` WHERE `e-mail` = '" . $name . "' AND `pass` = '" . $password . "'";
            $mysqli = new mysqli(SQLConfig::SERVERNAME, SQLConfig::USER, SQLConfig::PASSWORD, SQLConfig::DATABASE);
            $result = $mysqli->query($query);

            if ($result->num_rows) {
                session_start();
                $_SESSION['name'] = $name;
                $_SESSION['password'] = $password;
                setcookie('name', $name, time() + 86400 * 30 * 12);
                setcookie('pass', $password, time() + 86400 * 30 * 12);
                return true;
            }
        }
        return false;
    }
}

?>

