<?php


require_once('utils/dbUtil.php');

header('Content-Type: application/json');

//$selection, $table, $condition

$table = $_POST["table"];
$fields = $_POST["fields"];
$values = $_POST["values"];

$object = new dbUtil();
$getString = $object->insert($table, $fields, $values);
print $getString;


$file = 'error.log';

$current = file_get_contents($file);

$current .= "\n" . time() . "insert($table, $fields, $values);  ------   " . $getString . "-------\r\n";

file_put_contents($file, $current);