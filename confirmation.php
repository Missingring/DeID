<?php
	require_once('iim_fns.php');
	
	// Passkey that got from link
	$passkey=$_GET['passkey'];
	
	// retrieve data from temp table where row that match this passkey
	$conn=db_connect();
	$result=$conn->query("select * from Account_Temp where confirmCode='".$passkey."'");
	
	if($result){
		if($result->num_rows==1){
			$rows=$result->fetch_array();
			$username=$rows['username'];
			$email=$rows['email'];
			$password=$rows['password'];
			$role=$rows['authorityLevel'];
			$groupID=$rows['groupID'];
			
			$result2 = $conn->query("insert into Account values (NULL, '".$username."', '".$email."', '".$password."', '".$role."')");
			if($groupID>0){
				$accountID = $conn->insert_id;
				$result2_1 = $conn->query("insert into Group_Account values ('".$groupID."', '".$accountID."')");
			}
			if (!$result2){
				throw new Exception("Could not register you in database - please try again later.");
			}
			do_html_header("Activate Account");
			echo "Registration Successful!";
			echo "<script language=\"javascript\">window.setTimeout(\"this.location.href=\'http://iim.clemson.edu\'\",3000)</script>";
			do_html_footer();
			$result3 = $conn->query("delete from Account_Temp where confirmCode='".$passkey."'");
			if (!$result3){
				throw new Exception("Could not delete the temporary account");
			}
		} 
	} else {
		throw new Exception("Wrong Confirmation code");
	}
?>