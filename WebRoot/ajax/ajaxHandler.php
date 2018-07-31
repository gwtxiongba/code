<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
   require dirname(__FILE__).DIRECTORY_SEPARATOR.'Curl.class.php';
   
   $requireBase = 'http://www.carmon.com/ajax';
   $acc = array();
   if(!isset ($_REQUEST['cmd'])) {
      die(json_encode(array('code'=> -2, 'reason'=> "丢失必要参数cmd")));       
   }
   
   $cmd = $_REQUEST['cmd'];
   unset($_REQUEST['cmd']);
   
   $data = array_merge($_GET,$_POST);
   $client = new Curl();
   if(in_array($cmd,$acc)) {
           echo $client->get("api.action?", $data);
   } else {
       $fileName = "$cmd.php";
       $subDir = explode('_', $cmd);
       $subDir = count($subDir) > 1 ? DIRECTORY_SEPARATOR.$subDir[0] : '';
       @include_once dirname(__FILE__).DIRECTORY_SEPARATOR.'api'.$subDir.DIRECTORY_SEPARATOR.$fileName;
       $errs=error_get_last();
       if(isset($errs)) {
          echo json_encode(array('code'=> 500, 'reason'=> "请求代理未注册或者未定义模拟请求:$cmd.php"));   
       }
   }
   
      //模拟唯一性验证函数
   function checkRequired($names) {
       var_dump($names);
        foreach ($names as $item) {
            if (!isset($data[$item]) || empty($data[$item])){
                die('{"code":23,"reason":"'.$item.'为必填项!"}');
                break;
            }
        }
    }
?>
