<?php


$response = array();

if(isset($_POST['email']) && isset($_POST['photoref']))
{
	$photoref=$_POST['photoref'];
	$email=$_POST['email'];
	$comment=$_POST['comment'];

	//connect
	require_once 'include/DB_Connect.php';
	$db = new DB_CONNECT();
	$db->connect();
	

	if($email&&$comment)
	{
		//$user=$db->
		$insert=mysql_query("INSERT INTO comment(photoref,email,comment) VALUES('$photoref','$email','$comment')");
		$response["success"]=1;
		$response["message"]="commented";
		echo json_encode($response);
	}
	else
	{
		//$response["success"] = 0;
		$response["message"] = "comment error";
		echo json_encode($response);
	}
}
else
{
	$response["success"]=0;
	$response["message"]="missing requirment item";//"missing requirement itemz";
	echo json_encode($response);
}
?>