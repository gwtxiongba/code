/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.Trace',{
   extend:'MyApp.Base.Page',
   title:'车辆轨迹回放',
   onInit:function(){
       Ext.create('MyApp.Component.TracePanel');
   }
});

