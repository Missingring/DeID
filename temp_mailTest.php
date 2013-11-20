<?php
    $to = "Qi.Tiger.Zhang@Gmail.com";
    $subject = "Test mail";
    $message = "Hello! This is a simple email message.";
    $from = "someonelse@example.com";
    $headers = "From:" . $from;
    if(mail($to,$subject,$message,$headers)){
    	echo "Mail Sent.";
    } else {
	    echo "Mail not Sent.";
    }
?>
