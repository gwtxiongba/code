/* 
 * To change this template, choose Tools | Templates
 * 按照月来统计车辆行驶里程和油耗
 */
Ext.define('MyApp.Model.StatsDaysMF',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['date','plate','team','mileage','fuel','overspeedtime','speeduptimes','breaktimes']
   }
});

