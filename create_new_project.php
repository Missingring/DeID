<?php
	require_once('iim_fns.php');
	// start it now because it must go before headers
	session_start();
		
	// create short variable names
	$groupName=$_POST['groupName'];
	$groupDescription=$_POST['description'];	

	try{
		// check forms filled in
		if (!filled_out($_POST)){
			throw new Exception("You have not filled the form out correctly - please go back and try again.");
		}

		// check group name length is ok
		// ok if username truncates, but passwords will get
		// munged if they are too long.
		if ((strlen($groupName) < 6) || (strlen($groupName) > 16)) {
			throw new Exception('Your project name must be between 6 and 16 characters. Please go back and try again.');
		}
		// attempt to create
		// this function can also throw an exception
		create_group($groupName, $groupDescription);
		//register_temp($username, $email, $name, $passwd, $role);
		// register session variable
		//$_SESSION['valid_user'] = $username;
		
		// provide link to members page
		do_html_header("Create a new Project");
		check_valid_user();
		display_user_menu();
		echo "<p>Create a new Project successful!</p>";

		// end page
		do_html_footer();
	}
	
	catch (Exception $e) {
		do_html_header("Problem:");
		check_valid_user();
		display_user_menu();
		echo $e->getMessage();
		do_html_footer();
		exit;
	}
?>