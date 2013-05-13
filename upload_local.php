<?php
	require_once('iim_fns.php');
	session_start();
	// create short variable names
	$username=$_SESSION['valid_user'];

	check_valid_user();
	
    if ($_FILES["file"]["type"] == "application/json"){
        if ($_FILES["file"]["error"] > 0){
            echo "Error: ".$_FILES["file"]["error"]."<br />";
        }
        else{
            echo "Upload: ".$_FILES["file"]["name"]."<br />";
            echo "Type: " . $_FILES["file"]["type"] . "<br />";
            echo "Size: " . ($_FILES["file"]["size"] / 1024) . " Kb<br />";
            echo "Temp file: " . $_FILES["file"]["tmp_name"]."<br />";

            $filePath="uploads/".date("YmdHis").".json";
            move_uploaded_file($_FILES["file"]["tmp_name"], $filePath);
            echo "Stored in: ".$filePath."<br />";
            
            // Connecting, selecting database
            $conn=db_connect();
            
            // Performing SQL query
            $result=$conn->query("INSERT INTO uploads_json(filePath, uploader) VALUES('$filePath', '$username')");
            if($result){
            	echo "Upload Success!";
            } else {
	            echo "Upload Failed!";
            }
        }
    }
    else{
        echo "Invalid file";
    }
?>