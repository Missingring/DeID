<?php
	require_once('iim_fns.php');

	$username=$_POST['username'];
	$passwd=$_POST['passwd'];
	
	if($username && $passwd){
		try{
			iPad_login($username, $passwd);
			
			$conn = db_connect();
			
			$result = $conn->query("SELECT groupName, groupID FROM Group_Account, Account WHERE Account.accountID = Group_Account.accountID AND Account.username = '$username'");
			
			$arr=array();
			while($line = $result->fetch_array(MYSQLI_NUM)){
				//echo $line[0].' '.$line[1].'<br>';
				array_push($arr, array('name'=>$line[0], 'ID'=>$line[1]));
			}
			$proj_list=array('Count'=>count($arr), 'Projects'=>$arr);
			echo json_encode($proj_list);
		}
		catch(Exception $e){
			// unsuccessful login
			echo json_encode(array('Status' => 1, 'Message' => $e->getMessage()."Login Failed!"));
			//exit;
		}
	} else {
		// username or password missed
		echo json_encode(array('Status' => 1, 'Message' => 'username or password missed!'));
	}
?>