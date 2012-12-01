<?php

/*
 * Following code will list all the friends
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$twitterID=$_POST['twitterID'];
// get all friends from friends table
$result = mysql_query("SELECT *FROM friends WHERE twitterID=$twitterID") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // friends node
    $response["friends"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $friend = array();
        $friend["pid"] = $row["pid"];
        $friend["twitterID"] = $row["twitterID"];
        $friend["twitterFriend"] = $row["twitterFriend"];
        $friend["twitterFriendImg"] = $row["twitterFriendImg"];
        $friend["created_at"] = $row["created_at"];
        $friend["updated_at"] = $row["updated_at"];



        // push single friend into final response array
        array_push($response["friends"], $friend);
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
