<?php
	require_once('iim_fns.php');
	session_start();
	
	do_html_header("Apply To Join a Project");
	check_valid_user();
	display_user_menu();
	
	display_join_group_form();
	
	do_html_footer();
?>