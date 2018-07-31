/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.FuelDateRep',{
   extend:'MyApp.Base.Page',
   onInit:function(){
       switch(location.hash) {
           case '#0': //日行油耗报表
               Ext.create('MyApp.Component.FuelDateRepPanel');
               break;
       }
   }
});

