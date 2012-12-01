<?php

require_once 'include/DB_Functions.php';
    $db = new DB_Functions();
    $db->connect();

 $response=array();

if(isset($_POST['twitterID'])&&isset($_POST['twitterFriend']))
{
	$twitterID=$_POST['twitterID'];
	$twitterFriend=$_POST['twitterFriend'];
	$result=mysql_query("SELECT * FROM friends WHERE twitterID='$twitterID' AND twitterFriend='$twitterFriend' ");

	if(is_null($result)){
		$response['success']=1;
	}

	// $response['success']=1;
	// $response['return']=array();

	// while($row=mysql_fetch_array($result))
	// {
	// 	$response['success']=1;
	// 	$row_array=array();
	// 	$row_array['pid']=$row['pid'];
	// 	$row_array['twitterID']=$row['twitterID'];
	// 	$row_array['twitterFriend']=$row['twitterFriend'];
	// 	array_push($response['return'],$row_array);
	// }
	echo json_encode($response);

}
else
{
	$response['success']=0;
	echo json_encode($response);
}
?>