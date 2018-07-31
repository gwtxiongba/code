/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.VehicleOnlines',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['vehicleId','identifier','plate'],
        sortInfo:{field:'vehicleId', direction:"DESC"}
    }
});

