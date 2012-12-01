<?php
$response = array();

require_once 'include/DB_Connect.php';
	$db = new DB_CONNECT();
	$db->connect();
$photoref=$_POST['photoref'];
$result=mysql_query("SELECT * FROM comment WHERE photoref = '$photoref'") or die(mysql_error());


$no_of_rows = mysql_num_rows($result);
if($no_of_rows>0)
{
	$response['success']=1;
	$response['comments']=array();
	while($row=mysql_fetch_array($result))
	{
		$row_array=array();
		$row_array['email']=$row['email'];
		$row_array['comment']=$row['comment'];
		array_push($response['comments'],$row_array);
	}
	 echo json_encode($response);
}
else
{
	$response['success']=0;
	echo json_encode($response);
}
?>