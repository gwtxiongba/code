/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AlarmTabPanel',{
   extend:'Ext.tab.Panel',
   constructor:function(){
       var alarmListPanel =  Ext.create('MyApp.Component.AlarmListPanel');
       var abnormalListPanel =  Ext.create('MyApp.Component.AbnormalListPanel');
       this.superclass.constructor.call(this,{
           id:'alarmTabPanel',
           border: false,
           layout: 'fit',
           region: 'center',
           items: [{//报警车辆列表
               title: '报警车辆列表',
               layout: 'fit',
               itemId: 'alarmList',
               items: [alarmListPanel]
           },{//异常参数车辆列表
               title: '异常参数车辆列表',
               layout: 'fit',
               itemId: 'abnormalList',
               items: [abnormalListPanel]
           }]
       });
       
   }
   
});

