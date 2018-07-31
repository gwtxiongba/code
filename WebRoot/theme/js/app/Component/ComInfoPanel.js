/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.ComInfoPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'ComInfoPanel_id'},
    constructor:function(){
        var gp = this;
        var store = Ext.ajaxModelLoader('MyApp.Model.ListCmdInfo');
        var myGeo = new BMap.Geocoder();
        function bdGEO1(adds ,index){
        	for(var i=0;i < adds.length;i++){
	            var pt = adds[i];
	            geocodeSearch(pt, index);
        	}
        };
         
        function geocodeSearch(pt, index){
            myGeo.getLocation(pt, function(rs){
				if(rs){
					var addComp = rs.addressComponents;
					var address = addComp.province + 
							  addComp.city + 
							  addComp.district + 
							  addComp.street + 
							  addComp.streetNumber;
					store.getAt(index).set('address', address);
				}
                
                
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
        	for(var i = 0;i < store.getCount();i++){
        		var x = store.getAt(i).get('x');
        		var y = store.getAt(i).get('y');
        		//通过地图坐标点获取百度地图位置详细信息
        		getMapAddress(x, y, i);
        	}
        });
         
        this.superclass.constructor.call(this,{
            frame: false,
            border:false,
            width:'100%',
            maxWidth:'100%',
            autoScroll: true,
            scroll:'vertical',
            store:store,
            columns: [
                //{xtype: 'rownumberer', text:'序'},
                {text:'报警类型', dataIndex:'reason', sortable: false, menuDisabled:true, width:'10%'},
                {text:'消息内容', dataIndex: 'reason', sortable: false, menuDisabled:true, width:'70%',
                	renderer:function(val, x, store){ 
                		var str = '';
                		str += store.data.plate;
                		str += '于 ' + store.data.time;
                		str += '在 ' + store.data.address;
                		str += '发生 ' + val;
                		return str;
                	}
                },
                {text:'接收时间', dataIndex:'createTime', sortable: false, menuDisabled:true, width:'20%'}
            ],
           /* tbar: [{
                text: '添加车辆',
                handler: function(){
                	Ext.getCmp('ComInfoPanel_id').addCmdInfo();
                }
            }],*/
            listeners:{
            	'itemcontextmenu':function(view,record,items,index,e,obj){
                    e.preventDefault();
                    e.stopEvent();
                    
                    //获取所有选中项
                    var selData = gp.getSelectionModel().getSelection();
                    
                    var recmenu = new Ext.menu.Menu({
                        floating:true,
                        items:[{
                            text:'车辆定位',
                            handler:function(){
                            	var identifier = selData[0].get('identifier');
            	                if(identifier) G_MonitMap.getInfo(identifier);
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
                        }]
                    });
                    recmenu.showAt(e.getXY());
            	},
            	'itemdblclick': function(scope, record, item, index, e, eOpts ){
            		Ext.create('MyApp.Component.AlarmHistoryListWindow');
	            }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    },
    addCmdInfo : function() {
    	var store1 = Ext.getCmp('ComInfoPanel_id').getStore();
    	var length = store1.getCount();
    	if(length==0){
    		//Ext.getCmp('ComInfoPanel_id').getStore().load();
    		return;
    	}
    	var lastTime = store1.getAt(0).get('createTime');
        // alert(store1.getAt(0).get('createTime'));
        //store1.loadData(store1.getAt(0), true);
        //store1.insert(2, store1.getAt(0));
        // alert(store1.getCount());
    	 var myGeo = new BMap.Geocoder();
         
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
                 store1.getAt(index).set('address', address);
                 
                 if(index == store1.getCount()-1){
                 	store1.sync();
                 }
             });
              
         };
          
         function getMapAddress(x ,y, index){
          	var adds = [
  	            new BMap.Point(x, y)
  	        ];

  	        bdGEO1(adds, index);
         }
    	
        
        var store2 = Ext.ajaxModelLoader('MyApp.Model.ListCmdInfo',{params:{lastTime:lastTime}});
        store2.on('load',function(){
	        for(var i = 0;i < store2.getCount();i++){
	        	store1.insert(i, store2.getAt(i));
	        	var x = store1.getAt(i).get('x');
        		var y = store1.getAt(i).get('y');
        		//通过地图坐标点获取百度地图位置详细信息
        		getMapAddress(x, y, i);
	        }
        });
        
    }
})



