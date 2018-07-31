/* 
 * To change this template, choose Tools | Templates
 * 设备日上线统计
 */
Ext.define('MyApp.Model.DateFuel',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields: ['order', 'plate','date','mileage', 'fuel','overspeedtime','speeduptimes','breaktimes','avgfuel']
   }
});