<?php
	require_once('iim_fns.php');
	function config_json($json_file, $username, $projectID){
	    $content=file_get_contents($json_file);
	    $output_Json=json_decode($content, true);
	    //print_r($output_Json);
	    //var_dump($output_Json);
		// Parsing page 0
		try{
			foreach($output_Json['PageData'][0] as $opt){
				switch ($opt['name']){
					case "Intervention Year":
					    $interventionYear=$opt['value'];
					    break;
					case "9 Weeks":
					    $nineWeeks=$opt['value'];
					    break;
					case "Observer":
					    $observerName=$opt['value'];
					    break;
					case "School":
					    $school=$opt['value'];
					    break;
					case "Level":
					    $level=$opt['value'];
					    break;
					case "District":
					    $district=$opt['value'];
					    break;
					case "Teacher":
					    $teacherName=$opt['value'];
					    break;
					default:
					    throw new Exception("No ".$opt['name']);
				}
			}
			foreach($output_Json['PageData'][1] as $opt){
				switch ($opt['name']){
					case "Teacher Gender":
					    $gender=$opt['value'];
					    break;
					case "Teacher Ethnicity":
					    $ethnicity=$opt['value'];
					    break;
					case "Highest Degree Earned":
					    $highestDegree=$opt['value'];
					    break;
					case "Years Of Experience":
					    $yearsOfExperience=$opt['value'];
					    break;
					case "Grade Level":
					    $gradeLevel=$opt['value'];
					    break;
					case "Discipline":
					    $discipline=$opt['value'];
					    break;
					case "If Other":
					    $ifOther=$opt['value'];
					    break;
					default:
					    throw new Exception("No ".$opt['name']);
				}
			}
			foreach($output_Json['PageData'][2] as $opt){
				switch ($opt['name']){
					case "Number of Students in the Class":
					    $numberInClass=$opt['value'];
					    break;
					case "Males":
					    $numberOfMale=$opt['value'];
					    break;
					case "Females":
					    $numberOfFemale=$opt['value'];
					    break;
					case "Caucasian":
					    $numberOfCaucasian=$opt['value'];
					    break;
					case "African American":
					    $numberOfAfricanAmerican=$opt['value'];
					    break;
					case "Latino":
					    $numberOfLatino=$opt['value'];
					    break;
					case "Other":
					    $numberOfOther=$opt['value'];
					    break;
					default:
					    throw new Exception("No ".$opt['name']);
				}
			}
			foreach($output_Json['PageData'][3] as $opt){
				switch ($opt['name']){
					case "Lesson Start Time":
					    $startTime=$opt['value'];
					    break;
					case "Lesson Linked to Intervention":
					    $linkedToIntervention=$opt['value'];
					    break;
					case "Scheduled Visit":
					    $scheduled=$opt['value'];
					    break;
					case "Lesson Title":
					    $title=$opt['value'];
					    break;
					case "Objectives Visible":
					    $objectivesVisible=$opt['value'];
					    break;
					case "Standards Present":
					    $standardsPresent=$opt['value'];
					    break;
					case "Standards":
					    $standards=$opt['value'];
					    break;
					default:
					    throw new Exception("No ".$opt['name']);
				}
			}
		}
		catch(Exception $e) {
			echo json_encode(array('Status' => 3, 'Message' => 'File Content Error! '.$e->getMessage()));
		}
	
	    // Connecting, selecting database
	    $conn = db_connect();
		        
		// Performing SQL query
		$result0=$conn->query("SELECT accountID FROM Account WHERE username='$username'");
		if($result0){
			$row=$result0->fetch_object();
			$observerID=$row->accountID;
		} else {
			$observerID=NULL;
		}
		$insert1=$conn->query("INSERT INTO Observations VALUES(NULL, '$observerID', NULL, '$interventionYear', '$nineWeeks', '$school', '$level', '$district', NULL, '$teacherName', '$gender', '$ethnicity', '$highestDegree', '$yearsOfExperience', '$gradeLevel', '$discipline', '$ifOther', '$startTime', '$linkedToIntervention', '$scheduled', '$title', '$objectivesVisible', '$standardsPresent', '$standards', '$numberInClass', '$numberOfMale', '$numberOfFemale', '$numberOfCaucasian', '$numberOfAfricanAmerican', '$numberOfLatino', '$numberOfOther')");
		
		$observationID=$conn->insert_id;
		
		$insert1_1=$conn->query("INSERT INTO Group_Observation VALUES('$projectID', '$observationID')");
		
		//echo $observationID;
		foreach($output_Json['PageData'][4] as $opt){
		    $optName=$opt['name'];
		    $optValue='';
		    foreach($opt['value'] as $optVal){
		        //echo $optVal;
		        $optValue.=$optVal.', ';//
		    }
		    // trim the last comma and space!!!
		    $optValue=substr($optValue, 0, strlen($optValue)-2);
		    //echo $optValue;
			$insert2=$conn->query("INSERT INTO TimeUsage_Results VALUES('$observationID', '$optName', '$optValue')");
		}
		foreach($output_Json['PageData'][5] as $opt){
		    $optName=$opt['name'];
		    $optValue='';
		    foreach($opt['value'] as $optVal){
		        //echo $optVal;
		        $optValue.=$optVal.', ';
		    }
		    $optValue=substr($optValue, 0, strlen($optValue)-2);
		    //echo $optValue;
			$insert3=$conn->query("INSERT INTO Lesson_Details VALUES('$observationID', '$optName', '$optValue')");
		}
		foreach($output_Json['PageData'][6] as $opt){
		    $optName=$opt['name'];
		    $optValue=$opt['value'];
			$insert4=$conn->query("INSERT INTO Factors_Results VALUES('$optName', '$observationID', '$optValue')");
		}
		foreach($output_Json['PageData'][7] as $opt){
		    $optName=$opt['name'];
		    $optValue=$opt['value'];
			$insert5=$conn->query("INSERT INTO Factors_Results VALUES('$optName', '$observationID', '$optValue')");
		}
		foreach($output_Json['PageData'][8] as $opt){
		    $optName=$opt['name'];
		    $optValue=$opt['value'];
			$insert6=$conn->query("INSERT INTO Factors_Results VALUES('$optName', '$observationID', '$optValue')");
		}
		foreach($output_Json['PageData'][9] as $opt){
		    $optName=$opt['name'];
		    $optValue=$opt['value'];
			$insert7=$conn->query("INSERT INTO Factors_Results VALUES('$optName', '$observationID', '$optValue')");
		}
		foreach($output_Json['PageData'][10] as $opt){
		    $optName=$opt['name'];
		    $optComments=$opt['value'][0];
		    $optComments=str_replace("\n", " ", $optComments);
		    $optComments=str_replace("\r", " ", $optComments);
		    $optComments=str_replace("\r\n", " ", $optComments);
		    $optComments=str_replace(",", " . ", $optComments);
		    $optScore=$opt['value'][1];
			$insert8=$conn->query("INSERT INTO Overview VALUES('$observationID', NULL, '$optName', '$optComments', '$optScore')");
		}
	}
?>