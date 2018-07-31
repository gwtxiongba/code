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
                region: 'north',
                layout: 'table',
                border: false,
                height: 35,
                items: [{
                    style: 'margin: 5px 12px 3px 5px',
                    xtype: 'textfield',
                    width: 155,
                    id: 'selectCarNo',
                    emptyText: '请输入要查询的车牌号码',
                    hideLabel: true
                },{
                    minWidth: 60,
                    xtype: 'button',
                    text: '查询',
                    handler: function(){
                        Ext.getCmp('leftTabPanel').setActiveTab(1);
                       // var resultStore = Ext.getCmp('CarSelectPanel_id').getStore();
                        Ext.getCmp('CarSelectPanel_id').searchData();
                    }
                }]
            },{
				region: 'center',
				layout: 'fit',
				border: false,
				items: [childPanel]
            }]

       });
       
   }
   
});

