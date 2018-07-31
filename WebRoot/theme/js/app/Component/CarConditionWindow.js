/* 
 * 实施车况检测
 */
Ext.define('MyApp.Component.CarConditionWindow',{
    extend: 'Ext.window.Window',
    config: {id: 'carConditionWindow_id'},
    constructor: function(){
    	//获取参数值
    	var w = this;
    	var time = 0;
    	var key = "";
    	var state = true;
    	var timeInt;
    	
    	var VehicleOnlines = Ext.getCmp('VehicleOnlines_id');
    	var tree = Ext.getCmp('CarTreePanel_id');
        var select = Ext.getCmp('CarSelectPanel_id')
        var control = Ext.getCmp('CarControlPanel_id');
        var panel = Ext.getCmp('leftTabPanel').getActiveTab().getId();
//        alert(panel)
        if(VehicleOnlines){
        	var vehicle = Ext.getCmp('gdOnline').getCheckedModel();
        	key = vehicle.get('vehicleId');
        }else{
        	if(tree || select || control){
	        	if('panel-1069' == panel){
		        	key = tree.getCheckedModel().get('id');
		        }
		        if('panel-1070' == panel){
		        	key = select.getCheckedModel().get('vehicleId');
		        }
		        if('panel-1071' == panel){
		        	key = control.getCheckedModel().get('vehicleId');
		        }	
       		 }
        }
        
        Ext.ajaxModelLoader('MyApp.Model.ObdSwitch',{
			params:{
				vehicleId : key,
				state : 'true'
			}
		}).request();
        
        //调用后台数据
    	var store = Ext.create('Ext.data.Store', {
               autoLoad: true,
	           autoSync: true,
	           fields:['eSpeed','cSpeed','temperature','engine','oil','voltage','standard','agreement','time','result','in_temperature','air','position']
	       });
	       
        var store1 = Ext.create('Ext.data.Store', {
          // autoLoad: true,
          autoSync: true,
          fields:['eSpeed','cSpeed','temperature','engine','oil','voltage','standard','agreement','time','result','in_temperature','air','position'],
          proxy: {
               type: 'ajax',
               url: CRMApp.Configuration.servicePath + '?cmd=sendObdData&vehicleId='+key,
               reader: {
                   type : 'json'
               }
         },
         autoLoad: true,
         listeners:{
	         load:function(){
	       		var rec = store1.getAt(0);
	       		store.add({
	       			'oil' : rec.get('oil'),
	       			'time' : time,
	       			'eSpeed' : rec.get('eSpeed'),
	       			'cSpeed' : rec.get('cSpeed'),
	       			'temperature' : rec.get('temperature'),
	       			'engine' : rec.get('engine'),
	       			'voltage' : rec.get('voltage'),
	       			'standard': rec.get('standard'),
	       			'agreement': rec.get('agreement'),
	       			'result' : rec.get('result'),
	       			'in_temperature' : rec.get('in_temperature'),
	       			'air' : rec.get('air'),
	       			'position' : rec.get('position')
	       			});
		       	if(rec.get('result') == 0){
	        		Ext.getCmp('obd_1').setValue(rec.get('agreement'));
	        		Ext.getCmp('obd_2').setValue(rec.get('standard'));
	        	}
	        	if(rec.get('result') == 1){
	        		//Ext.MessageBox.alert('提示信息:', '车辆正在上传数据，请点击开始检测...');
	        	}
	        	if(rec.get('result') == 2 || rec.get('result') == 3){
	        		state = false;
	        		Ext.MessageBox.alert('提示信息:', '车辆离线，无法获取obd数据');
	        	}
	       	}
	       	
       }
       }); 
		//折线图
		var chart = Ext.create('Ext.chart.Chart', {
			id : 'eSpeedChart',
			theme: 'Category2',
		   	renderTo: Ext.getBody(),
		   	flex: 1,
//		   	legend: {position:'bottom'},
		   	store: store,
		  	axes: [
		        {
		            title: '发动机转速(r/min)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['eSpeed'],
		            grid: true,
		            minimum: 0,
		            maximum: 5000,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'发动机转速',
		            type: 'Category',
		            position: 'bottom',
		            fields:['time'],
		            labelTitle: {
              			font: '15px Arial'
            		}
		        }],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'eSpeed',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('eSpeed') + " r/min");
					  }
					}
		        }
		    ]
		});
		
		var chart2 = Ext.create('Ext.chart.Chart', {
		   renderTo: Ext.getBody(),
		   flex:1,
		   theme: 'Category2',
		   store: store,
//		   legend: {position:'bottom'},
		   axes: [
		        {
		            title: '车辆速度(km/h)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['cSpeed'],
		            grid: true,
		            minimum: 0,
		            maximum: 240,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'车辆速度',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
		            }
            	}],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'cSpeed',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('cSpeed') + " km/h");
					  }
					}
		        }
		    ]
		});
		
		var chart3 = Ext.create('Ext.chart.Chart', {
			theme: 'Category2',
		   renderTo: Ext.getBody(),
		   flex: 1,
//		   legend: {position: 'bottom'},
		   store: store,
		   axes: [
		        {
		            title: '冷却液温度(℃)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['temperature'],
		            grid: true,
		            minimum: 0,
		            maximum: 120,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'冷却液温度',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
            		}
		        }],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'temperature',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('temperature') + " ℃");
					  }
					}
		        }
		    ]
		});
		
		var chart4 = Ext.create('Ext.chart.Chart', {
		   renderTo: Ext.getBody(),
		   flex:1,
		   theme: 'Category2',
		   store: store,
//		   legend: {position: 'bottom'},
		   axes: [
		        {
		            title: '发动机负荷(%)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['engine'],
		            grid: true,
		            minimum: 0,
		            maximum: 100,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'发动机负荷',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
            		}
		        }],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'engine',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('engine') + " %");
					  }
					}
		        }
		    ]
		});
		
		var chart5 = Ext.create('Ext.chart.Chart', {
			theme: 'Category2',
		    renderTo: Ext.getBody(),
		   flex: 1,
//		   legend: {position: 'bottom'},
		   store: store,
		   axes: [
		        {
		            title: '燃油量(%)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['oil'],
		            grid: true,
		            minimum: 0,
		            maximum: 100,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'燃油量',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
            		}
		        }],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'oil',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('oil') + " %");
					  }
					}
		        }
		    ]
		});
		
		var chart6 = Ext.create('Ext.chart.Chart', {
		   renderTo: Ext.getBody(),
		   flex:1,
		   theme: 'Category2',
		   store: store,
//		   legend: {position: 'bottom'},
		   axes: [
		        {
		            title: '电瓶电压(V)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['voltage'],
		            grid: true,
		            minimum: 0,
		            maximum: 18,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'电瓶电压',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
            		}
		        }],
		    series: [
		        {
		        	title: '电瓶电压(V)',
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'voltage',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('voltage') + " V");
					  }
					}
		        }
		    ]
		});
		
		var chart7 = Ext.create('Ext.chart.Chart', {
			id : 'in_temperatureChart',
			theme: 'Category2',
		   	renderTo: Ext.getBody(),
		   	flex: 1,
//		   	legend: {position:'bottom'},
		   	store: store,
		  	axes: [
		        {
		            title: '进气温度(℃)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['in_temperature'],
		            grid: true,
		            minimum: 0,
		            maximum: 100,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'进气温度',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
            		}
		        }],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'in_temperature',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('in_temperature') + " ℃");
					  }
					}
		        }
		    ]
		});
		
		var chart8 = Ext.create('Ext.chart.Chart', {
		   renderTo: Ext.getBody(),
		   flex:1,
		   theme: 'Category2',
		   store: store,
//		   legend: {position:'bottom'},
		   axes: [
		        {
		            title: '空气流量(g/s)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['air'],
		            grid: true,
		            minimum: 0,
		            maximum: 50,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'空气流量',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
		            }
            	}],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'air',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('air') + " g/s");
					  }
					}
		        }
		        
		    ]
		});
		

		
		var chart9 = Ext.create('Ext.chart.Chart', {
		   renderTo: Ext.getBody(),
		   flex:1,
		   theme: 'Category2',
		   store: store,
//		   legend: {position: 'bottom'},
		   axes: [
		        {
		            title: '节气门绝对位置(%)',
		            type: 'Numeric',
		            position: 'left',
		            fields: ['position'],
		            grid: true,
		            minimum: 0,
		            maximum: 70,
		            labelTitle: {
              			font: '13px Arial'
            		}
		        },{
		        	title:'节气门绝对位置',
		            type: 'Category',
		            position: 'bottom',
		            fields: ['time'],
		            labelTitle: {
              			font: '15px Arial'
            		}
		        }],
		    series: [
		        {
		        	smooth: true,
		            type: 'line',
		            xField: 'time',
		            yField: 'position',
		            fill: true,
		            tips: {
					  trackMouse: true,
					  width: 80,
					  height: 28,
					  renderer: function(storeItem, item) {
					    this.setTitle(storeItem.get('position') + " %");
					  }
					}
		        }
		    ]
		});
		
		//中心面板
		var centerPanel = {
			id:'centerPanel',
//			height:750,
//			width:780,
			xtype:'panel',
			border : false,
			layout: {
                type: 'hbox',
                align: 'center'
            },
            items:[{
                //flex: 1,
                width: 250,
                height: 720,
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                   chart,
                   chart2,
                   chart7
                ]
            }, {
               // flex: 1,
                width: 250,
                height: 720,
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                	chart3,
                    chart4,
                    chart8
                ]
            },{
            //	flex: 1,
                width: 250,
                height: 720,
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                	chart5,
                    chart6,
                    chart9
                ]
            }]
		}
		//tbar
		var carVehicleId = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'vehicle_Id',
            id: 'vehicle_Id'
        };
		
		var carPlate = {
            xtype: 'displayfield',
            labelWidth : 30,
            labelAlign:'right',
            fieldLabel:'车牌:',
            name:'plate',
            id: 'plate'
        };
        
        var carIdentifier = {
            xtype: 'displayfield',
            labelAlign:'right',
            fieldLabel:'设备识别码:',
            name:'identifier',
            id: 'identifier'
        };
        
        var obd1 = {
            xtype: 'displayfield',
            labelAlign:'right',
            fieldLabel:'OBD协议:',
            name:'obd_1',
            id: 'obd_1'
        };
        
        var obd2 = {
            xtype: 'displayfield',
            labelAlign:'right',
            fieldLabel:'OBD标准:',
            name:'obd_2',
            id: 'obd_2'
        };
        
        this.superclass.constructor.call(this,{
            title: '实时车况监测',
            width: 830,
            height: 585,
            resizable: false,
           // closable: false,
            autoScroll:true,
            scroll:'vertical',
//          layout: 'border',
             buttons : [{
                text : '开始监测',
                handler : function() {
                	if(state){
                		Ext.MessageBox.alert('提示信息:', '车况检测开始，每十秒获取一次数据');
	                timeInt = setInterval(function(){
//	                	alert(111111111111111)
					       if(store.getCount()>7){
					       		store.remove(store.getAt(0));   
					       };
					       time+=10;
					       store1.load()
		        		},10000);
                	}
                }
            },{
                text : '关闭',
                handler : function() {
                	Ext.ajaxModelLoader('MyApp.Model.ObdSwitch',{
	                	params:{
	                    vehicleId : Ext.getCmp('vehicle_Id').getValue(),
	                    state : 'false'
	                }
	                }).request();
	                clearInterval(timeInt);
	                w.close();
                }
            }],
            items:[{
            	region:'north',
            	height:40,
            	width:800,
            	border : false,
            	layout: {
				    align: 'middle',
			        pack: 'center',
			        type: 'hbox'
				},
            	items:[carPlate,carIdentifier,obd1,obd2,carVehicleId]
            },{
            	region:'center',
            	height:750,
            	width:800,
            	items:[centerPanel],
            	border : false
            }
//            ,{
//            	region:'west',
//            //	width:100,
//            	border : false
//            }
//            ,{
//            	region:'east',
//            	width:100,
//            	border : false
//            }
            ]
        });
        this.show();
        
    },
    setValue : function(vehicleIdVal, plateVal, identifierVal) {
    	Ext.getCmp('vehicle_Id').setValue(vehicleIdVal);
		Ext.getCmp('identifier').setValue(identifierVal);
		Ext.getCmp('plate').setValue(plateVal);
	}
});



