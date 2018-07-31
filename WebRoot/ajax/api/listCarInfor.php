<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$resp = array('pageNo'=>1,'pageSize'=>20,'totalPages'=>1,'result'=>array(),'totalCount'=>4);
$resp['result'][] = array('Id'=>1, 'plate'=>'NO001','identifier'=>00001);
$resp['result'][] = array('Id'=>2, 'plate'=>'NO002','identifier'=>00002);
$resp['result'][] = array('Id'=>3, 'plate'=>'NO003','identifier'=>00003);
$resp['result'][] = array('Id'=>4, 'plate'=>'NO004','identifier'=>00004);
echo json_encode($resp);
?>
