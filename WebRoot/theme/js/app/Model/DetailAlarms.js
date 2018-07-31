/* 
 * To change this template, choose Tools | Templates
 * 报警总数
 */
Ext.define('MyApp.Model.DetailAlarms',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['plate','team','time','pt','gps','address']
   }
});