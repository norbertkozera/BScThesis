<?php

/**
 * select anything from database without conditions
 * @author: Norbert Kozera
 */
require_once('utils/dbUtil.php');

header('Content-Type: application/json');

$table = $_POST["table"];
$field = $_POST["field"];
$value = $_POST["value"];
$condition = $_POST["condition"];

$object = new dbUtil();
$getString= $object ->update($table, $field, $value, $condition);
print $getString;


    $file = 'error.log';

    $current = file_get_contents($file);

    $current .= "\n" . time() . "update($table, $field, $value, $condition);;  ------   " . $getString . "-------\r\n";

    file_put_contents($file, $current);

