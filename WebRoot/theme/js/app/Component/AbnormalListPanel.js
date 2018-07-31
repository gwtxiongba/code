/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.AbnormalListPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'AbnormalListPanel_id'},
    constructor:function(){
        var gd = this;
    	
        var store = Ext.ajaxModelLoader('MyApp.Model.ListAbnormal');
        
        var setBlod = function(val, ifread){
        	if(ifread == 0){
        		return '<b>'+val+'</b>';
        	}else{
        		return val;
        	}
        };
        
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
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
                {text:'异常原因', dataIndex:'reason', sortable: false, menuDisabled:true, width:'40%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.ifRead);
                	}
                },
                {text:'上报时间', dataIndex:'time', sortable: false, menuDisabled:true, width:'30%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.ifRead);
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
	   	    }),
            listeners:{
            	'itemdblclick': function(scope, record, item, index, e, eOpts ){
            		Ext.create('MyApp.Component.AbnormalHistoryListWindow');
            		var model = this.getSelectionModel().getSelection()[0];
            		//设置已读,将粗体去掉
            		model.set('ifRead', '1');
            		var identifier = model.get('identifier');
	                G_MonitMap.getInfo(identifier);
	            }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});



