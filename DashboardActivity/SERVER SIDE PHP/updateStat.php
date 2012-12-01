<?php


$response = array();

if(isset($_POST['stat']) && isset($_POST['tuser']) && isset($_POST['tid']))
{
	$stat=$_POST['stat'];
	$tuser=$_POST['tuser'];
	$tid=$_POST['tid'];
	$lat=$_POST['lat'];
	$lon=$_POST['lon'];
	$imgurl=$_POST['imgurl'];
	
	//connect
	require_once 'include/DB_Connect.php';
	$db = new DB_CONNECT();
	$db->connect();
	
	$hasUser=mysql_query("SELECT * FROM resentstat WHERE tuser='$tuser'");
	$no_of_row = mysql_num_rows($hasUser);
	if($no_of_row>0){
		
		$result=mysql_query("UPDATE resentstat SET tid='$tid', lat='$lat', lon='$lon', stat='$stat', imgurl='$imgurl' WHERE tuser=$tuser");
		$response["success"]=1;
		$response["message"]="updated";
		echo json_encode($response);


	}else{

		$result=mysql_query("INSERT INTO resentstat(tuser,tid,stat,lat,lon,imgurl) VALUES('$tuser','$tid','$stat','$lat','$lon','$imgurl')");
		if($result){
			$response["success"]=1;
			$response["message"]="inserted";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["message"]=$tid;
			echo json_encode($response);
		}



	}
}
else
{
	$response["success"]=0;
	$response["message"]="missing requirment item";//"missing requirement itemz";
	echo json_encode($response);
}
?>