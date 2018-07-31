/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.Stats',{
   extend:'MyApp.Base.Page',
   title: '企业车辆统计系统1.0',
   onInit:function(){
       Ext.create('MyApp.Component.StatsViewport');
       Ext.create('MyApp.Component.StatsTabPanel');
   }
});

