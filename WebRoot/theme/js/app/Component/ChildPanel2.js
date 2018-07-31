/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.ChildPanel2',{
   extend:'Ext.tab.Panel',
   constructor:function(){
       var carTree =  Ext.create('MyApp.Component.DeptTreePanel');
       this.superclass.constructor.call(this,{
           id:'leftTabPanel1',
           //region: 'center',
           //xtype: 'tabpanel',      
           border: false,
           layout: 'fit',
           items: [{//车辆列表
                   title: '组织架构',
                   layout: 'fit',
                   itemId: 'carList',
                   items: [carTree]
            }],
            listeners:{
                tabchange:function(tabPanel, newCard, oldCard, eOpts){
                    if(newCard.title == '组织架构') {
                    } else {
                        G_MonitMap.setVisible([]);
                    }
                }
            }

       });
       
   }
   
});

