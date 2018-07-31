<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$uid = $_GET['uid'];
$strtime = $_GET['date'];
//echo $uid.':'.$strtime;
echo getLogs($uid, $strtime);
//echo trackWebservice();
function getLogs($uid,$strtime) {
    $url="http://www.chanceit.cn:8081/services/UserRecorderService?wsdl";
    $client=new SoapClient($url);
    $arguments=array('in0'=>$uid,'in1'=>$strtime);
    //$arguments=array('in1'=>$strtime);
    $function_name='getDataByMonth_and';
    $result=$client->__soapCall($function_name, array($arguments));
    $str=$result->out;
    return $str;
}
?>
