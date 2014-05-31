<?php

class Rest
{
    public function post() {
        $name = (isset($_POST['name']) ? $_POST['name'] : '');
        echo 'Hello ' . htmlspecialchars($name) . '!';
    }
}