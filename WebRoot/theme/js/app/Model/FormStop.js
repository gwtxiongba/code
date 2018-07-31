/* 
 * To change this template, choose Tools | Templates
 * 按照自然天来统计停车时长
 */
Ext.define('MyApp.Model.FormStop',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['vehicleId','plate','stopseconds']
   }
});