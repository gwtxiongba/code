/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.UnbindDrivers',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['driverId','driverName'],
        sortInfo:{field:'driverId', direction:"DESC"},
        pageSize:0
    }
});

