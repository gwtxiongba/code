<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//sleep(1);

//die('[]');
//$fileName = 'http://'.$_SERVER['HTTP_HOST']."/ajax/traceData";
//$data = json_decode(str_replace('\r\n','',file_get_contents($fileName)),true);
//$uid = $_GET['identifier'];
$uid = 153206344;
$dateStr = $_GET['date'];
$time1 = date('Ymd0000',strtotime($dateStr));
$time2 = date('Ymd0000',strtotime($dateStr)+24*3600);
$url = "http://www.chanceit.cn:82/GetBaiduPoints/getDayPointsInfo?uid=$uid&getBeginDate=$time1&getEndDate=$time2";
$data = json_decode(file_get_contents($url),true);
$isok = $data['isok'];
$isbase64 = $isok ? $data['base64'] : false;
$count = $data['count'];
$gps = array();
for($i=0; $i<$count;$i++) {
    $speed = $data['points'][$i]['speed'] ? $data['points'][$i]['speed'] : 0;
    $azimuth = $data['points'][$i]['azimuth'] ? round($data['points'][$i]['azimuth']/10) : 0;

    if($isbase64){
        $data['points'][$i]['x'] = base64_decode($data['points'][$i]['x']);
        $data['points'][$i]['y'] = base64_decode($data['points'][$i]['y']);
    }

    $gps[] = array($data['points'][$i]['x'],$data['points'][$i]['y'], $data['points'][$i]['t'],$speed,$azimuth);    
    
}

echo $isok ? json_encode($gps) : '[]';

function toGps($int) {
    return doubleval(number_format($int/86400, 6));
}
?>
