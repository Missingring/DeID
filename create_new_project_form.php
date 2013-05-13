<?php
	require_once('iim_fns.php');
	session_start();
	do_html_header("Create New Project");
	check_valid_user();
	display_user_menu();
	
	display_create_new_project_form();
	
	do_html_footer();
?>