<?php
	require_once('iim_fns.php');
	session_start();
	do_html_header("Upload");
	check_valid_user();
	?>
	<form action="upload_local.php" method="POST" enctype="multipart/form-data">
		<label for="file">Please browse the json file you want to upload: </label><br />
		<input type="hidden" name="max_file_size" value="200000">
		<input type="file" name="file" id="file" />
		<input type="submit" name="submit" value="Submit" />
	</form>
	<?php
	display_user_menu();
	do_html_footer();
?>
