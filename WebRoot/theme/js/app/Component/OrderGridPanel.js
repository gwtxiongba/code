/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.OrderGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'OrderGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListOrder');
        
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
                //{text:'司机ID', dataIndex: 'driverId', sortable: false, menuDisabled:true, width:'15%'},
                {text:'司机', dataIndex: 'driverName', sortable: true, menuDisabled:true, width:'18%'},
                {text:'账号', dataIndex: 'loginName', sortable: true, menuDisabled:true, width:'10%'},
                {text:'联系电话', dataIndex: 'driverTel', sortable: true, menuDisabled:true, width:'20%'},
                {text:'驾照', dataIndex: 'license', sortable: false, menuDisabled:true, width:'20%'},
                {text:'备注', dataIndex:'remark', sortable: false, menuDisabled:true, width:'32%'}
            ],
           
            tbar: [{
                text: '添加司机',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveDriverWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_id',
                text: '删除司机',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选司机，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while((model = selModels.shift())){keys.push(model.get('driverId'));}
                            Ext.ajaxModelLoader('MyApp.Model.DelDriver',{target: gp, params: {driverIds:keys.join(',')}}).request();   
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改司机信息',
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveDriverWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                xtype:'tbfill'
            },{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }],
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
                    Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
                },
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



