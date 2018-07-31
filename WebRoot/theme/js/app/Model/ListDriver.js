/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListDriver',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['driverId','driverName','driverTel','license','remark','userName','pwd','zjcx','cardNo','endTime'],
        sortInfo:{field:'driverId', direction:"DESC"}
    }
});

