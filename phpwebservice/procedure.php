<?php

/**
 * select anything from database without conditions
 * @author: Norbert Kozera
 */
require_once('utils/dbUtil.php');

header('Content-Type: application/json');

        $pName = $_POST["pName"];
        $pPamams = $_POST["pPamams"];

        $object = new dbUtil();
        $getString= $object -> callProcedure($pName, $pPamams);

