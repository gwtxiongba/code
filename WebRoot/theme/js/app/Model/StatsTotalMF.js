/* 
 * To change this template, choose Tools | Templates
 *按照时间间隔来统计总行驶里程和总油耗
 */
Ext.define('MyApp.Model.StatsTotalMF',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['vehicleId','plate','team','mileage','fuel', 'avgfuel', 'overspeedtime','speeduptimes','breaktimes']
   }
});

