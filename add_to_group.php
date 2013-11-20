<?php
	require_once('iim_fns.php');
	session_start();
	
	do_html_header("Adding");
	check_valid_user();
	
	$username = $_POST['user'];
	$groupName = $_POST['group'];
	
	$conn = db_connect();
	$result = $conn->query("INSERT INTO Group_Account(groupID, accountID, groupName) SELECT groupID, accountID, '$groupName' as groupName FROM Account, Groups WHERE Account.username='$username' and Groups.groupName='$groupName'");
	if($result){
		echo("<p>Add user Success!</p>");
		do_html_url('choose_unjoined_group.php', 'Return');
	} else {
		echo ("<p>Add user failed! Please try again later.</p>");
	}
	
	do_html_footer();

?>