/* 
 * To change this template, choose Tools | Templates
 * 按照自然天来统计车辆行驶里程和油耗
 */
Ext.define('MyApp.Model.FormObdList',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['order','plate','team','time0','mileage','fuel','lasttime','pt1']
   }
});