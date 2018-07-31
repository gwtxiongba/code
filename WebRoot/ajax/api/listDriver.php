<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$resp = array('pageNo'=>1,'pageSize'=>20,'totalPages'=>1,'result'=>array(),'totalCount'=>4);
$resp['result'][] = array('driverId'=>11 ,'driverName'=>'陈毅  ','driverTel'=>18956232354,'license'=>'541254','remark'=>'');
$resp['result'][] = array('driverId'=>22 ,'driverName'=>'刘少奇','driverTel'=>18956123354,'license'=>'541252','remark'=>'');
$resp['result'][] = array('driverId'=>33 ,'driverName'=>'张真人','driverTel'=>18654232354,'license'=>'541253','remark'=>'');
$resp['result'][] = array('driverId'=>44 ,'driverName'=>'李世明','driverTel'=>18956295954,'license'=>'541251','remark'=>'');
echo json_encode($resp);
?>
