<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//echo '[{"driverName":"sss叔叔苏","driverId":11},{"driverName":"撒旦","driverId":10},{"driverName":"爱尚","driverId":12}]';
$list = array();
for($i=1; $i<=30;$i++) {
    $list[] = array('driverName'=>'用户名'.$i,'driverId'=>$i);
}
/*
$page = json_decode('{"pageNo":1,"pageSize":20,"totalPages":1,"result":[],"totalCount":30}');
for($i=1; $i<=$page->totalCount;$i++) {
    $page->result[] =  array('driverName'=>'用户名'+$i,'driverId'=>$i);
}*/
echo json_encode($list)
?>
