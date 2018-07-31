/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.AbnormalHistoryListPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'AbnormalHistoryListPanel_id'},
    constructor:function(){
        var gd = this;
    	
        var abModel = Ext.getCmp('AbnormalListPanel_id').getCheckedModel();
        var vehicleId = abModel.get('vehicleId');
        var store = Ext.ajaxModelLoader('MyApp.Model.HistoryAbnormal',{params:{vehicleId:vehicleId}});
              
        var setBlod = function(val, reader){
        	if(reader == null || reader == ''){
        		return '<b>'+val+'</b>';
        	}else{
        		return val;
        	}
        };
        
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            region: 'center',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
                {text:'司机', dataIndex: 'driver', sortable: false, menuDisabled:true, width:'10%', 
                	renderer:function(val, x, store){ 
                		if(val == null || val == 'null') val = '';
                		return setBlod(val, store.data.reader);
                	}
                },
                {text:'异常原因', dataIndex:'reason', sortable: false, menuDisabled:true, width:'45%',
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
                {text:'上报时间', dataIndex:'time', sortable: false, menuDisabled:true, width:'30%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.reader);
                	}
                }
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



