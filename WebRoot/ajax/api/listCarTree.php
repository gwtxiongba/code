<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$cars[] = array('vehicleId'=>1, 'plate'=>'鄂K.53127大悟');
$cars[] = array('vehicleId'=>2, 'plate'=>'鄂K.53127大悟');
$resp[] = array('teamName'=>'大悟客线.1', 'vehicles'=>$cars);

$cars1[] = array('vehicleId'=>3, 'plate'=>'鄂K.53127汉川');
$cars1[] = array('vehicleId'=>4, 'plate'=>'鄂K.53127汉川');
$resp[] = array('teamName'=>'汉川客线.1', 'vehicles'=>$cars1);
/*
$resp['result'][] = array('teamName'=>'汉川客线.1', 'cars'=>array('carId'=>2, 'carName'=>'鄂K.53127汉川'));
$resp['result'][] = array('teamName'=>'黄陂客线.1', 'cars'=>array('carId'=>3, 'carName'=>'鄂K.53189黄陂'));
$resp['result'][] = array('teamName'=>'孝昌客线.4', 'cars'=>array('carId'=>4, 'carName'=>'鄂K.53019孝昌'));
 * 
 */
echo json_encode($resp);
?>
