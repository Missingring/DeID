<?php
	require_once('iim_fns.php');
	$group=$_POST['groupID'];
	session_start();
	do_html_header("Browse Project");
	check_valid_user();
	display_user_menu();
	?>
	<div id="choose_project">
		<p>Choose any observation you want to export as CVS file:</p>
		<form action="export_data.php" method="post">
			<table border="1">
				<tr>
					<th></th>
					<th>ObservationID</th>
					<th>Observer</th>
					<th>Teacher</th>
					<th>Time</th>
				</tr>
				
			<?php
			$conn = db_connect();
			$result = $conn->query("SELECT observationID FROM Groups, Group_Observation WHERE Groups.groupID='$group' and Groups.groupID=Group_Observation.groupID");
			//echo "Query is: "."SELECT observationID FROM Groups, Group_Observation WHERE Groups.groupName='".$group."' and Groups.groupID=Group_Observation.groupID"."<br />";
			while($line = $result->fetch_array(MYSQLI_NUM)){
				$result_detail = $conn->query("SELECT username, teacher, startTime FROM Observations, Account WHERE Observations.observationID='$line[0]' and Account.accountID=Observations.observerID");
				$line_detail = $result_detail->fetch_array(MYSQLI_NUM);
				echo("<tr><td><input type='checkbox' name='observation[]' value=$line[0]></td><td>Observation: $line[0]</td> <td>$line_detail[0]</td> <td>$line_detail[1]</td> <td>$line_detail[2]</td></tr>");
			}
			?>
			<tr>
				<td><input type="checkbox" onClick="toggle(this, 'observation[]')" /></td><td colspan="4" align="center">Click All</td>
			</table>
			<br /><input type='submit' value='Get'>
		</form>
	</div>
	
	<?php
	do_html_footer();
?>