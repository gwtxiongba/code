/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.ListLogPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'ListLogPanel_id'},
    constructor:function(){
        var gd = this;
    	
        var store = Ext.ajaxModelLoader('MyApp.Model.ListDriverLog',{params:{driverId:''}});
        
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
                {text:'司机', dataIndex: 'driver', sortable: false, menuDisabled:true, width:'20%', 
                	renderer:function(val){ 
                		return val.driverName;
                	}
                },
                {text:'车牌', dataIndex:'car', sortable: false, menuDisabled:true, width:'20%',
                	renderer:function(val){ 
                		return val.plate;
                	}
                },
                {text:'上报时间', dataIndex:'signInTime', sortable: false, menuDisabled:true, width:'30%'},
                {text:'使用状态', dataIndex:'ifLegal', sortable: false, menuDisabled:true, width:'30%',
                	renderer:function(val){ 
                		return val == 1 ? "正常使用":"非法使用";
                	}
                }
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



