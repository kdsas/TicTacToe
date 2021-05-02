<?php
$myfile = fopen("tictactoeturn.txt", "r");
echo fread($myfile, filesize("tictactoeturn.txt"));
$lines = file("tictactoeturn.txt");
fclose($myfile);
?>