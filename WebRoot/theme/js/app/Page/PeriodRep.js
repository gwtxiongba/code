/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.PeriodRep',{
   extend:'MyApp.Base.Page',
   onInit:function(){
       switch(location.hash) {
           case '#0': //日报表
               Ext.create('MyApp.Component.DateRepPanel');
               break;
           case '#1': //月报表
               Ext.create('MyApp.Component.MonthRepPanel');
               break;
       }
   }
});

