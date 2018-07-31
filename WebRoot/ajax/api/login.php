<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//$if_enum = array('OK','NOTMATCH','VALIDATE_FAILED');
$if_enum = 1;

checkRequired(array('account','pwd'));

if(!empty($data['vcode']) && $data['vcode'] != '1234') die('{"code":203,"data":"验证码错误!"}');

if($if_enum == 1) {//登录成功
       die(json_encode(array('appid'=>1, 'company'=>new Company,'visitor'=>new Visitor,'scanDate'=>'2014-08-12')));
}

if($if_enum == 2) {//账号和密码不匹配
    die('{"code":201,"data":"密码错误或者账号不存在!"}');
}

die('{"code":100, "data": "服务未知异常"}');


class Company {
    public $id = 1;
    public $name = '畅讯网络科技有限公司';
    public $address = '武汉大学园一路兴业楼北楼 401室';
    public $phone = '027-84747390';
    public $email = 'cxwl@chanceit.cn';
}

class Visitor {
    public $account = '';
    public $role = 0;
    public $ctime = 0;
    public $lasttime = 0;
    public $teamIds = '';
    function __construct() {
        $this->account = $_REQUEST['account'];
        $this->role = rand(0,1);
        $this->ctime = strtotime('2014-5-21 09:12:45');
        $this->lasttime = time();
        $this->teamIds = array();
    }
}
?>
