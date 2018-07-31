/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListMonitor',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['vehicleId','identifier','plate','type','ifRelay','ifOff'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

