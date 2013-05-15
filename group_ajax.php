<?php
   require_once('iim_fns.php'); 
   session_start();
   do_html_header("Delete Group");   
    
   if(isset($_REQUEST["action"]))
   {
       $action=$_REQUEST["action"];
       if($action=="delete")
       {
           if($_SESSION['user_role']!="0")
           {
               echo "You don't have the permission!.";
           }
            else {
           $groupID=$_REQUEST["id"];
           $conn=  db_connect();
           $sql_update_account_temp="UPDATE Account_Temp set groupID=null WHERE groupID={$groupID};";
           $sql_delete_group_account="DELETE FROM Group_Account WHERE groupID={$groupID};";
           $sql_delete_group_observation="DELETE FROM Group_Observation WHERE groupID={$groupID};";
           $sql_delete_upload_json="DELETE FROM uploads_json WHERE groupID={$groupID};";
           $sql_delete_group="DELETE FROM Groups WHERE groupID={$groupID};";
           $conn->autocommit(false);
           $result0=$conn->query($sql_update_account_temp);
           $result1=$conn->query($sql_delete_group_account);
           $result2=$conn->query($sql_delete_group_observation);
           $result3=$conn->query($sql_delete_upload_json);
           $result4=$conn->query($sql_delete_group);
           
           if($result0 && $result1 && $result2 && $result3 && $result4)
           {
               $conn->commit();
               echo "<p>Delete successfully! <a href='choose_unjoined_group.php'>Go back</a></p>";
           }
           else
           {
               $conn->rollback();
               echo "Delete failed. Please try again.".$conn->error;
           }
          }
       }
   }
          
   do_html_footer();
?>
