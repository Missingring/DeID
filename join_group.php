<?php
	require_once('iim_fns.php');
	// start it now because it must go before headers
	session_start();
		
	// create short variable names
	$group=$_POST['group'];
	$info=$_POST['info'];
	$username=$_SESSION['valid_user'];

	try{
		// check forms filled in
		if (!filled_out($_POST)){
			throw new Exception("You have not filled the form out correctly - please go back and try again.");
		}

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
			$from="From: $email \r\n";
			$to="QZHANG4@g.clemson.edu";
			$mesg="$username apply to join project: $group \r\nFollowing is what he/she says: \r\n $info";
			
			if(mail($to, 'Apply To Join Project', $mesg, $from)){
				;
			} else {
				throw new Execption("Could not send email.");
			}
		}

		do_html_header("Join a new Project");
		check_valid_user();
		display_user_menu();
		echo "<p>The application has been sent to Project Coordinator, please wait for approval.</p>";

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