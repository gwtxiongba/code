/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.OrderHistoryOverGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'OrderHistoryOverGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListOrder',{params:{status:1}});
        
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
                {text:'乘车人姓名', dataIndex: 'carUserName', sortable: true, menuDisabled:true, width:'10%'},
                {text:'起始时间', dataIndex: 'beginTime', sortable: true, menuDisabled:true, width:'15%'},
                {text:'还车时间', dataIndex: 'endTime', sortable: true, menuDisabled:true, width:'15%'},
                {text:'上车地点', dataIndex: 'beginAddr', sortable: false, menuDisabled:true, width:'17%'},
                {text:'下车地点', dataIndex:'endAddr', sortable: false, menuDisabled:true, width:'17%'},
                {text:'预约电话', dataIndex:'carUserTel', sortable: true, menuDisabled:true, width:'10%'},
                 {text:'状态', dataIndex:'status', sortable: false, menuDisabled:true, width:'16%',renderer:function(v){
                     
                     if(v == 7){
                       return '已完成';
                     }else if(v == -2){
                       return '公司领导已拒绝';
                     }else if(v == -1){
                       return '管理员已拒绝';
                     }else if(v == -3){
                       return '部门领导已拒绝';
                     }else if(v == -5){
                       return '已取消';
                     }else if(v == -4){
                       return '车队长已拒绝';
                     }
                 }}
            ],
           
            tbar: [{
                text: '添加预约',
                hidden:true,
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveOrderWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id:'ss_name',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '乘车人姓名',
                labelAlign:'right'
            },{
                id:'ss_tel',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '联系电话',
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                	var ss_name = Ext.getCmp("ss_name").getValue();
                	var ss_tel = Ext.getCmp("ss_tel").getValue();
                    store.load({params:{start:0,limit:20, ss_name:ss_name,ss_tel:ss_tel}});
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
                },
                 itemDblClick: function(thisTab, td, cellIndex,record,tr,rowIndex,event,eventObj) {
                       Ext.create('MyApp.Component.DetailOrderWindow','OrderHistoryOverGridPanel_id');
                 }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



