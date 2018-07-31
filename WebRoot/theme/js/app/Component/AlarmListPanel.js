/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.AlarmListPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'AlarmListPanel_id'},
    constructor:function(){
        var gd = this;
    	var store = null;
        //var store = Ext.ajaxModelLoader('MyApp.Model.ListAlarm');
        
        /*
        var data = [
            {'vehicleId':'111','identifier':'AA101','plate':'aaa11','ifRead':'0','reason':'aaa111','time':'2014-01-01','x':'111','y':'111' },
            {'vehicleId':'222','identifier':'BB202','plate':'bbb22','ifRead':'1','reason':'bbb222','time':'2014-02-02','x':'222','y':'222' }
					];
   
        var store = Ext.create('Ext.data.Store', {
        	fields:['vehicleId','identifier','plate','reason','ifRead','time','x','y'],
            data: data,
        	proxy: {
                type: 'memory',
                reader: 'json'
            },
            autoLoad: true,
            sortInfo: {
                field: 'vehicleId',
                direction: 'ASC'
            }
        });*/
        
        var setBlod = function(val, ifread){
        	if(ifread == 0){
        		return '<b>'+val+'</b>';
        	}else{
        		return val;
        	}
        };
        
        // 百度地图API功能
        // var map = new BMap.Map("l-map");
        // map.centerAndZoom(new BMap.Point(116.328749,40.026922), 13);
        var myGeo = new BMap.Geocoder();
        
        /*
        var adds = [
            new BMap.Point(116.307852,40.057031),
            new BMap.Point(116.313082,40.047674)
        ];

        var s = bdGEO();
         */
         
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
        	/*
            if(index < adds.length){
                setTimeout(window.bdGEO1,300);
            }*/
            //alert(store.getAt(index).get('x'));
            myGeo.getLocation(pt, function(rs){
                var addComp = rs.addressComponents;
                var address = addComp.province + 
                 			  addComp.city + 
                 			  addComp.district + 
                 			  addComp.street + 
                 			  addComp.streetNumber;
              
                store.getAt(index).set('address', address);
                /*
                if(index == store.getCount()-1){
                	store.sync();
                }*/
            });
             
        };
         
        function getMapAddress(x ,y, index){
         	var adds = [
 	            new BMap.Point(x, y)
 	        ];

 	        bdGEO1(adds, index);
        }
        
        var pager = Ext.create('Ext.PagingToolbar', {
            store: store,
            displayInfo: true
        });
        
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            //width:'100%',
            //maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer', width:'4%'},
                {text:'车牌号码', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'15%', 
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.ifRead);
                	}
                },
                {text:'设备识别码', dataIndex:'identifier', sortable: false, menuDisabled:true, width:'15%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.ifRead);
                	}
                },
                {text:'报警原因', dataIndex:'reason', sortable: false, menuDisabled:true, width:'10%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.ifRead);
                	}
                },
                {text:'报警时间', dataIndex:'time', sortable: false, menuDisabled:true, width:'20%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.ifRead);
                	}
                },
                {text:'地址', dataIndex:'address', sortable: false, menuDisabled:true, width:'36%'}
            ],
            
            tbar: [{
                       id:'typefilter',
            		xtype:'checkboxgroup',
                        fieldLabel:'筛选类型',
                        labelAlign: 'right',
                        labelWidth:60,
                        vertical:true,
                        columns:[60,60,70,60,70,80],
                        items:[
                            {boxLabel:'碰撞',name:'type',inputValue:1,checked:true},
                            {boxLabel:'侧翻',name:'type',inputValue:2,checked:true},
                            {boxLabel:'拔电池',name:'type',inputValue:5,checked:true},
                            {boxLabel:'震动',name:'type',inputValue:6,checked:true},
                            {boxLabel:'低电压',name:'type',inputValue:7,checked:true},
                           // {boxLabel:'疲劳驾驶',name:'type',inputValue:8,checked:true},
                            {boxLabel:'超速报警',name:'type',inputValue:16,checked:true},
                        ],
                        listeners:{
                            change:function(){
                                store = Ext.ajaxModelLoader('MyApp.Model.ListAlarm',{params:{type:gd.getFilterTypes()}});
                                gd.bindStore(store);
                                pager.bindStore(store);
                                store.on('load',function(){
                                    for(var i = 0;i < store.getCount();i++){
                                        var x = store.getAt(i).get('x');
                                        var y = store.getAt(i).get('y');
                                        //通过地图坐标点获取百度地图位置详细信息
                                        getMapAddress(x, y, i);
                                    }
                                });
                            },
                            render:function() {
                                store = Ext.ajaxModelLoader('MyApp.Model.ListAlarm',{params:{type:gd.getFilterTypes()}});
                                gd.bindStore(store);
                                pager.bindStore(store);
                                store.on('load',function(){
                                    for(var i = 0;i < store.getCount();i++){
                                        var x = store.getAt(i).get('x');
                                        var y = store.getAt(i).get('y');
                                        //通过地图坐标点获取百度地图位置详细信息
                                        getMapAddress(x, y, i);
                                    }
                                });
                            }
                        }
            	}
            ],
            bbar: pager,
            listeners:{
            	'afterlayout': function(){
              		//Ext.getCmp('AlarmListWindow_id').center();
              	},
            	'itemdblclick': function(scope, record, item, index, e, eOpts ){
            		Ext.create('MyApp.Component.AlarmHistoryListWindow');
            		var model = this.getSelectionModel().getSelection()[0];
            		//设置已读,将粗体去掉
            		model.set('ifRead', '1');
            		//this.getStore().sync();
            		var identifier = model.get('identifier');
	                G_MonitMap.getInfo(identifier);
	            }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    },
    getFilterTypes : function() {
        var nv = Ext.getCmp('typefilter').getValue();
        return !nv.type ? '' :  (isNaN(nv.type) ?  nv.type.join(',') : nv.type);
    }
});



