/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.ParkingGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'ParkingGridPanel_id',parkingId:null},
    constructor:function(){
        var gp = this;
        var strs =[] ;
        var store = Ext.ajaxModelLoader('MyApp.Model.ListParking');
        this.superclass.constructor.call(this,{
           // renderTo:Ext.getBody(),
            title:'车库信息',
            height:600,
            //layout:'fit',
            frame: false,
            border:true,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
            	{xtype: 'rownumberer', text:'序', width:'5%'},
                {text:'车库名称', dataIndex: 'driver',width:'17%',sortable: false, menuDisabled:true,renderer:function(v){return v.driverName}},
                {text:'车库地址', dataIndex: 'parkingPosition',width:'41%',sortable: false, menuDisabled:true},
                {text:'车库管理员', dataIndex: 'adminName',width:'17%',sortable: false, menuDisabled:true},
                {text:'车库电话', dataIndex: 'parkingTel',width:'20%',sortable: false, menuDisabled:true}
            ], 
            tbar: [{
                text: '添加车库',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveCarParkingWindow');
                }
            },"-",{
            	text: '删除车库',
            	disabled: true,
            	id:'deleteParking_id',
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选库，是否继续？',function(btn){
                        if(btn=='yes') {
                            var selModels = gp.getSelectionModel().getSelection();
                            var parkingId = selModels[0].get('parkingId');
                            var driver = selModels[0].get('driver');
		                	var driverId;
		                	if(!driver){
		                	 driverId = "";
		                	}else{
		                	 driverId = driver.driverId;
		                	}
                            Ext.ajaxModelLoader('MyApp.Model.DelParking',{
                            target: gp,
                            params: {
	                            parkingId:parkingId,
	                            driverId:driverId
                            }}).request();   
                        }
                    });
                }
            },"-",{
            	id:'updateParking_id',
            	text: '修改车库',
            	disabled: true,
                handler: function(){
                     Ext.create('MyApp.Component.SaveCarParkingWindow');
                     Ext.getCmp('parkingName').setReadOnly(true);
                	 var selModels = gp.getSelectionModel().getSelection();
                	 var p_id = selModels[0].get('parkingId');
                	 var p_name = selModels[0].get('driver').driverName;
                	 var p_address = selModels[0].get('parkingPosition');
                	 var admin = selModels[0].get('adminName');
                	 var p_position = selModels[0].get('x')+','+selModels[0].get('y');
                	 var p_tel = selModels[0].get('parkingTel');
                	 var driverId = selModels[0].get('driver').driverId;
                	 Ext.getCmp('SaveCarParkingWindow_id').setParkingValue(p_id,p_name,p_address,p_position,p_tel,admin,driverId);
                }
            },"-",{
            	text: '查看所有车库位置',
            	border: true,
                handler: function(){
				 var data = Ext.getCmp('ParkingGridPanel_id').getStore();
        			document.getElementById('map_frame').contentWindow.addMarker(data);               	
                }
            },{
            	xtype:'panel',//设置空白 
                border:0, 
                width:60
            },{
//           	style: 'margin: 5px 12px 3px 5px',
	            xtype: 'textfield',
	            width: 145,
	            id: 'plateKey',
	            emptyText: '请输入要查询的车牌号码',
	            hideLabel: true
            },{
            	text: '查询',
                border: true,
                handler: function(){
                	var plate = Ext.getCmp('plateKey').getValue();
                	store.load({params:{start:0,limit:20, plate:plate}});
                }
            }
            ],
            bbar:[{
                xtype:'pagingtoolbar',
                border:false,
                aling:'center',
                store:store,
                displayInfo : true
            }],
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteParking_id').setDisabled(!selections.length);
                    Ext.getCmp('updateParking_id').setDisabled(selections.length != 1);
                },
          		'itemcontextmenu':function(view,record,items,index,e,obj){
                    e.preventDefault();
                    e.stopEvent();
                    var selData = this.getSelectionModel().getSelection();
                    var recmenu = new Ext.menu.Menu({
                        floating:true,
                        items:[{
                            text:'车库车辆列表',
                            handler:function(){
                            	if(selData[0].get('driver') == null){
                            		Ext.MessageBox.alert('提示信息:', '车库未绑定管理员');
                            	}else{
                            		Ext.create('MyApp.Component.ParkingCarListWindow');
                            	}
                            }
                        }]
                    });
                    recmenu.showAt(e.getXY());
          		},
          		'itemclick' : function(view,rec,item,index,e,eOpts){
          			document.getElementById('map_frame').contentWindow.parLocation(rec,index);     
          		}
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



