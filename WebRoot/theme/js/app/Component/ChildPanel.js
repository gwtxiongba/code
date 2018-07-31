/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.ChildPanel',{
   extend:'Ext.tab.Panel',
   constructor:function(){
       var carTree =  Ext.create('MyApp.Component.CarTreePanel');
       var carSelectPanel = Ext.create('MyApp.Component.CarSelectPanel');
       var carControlPanel = Ext.create('MyApp.Component.CarControlPanel');
       this.superclass.constructor.call(this,{
           id:'leftTabPanel',
           //region: 'center',
           //xtype: 'tabpanel',      
           border: false,
           layout: 'fit',
           items: [{//车辆列表
                   title: '车辆列表',
                   layout: 'fit',
                   itemId: 'carList',
                   items: [carTree]
            }, {//查询结果
                   title: '查询结果',
                   itemId: 'selectResult',
                   layout: 'fit',
                   height: 'auto',
                   items:[carSelectPanel]
            }, {//监控列表
                   title: '监控列表',
                   layout: 'fit',
                   itemId: 'controlList',
                   items:[carControlPanel]
            }],
            listeners:{
                tabchange:function(tabPanel, newCard, oldCard, eOpts){
                    if(newCard.title == '监控列表') {
                        var st = carControlPanel.getStore();
                        var v = [];
                        st.each(function(){
                           v.push(this.get('identifier'));
                        });
                        G_MonitMap.setVisible(v);
                    } else {
                        G_MonitMap.setVisible([]);
                    }
                }
            }

       });
       
   }
   
});

