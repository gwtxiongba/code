/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.OnlinesRep',{
   extend:'MyApp.Base.Page',
   onInit:function(){
       switch(location.hash) {
           case '#0': //车辆月上线率
               Ext.create('MyApp.Component.OnlinesMRepPanel');
               break;
           case '#1': //设备日上线率
               Ext.create('MyApp.Component.OnlinesDRepPanel');
               break;
       }
   }
});

