/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.AlarmsRep',{
   extend:'MyApp.Base.Page',
   onInit:function(){
       switch(location.hash) {
           case '#0': //断电报警
               Ext.create('MyApp.Component.AlarmsRepPanel','poweroff');
               break;
           case '#1': //碰撞报警
               Ext.create('MyApp.Component.AlarmsRepPanel','crash');
               break;
           case '#2': //侧翻报警
               Ext.create('MyApp.Component.AlarmsRepPanel','rollover');
               break;
           case '#3': //震动报警
               Ext.create('MyApp.Component.AlarmsRepPanel','quake');
               break;
           case '#4': //超速报警
               Ext.create('MyApp.Component.AlarmsRepPanel','overspeed');
               break;
       }
   }
});

