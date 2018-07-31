/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListAlarm',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['vehicleId','identifier','plate','reason','ifRead','time','x','y','address'],
        sortInfo:{field:'time', direction:"DESC"}
    }
});

