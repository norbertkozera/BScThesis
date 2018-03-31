<?php

require_once('utils/dbUtil.php');

header('Content-Type: application/json');

$pName = $_POST["pName"];
$pPamams = $_POST["pPamams"];

$object = new dbUtil();
$getString = $object->selectFromNestedTable($pName, $pPamams);

print $getString;

if ($getString == "{\"selection\":null}") {
    $file = 'error.log';

    $current = file_get_contents($file);

    $current .= "selectFromNestedTable($pName, $pPamams)++++" . $getString . "\r\n";

    file_put_contents($file, $current);

}

