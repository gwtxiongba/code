/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.CarControlPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'CarControlPanel_id'},
    constructor:function(){
        var gd = this;
    	
        var store = Ext.ajaxModelLoader('MyApp.Model.ListMonitor');
    
        var selModel = Ext.create('Ext.selection.CheckboxModel',{
            injectCheckbox:1
        });
       
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            //width:'100%',
            //maxWidth:'100%',
            autoScroll: false,
            selModel:selModel,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
                {text:'车牌号码', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'40%'},
                {text:'设备识别码', dataIndex:'identifier', sortable: false, menuDisabled:true, width:'40%'}
            ],
            /*tbar: [{
                xtype: 'checkbox',
                boxLabel: 'GPS过滤(仅显示列表车辆GPS)',
                name: 'controlCheck',
                checked: true,
                handler:function(){

                }
            }],*/
            listeners:{
            	'itemcontextmenu':function(view,record,items,index,e,obj){
                    e.preventDefault();
                    e.stopEvent();
                    
                    //获取所有选中项
                    var selData = gd.getSelectionModel().getSelection();
                    
                    var recmenu = new Ext.menu.Menu({
                        floating:true,
                        items:[{
                            text:'车辆定位',
                            handler:function(){
                            	var identifier = selData[0].get('identifier');
            	                if(identifier) G_MonitMap.getInfo(identifier);
                            }
                        },
                        /*{
                            text:'发送调度',
                            handler:function(){
                            	var w = Ext.getCmp("sendMsgWindow_id");
							        if(w){
							           w.close();
							           Ext.create('MyApp.Component.SendMsgWindow');
							        }else
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
                        },*/{
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
                        }/*,{
                        	text:'车辆操作指令',
                        	handler:function(){
                            	var plate = selData[0].data.text;
                        		var identifier = selData[0].get('identifier');
//                            	var identifier = "153117990";
                        		var first = identifier.charAt(0);
                        		if(first != '5'){
                        			return Ext.MessageBox.alert('提示信息:','车辆没有该操作');
                        		}
                        		
                        		var store = Ext.ajaxModelLoader('MyApp.Model.TelphoneOder',{params:{identifier:identifier}});
                        		store.on('load',function(store){
				                   	var tel = store.getAt(0).get('phoneno');
                            		return Ext.MessageBox.alert(plate + "指令提示内容:","发送短信 2220000 到"+ tel +" 可实现短信断电操作<br/>发送短信 2320000 到"+ tel +" 可实现短信供电操作<br/><br/><span style='color:red'>警告：断电功能会切断车辆油路供电，使车辆无法继续行驶。<br/>可能造成车辆事故,请确保在安全场景下使用!</span>");
				               });
                            }
                       	},{
                       		text:'油电操作',
                        	handler:function(){
                            	var state;
                            	var identifier = selData[0].get('identifier');
                            	var ifOff = selData[0].get('ifOff');
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
                       	}*/,{
                        	text:'实时车况监测',
                                handler:function(){
                                    if(!Ext.getCmp('OBDMonitorWindow_id')){
                                            var vehid = record.get('vehicleId');
                                            var plate = record.get('plate');
                                            var identifier = record.get('identifier');
                                            Ext.create('MyApp.Component.OBDMonitorWindow').setValue(vehid,plate,identifier);
                                    }
                            	}
                        },{
                            text:'从监控列表删除',
                            handler:function(){
                                var vehicleIds = [];
                                //获取所有选中项,将ID放入数组
                                var selModel = gd.getSelectionModel();
                                if(selModel.hasSelection()){
                                	var selected = selModel.getSelection();
                                	Ext.each(selected, function (rec) {
                                		vehicleIds.push(rec.data.vehicleId);
                                    });
                                }
                                
                                Ext.ajaxModelLoader('MyApp.Model.SaveMonitor',{
                                    params:{
                                    	vehicleIds:vehicleIds.join(','),
                                    	//修改是否监控属性,0为不监控,1为监控
                                    	ifMonitor:0
                                    }
                                }).request();
                            }
                        }]
                    });
                    recmenu.showAt(e.getXY());
                    
                    if(selData[0].get('ifRelay')==0 || selData[0].get('ifRelay')=="" || selData[0].get('ifRelay')== null || selData[0].get('ifRelay') == undefined){
                    	recmenu.items.each(function(item){   
                    		if(item.text =='油电操作'){
                            	//item.disable(); 
                    		}
                    	});
                    }
                    
                    if(selData.length > 1){
                    	recmenu.items.each(function(item){   
                    		if(item.text == '车辆定位' || item.text == '车辆司机规则信息' || item.text == '实时车况监测' || item.text =='油电操作'){
                            	//item.disable();   
                    		}
                    	});
                    }
                    
            	},
            	'itemdblclick': function(scope, record, item, index, e, eOpts ){
            		var selData = gd.getSelectionModel().getSelection()[0];
	                var identifier = selData.get('identifier');
	                if(identifier) G_MonitMap.getInfo(identifier);
	            }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});



