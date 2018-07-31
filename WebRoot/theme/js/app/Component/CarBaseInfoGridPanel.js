/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.CarBaseInfoGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'CarBaseInfoGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListBaseInfo');
        
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
                {xtype: 'rownumberer', text:'序', width:'2%'},
                {text:'车牌', dataIndex:'user', sortable: false, menuDisabled:true, width:'15%',renderer:function(v){return v.plate}},
                {text:'品牌', dataIndex:'brand', sortable: false, menuDisabled:true, width:'15%'},
                {text:'核载重量(吨)', dataIndex: 'weight', sortable: false, menuDisabled:true, width:'15%'},
                {text:'核载人数(人)', dataIndex: 'totalNumber', sortable: false, menuDisabled:true, width:'15%'},
                {text:'生产日期', dataIndex: 'prcTime', sortable: false, menuDisabled:true, width:'20%',renderer:function(v) {return Ext.Date.format(new Date(v),'Y-m-d')}},
                {text:'年审日期', dataIndex: 'limTime', sortable: false, menuDisabled:true, width:'20%',renderer:function(v) {return Ext.Date.format(new Date(v),'Y-m-d')}}
            ],
           
            tbar: [{
                text: '添加车辆',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveCarBaseInfoWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                text: '删除车辆',
                id: 'deleteBtn_id',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选车辆，是否继续？',function(btn){
                    	if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            Ext.ajaxModelLoader('MyApp.Model.DelBaseInfo',{
                            target: gp,
                            params: {
	                           baseId : selModels[0].get('baseInfoId'),
	                           userId : selModels[0].get('user').vehicleId
                            }}).request();   
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改车辆信息',
                disabled: true,
                handler: function(){
					Ext.create('MyApp.Component.SaveCarBaseInfoWindow');
					Ext.getCmp('plateCombox').setReadOnly(true);
                	var selModels = gp.getSelectionModel().getSelection();
                	var baseId = selModels[0].get('baseInfoId');
//                	var brand = selModels[0].get('brand');
                	var userId = selModels[0].get('user').vehicleId;
                	var plate = selModels[0].get('user').plate;
                	var weight = selModels[0].get('weight');
                	var totalNumber = selModels[0].get('totalNumber');
                	var prcTime = selModels[0].get('prcTime');
                	var limTime = selModels[0].get('limTime');
                	var brand = selModels[0].get('brand');
                	Ext.getCmp('SaveCarBaseInfoWindow_id').setBaseValue(baseId,userId,plate,weight,totalNumber,prcTime,limTime,brand);
					
                }
            }
           /* ,{
                xtype: 'tbseparator'
            },{
                id:'platekey',
                xtype: 'textfield',
                labelWidth: 40,
                fieldLabel : '车牌',
                labelAlign:'right'
            },{
                id:'identifierkey',
                xtype: 'textfield',
                labelWidth: 75,
                fieldLabel : '设备识别码',
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                	
                }
            }*/,
            {
                xtype:'tbfill'
            },{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }
            ],
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



