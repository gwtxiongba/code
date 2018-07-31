/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.CarInfoPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'CarInfoPanel_id'},
    constructor:function(){
        var gp = this;
		Ext.define('CarInfoModel', {  
		extend: 'Ext.data.Model',  
		fields: [  
                {name:'vehid'},
				{name: 'plate'},  
				{name: 'identifier'},  
				{name: 'type'},  
				{name: 'driver'}, 
				{name: 'angle'},  
				{name: 'speed'}, 
				{name: 'online'},  
				{name: 'location'},  
				{name: 'locTime'}  
		     ]  
		});  
  
		var store = Ext.create('Ext.data.Store', {  
	        model: CarInfoModel
	    	});  
        
        function direc(n){
        	if(""!==n){
		        	v=n/9.0;
		            if(0<v&&v<1) return '北偏东';
		            else if(v==0) return '北方';
		            else if(v==1) return '东方';
		            else if(1<v&&v<2) return '南偏东';
		            else if(v==2) return '南方';
		            else if(2<v&&v<3) return '南偏西';
		            else if(v==3) return '西方';
		            else if(3<v&&v<4) return '北偏西';
        	}
            return '未获取';
        }
        
        var stinfo = function(v){
        	if(v==1)return "在线";
        	else return "离线"
        
        }
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: false,
            border:false,
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {text:'车牌', dataIndex:'plate', sortable: false, menuDisabled:true, width:'10%'},
                {text:'设备码', dataIndex: 'identifier', sortable: false, menuDisabled:true, width:'10%'},
                {text:'车型', dataIndex:'type', sortable: false, menuDisabled:true, width:'6%',renderer:function(v) { return MyApp.Config.Golbal.VehicleTypes[~~v]}},
                {text:'司机', dataIndex:'driver', sortable: false, menuDisabled:true, width:'6%',renderer:function(v){ return v ? v.driverName : '未绑定'}},
                {text:'司机电话', dataIndex:'driver', sortable: false, menuDisabled:true, width:'9%',renderer:function(v){return v ? v.driverTel : '未绑定'}},
                {text:'方向', dataIndex:'angle', sortable: false, menuDisabled:true, width:'6%',renderer:direc},
                {text:'速度', dataIndex:'speed', sortable: false, menuDisabled:true, width:'9%',renderer:function(v){return v + '公里/小时'}},
                /*{text:'油电状态', dataIndex:'ifOff', sortable: false, menuDisabled:true, width:'6%',
                	renderer:function(v){ 
                		if(v == 1){
                			return '断开';
                		}else{
                			return '连通';
                		}
                	}
                },*/
                {text:'状态信息', dataIndex:'online', sortable: false, menuDisabled:true, width:'6%',renderer:stinfo},
                {text:'参考位置', dataIndex:'location', sortable: false, menuDisabled:true, width:'18%'},
                {text:'定位时间', dataIndex:'locTime', sortable: false, menuDisabled:true, width:'15%'}
              ],

            listeners:{
       
            	'itemcontextmenu':function(view,record,items,index,e,obj){
                    e.preventDefault();
                    e.stopEvent();
                    
                    var recmenu = new Ext.menu.Menu({
                        floating:true,
                        items:[{
                            text:'查看历史轨迹',
                            handler:function(){
                                var rec = gp.getSelectionModel().getSelection()[0];
                                Ext.getCmp('apptabs').open({title:'历史回放:'+rec.get('plate'),url:'trace.html?vehicleId='+rec.get('vehid')});
                            }
                        },{
                            text:'删除选中记录',
                            handler:function(){
                            	var selModel = gp.getSelectionModel();
                                var selected = selModel.getSelection();
	                        store.remove(selected);
                            }
                        },{
                            text:'删除全部记录',
                            handler:function(){
                                store.removeAll();
                            }
                        }]
                    });
                    recmenu.showAt(e.getXY());
            	}
            }
        });
    },
    
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    },
    
    addInfo : function(CarInfoModel) {  
		 var adstore = Ext.getCmp('CarInfoPanel_id').getStore();  
		 adstore.insert(0, CarInfoModel);  
		}
})



