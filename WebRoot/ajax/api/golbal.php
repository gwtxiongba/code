<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//$if_enum = rand(1,3);
$if_enum = 3 ;
//die('{"code":200, "reason": "您未登录系统或长时间未操作!"}');
if($if_enum == 1) {//未知错误
    die('{"code":100, "data": "应用服务产生异常"}');
} 

if($if_enum == 2) { //未登录状态
    die('{"code":200, "data": "您未登录系统或长时间未操作!"}');
}

$key = isset($_GET['key']) ? $_GET['key'] : '';
switch ($key) {
   case 'company':
       echo json_encode(array('company'=>new Company));
       break;
   case 'visitor':
       echo json_encode(array('visitor'=>new Visitor));
       break;
   case 'teams':
       echo json_encode(array('teams'=>new Teams));
       break;
   default : {
       echo json_encode(array('company'=>new Company,'visitor'=>new Visitor, 'teams'=>new Teams));
   }       
}

class Company {
    public $name = '畅讯网络科技有限公司';
    public $address = '武汉大学园一路兴业楼北楼 401室';
    public $phone = '027-84747390';
    public $email = 'cxwl@chanceit.cn';
}

class Visitor {
    public $account = 'superman';
    public $role = 0;
    public $ctime = 0;
    public $lasttime = 0;
    function __construct() {
        $this->ctime = strtotime('2014-5-21 09:12:45');
        $this->lasttime = time();
    }
}

class Teams {
    public $total = 10;
    public $onlines = 5;
    public $teams = array();
    //{"time":"2014-06-18 12:57:00","vehicleId":170,"plate":"鄂A456GH","identifier":"44704","online":0}
    function __construct() {
        $vehicles[] = array('time'=> '2014-06-18 12:57:00','vehicleId'=>170,'plate'=>'20072','identifier'=>'20072','online'=>0);
        $vehicles[] = array('time'=> '2014-06-18 12:57:00','vehicleId'=>171,'plate'=>'35556','identifier'=>'35556','online'=>1);
        $vehicles[] = array('time'=> '2014-06-18 12:57:00','vehicleId'=>172,'plate'=>'19957','identifier'=>'19957','online'=>1);
        
        for($i=0; $i < $this->total; $i++) {
            $this->teams[] = array('teamName'=>'车队名称'.$i,'vehicles'=>$vehicles);
        }
    }
}
?>
