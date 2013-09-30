<?php
require_once('iim_fns.php');
session_start();
if(isset($_REQUEST['action']))
{
	$action=$_REQUEST["action"];
	$observationID=$_POST['observation'];
	if($action =="Get")
	{


		$conn=db_connect();
		$csvHeader = "observationID, Date, Observ. Time, observerID, intervention Year, 9 Weeks, School, Level, District, Teacher, Teacher Gender, Teacher Ethnicity, Highest Degree, Yrs Experience, GrdLvl, DiScipline, If other, LinkedToIntervention, Scheduled Visit, Lesson Title, Objectives, Standards Present, Standards, # Studs, . Male Students, . Female Students, . Caucasian Students, . African-American Students, . African-American Students, . Other Students, I1, I2, I3, I4, I5, D1, D2, D3, D4, D5, A1, A2, A3, A4, A5, C1, C2, C3, C4, Instruction Score, Instruction Comments, Discourse Score, Discourse Comments, Curriculum Score, Curriculum Comments, Assessment Score, Assessment Comments, OverallView Score, OverallView Comments, TimeRange1, Act1, Org1, Attn1, Cog1, Inq1, Ass1, TimeRange2, Act2, Org2, Attn2, Cog2, Inq2, Ass2, TimeRange3, Act3, Org3, Attn3, Cog3, Inq3, Ass3, TimeRange4, Act4, Org4, Attn4, Cog4, Inq4, Ass4, TimeRange5, Act5, Org5, Attn5, Cog5, Inq5, Ass5, TimeRange6, Act6, Org6, Attn6, Cog6, Inq6, Ass6, TimeRange7, Act7, Org7, Attn7, Cog7, Inq7, Ass7, TimeRange8, Act8, Org8, Attn8, Cog8, Inq8, Ass8, TimeRange9, Act9, Org9, Attn9, Cog9, Inq9, Ass9, TimeRange10, Act10, Org10, Attn10, Cog10, Inq10, Ass10, TimeRange11, Act11, Org11, Attn11, Cog11, Inq11, Ass11, TimeRange12, Act12, Org12, Attn12, Cog12, Inq12, Ass12, TimeRange13, Act13, Org13, Attn13, Cog13, Inq13, Ass13, TimeRange14, Act14, Org14, Attn14, Cog14, Inq14, Ass14, TimeRange15, Act15, Org15, Attn15, Cog15, Inq15, Ass15, TimeRange16, Act16, Org16, Attn16, Cog16, Inq16, Ass16, TimeRange17, Act17, Org17, Attn17, Cog17, Inq17, Ass17, TimeRange18, Act18, Org18, Attn18, Cog18, Inq18, Ass18, Time A, Note A, Comment A, Time B, Note B, Comment B, Time C, Note C, Comment C, Time D, Note D, Comment D, Time E, Note E, Comment E, Time F, Note F, Comment F, Time G, Note G, Comment G\n";

		foreach($observationID as $observID){

			$result=$conn->query("SELECT observationID, startTime, observerID, interventionYear, 9Weeks, school, level, district, teacher, teacherGender, teacherEthnicity, highestDegree,yearsExperience, gradeLevel, discipline, ifOther, linkedToIntervention, scheduled, title, objectivesVisible, standardsPresent, standards, numberInClass, numberOfMale, numberOfFemale, numberOfCacasian, numberOfAfricanAmerican, numberOfLatino, numberOfOtherEthnicity FROM Observations WHERE observationID='$observID' ORDER BY interventionYear DESC");
			$line = $result->fetch_array(MYSQLI_NUM);
			foreach($line as $item){
				$str.=$item.', ';
			}

			$result_Factors=$conn->query("SELECT selection FROM `Factors_Results` WHERE observationID='$observID' and factorID REGEXP '[I]' ORDER BY factorID");
			while($line_add = $result_Factors->fetch_row()){
				$str.=$line_add[0].', ';
			}
			$result_Factors=$conn->query("SELECT selection FROM `Factors_Results` WHERE observationID='$observID' and factorID REGEXP '[D]' ORDER BY factorID");
			while($line_add = $result_Factors->fetch_row()){
				$str.=$line_add[0].', ';
			}
			$result_Factors=$conn->query("SELECT selection FROM `Factors_Results` WHERE observationID='$observID' and factorID REGEXP '[A]' ORDER BY factorID");
			while($line_add = $result_Factors->fetch_row()){
				$str.=$line_add[0].', ';
			}
			$result_Factors=$conn->query("SELECT selection FROM `Factors_Results` WHERE observationID='$observID' and factorID REGEXP '[C]' ORDER BY factorID");
			while($line_add = $result_Factors->fetch_row()){
				$str.=$line_add[0].', ';
			}

			$result_Overviews=$conn->query("SELECT score, comments FROM `Overview` WHERE observationID='$observID' ORDER BY overviewOF DESC");
			while($line_add2 = $result_Overviews->fetch_row()){
				$str.=$line_add2[0].', '.$line_add2[1].', ';
			}

			for ($i=0; $i<114; $i++){
				$timeUsage[$i]='';
			}
			$result_TimeUsage=$conn->query("SELECT selection FROM `TimeUsage_Results` WHERE observationID='$observID' ORDER BY cast(timeInterval as unsigned)");
			for ($i=0; $i<18; $i++){
				$line_add1=$result_TimeUsage->fetch_row();
				if($line_add1){
					$timeUsage[$i]=$line_add1[0];
				}
			}
			foreach($timeUsage as $timeU){
				$str.=$timeU.', ';
			}

			for ($i=0; $i<21; $i++){
				$timeNotes[$i]='';
			}
			$result_TimeNotes=$conn->query("SELECT notes FROM `Lesson_Details` WHERE observationID='$observID' ORDER BY time DESC");
			for ($i=0; $i<7; $i++){
				$line_add2=$result_TimeNotes->fetch_row();
				if($line_add2){
					$timeNotes[$i]=$line_add2[0];
				}
			}
			foreach($timeNotes as $timeN){
				$str.=$timeN.', ';
			}

			$str.="\n";
		}
		$str=$csvHeader.$str;

		$filename=date('Ymd').'.csv';
		export_csv($filename, $str);
	}
	else if($action=="Delete")
	{
		do_html_header("Delete Observation");
                check_valid_user();
                display_user_menu();
                if($_SESSION['user_role']!="1" && $_SESSION['user_role']!="0")
                {
                    echo "You don't have the permission! <a href='browse_my_projects_form.php'>Go back</a></p>";
                }
                else
                {
		$conn=db_connect();
		$conn->autocommit(false);
		$results=array();
		foreach($observationID as $observID){
			$delete_factors_reuslts_sql="DELETE FROM Factors_Results WHERE observationID={$observID};";
			$delete_group_observation_sql="DELETE FROM Group_Observation WHERE observationID={$observID};";
			$delete_lesson_details_sql="DELETE FROM Lesson_Details WHERE observationID={$observID};";
			$delete_overview_sql="DELETE FROM Overview WHERE observationID={$observID};";
			$delete_time_usages_result_sql="DELETE FROM TimeUsage_Results WHERE observationID={$observID};";
			$delete_observation_sql="DELETE FROM Observations WHERE observationID={$observID};";
			$results[]=$conn->query($delete_factors_reuslts_sql);
			$results[]=$conn->query($delete_group_observation_sql);
			$results[]=$conn->query($delete_lesson_details_sql);
			$results[]=$conn->query($delete_overview_sql);
			$results[]=$conn->query($delete_time_usages_result_sql);
			$results[]=$conn->query($delete_observation_sql);                       
		}

		$finalResult=true;
		foreach($results as $flag)
		{
			$finalResult=$finalResult && $flag;
		}

		if($finalResult)
		{
			$conn->commit();
			echo "<p>Delete successfully!. <a href='browse_my_projects_form.php'>Go back</a></p>";
		}
		else
		{
			$conn->rollback();
			echo "Delete failed. Please try again.".$conn->error;
		}
                }
		do_html_footer();
	}
}



function export_csv($filename, $data){
	header("Content-type:text/csv"); 
	header("Content-Disposition:attachment;filename=".$filename); 
	header('Cache-Control:must-revalidate,post-check=0,pre-check=0'); 
	header('Expires:0'); 
	header('Pragma:public'); 
	echo $data;
}

?>