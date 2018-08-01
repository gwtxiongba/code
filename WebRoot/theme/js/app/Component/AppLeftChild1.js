/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AppLeftChild1',{
   extend:'Ext.panel.Panel',
   constructor:function(){
       var childPanel =  Ext.create('MyApp.Component.ChildPanel');
       this.superclass.constructor.call(this,{
           id:'leftChild1',
           title: '车辆监控',
           layout: 'border',
           border: false,
           items: [{
				region: 'center',
				layout: 'fit',
				border: false,
				items: [childPanel]
            }]

       });
       
   }
   
});

