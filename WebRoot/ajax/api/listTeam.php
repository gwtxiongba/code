<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//die('{"code":200, "data": "您未登录系统或长时间未操作!"}');
//die('{"code":500, "data": "系统错误!"}');
$resp = array('pageNo'=>1,'pageSize'=>20,'totalPages'=>1,'result'=>array(),'totalCount'=>4);
$resp['result'][] = array('teamId'=>1, 'teamName'=>'未分组车辆','amount'=>5,'order'=>1);
$resp['result'][] = array('teamId'=>2, 'teamName'=>'我的车队1','amount'=>2,'order'=>2);
$resp['result'][] = array('teamId'=>3, 'teamName'=>'我的车队2','amount'=>1,'order'=>3);
$resp['result'][] = array('teamId'=>4, 'teamName'=>'我的车队3','amount'=>0,'order'=>4);
echo json_encode($resp);
?>
