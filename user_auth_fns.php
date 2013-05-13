<?php
	require_once('db_fns.php');
	
	function register_temp($username, $email, $name, $password, $role, $groupID){
		// connect to db
		$conn=db_connect();
		
		// check if username is unique
		$result = $conn->query("select * from Account where username='".$username."'");
		if(!$result){
			throw new Exception("Could not execute query");
		}
		
		if($result->num_rows>0){
			throw new Exception('That username is taken - go back and choose another one.');
		}
		
		// if ok, put in temporary db table
		$confirm_code=md5(uniqid(rand()));
		$result=$conn->query("insert into Account_Temp values ('".$username."', '".$email."', sha1('".$password."'), '".$role."', '".$confirm_code."', '".$groupID."')");
		if(!$result){
			throw new Exception("Could not register you in database - please try again later.");
		} else {
			if($groupID<0){
				$groupName="None";
			} else {
				$result_group=$conn->query("select * from Groups where groupID='".$groupID."'");
				$obj=$result_group->fetch_object();
				$groupName=$obj->groupName;
			}
			
			$email_addr="MARSHA9@clemson.edu";
			$from="From: admin@iim.clemson.edu \r\n";
			$subject="New Account Registration Request";
			$mesg="There is a new account registration request, the user information is followed: \r\n";
			$mesg.="Request From: ".$name."\r\n";
			$mesg.="Email Address: ".$email."\r\n";
			$mesg.="Account username: ".$username."\r\n";
			$mesg.="Account Authority Level: ".$role."\r\n";
			$mesg.="Project join in: ".$groupName."\r\n";
			$mesg.="Please click on this link to activate the new account \r\n";
			$mesg.="http://iim.clemson.edu/confirmation.php?passkey=$confirm_code";
			// send email
			if(mail($email_addr, $subject, $mesg, $from)){
				return true;
			} else {
				throw new Exception("Could not send email.");
			}
		}
	}
	
	function create_group($groupName, $groupDescription){
		// connect to db
		$conn=db_connect();
		
		// check if username is unique
		$result = $conn->query("select * from Groups where groupName='".$groupName."'");
		if(!$result){
			throw new Exception("Could not execute query");
		}
		
		if($result->num_rows>0){
			throw new Exception('That project name is taken - go back and choose another one.');
		}
		
		// if ok, put in db
		$result = $conn->query("insert into Groups values (NULL, '".$groupName."', '".$groupDescription."', '".$_SESSION['valid_user']."')");
		if (!$result){
			throw new Exception("Could not create a new group in database - please try again later.");
		}
		
		return true;
	}

	/*
	function register($username, $email, $password, $role){
		// connect to db
		$conn = db_connect();
		
		// check if username is unique
		$result = $conn->query("select * from Account where username='".$username."'");
		if(!$result){
			throw new Exception("Could not execute query");
		}
		
		if($result->num_rows>0){
			throw new Exception('That username is taken - go back and choose another one.');
		}
		
		// if ok, put in db
		$result = $conn->query("insert into Account values (NULL, '".$username."', '".$email."', sha1('".$password."'), '".$role."')");
		if (!$result){
			throw new Exception("Could not register you in database - please try again later.");
		}
		
		return true;
	}
	*/
	function login($username, $password){
		// check username and password with db
		// if yes, return true
		// else throw exception
		$conn=db_connect();
		
		// check if username is unique
		$result=$conn->query("select * from Account where username='".$username."' and password=sha1('".$password."')");
		if(!$result){
			throw new Exception("Could not log you in.");
		}
		if($result->num_rows==1 && $obj=$result->fetch_object()){
			if (intval($obj->authorityLevel) > 3){
				throw new Exception("You need a higher authority.");
			} else {
				return $obj->authorityLevel;
			}
		} else {
			throw new Exception("Could not log you in.");
		}
	}
	
	function iPad_login($username, $password){
		// check username and password with db
		// if yes, return true
		// else throw exception
		$conn=db_connect();
		
		// check if username is unique
		$result=$conn->query("select * from Account where username='".$username."' and password='".$password."'");
		if(!$result){
			throw new Exception("Could not log you. Something wrong happened.");
		}
		if($result->num_rows==1 && $obj=$result->fetch_object()){
			if (intval($obj->authorityLevel) >= 4){
				throw new Exception("You need a higher authority.");
			} else {
				return true;
			}
		} else {
			throw new Exception("Could not log you in. No matched user or Wrong password!");
		}
	}
	
	function check_valid_user(){
		// see if somebody is logged in and notify them if not
		if(isset($_SESSION['valid_user']) && isset($_SESSION['user_role'])){
			if($_SESSION['user_role']=='0'){
				$role='System Coordinator';
			} else if($_SESSION['user_role']=='1'){
				$role='Project Coordinator';
			} else if($_SESSION['user_role']=='2'){
				$role='Researcher';
			} else if($_SESSION['user_role']=='3'){
				$role='Observer';
			} else {
				echo "The role permission is not valid.<br />";
				do_html_url("index.php", "Login");
				do_html_footer();
				exit;
			}
			echo "<p id='check_valid_user'>Logged in as ".$_SESSION['valid_user'].". Role as ".$role.".</p>";
		} else {
			// they are not logged in
			do_html_header("Problem:");
			echo "You are not logged in.<br />";
			do_html_url("index.php", "Login");
			do_html_footer();
			exit;
		}
	}
	
	function change_password($username, $old_password, $new_password){
		// change password for username/old_password to new_password
		// return true or false
		// if the old password is right, change their password to new_password and return true
		// else throw an exception
		login($username, $old_password);
		$conn=db_connect();
		$result=$conn->query("update Account set password=sha1('".$new_password."') where username='".$username."'");
		if(!$result){
			throw new Exception("Password could not be changed.");
		} else {
			return true; // changed successfully
		}
	}
	
	function get_random_word($min_length, $max_length){
		// grab a random word from dictionary between the two lengths and return it
		// generate a random word
		$word='';
		// remember to change this path to suit system!!!
		$dictionary='/usr/share/dict/words';
		$fp=@fopen($dictionary, 'r');
		if(!$fp){
			return false;
		}
		$size=filesize($dictionary);
		
		// go to a random location in dictionary
		$rand_location=rand(0, $size);
		fseek($fp, $rand_location);
		
		// get the next whole word of the right length in the file
		while((strlen($word)<$min_length)||(strlen($word)>$max_length)||(strstr($word, "'"))){
			if(feof($fp)){
				fseek($fp, 0);
			}
			$word=fgets($fp, 80); // skip first word as it could be partial
			$word=fgets($fp, 80); // the potential password
		}
		$word=trim($word);
		return $word;
	}
	
	function reset_password($username){
		// set password for username to a random value
		// return the new password or false on failure
		// get a random dictionary word between 6 and 13 chars in length
		$new_password=get_random_word(6, 13);
		
		if($new_password==false){
			throw new Exception("Could not generate new password.");
		}
		// add a number between 0 and 999 to it
		// to make it a slightly better password
		$rand_number=rand(0, 999);
		$new_password.=$rand_number;
		
		// set user's password to this in database or return false
		$conn=db_connect();
		$result=$conn->query("update Account set password=sha1('".$new_password."') where username='".$username."'");
		if(!$result){
			throw new Exception("Could not change password.");
		} else {
			return $new_password; // changed successfully
		}
	}
	
	function notify_password($username, $password){
		// notify the user that their password has been changed
		$conn=db_connect();
		$result=$conn->query("select email from Account where username='".$username."'");
		if(!$result){
			throw new Exception('Could not find email address.');
		} else if($result->num_rows==0){
			throw new Exception('Could not find email address.');
			// username not in db
		} else {
			$row=$result->fetch_object();
			$email=$row->email;
			$from="From: admin@iim.clemson.edu \r\n";
			$mesg="Your IIM account password has been changed to ".$password."\r\n"."Please change it next time you log in.\r\n";
			
			if(mail($email, 'IIM login information', $mesg, $from)){
				return true;
			} else {
				throw new Execption("Could not send email.");
			}
		}
	}
?>