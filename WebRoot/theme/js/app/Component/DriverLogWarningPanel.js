/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.DriverLogWarningPanel',{
   extend:'Ext.tab.Panel',
   constructor:function(){
       var listLogPanel =  Ext.create('MyApp.Component.ListLogPanel');
       var abnormalLogListPanel =  Ext.create('MyApp.Component.AbnormalLogListPanel');

       this.superclass.constructor.call(this,{
           id:'warningTabPanel',
           border: false,
           layout: 'fit',
           region: 'center',
           items: [{
               title: '考勤异常信息',
               layout: 'fit',
               itemId: 'listLog',
               items: [abnormalLogListPanel]
           },{
               title: '考勤信息',
               layout: 'fit',
               itemId: 'abnormalLogList',
               items: [listLogPanel]
           }]
       });
       
   }
   
});

