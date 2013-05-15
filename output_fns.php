<?php
	function do_html_header($title){
		// print an HTML header
	?>
		<!doctype html>
		<html>
		<head>
			<meta charset="utf-8">
			<meta name="author" content="Qi Zhang">
			<title><?php echo $title;?></title>
			<link type="text/css" rel="stylesheet" href="./css/elements.css"
				media="screen and (min-width: 481px)">
			<link type="text/css" rel="stylesheet" href="./css/reset.css"
				media="screen and (min-width: 481px)">
			<link type="text/css" rel="stylesheet" href="./css/reset-mobile.css"
				media="screen and (max-width: 480px)">
			<link type="text/css" rel="stylesheet" href="./css/elements-mobile.css"
				media="screen and (max-width: 480px)">
			<script src="js/js_fns.js"></script>                       
		</head>
		<body class="box">
			<div id="header">
				<img src="./images/logo.jpg"/>
			</div>	
	<?php
	}
	
	function do_html_footer(){
		// print an HTML footer
	?>
		<div id="footer">
			<img src="images/greylogo.jpg"/>
            <p>
	            COPYRIGHT&copy;2013. <a href="http://www.clemson.edu/iim" target="_blank">Inquiry in Motion</a>. All Rights Reserved.
	        </p>
	        <p>
		        Powered by <a href="http://bioinformatics.clemson.edu/lab/" target="_blank">Multimedia and Informatics Lab</a>, School of Computing, Clemson University.
		    </p>
		</div>				
		</body>
		</html>
	<?php
	}
	
	function display_login_form(){
	?>
		<p><a href="register_form.php">Not a member?</a></p>
		<form method="post" action="member.php">
		<table bgcolor="#cccccc">
			<tr>
				<td colspan="2">Members log in here:</td>
			<tr>
				<td>Username:</td>
				<td><input type="text" name="username"/></td></tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" name="passwd"/></td></tr>
			<tr>
				<td colspan="2" align="center">
				<input type="submit" value="Log in"/></td></tr>
			<tr>
				<td colspan="2"><a href="forgot_form.php">Forgot your password?</a></td></tr>
			</tr>
		</table></form>
	<?php
	}
	
	function display_registration_form(){
	?>
		<form action="register_new.php" method="post">
		<table bgcolor="#cccccc">
			<tr>
				<td>Email address:</td>
				<td><input type="text" name="email" size="30" maxlength="100" /></td></tr>
			<tr>
				<td>Name <br />(your full name):</td>
				<td valign="top"><input type="text" name="name" size="16" maxlength="16" /></td></tr>
			<tr>
				<td>Preferred username <br />(max 16 chars):</td>
				<td valign="top"><input type="text" name="username" size="16" maxlength="16" /></td></tr>
			<tr>
				<td>Password <br />(between 6 and 16 chars):</td>
				<td valign="top"><input type="password" name="passwd" size="16" maxlength="16" /></td></tr>
			<tr>
				<td>Confirm password:</td>
				<td><input type="password" name="passwd2" size="16" maxlength="16" /></td></tr>
			<tr>
				<td>Role:</td>
				<td><select name="role">
					<option value="3">Observer</option>
					<option value="2">Researcher</option>
					<option value="1">Project Coordinators</option>
				</select></td></tr>
			<?php display_group_select(); ?>
			<tr>
				<td colspan="2" align="center">
				<input type="submit" value="Register"></td></tr>
		</table></form>
	<?php
	}
	
	function do_html_url($url, $name){
		// output URL as link and br
	?>
		<br /><a href='<?php echo $url;?>'><?php echo $name;?></a><br />
	<?php
	}
	
	function display_user_menu(){
		// display the menu options on this page
	?>
		<div id="user_menu">
			<a href="member.php">Home</a> &nbsp;|&nbsp;
			<a href="change_passwd_form.php">Change password</a> &nbsp;|&nbsp;
			<?php if(intval($_SESSION['user_role'])>1 && intval($_SESSION['user_role'])<=3){?>
				<a href="join_group_form.php">Join a new project</a> &nbsp; |&nbsp;
			<?php } ?>
			<?php if(intval($_SESSION['user_role'])<3 && intval($_SESSION['user_role'])>=0){?>
				<a href="browse_my_projects_form.php">Browse my projects</a> &nbsp; |&nbsp;
				<?php if(intval($_SESSION['user_role'])<2 && intval($_SESSION['user_role'])>=0){ ?>
					<a href="create_new_project_form.php">Create a new project</a> &nbsp; |&nbsp;
					<a href="choose_unjoined_group.php">Modify projects</a> &nbsp; |&nbsp;
				<?php } ?>
			<?php } ?>
			<a href="logout.php">Logout</a>
		</div>
	<?php
	}
	
	function display_create_new_project_form(){
		// display html create new project form
	?>
		<br />
		<form action="create_new_project.php" method="post">
		<table bgcolor="#cccccc">
			<tr>
				<td>Preferred Project Name <br />(between 6 and 16 chars):</td>
				<td valign="top"><input type="text" name="groupName" size="16" maxlength="16" /></td></tr>
			<tr>
				<td>Project Description</td></tr>
			<tr>
				<td colspan="2" align="center">
				<textarea name="description" rows="10" cols="35"></textarea></td>
			<tr>
				<td colspan="2" align="center">
				<input type="submit" value="Create"></td></tr>
		</table></form>
	<?php
	}
	
	function display_join_group_form(){
		// display html create new project form
	?>
		<br />
		<form action="join_group.php" method="post">
		<table bgcolor="#cccccc">
			<tr>
				<td>Project you apply to join in: <br /> (Project ID or Project Name) </td>
				<td valign="top"><input type="text" name="group" size="16" maxlength="16" /></td></tr>
			<tr>
				<td>Additional Information</td></tr>
			<tr>
				<td colspan="2" align="center">
				<textarea name="info" rows="10" cols="35"></textarea></td>
			<tr>
				<td colspan="2" align="center">
				<input type="submit" value="Apply"></td></tr>
		</table></form>
	<?php
	}
	
	function display_password_form(){
		// display html change password form
	?>
		<br />
		<form action="change_passwd.php" method="post">
			<table width="250" cellpadding="2" cellspacing="0" bgcolor="#cccccc">
				<tr><td>Old password:</td>
					<td><input type="password" name="old_passwd" size="16" maxlength="16"/></td>
				</tr>
				<tr><td>New password:</td>
					<td><input type="password" name="new_passwd" size="16" maxlength="16"/></td>
				</tr>
				<tr><td>Repeat new password:</td>
					<td><input type="password" name="new_passwd2" size="16" maxlength="16"/></td>
				</tr>
				<tr><td colspan="2" align="center">
					<input type="submit" value="Change password"/>
				</td></tr>
			</table>
		<br />
	<?php
	}
	
	function display_forgot_form(){
		// display HTML form to reset and email password
	?>
		<br />
		<form action="forgot_passwd.php" method="post">
		<table width="250" cellpadding="2" cellspacing="0" bgcolor="#cccccc">
		<tr><td>Enter your username</td>
			<td><input type="text" name="username" size="16" maxlength="16"/></td>
		</tr>
		<tr><td colspan="2" align="center">
			<input type="submit" value="Change password"/>
		</td></tr>
		</table>
		<br />
	<?php	
	}
	
	function display_group_select(){
		// display HTML select for group
		$conn = db_connect();
		$result = $conn->query("SELECT * FROM Groups");
		
		echo("<tr><td>Project you want to join: </td><td><select name='groupID'>");
		echo("<option value='-1'>None</option>");
		while($line = $result->fetch_array(MYSQLI_NUM)){
			echo("<option value=$line[0]>$line[1]</option>");
		}
		echo("</select></td></tr>");
	}
	
	function display_add_to_group($groupName){
		$conn = db_connect();
		$result = $conn->query("SELECT username FROM Account WHERE username not in (SELECT username FROM Account, Group_Account WHERE Account.accountID = Group_Account.accountID and Group_Account.groupName = '$groupName')");
		?>
		<div>
			<p>
				Select the user you want to add into <?php echo $groupName; ?> :</p>
				<p>
					<form action="add_to_group.php" method="post">
						<select name="user">
		<?php
		while($line = $result->fetch_array(MYSQLI_NUM)){
			echo("<option value=$line[0]>$line[0]</option>");
		}
		?>
		</select><input type="hidden" name="group" value="<?php echo $groupName; ?>"><input type="submit" value="Confirm"></form>
		</p></div>
		<?php
	}
	
	function display_joined_groups($username){
		$conn = db_connect();
		$result = $conn->query("SELECT groupID, groupName FROM Group_Account, Account WHERE Account.accountID = Group_Account.accountID AND Account.username = '$username'");
		//echo "SELECT groupName FROM Group_Account, Account WHERE Account.accountID = Group_Account.accountID AND Account.username = '".$username."'";
		?>
		<div>
			<p>
				Select the project you want to browse: </p>
				<p>
					<form action="browse_project_form.php" method="post">
						<select name="groupID">
		<?php
		while($line = $result->fetch_array(MYSQLI_NUM)){
			echo("<option value=$line[0]>$line[1]</option>");
		}
		?>
		</select><input type="submit" value="Confirm"></form>
		</p></div>
		<?php
	}	