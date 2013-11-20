<?php
	require_once('iim_fns.php');
	session_start();
	$username=$_SESSION['valid_user'];
	
	do_html_header("Modify Projects");
	check_valid_user();
	display_user_menu();
	    
        output_confirm_function();  
    // Connecting, selecting database
    $conn=db_connect();
            
    // Performing SQL query
    if($_SESSION['user_role']=='0'){
    	$result_1=$conn->query("SELECT DISTINCT groupName,groupID FROM Groups");
    } else if($_SESSION['user_role']=='1'){
	    $result_1=$conn->query("SELECT DISTINCT groupName FROM Groups WHERE groupCreater='$username'");
    }	
	// Printing results in HTML
	echo "<h2>Choose the project you want to modify</h2>";
	echo "<form action='modify_group_form.php' method='POST'>\n";
	echo "<table border='1'>\n";
	echo "<tr><td></td><td align='center'>Group</td><td align='center'>User</td></tr>";
	while ($line_1 = $result_1->fetch_array(MYSQLI_NUM)){
		echo "<tr><td><input type='radio' name='group' value='$line_1[0]' onClick='this.form.submit()'></td><td>$line_1[0]</td><td>";
		
		$result_2=$conn->query("SELECT accountID FROM Group_Account WHERE groupName='".$line_1[0]."'");
		while ($line_2 = $result_2->fetch_array(MYSQLI_NUM)){
			$result_3=$conn->query("SELECT username FROM Account WHERE accountID='".$line_2[0]."'");
			$line_3 = $result_3->fetch_array(MYSQLI_NUM);
			echo "$line_3[0]<br />";
		}
		echo "</td>";
                if($_SESSION['user_role']=='0')
                {
                    echo "<td><a href='group_ajax.php?action=delete&id={$line_1[1]}' onclick='return confirmBox();'>Delete</a></td>";
                }
                echo   " </tr>";
	}
	echo "</table></form>\n";
       
	do_html_footer();
?>