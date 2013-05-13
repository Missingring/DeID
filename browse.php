<?php
	require_once('iim_fns.php');
	session_start();
	do_html_header("Browse");
	check_valid_user();
	display_user_menu();

    // Connecting, selecting database
    $conn=db_connect();
            
    // Performing SQL query
    $result=$conn->query("SELECT * FROM uploads_json");
	
	// Printing results in HTML
	echo "<h2>Choose the file you want to config</h2>";
	echo "<form action='config.php' method='POST'>\n";
	echo "<table border='1'>\n";
	echo "<tr><td></td><td align='center'>Path</td><td align='center'>Uploader</td><td align='center'>Upload Time</td><td align='center'>Project</td></tr>";
	while ($line = $result->fetch_array(MYSQLI_NUM)){
		echo "<tr><td><input type='radio' name='uploads' value='$line[1]' onClick='this.form.submit()'></td><td>$line[1]</td><td>$line[2]</td><td>$line[3]</td><td>$line[5]</td></tr>";
	}
	echo "</table></form>\n";
	

	do_html_footer();
?>