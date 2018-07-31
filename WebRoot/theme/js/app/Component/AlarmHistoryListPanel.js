/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.AlarmHistoryListPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'AlarmHistoryListPanel_id'},
    constructor:function(){
        var gd = this;
    	
        var alarmPanel = Ext.getCmp('AlarmListPanel_id');
        var alarmModel = null;
        if(alarmPanel != null || alarmPanel != undefined){
        	alarmModel = alarmPanel.getCheckedModel();
        }else if(alarmPanel == null || alarmPanel == undefined){
        	alarmModel = Ext.getCmp('ComInfoPanel_id').getCheckedModel();
        }
        var vehicleId = alarmModel.get('vehicleId');
        
        var store = Ext.ajaxModelLoader('MyApp.Model.HistoryAlarm',{params:{vehicleId:vehicleId, type: alarmPanel.getFilterTypes()}});
       
        var setBlod = function(val, reader){
        	if(reader == null || reader == ''){
        		return '<b>'+val+'</b>';
        	}else{
        		return val;
        	}
        };
        
        // 百度地图API功能
        var myGeo = new BMap.Geocoder();
        
        function bdGEO(){
        	for(var i=0;i < adds.length;i++){
	            var pt = adds[i];
	            geocodeSearch(pt, i);
        	}
        };
         
        function bdGEO1(adds ,index){
        	for(var i=0;i < adds.length;i++){
	            var pt = adds[i];
	            geocodeSearch(pt, index);
        	}
        };
         
        function geocodeSearch(pt, index){
            myGeo.getLocation(pt, function(rs){
                var addComp = rs.addressComponents;
                var address = addComp.province + 
                 			  addComp.city + 
                 			  addComp.district + 
                 			  addComp.street + 
                 			  addComp.streetNumber;
                
                store.getAt(index).set('address', address);
               
                if(index == store.getCount()-1){
                	store.sync();
                }
            });
        };
         
        function getMapAddress(x ,y, index){
         	var adds = [
 	            new BMap.Point(x, y)
 	        ];
 	        bdGEO1(adds, index);
        }
         
        store.on('load',function(){
        	//刷新报警信息数量
        	Ext.ajaxModelLoader('MyApp.Model.GetAlarmCount').request();
        	for(var i = 0;i < store.getCount();i++){
        		var x = store.getAt(i).get('x');
        		var y = store.getAt(i).get('y');
        		//通过地图坐标点获取百度地图位置详细信息
        		getMapAddress(x, y, i);
        	}
        });
        
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            //width:'100%',
            //maxWidth:'100%',
            region: 'center',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
                {text:'司机', dataIndex: 'driver', sortable: false, menuDisabled:true, width:'15%', 
                	renderer:function(val, x, store){ 
                		if(val != null){
                			return setBlod(val.driverName, store.data.reader);
                		}
                	}
                },
                {text:'报警原因', dataIndex:'reason', sortable: false, menuDisabled:true, width:'15%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.reader);
                	}
                },
                {text:'信息处理人', dataIndex:'reader', sortable: false, menuDisabled:true, width:'15%',
                	renderer:function(val, x, store){ 
                		if(val == null || val == 'null') val = '';
                		return setBlod(val, store.data.reader);
                	}
                },
                {text:'报警时间', dataIndex:'time', sortable: false, menuDisabled:true, width:'25%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.reader);
                	}
                },
                {text:'地址', dataIndex:'address', sortable: false, menuDisabled:true, width:'30%'}
            ],
            /*
            tbar: [{
            		xtype:'textfield',
            		id: 'plateCondition',
        			//style: 'margin: 5px 12px 3px 5px',
        			labelWidth: 55,
        			width:180,
        			fieldLabel: '车牌筛选',
        			name: 'plateCondition',
	                handler:function(){
	                    //store.removeAll();
	                }
            	}
            ],*/
            bbar: Ext.create('Ext.PagingToolbar', {
				store: store,
				displayInfo: true
	   	    })
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});



