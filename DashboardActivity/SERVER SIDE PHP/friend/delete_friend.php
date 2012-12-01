(<?php

/*
 * Following code will delete a friend from table
 * A friend is identified by friend id (pid)
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['twitterID'])&&isset($_POST['twitterFriend'])) {
    $twitterID=$_POST['twitterID'];
    $twitterFriend=$_POST['twitterFriend'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql update row with matched pid
    $result = mysql_query("DELETE FROM friends WHERE  twitterID=$twitterID and twitterFriend=$twitterFriend");
    
    // check if row deleted or not
    if (mysql_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "friend successfully deleted";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // no friend found
        $response["success"] = 0;
        $response["message"] = "No friend found";

        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>