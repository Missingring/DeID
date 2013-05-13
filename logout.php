<?php
	// include function files for this application
	require_once('iim_fns.php');
	session_start();
	$old_user=$_SESSION['valid_user'];
	
	// store to test if they were logged in
	unset($_SESSION['valid_user']);
	$result_dest=session_destroy();
	
	// start output html
	do_html_header("Logging out");
	
	if(!empty($old_user)){
		if($result_dest){
			// if they were loggin and now logged out
			echo 'Logged out.<br />';
			do_html_url("index.php","Login");
		} else{
			// if they were logged in and could not be logged out
			echo 'Could not log you out.<br />';
		}
	} else{
		// if they weren't logged in but came to this page somehow
		echo 'You were not logged in, and so have not been logged out.<br />';
		do_html_url("index.php", "Login");
	}
	do_html_footer();
?>