<?php


require_once('utils/dbUtil.php');

header('Content-Type: application/json');

$object = new dbUtil();

$getString = $object->selectFromTableWithConditions('distinct user_id', 'APPUSERLOGININFO', 'SEEN = \'true\' and SUCCESS = \'true\'');

echo $getString;


if ($getString == "{\"selection\":null}") {
    $file = 'error.log';

    $current = file_get_contents($file);

    $current .= "\n" . time() . "--------firstLogin!  ------   " . $getString . "-------\r\n";

    file_put_contents($file, $current);

}