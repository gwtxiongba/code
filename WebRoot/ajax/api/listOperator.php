<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$resp = array('pageNo'=>1,'pageSize'=>20,'totalPages'=>1,'result'=>array(),'totalCount'=>30);
$resp['result'][] = array('accountId'=>1, 'account'=>'陈一','realName'=>'陈毅','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-08 12:21:27','loginTimes'=>1,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>2, 'account'=>'刘二','realName'=>'刘少奇','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-08 12:21:27','loginTimes'=>1,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>3, 'account'=>'张三','realName'=>'张真人','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-08 12:21:27','loginTimes'=>1,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>4, 'account'=>'李四','realName'=>'李世明','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-05 12:21:27','loginTimes'=>20,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>5, 'account'=>'王五','realName'=>'王老吉','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-03 12:21:27','loginTimes'=>5,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>6, 'account'=>'赵六','realName'=>'赵无忌','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-01 12:21:27','loginTimes'=>12,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>7, 'account'=>'钱七','realName'=>'钱堆','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-01 12:21:27','loginTimes'=>10,'visitTime'=>'2014-06-06 14:28:12');
$resp['result'][] = array('accountId'=>8, 'account'=>'孙八','realName'=>'孙虹','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-03 12:21:27','loginTimes'=>5,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>9, 'account'=>'周久','realName'=>'周迅','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-05 12:21:27','loginTimes'=>20,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>10, 'account'=>'五十','realName'=>'武松','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-05 12:21:27','loginTimes'=>20,'visitTime'=>'2014-06-05 07:31:36');
$resp['result'][] = array('accountId'=>11, 'account'=>'陈一','realName'=>'陈毅','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-08 12:21:27','loginTimes'=>1,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>12, 'account'=>'刘二','realName'=>'刘少奇','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-08 12:21:27','loginTimes'=>1,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>13, 'account'=>'张三','realName'=>'张真人','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-08 12:21:27','loginTimes'=>1,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>14, 'account'=>'李四','realName'=>'李世明','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-05 12:21:27','loginTimes'=>20,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>15, 'account'=>'王五','realName'=>'王老吉','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-03 12:21:27','loginTimes'=>5,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>16, 'account'=>'赵六','realName'=>'赵无忌','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-01 12:21:27','loginTimes'=>12,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>17, 'account'=>'钱七','realName'=>'钱堆','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-01 12:21:27','loginTimes'=>10,'visitTime'=>'2014-06-06 14:28:12');
$resp['result'][] = array('accountId'=>18, 'account'=>'孙八','realName'=>'孙虹','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-03 12:21:27','loginTimes'=>5,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>19, 'account'=>'周久','realName'=>'周迅','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-05 12:21:27','loginTimes'=>20,'visitTime'=>'2014-06-06 07:31:36');
$resp['result'][] = array('accountId'=>20, 'account'=>'五十','realName'=>'武松','address'=>'','phone'=>'','email'=>'','createTime'=>'2014-06-05 12:21:27','loginTimes'=>20,'visitTime'=>'2014-06-05 07:31:36');
echo json_encode($resp);
?>
