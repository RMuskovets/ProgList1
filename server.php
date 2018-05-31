<?php

function put_data() {
    $DATABASE = 'php';
    $MYSQL_PASSWORD = '';
    $MYSQL_USERNAME = 'root';
    $MYSQL_HOST = '/';
    $apps = $_POST['data'];
    $apps = urldecode($apps);
    $compname = $_POST['compName'];
    $compname = urldecode($compname);
    $compip = $_POST['ip'];
    $compip = urldecode($compip);
    $mysqli = new mysqli($MYSQL_HOST, $MYSQL_USERNAME, $MYSQL_PASSWORD, $DATABASE);
    $mysqli->query("insert into applications (id, apps, compname) values ($compip, $apps, $compname);");
}
if ($_POST) {
    put_data();
}
?>

<div style="text-align: center;"><h1 style="color: red">YOU MUSTN'T USE THIS FILE</h1></div>