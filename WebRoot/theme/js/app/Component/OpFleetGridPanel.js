/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.OpFleetGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'OpFleetGridPanel_id',thisstore:null},
    constructor:function(){
        var gp = this; 
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListOpTeam');
       
        this.thisstore = store;
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            maxWidth:'100%',
            autoload:true,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer', text:'序'},
                {text:'操作员名称', dataIndex: 'operatorName', sortable: false, menuDisabled:true, width:'30%'},
                {text:'管理车队', dataIndex: 'teams', sortable: false, menuDisabled:true, width:'70%'}
            ],
            tbar: [
            	{
                id: 'updateBtn_id',
                text: '修改操作员权限',
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveOpFleetWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                xtype:'tbfill'
            },{
                //id: 'updateBtn_id',
                text: '刷新列表',
                handler: function(){
                     Ext.getCmp('OpFleetGridPanel_id').getStore().load();
                }
            }],
            listeners:{
                selectionchange : function(sm, selections) {
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



