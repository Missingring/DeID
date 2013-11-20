<?php
	require_once('iim_fns.php');
	session_start();
	
	$username=$_SESSION['valid_user'];
	
	do_html_header("Browse My Projects");
	check_valid_user();
	display_user_menu();
	
	display_joined_groups($username);
	
	do_html_footer();
?>