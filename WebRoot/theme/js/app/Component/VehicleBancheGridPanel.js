/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.VehicleBancheGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'VehicleBancheGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListVehicle',{params: {flag:1}});
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            maxWidth:'100%',
            //height: tab.getEl().down('iframe').dom.scrollHeight,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer', text:'序', width:'3%'},
                //{text:'车辆ID', dataIndex: 'vehicleId', sortable: true, menuDisabled:true, width:'13%'},
                {text:'设备码', dataIndex: 'identifier', sortable: false, menuDisabled:true, width:'10%'},
                {text:'车牌', dataIndex:'plate', sortable: false, menuDisabled:true, width:'10%'},
                {text:'班车序号', dataIndex:'carModel', sortable: false, menuDisabled:true, width:'10%'},
                {text:'所属机构', dataIndex:'teamName', sortable: false, menuDisabled:true, width:'20%',renderer:function(v){ return v ? v : '未分组车辆'}},
                {text:'品牌', dataIndex:'brand', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){return v ? v  : '--'}},
                //{text:'排量', dataIndex:'discc', sortable: false, menuDisabled:true, width:'10%', renderer:function(v){return v ? v  : '--'}},
                
                {text:'司机', dataIndex: 'driverName', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){return v ? v : '--'}},
                {text:'司机电话', dataIndex:'driverTel', sortable: false, menuDisabled:true, width:'15%',renderer:function(v){ return v ? v : '--'}},
                {text:'录入时间', dataIndex: 'regTime', sortable: false, menuDisabled:true, width:'15%',renderer:function(v){return v ? v.substr(0,11) : '--'}},
                //{text:'购买时间', dataIndex: 'buyTime', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){return v ? v.substr(0,11) : '--'}},
             
                {text:'操作', dataIndex:'lineGps', sortable: false, menuDisabled:true, width:'10%',
                	renderer:function(v){
//                		if (v==1) return '<a class="btn good">切换状态</a>';
//                		else if (v==2) return '--';
//                		else return '<a class="btn bad">切换状态</a>'
                		return '<a class="btn">查看路线</a>';
                	}}
                
                //{text:'指令码', dataIndex: 'cutpwd', sortable: false, menuDisabled:true, width:'15%'}
            ],
           
            tbar: [{
                text: '添加车辆',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveBancheWindow');
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
                    Ext.create('MyApp.Component.SaveBancheWindow');
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
                 hidden:true,
                icon:'theme/imgs/ico_excel.png',
                //scale:'medium',
                handler:function(){
                    Ext.Msg.confirm('系统确认','您确定要导出当前报表吗？',function(opt){
                        if(opt=='yes') {
                            //这里添加下载地址
//                            var date = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
//                           var p = Ext.getCmp('vehicleId').getValue();
//                            var plate = Ext.getCmp('plate').getValue();
//                              var index= getIndex(p);
//                             var  vehicleId= list[index].id;
//                            window.open('api.action?cmd=formdLocation_excel&date='+date+'&plate='+plate+'&vehicleId='+vehicleId);
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
                    if(cellIndex == 9) {
                        var extTarget = Ext.get(event.target);
                        if(!extTarget.hasCls('btn')) return;
                        var lineGps = record.get('lineGps');
                         Ext.create('MyApp.Component.BancheLineWindow',lineGps);
                    	//if(!Ext.getCmp('carStatusWindow_id')){
                    	//	Ext.create('MyApp.Component.CarStatusWindow');
                    		//Ext.getCmp('carStatusWindow_id').setTitle("车辆状态设置");
                			//Ext.getCmp('carStatusWindow_id').setValue(vehicleId);
                    	//}
//                        if(extTarget.hasCls('good')) {
//                                Ext.Msg.confirm('系统提示','请确认是否切换车辆状态为:空闲',function(opt){
//                                	if(opt=='yes')
//                                		//alert(record.get('id'));
//                                    Ext.ajaxModelLoader('MyApp.Model.SaveVehicle',{
//                                        target: gp, 
//                                        params: {online:'0',vehicleId:record.get('vehicleId')}
//                                    }).request();
//                                });
//                             
//                        };
//                        if(extTarget.hasCls('bad')) {
//                            Ext.Msg.confirm('系统提示','请确认是否切换车辆状态为:维修中',function(opt){
//                            	if(opt=='yes')
//                            		//alert(record.get('id'));
//                                Ext.ajaxModelLoader('MyApp.Model.SaveVehicle',{
//                                    target: gp, 
//                                    params: {online:'1',vehicleId:record.get('vehicleId')}
//                                }).request();
//                            });
//                         
//                    }
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
                  // alert(index.dataIndex);
                   
                 //Ext.ajaxModelLoader('MyApp.Model.ListVehicle');
                //this.store.setParams("sortF", "p11");
                //if(index.dataIndex == 'teamName' || index.dataIndex == 'type' || index.dataIndex == 'brand' || index.dataIndex == 'driverName'){
                 this.store.reload({params:{sortF:index.dataIndex}});
                 //}
                }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



