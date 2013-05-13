<?php
	function db_connect(){
		$result = new mysqli("mysql1.clemson.edu", "iimipad", "8d3{b|g:X2KL", "iim_ipad");
		if (!$result){
			throw new Exception("Could not connect to database server");
		} else {
			return $result;
		}
	}
?>