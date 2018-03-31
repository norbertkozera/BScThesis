<?php

require_once('utils/dbUtil.php');

header('Content-Type: application/json');


$selection = $_POST["selection"];
$table = $_POST["table"];

$object = new dbUtil();
$getString = $object->selectFromTable($selection, $table);

echo $getString;


if ($getString == "{\"selection\":null}") {
    $file = 'error.log';

    $current = file_get_contents($file);

    $current .= "\n" . time() . "--------selectFromTable($selection, $table);  ------   " . $getString . "-------\r\n";

    file_put_contents($file, $current);

}