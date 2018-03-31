<?php

require_once('utils/dbUtil.php');

header('Content-Type: application/json');


$selection = $_POST["selection"];
$table = $_POST["table"];
$condition = $_POST["condition"];

$object = new dbUtil();
$getString = $object->selectFromTableWithConditions($selection, $table, $condition);

echo $getString;


if ($getString == "{\"selection\":null}") {
    $file = 'error.log';

    $current = file_get_contents($file);

    $current .= "\n" . time() . "selectFromTableWithConditions($selection, $table, $condition);  ------   " . $getString . "-------\r\n";

    file_put_contents($file, $current);

}