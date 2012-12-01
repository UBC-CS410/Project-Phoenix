<?php


$response = array();

if(isset($_POST['twid']) && isset($_POST['twuserid']))
{
	$twid=$_POST['twid'];
	$twuserid=$_POST['twuserid'];
	$comment=$_POST['comment'];

	//connect
	require_once 'include/DB_Connect.php';
	$db = new DB_CONNECT();
	$db->connect();
	

	if($twuserid&&$comment)
	{
		//$user=$db->
		$insert=mysql_query("INSERT INTO tcomment(twid,twuserid,comment) VALUES('$twid','$twuserid','$comment')");
		$response["success"]=1;
		$response["message"]="commented";
		echo json_encode($response);
	}
	else
	{
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