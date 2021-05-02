<?php
   if (isset($_POST["wintext"])){
 
  $data= $_POST["wintext"]."\n";
    
    $myFile = "tictactoewin.txt";

    if(file_put_contents($myFile, $data, FILE_APPEND))
        echo ("success");
    else{
		echo("fail");
	}
   }
?>