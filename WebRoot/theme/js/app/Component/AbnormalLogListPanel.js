/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.AbnormalLogListPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'AbnormalLogListPanel_id'},
    constructor:function(){
        var gd = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListAbnormaLog');
        
        store.on('load',function(){
        	for(var i = 0;i < store.getCount();i++){
        		var x = store.getAt(i).get('x');
        		var y = store.getAt(i).get('y');
        		//通过地图坐标点获取百度地图位置详细信息
        		getMapAddress(x, y, i);
        	}
        });
        
        // 百度地图API功能
        var myGeo = new BMap.Geocoder();
        
	    function getMapAddress(x ,y, index){
	     	var adds = [
	            new BMap.Point(x, y)
	        ];
	
	        bdGEO1(adds, index);
	    }
	    
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
                
                if(index == store.getCount()-1){
                	store.sync();
                }
            });
             
        };
        
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
                {text:'司机', dataIndex: 'driver', sortable: false, menuDisabled:true, width:'10%', 
                	renderer:function(val){ 
                		return val.driverName;
                	}
                },
                {text:'车牌', dataIndex:'car', sortable: false, menuDisabled:true, width:'15%',
                	renderer:function(val){ 
                		return val.plate;
                	}
                },
                {text:'报警原因', dataIndex:'ifLegal', sortable: false, menuDisabled:true, width:'15%',
                	renderer:function(val){ 
                		return val == 1 ? "正常使用":"非法使用";
                	}
                },
                {text:'上报时间', dataIndex:'signInTime', sortable: false, menuDisabled:true, width:'20%'},
                {text:'地址', dataIndex:'address', sortable: false, menuDisabled:true, width:'40%'}
            ],
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



