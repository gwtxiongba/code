<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$total = 200;
$onlines = rand(0,200);
echo json_encode(array('total'=>$total,'onlines'=>$onlines));
?>
