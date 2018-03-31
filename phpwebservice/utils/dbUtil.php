<?php

require_once('conn.php');

class dbUtil extends dbConn
{


    function selectFromTable($selection, $table)
    {
        $connect = new dbConn();

        $connectionResource = $connect->dbConnect();


        $sqlString = "select $selection from $table";

        $stid = oci_parse($connectionResource, $sqlString);
        oci_execute($stid);

        while ($row = oci_fetch_array($stid, OCI_ASSOC + OCI_RETURN_NULLS)) {
            if ($row != null)
                $output[] = $row;
        }

        $encode = json_encode(array("selection" => $output));

        $connect->dbCloce($connectionResource);

        if ($encode == "{\"selection\":null}") {
            $file = 'error.log';

            $current = file_get_contents($file);

            $current .= "SQL CODE: " . $sqlString;

            file_put_contents($file, $current);

        }
        return $encode;

    }


    function insert($table, $fields, $values)
    {
        $connect = new dbConn();

        $connectionResource = $connect->dbConnect();


        $sqlString = "insert into $table($fields) values ($values)";

        $stid = oci_parse($connectionResource, $sqlString);
        oci_execute($stid);


        $connect->dbCloce($connectionResource);

        return $sqlString;


    }

    function update($table, $field, $value, $condition)
    {
        $connect = new dbConn();

        $connectionResource = $connect->dbConnect();


        $sqlString = "update $table set $field = $value where $condition";

        $stid = oci_parse($connectionResource, $sqlString);
        oci_execute($stid);


        $connect->dbCloce($connectionResource);

        return $sqlString;


    }


    function selectFromTableWithConditions($selection, $table, $condition)
    {
        $connect = new dbConn();

        $connectionResource = $connect->dbConnect();

        $sqlString = "select $selection from $table where $condition";

        $stid = oci_parse($connectionResource, $sqlString);
        oci_execute($stid);

        while ($row = oci_fetch_array($stid, OCI_ASSOC + OCI_RETURN_NULLS)) {
            if ($row != null)
                $output[] = $row;
        }

        $encode = json_encode(array("selection" => $output));

        $connect->dbCloce($connectionResource);
        if ($encode == "{\"selection\":null}") {
            $file = 'error.log';

            $current = file_get_contents($file);

            $current .= "SQL CODE: " . $sqlString;

            file_put_contents($file, $current);

        }
        return $encode;

    }


    function callFunction($functionName, $conditions)
    {
        $connect = new dbConn();

        $connectionResource = $connect->dbConnect();

        $sqlString = "begin :r := $functionName($conditions); end;";

        $stid = oci_parse($connectionResource, $sqlString);

        if (!$stid) {
            $e = oci_error($connectionResource);  // For oci_parse errors pass the connection handle
            trigger_error(htmlentities($e['message']), E_USER_ERROR);
        }

        oci_bind_by_name($stid, ':r', $output, 10000);
        oci_execute($stid);


        $encode = json_encode(array("return" => $output));


        $connect->dbCloce($connectionResource);


        if ($encode == "{\"return\":null}") {
            $file = 'error.log';

            $current = file_get_contents($file);

            $current .= "SQL CODE: " . $sqlString;

            file_put_contents($file, $current);

        }

        return $encode;

    }


    function callProcedure($procedureName, $conditions)
    {
        $connect = new dbConn();

        $connectionResource = $connect->dbConnect();

        $sqlString = "BEGIN $procedureName($conditions); END;";

        $stid = oci_parse($connectionResource, $sqlString);

        if (!$stid) {
            $e = oci_error($connectionResource);  // For oci_parse errors pass the connection handle
            trigger_error(htmlentities($e['message']), E_USER_ERROR);
        }

        oci_execute($stid);

        $connect->dbCloce($connectionResource);


    }


    function selectFromNestedTable($nestedFunction, $nestedData)
    {
        $connect = new dbConn();

        $connectionResource = $connect->dbConnect();


        $sqlString = "select * from table($nestedFunction($nestedData))";

        $stid = oci_parse($connectionResource, $sqlString);
        oci_execute($stid);

        while ($row = oci_fetch_array($stid, OCI_ASSOC + OCI_RETURN_NULLS)) {
            if ($row != null)
                $output[] = $row;
        }

        $encode = json_encode(array("selection" => $output));

        $connect->dbCloce($connectionResource);

        if ($encode == "{\"selection\":null}") {
            $file = 'error.log';

            $current = file_get_contents($file);

            $current .= "SQL CODE: " . $sqlString;

            file_put_contents($file, $current);

        }

        return $encode;

    }
}


