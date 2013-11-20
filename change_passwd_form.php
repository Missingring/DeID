<?php
	require_once('iim_fns.php');
	session_start();
	do_html_header("Change password");
	check_valid_user();
	display_user_menu();
		
	display_password_form();
	
	do_html_footer();
?>