/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.WeibaoWarningPanel',{
   extend:'Ext.tab.Panel',
   constructor:function(){
       var listLogPanel =  Ext.create('MyApp.Component.WeibaoGridPanel',1);
       //var abnormalLogListPanel =  Ext.create('MyApp.Component.AbnormalLogListPanel');

       this.superclass.constructor.call(this,{
           id:'weibaowarningTabPanel',
           border: false,
           layout: 'fit',
           region: 'center',
           items: [{
               title: '车辆信息',
               layout: 'fit',
               itemId: 'listLog',
               items: [listLogPanel]
           }]
       });
       
   }
   
});

