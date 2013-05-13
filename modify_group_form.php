<?php
	require_once('iim_fns.php');
	session_start();
	$groupName=$_POST['group'];
	do_html_header("Modify Group");
	check_valid_user();
	display_user_menu();
	
	display_add_to_group($groupName);
	
	do_html_footer();
?>