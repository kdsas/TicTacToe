<?php
   if (isset($_POST["turntext"])){
 
  $data= $_POST["turntext"]."\n";
    
    $myFile = "tictactoeturn.txt";

    if(file_put_contents($myFile, $data, FILE_APPEND))
        echo ("success");
    else{
		echo("fail");
	}
   }
?>