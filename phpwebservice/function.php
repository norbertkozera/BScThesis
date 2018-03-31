<?php

require_once('utils/dbUtil.php');

header('Content-Type: application/json');

$fName = $_POST["fName"];
$fPamams = $_POST["fPamams"];

$object = new dbUtil();
$getString = $object->callFunction($fName, $fPamams);

print $getString;


if ($getString == "{\"return\":null}") {
    $file = 'error.log';

    $current = file_get_contents($file);

    $current .= "callFunction($fName, $fPamams)++++" . $getString . "\r\n";
    $current .= "fPamams++++\"" . $fPamams . "\"\r\n";

    file_put_contents($file, $current);

}



