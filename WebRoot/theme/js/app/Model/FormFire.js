/* 
 * To change this template, choose Tools | Templates
 * 按照自然天来统计启动位置
 */
Ext.define('MyApp.Model.FormFire',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['vehicleId','plate','timespan']
   }
});