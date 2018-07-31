/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.BrandGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'BrandGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListBrandPage');
        
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
                {text:'品牌', dataIndex: 'name', sortable: false, menuDisabled:true, width:'20%'},
                {text:'操作', dataIndex:'id', sortable: false, menuDisabled:true, width:'80%',
                	renderer:function(v){
                		return '<a class="btn edit">编辑</a>　<a class="btn del">删除</a>';
                	}}
            ],
           
            tbar: [
                   {
                id: 'add_id',
                text: '添加',
                handler: function(){
                    Ext.create('MyApp.Component.SaveBrandWindow',0);
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
                            Ext.create('MyApp.Component.SaveBrandWindow',record.get('id'));
                              gp.getSelectionModel().deselectAll();//清空选择框
                        }
                        
                        if(extTarget.hasCls('del')) {
                            Ext.Msg.confirm('系统提示','确认是否删除该品牌',function(op){
                                if(op == 'yes') {
                                    Ext.ajaxModelLoader('MyApp.Model.DelBrand', {
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



