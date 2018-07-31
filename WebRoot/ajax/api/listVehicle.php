<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
die('{"pageNo":1,"pageSize":20,"totalPages":5,"result":[{"vehicleId":4626,"identifier":"253114684","plate":"鄂A7MX09","type":2,"driver":null,"ifMonitor":0},{"vehicleId":519,"identifier":"17785","plate":"鄂A17785","type":null,"driver":null,"ifMonitor":0},{"vehicleId":516,"identifier":"42067","plate":"鄂A42067","type":null,"driver":null,"ifMonitor":0},{"vehicleId":514,"identifier":"37853","plate":"鄂A37853","type":null,"driver":null,"ifMonitor":0},{"vehicleId":509,"identifier":"28099","plate":"鄂A28099","type":null,"driver":null,"ifMonitor":0},{"vehicleId":508,"identifier":"25658","plate":"鄂A25658","type":null,"driver":null,"ifMonitor":0},{"vehicleId":507,"identifier":"33804","plate":"鄂A33804","type":null,"driver":null,"ifMonitor":0},{"vehicleId":506,"identifier":"26042","plate":"鄂A26042","type":null,"driver":null,"ifMonitor":0},{"vehicleId":505,"identifier":"27787","plate":"鄂A27787","type":null,"driver":null,"ifMonitor":0},{"vehicleId":504,"identifier":"43692","plate":"鄂A43692","type":null,"driver":null,"ifMonitor":0},{"vehicleId":503,"identifier":"37249","plate":"鄂A37249","type":null,"driver":null,"ifMonitor":0},{"vehicleId":502,"identifier":"42139","plate":"鄂A42139","type":null,"driver":null,"ifMonitor":0},{"vehicleId":500,"identifier":"31929","plate":"鄂A31929","type":null,"driver":null,"ifMonitor":0},{"vehicleId":498,"identifier":"23997","plate":"鄂A23997","type":null,"driver":null,"ifMonitor":0},{"vehicleId":497,"identifier":"17359","plate":"鄂A17359","type":null,"driver":null,"ifMonitor":0},{"vehicleId":496,"identifier":"22281","plate":"鄂A22281","type":null,"driver":null,"ifMonitor":0},{"vehicleId":495,"identifier":"40606","plate":"鄂A40606","type":null,"driver":null,"ifMonitor":0},{"vehicleId":494,"identifier":"36451","plate":"鄂A36451","type":null,"driver":null,"ifMonitor":0},{"vehicleId":493,"identifier":"24072","plate":"鄂A24072","type":null,"driver":null,"ifMonitor":0},{"vehicleId":492,"identifier":"11084","plate":"鄂A11084","type":null,"driver":null,"ifMonitor":0}],"totalCount":91}');
$resp = array('pageNo'=>1,'pageSize'=>20,'totalPages'=>1,'result'=>array(),'totalCount'=>4);
$resp['result'][] = array('vehicleId'=>11,'identifier'=>1001, 'plate'=>'鄂A6QM99','type'=>1,'driver'=>array('driverId'=>122, 'driverName'=>'应师傅','driverTel'=>'18602706651'),'createTime'=>'2014-06-15 12:32:47','updateTime'=>'2014-06-15 12:32:47');
$resp['result'][] = array('vehicleId'=>12,'identifier'=>1002, 'plate'=>'鄂A55620','type'=>2,'driver'=>null,'createTime'=>'2014-06-15 12:32:47','updateTime'=>'2014-06-15 12:32:47');
$resp['result'][] = array('vehicleId'=>13,'identifier'=>1003, 'plate'=>'鄂AS9588','type'=>3,'driver'=>array('driverId'=>144, 'driverName'=>'王师傅','driverTel'=>'18602706653'),'createTime'=>'2014-06-15 12:32:47','updateTime'=>'2014-12-15 12:32:47');
$resp['result'][] = array('vehicleId'=>14,'identifier'=>1004, 'plate'=>'鄂A66S99','type'=>4,'driver'=>array('driverId'=>155, 'driverName'=>'找师傅','driverTel'=>'18602706654'),'createTime'=>'2014-06-15 12:32:47','updateTime'=>'2014-11-15 12:32:47');
echo json_encode($resp);

class emptyClass {
    
}
?>
