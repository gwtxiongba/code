<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$if_enum = 1;

checkRequired(array('opwd','npwd'));

if($if_enum == 1) {//修改成功
    die('{}');
}

if($if_enum == 2) {
    die('{"code":204,"data":"您输入的原始密码有误!"}');
}
?>
