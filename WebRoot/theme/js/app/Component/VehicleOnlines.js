/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.VehicleOnlines',{
    extend:'Ext.window.Window',
    config:{id:'VehicleOnlines_id'},
    constructor:function(){
       var ww = this;

       var store = Ext.ajaxModelLoader('MyApp.Model.VehicleOnlines');
	   
       var selModel = Ext.create('Ext.selection.CheckboxModel',{
            injectCheckbox:1
        });
       
	   var gridPanel = Ext.create('Ext.grid.Panel',{
	    	 id:'gdOnline',
	    	// maxwidth:400,
//	    	 autoScroll: true, 
//	         scroller:'vertical',
	    	 selModel:selModel,
	    	 store: store,
             columns: [
                {xtype: 'rownumberer', text:'序'},
                //{text:'车辆ID', dataIndex: 'vehicleId', sortable: true, menuDisabled:true, width:'30%'},
                {text:'车牌', dataIndex:'plate', sortable: true, menuDisabled:true, width:'40%'},
                {text:'设备码', dataIndex: 'identifier', sortable: true, menuDisabled:true, width:'47%'}
              ],
              listeners:{
            	'itemcontextmenu':function(view,record,items,index,e,obj){
                    e.preventDefault();
                    e.stopEvent();
                    
                    //获取所有选中项
                    var selData = this.getSelectionModel().getSelection();
                    
                    var recmenu = new Ext.menu.Menu({
                        floating:true,
                        items:[{
                            text:'车辆定位',
                            handler:function(){
                            	var identifier = selData[0].get('identifier');
            	                if(identifier) G_MonitMap.getInfo(identifier);
                            }
                        },{
                            text:'发送调度',
                            handler:function(){
                            	Ext.create('MyApp.Component.SendMsgWindow');
                            	
                                //赋值操作
                                var vehicleIds = '';
                                var plates = '';
                                var identifiers = '';
                                //获取所有选中项, 将车辆ID和车牌放入数组
                                //var selData = grid.getSelectionModel().getSelection();
                                Ext.each(selData, function(rec){
                                	vehicleIds += rec.get('vehicleId') + ',';
                                 	plates += rec.get('plate') + ',';
                                 	identifiers += rec.get('identifier') + ',';
                                });
                                
                                vehicleIds =  vehicleIds.substring(0, vehicleIds.length-1);
                                plates =  plates.substring(0, plates.length-1);
                                identifiers =  identifiers.substring(0, identifiers.length-1);
                                Ext.getCmp('sendMsgWindow_id').setKeyAreaValue(vehicleIds, plates, identifiers);
                            	
                            }
                        },{
                            text:'历史回放',
                            handler:function(){
                            	//延时请求历史轨迹
                            	var text = this.text;
                            	var i=0;
                            	function al(){   
                            	   if(i < selData.length){
                            		   var checkData = selData[i];
                            		   setTimeout(function(){
                            			   Ext.getCmp('apptabs').open({title:text+':'+checkData.get('plate'),url:'trace.html?vehicleId='+checkData.get('vehicleId')});
                            			   al();
                            		   },500);
                            	   }
                            	   i++;
                            	}
                            	al();
                            }
                        },{
                        	text:'实时车况监测',
                             handler:function(){
                              if(!Ext.getCmp('OBDMonitorWindow_id')){
                                    var vehId = selData[0].get('vehicleId');
                                    var plate = selData[0].get('plate');
                                    var identifier = selData[0].get('identifier');
                                    Ext.create('MyApp.Component.OBDMonitorWindow').setValue(vehId,plate,identifier);
                                }       
                            }
                        },{
                            text:'加入监控列表',
                            handler:function(){
                                var vehicleIds = [];
                                //获取所有选中项, 将车辆ID和车牌放入数组
                                var seleModel = Ext.getCmp('gdOnline').getSelectionModel();
                                if(seleModel.hasSelection()){
                                	var selected = seleModel.getSelection();
                                	Ext.each(selected, function (rec) {
                                		vehicleIds.push(rec.data.vehicleId);
                                    });
                                }
                                Ext.ajaxModelLoader('MyApp.Model.SaveMonitor',{
                                    params:{
                                    	vehicleIds: vehicleIds.join(','),
                                    	//修改是否监控属性,0为不监控,1为监控
                                    	ifMonitor:1
                                    }
                                }).request();
                            }
                        }]
                    });
                    recmenu.showAt(e.getXY());
                    
                    if(selData.length > 1){
                    	recmenu.items.each(function(item){   
                    		if(item.text == '车辆定位' || item.text == '车辆司机规则信息' || item.text == '实时车况监测'){
                            	item.disable();   
                    		}
                    	});
                    }
            	},
            	'itemdblclick': function(scope, record, item, index, e, eOpts ){
            		var selData = this.getSelectionModel().getSelection()[0];
	                var identifier = selData.get('identifier');
	                if(identifier) G_MonitMap.getInfo(identifier);
	            }
            },
           	getCheckedModel: function() {
		        var models = this.getSelectionModel().getSelection();
		        return models ? models[0] : null;
		    }
        });
        
        this.superclass.constructor.call(this,{
            title: '在线车辆',
            width:400,
	    	maxHeight: 395,
	    	autoScroll: true, 
	        scroller:'vertical',
		    resizable: false,
		    layout:"fit",
            items:[gridPanel],
            listeners:{
            	'afterlayout': function(){
//		        	if(Ext.getCmp('VehicleOnlines_id').getHeight()<395){
//		      			Ext.getCmp('gdOnline').setWidth(400)}
              		Ext.getCmp('VehicleOnlines_id').center();
              	
              	}
            }
        });
         this.show();
    }
});



