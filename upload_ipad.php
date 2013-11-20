<?php
	require_once('iim_fns.php');
	require_once('config_json.php');
	header('Content-Type: application/json');

	//parse_str(file_get_contents('php://input'),$_POST);
	
	// create short variable names
	$username=$_POST['username'];
	$passwd=$_POST['passwd'];
	$projID=$_POST['proj_ID'];
	//$passwd=sha1($passwd);
	
	if($username && $passwd){
		try{
			iPad_login($username, $passwd);
			
			// check project permission
			$conn = db_connect();
			
			$result_1 = $conn->query("SELECT * FROM Account WHERE username='".$username."'");
			$obj_1=$result_1->fetch_object();
			$accountID=$obj_1->accountID;
			
			$result_2 = $conn->query("SELECT * FROM Group_Account WHERE groupID='".$projID."' and accountID='".$accountID."'");
			
			$result_3 = $conn->query("SELECT * FROM Groups WHERE groupID='".$projID."'");
			$obj_3=$result_3->fetch_object();
			$groupName=$obj_3->groupName;
			
			// if they are in the database register the user id
			//$_SESSION['valid_user']=$username;
			//echo "Login Success!";
			if ($_FILES["file"]["error"] > 0) {
				//    echo "Error: " . $_FILES["file"]["error"] . "<br>";
				echo json_encode(array('Status' => 3, 'Message' => 'File Error'));
			} else if ($result_2->num_rows!=1){
				echo json_encode(array('Status' => 2, 'Message' => 'Project Permission Error'));
			} else {
				//echo "Upload: ".$_FILES["file"]["name"]."<br />";
				//echo "Type: " . $_FILES["file"]["type"] . "<br />";
				//echo "Size: " . ($_FILES["file"]["size"] / 1024) . "Kb<br />";
				//echo "Temp file: " . $_FILES["file"]["tmp_name"]."<br />";

				config_json($_FILES["file"]["tmp_name"], $username, $projID);

        		$filePath="uploads/".$groupName."_".$username."_".date("YmdHis").".json";
        		move_uploaded_file($_FILES["file"]["tmp_name"], $filePath);
        		//echo "Stored in: ".$filePath."<br />";
            
        		// Connecting, selecting database
        		//$conn=db_connect();
        
        		// Performing SQL query
        		$result=$conn->query("INSERT INTO uploads_json(filePath, uploader, groupID, groupName) VALUES('$filePath', '$username', '$projID', '$groupName')");
        		if($result){
	        		//echo json_encode(array('Post Data' => $_POST, 'File Data' => $_FILES['data']));
	        		echo json_encode(array('Status' => 0, 'Message' => 'Upload Complete!'));
	        	} else {
		        	echo json_encode(array('Status' => 3, 'Message' => 'Upload Failed!'));
		        }
		    }
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