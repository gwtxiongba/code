/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.DiaryTabPanel',{
   extend:'Ext.tab.Panel',
   constructor:function(){
       this.superclass.constructor.call(this,{
           id:'diaryTabPanel',
           frame:false,
           border: false,
           layout: 'fit',
           region: 'center',
           items: [{
               title: '按天记录',
               layout: 'fit',
               itemId: 'monthDiaryPanel',
               items: [Ext.create('MyApp.Component.MonthDiaryPanel')]
           },{
               title: '按月总计',
               layout: 'fit',
               itemId: 'yearDiaryPanel',
               items: [Ext.create('MyApp.Component.YearDiaryPanel')]
           }]
       });
       

   }
   
});

