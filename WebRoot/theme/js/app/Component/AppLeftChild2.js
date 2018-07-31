/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AppLeftChild2',{
   extend:'Ext.panel.Panel',
   constructor:function(){
       var childPanel =  Ext.create('MyApp.Component.ChildPanel2');
       this.superclass.constructor.call(this,{
           id:'leftChild2',
           title: '组织架构',
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

