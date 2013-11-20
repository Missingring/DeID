<?php
	require_once('iim_fns.php');
	session_start();
	
	// create short variable names
	$username=$_POST['username'];
	$passwd=$_POST['passwd'];
	
	try{
		if (!filled_out($_POST)){
			throw new Exception("You have not filled the form out correctly - please go back and try again.");
		}
	} 
	catch (Exception $e){
		do_html_header("Problem:");
		echo $e->getMessage()."You could not be logged in. You must be logged in to view this page.";
		do_html_url("index.php", "Login");
		do_html_footer();
		exit;
	}
	
	if($username && $passwd){
		try{
			$_SESSION['user_role']=login($username, $passwd);
			//echo $_SESSION['user_role'];
			// if they are in the database register the user id
			$_SESSION['valid_user']=$username;
		}
		catch(Exception $e){
			// unsuccessful login
			do_html_header("Problem:");
			echo $e->getMessage()."You could not be logged in. You must be logged in to view this page.";
			do_html_url("index.php", "Login");
			do_html_footer();
			exit;
		}
	}
	
	do_html_header("Home");
	check_valid_user();
	display_user_menu();

	do_html_footer();
?>