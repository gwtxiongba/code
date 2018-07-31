/* 
 * To change this template, choose Tools | Templates
 * 报警总数
 */
Ext.define('MyApp.Model.TotalAlarms',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['vehicleId','plate','times']
   }
});