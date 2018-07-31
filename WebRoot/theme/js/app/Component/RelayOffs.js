/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.RelayOffs',{
    extend:'Ext.window.Window',
    config:{id:'RelayOffs_id'},
    constructor:function(){
       var w = this;

       var store = Ext.ajaxModelLoader('MyApp.Model.RelayOffs');
	   
       var selModel = Ext.create('Ext.selection.CheckboxModel',{
            injectCheckbox:1
        });
       
	   var gridPanel = Ext.create('Ext.grid.Panel',{
	    	 id:'gdOffs',
	    	 selModel:selModel,
	    	 store: store,
             columns: [
                {xtype: 'rownumberer', text:'序'},
                {text:'车牌', dataIndex:'plate', sortable: true, menuDisabled:true, width:'30%'},
                {text:'设备码', dataIndex: 'identifier', sortable: true, menuDisabled:true, width:'35%'},
                {text:'油电状态', dataIndex:'ifOff', sortable: true, menuDisabled:true, width:'23%',renderer:function(v){return v==1 ? '断开' : '连通'}}
              ],
              tbar: [{
                  id:'platekey',
                  xtype: 'textfield',
                  labelWidth: 30,
                  width:110,
                  fieldLabel : '车牌',
                  labelAlign:'right'
              },{
                  id:'identifierkey',
                  xtype: 'textfield',
                  labelWidth: 80,
                  width:170,
                  fieldLabel : '设备识别码',
                  labelAlign:'right'
              },{
                  text: '查询',
                  border: true,
                  handler: function(){
                  	var plate = Ext.getCmp("platekey").getValue();
                  	var identifier = Ext.getCmp("identifierkey").getValue();
                      store.load({params:{plate:plate, identifier:identifier}});
                  }
              },{
                  xtype:'tbfill'
              }],
              bbar: Ext.create('Ext.PagingToolbar', {
  				store: store,
  				displayInfo: true
  	   	      }),
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
                        	text:'油电操作',
                        	handler:function(){
                        		var state;
                            	var identifier =  selData[0].get('identifier');
                            	var ifOff =  selData[0].get('ifOff');
                            	if(identifier) G_MonitMap.getInfo(identifier);
                            	if(!Ext.getCmp('orderWindowShow_id')){
                            		Ext.create('MyApp.Component.OrderWindow');
                            		if(ifOff == 0){
                            			state = 1;
                            			Ext.getCmp('orderWindowShow_id').setTitle("断油/断电操作");
                            		}else{
                            			state = 0
                            			Ext.getCmp('orderWindowShow_id').setTitle("供油/供电操作");
                            		}
                        			Ext.getCmp('orderWindowShow_id').setValue(identifier,state);
                            	}
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
            title: '油电控制',
            width:420,
	    	height: 395,
	    	autoScroll: true, 
	        scroller:'vertical',
		    resizable: false,
		    layout:"fit",
            items:[gridPanel],
            listeners:{
            	'afterlayout': function(){
//		        	if(Ext.getCmp('VehicleOnlines_id').getHeight()<395){
//		      			Ext.getCmp('gdOnline').setWidth(400)}
              		Ext.getCmp('RelayOffs_id').center();
              	
              	}
            }
        });
         this.show();
    }
});



