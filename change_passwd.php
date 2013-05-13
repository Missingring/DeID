<?php
	require_once('iim_fns.php');
	session_start();
	do_html_header("Changing password");
	
	// create short variable names
	$old_paaswd=$_POST['old_passwd'];
	$new_passwd=$_POST['new_passwd'];
	$new_passwd2=$_POST['new_passwd2'];
	
	try{
		check_valid_user();
		display_user_menu();
		if(!filled_out($_POST)){
			throw new Exception("You have not filled out the form completely. Please try again.");
		}
		if($new_passwd != $new_passwd2){
			throw new Exception("Passwords entered were not the same. Not changed.");
		}
		if((strlen($new_passwd)>16) || (strlen($new_passwd)<6)){
			throw new Exception("New password must be between 6 and 16 characters. Try again.");
		}
		
		// attempt update
		change_password($_SESSION['valid_user'], $old_paaswd, $new_passwd);
		echo "Password changed.";
	}
	catch (Exception $e) {
		echo $e->getMessage();
	}

	do_html_footer();
?>