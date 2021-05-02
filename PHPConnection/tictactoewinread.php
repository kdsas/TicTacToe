<?php
$myfile = fopen("tictactoewin.txt", "r");
echo fread($myfile, filesize("tictactoewin.txt"));
$lines = file("tictactoewin.txt");
fclose($myfile);
?>