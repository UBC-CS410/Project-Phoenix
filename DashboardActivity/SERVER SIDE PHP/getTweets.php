<?php

/*
 * Following code will list all the friends
 */

// array for JSON response
$response = array();


// include db connect class
require_once 'include/DB_Connect.php';

// connecting to db
$db = new DB_CONNECT();
$db->connect();

// get all friends from friends table
$result = mysql_query("SELECT *FROM status") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // friends node
    $response["status"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $status = array();
        $status["statId"] = $row["statId"];
        $status["tuser"] = $row["tuser"];
        $status["tid"] = $row["tid"];
        $status["stat"] = $row["stat"];
        $status["lat"] = $row["lat"];
        $status["lon"] = $row["lon"];

        // push single status into final response array
        array_push($response["status"], $status);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no friends found
    $response["success"] = 0;
    $response["message"] = "No friends found";

    // echo no users JSON
    echo json_encode($response);
}
?>
