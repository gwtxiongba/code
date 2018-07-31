/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.RelayOffs',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['vehicleId','identifier','plate','ifOff','cutpwd'],
        sortInfo:{field:'ifOff', direction:"DESC"}
    }
});

