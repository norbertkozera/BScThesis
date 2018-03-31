<?php


class dbConn
{

    protected function dbConnect()
    {

        $conn = oci_connect('musclesman', 'Inz$_%n@kozera!2015', 'localhost:1521/XE');

        if (!$conn) {
            $e = oci_error();
            trigger_error(htmlentities($e['message'], ENT_QUOTES), E_USER_ERROR);
        } else {
            return $conn;
        }
    }

    protected function dbCloce($conn)
    {
        oci_close($conn);
    }
}

