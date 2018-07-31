/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.WeixiuGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'WeixiuGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListWeixiu',{params:{status:1}});
        
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
                {text:'', dataIndex: 'id', sortable: false, hidden:true,},
                {text:'车牌', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'10%'},
                {text:'维修费用', dataIndex: 'fee', sortable: false, menuDisabled:true, width:'10%'},
                {text:'维修时间', dataIndex: 'time', sortable: false, menuDisabled:true, width:'10%'},
                {text:'维修原因', dataIndex: 'reason', sortable: false, menuDisabled:true, width:'15%'},
                {text:'维修人', dataIndex:'name', sortable: false, menuDisabled:true, width:'10%'},
                {text:'电话', dataIndex:'tel', sortable: false, menuDisabled:true, width:'10%'},
                {text:'拒绝原因', dataIndex:'reason2', sortable: false, menuDisabled:true, width:'15%'},
                {text:'审批状态', dataIndex:'status', sortable: false, menuDisabled:true, width:'20%',
                	renderer:function(v){
                		if(v==1)  return '审批通过';
                		else if(v==-1) return '已拒绝'
                	}}
            ],
           
            tbar: [
//                   {
//                id: 'updateBtn_id',
//                text: '修改司机信息',
//                disabled: true,
//                handler: function(){
//                    Ext.create('MyApp.Component.SaveDriverWindow');
//                }
//            },
			{
                id:'ss_plate',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '车牌',
                labelAlign:'right'
            },
            {
                id:'ss_name',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '维修人',
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                	var ss_name = Ext.getCmp("ss_name").getValue();
                	var ss_plate = Ext.getCmp("ss_plate").getValue();
                    store.load({params:{start:0,limit:20, ss_plate:ss_plate,ss_name:ss_name}});
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
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                }
            }
        });
    },
})



