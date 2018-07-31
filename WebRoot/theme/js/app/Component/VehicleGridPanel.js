/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.VehicleGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'VehicleGridPanel_id'},
    constructor:function(){
        var gp = this;
        var store = Ext.ajaxModelLoader('MyApp.Model.ListVehicle',{params: {flag:0}});
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
                {xtype: 'rownumberer', text:'序', width:'3%'},
                {text:'设备码', dataIndex: 'identifier', sortable: false, menuDisabled:true, width:'10%'},
                {text:'车牌', dataIndex:'plate', sortable: false, menuDisabled:true, width:'10%'},
                {text:'所属公司', dataIndex:'teamName', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){ return v ? v : '未分组车辆'}},
                {text:'车型', dataIndex:'type', sortable: false, menuDisabled:true, width:'10%'},
                {text:'品牌', dataIndex:'brand', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){return v ? v  : '--'}},
                {text:'司机', dataIndex: 'driverName', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){return v ? v : '--'}},
                {text:'司机电话', dataIndex:'driverTel', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){ return v ? v : '--'}},
                {text:'录入时间', dataIndex: 'regTime', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){return v ? v.substr(0,11) : '--'}},
                {text:'车辆状态', dataIndex:'online', sortable: false, menuDisabled:true, width:'10%',
                	renderer:function(v){ 
                		if (v==1) return '维修中';
                		else if (v==2) return '已派出';
                		else return '空闲'}
                },
                {text:'操作', dataIndex:'online', sortable: false, menuDisabled:true, width:'10%',
                	renderer:function(v){
                		return '<a class="btn">切换状态</a>';
                	}}
            ],
           
            tbar: [{
                text: '添加车辆',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveCarWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_id',
                text: '删除车辆',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选车辆，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while((model = selModels.shift())){keys.push(model.get('vehicleId'));}
                            Ext.ajaxModelLoader('MyApp.Model.DelVehicle',{target: gp, params: {vehicleIds:keys.join(',')}}).request();   
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
                    Ext.create('MyApp.Component.SaveCarWindow');
                }
            },{
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
                	var plate = Ext.getCmp("platekey").getValue();
                	var identifier = Ext.getCmp("identifierkey").getValue();
                    store.load({params:{start:0,limit:20, plate:plate, identifier:identifier}});
                }
            },{
                xtype:'tbfill'
            },{
                text:'导出报表',
                icon:'theme/imgs/ico_excel.png',
                handler:function(){
                    Ext.Msg.confirm('系统确认','您确定要导出当前报表吗？',function(opt){
                        if(opt=='yes') {
                        	window.open('api.action?cmd=car_excel');
                        }
                    });
                }
            },{
                xtype:'tbfill'
            },{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }],
            listeners:{
            	cellClick: function(thisTab, td, cellIndex,record,tr,rowIndex,event,eventObj) {
                    if(cellIndex == 10) {
                        var extTarget = Ext.get(event.target);
                        if(!extTarget.hasCls('btn')) return;
                        
                        var vehicleId = record.get('vehicleId');
                    	if(!Ext.getCmp('carStatusWindow_id')){
                    		Ext.create('MyApp.Component.CarStatusWindow');
                    		Ext.getCmp('carStatusWindow_id').setTitle("车辆状态设置");
                			Ext.getCmp('carStatusWindow_id').setValue(vehicleId);
                    	}
                    }
                },
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
                    Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
                },
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                },
                 headerClick:function(g,index,e){
                 this.store.reload({params:{sortF:index.dataIndex}});
                }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



