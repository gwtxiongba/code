/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.CarusesGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'CarusesGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListCaruses');
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer', text:'序'},
                //{text:'id', dataIndex: 'id', sortable: false, menuDisabled:true, width:'25%'},
                {text:'用车类型', dataIndex: 'name', sortable: false, menuDisabled:true, width:'20%'},
                {text:'操作', dataIndex:'id', sortable: false, menuDisabled:true, width:'80%',
                	renderer:function(v){
                		return '<a class="btn edit">编辑</a>　<a class="btn del">删除</a>';
                	}}
            ],
           
            tbar: [
                   {
                id: 'add_id',
                text: '添加',
//                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveCarusesWindow',0);
                }
            },{
                xtype: 'tbseparator'
            },
            {
                xtype:'tbfill'
            },{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }],
            listeners:{
            	cellClick: function(thisTab, td, cellIndex,record,tr,rowIndex,event,eventObj) {
                    if(cellIndex == 2) {
                        var extTarget = Ext.get(event.target);
                        if(!extTarget.hasCls('btn')) return;
                        if(extTarget.hasCls('edit')) {
                            Ext.create('MyApp.Component.SaveCarusesWindow',record.get('id'));
                        }
                        
                        if(extTarget.hasCls('del')) {
                            Ext.Msg.confirm('系统提示','确认是否删除该用车类型',function(op){
                                if(op == 'yes') {
                                    Ext.ajaxModelLoader('MyApp.Model.DelCaruses', {
                                        target:gp,
                                        params:{id: record.get('id')}
                                    }).request();
                                }
                            });
                        }
                    }
                },
            	
              beforerender:function(){
                  var tab = top.Ext.getCmp('apptabs').getActiveTab();
                  this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                  this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
              }
          }
        });
    }
})



