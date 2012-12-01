<?php


$response = array();

if(isset($_POST['stat']) && isset($_POST['tuser']) && isset($_POST['tid']))
{
	$stat=$_POST['stat'];
	$tuser=$_POST['tuser'];
	$tid=$_POST['tid'];
	$lat=$_POST['lat'];
	$lon=$_POST['lon'];

	
	
	

	//connect
	require_once 'include/DB_Connect.php';
	$db = new DB_CONNECT();
	$db->connect();
	

		$result=mysql_query("INSERT INTO status(tuser,tid,stat,lat,lon) VALUES('$tuser','$tid','$stat','$lat','$lon')");
		if($result){
			$response["success"]=1;
			$response["message"]="commented";
			echo json_encode($response);
		}
		else{
			$response["success"]=$stat;
			$response["message"]=$tid;
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