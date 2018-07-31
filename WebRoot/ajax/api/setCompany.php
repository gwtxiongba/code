<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$if_enum = 1;

checkRequired(array('name','phone'));

if($if_enum == 1) {
    die('{}');
}else{
    die('{"code":205, "data":"企业资料保存失败"}');
}
?>
