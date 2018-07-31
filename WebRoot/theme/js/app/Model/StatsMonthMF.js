/* 
 * To change this template, choose Tools | Templates
 * 按照月来统计车辆行驶里程和油耗
 */
Ext.define('MyApp.Model.StatsMonthMF',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['vehicleId','plate','team','mileage','fuel','overspeedhours','acctimes','sleepytimes']
   }
});

