<?php
  //die('{"code": 200,"data":"未登录"}');
  $obj = json_decode('{"status":0,"total":1,"size":1,"contents":[{"identifer":"38527","online":0,"angle":0,"time":"2014-05-28 00:28:57","speed":20,"vehicle":{},"location":[120.2529522515,29.722527450809]}]}');
  $obj->contents[0]->vehicle = array('vehicleId'=>11,'identifier'=>1001, 'plate'=>'鄂A6QM99','type'=>1,'driver'=>array('driverId'=>122, 'driverName'=>'应师傅','driverTel'=>'18602706651'),'createTime'=>'2014-06-15 12:32:47','updateTime'=>'2014-06-15 12:32:47');
  if(isset($_GET['callback']))
      echo $_GET['callback'].'&&'.$_GET['callback'].'('.json_encode($obj).')';
  else echo json_encode($obj);
?>
