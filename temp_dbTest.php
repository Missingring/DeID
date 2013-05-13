<?php
	require_once('iim_fns.php');
	$conn=db_connect();
	$result=$conn->query("select * from Groups where groupID='1'");
	$obj=$result->fetch_object();
	echo $obj->groupName;
?>