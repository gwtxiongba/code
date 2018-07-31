/* 
 * To change this template, choose Tools | Templates
 * 按照自然天来统计车辆行驶里程和油耗
 */
Ext.define('MyApp.Model.FormdDriving',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['vehicleId','plate','team','mileage','fuel','obdmile']
   }
});