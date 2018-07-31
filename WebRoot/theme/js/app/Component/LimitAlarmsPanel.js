/* 
 * To change this template, choose Tools | Templates
 * 围栏报警列表
 */
Ext.define('MyApp.Component.LimitAlarmsPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'LimitListPanel_id'},
    constructor:function(){
        var grid = this;
        var store = Ext.ajaxModelLoader('MyApp.Model.LimitAlarms');
        
        this.superclass.constructor.call(this,{
            frame:true,
            border:true,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
                {text:'车牌号码', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'25%', 
                    renderer:function(val, x, store){ 
                        return setBlod(val, store.data.ifRead);
                    }
                 },
                {text:'设备识别码', dataIndex:'identifier', sortable: false, menuDisabled:true, width:'20%',
                	renderer:function(val, x, store){ 
                		return setBlod(val, store.data.ifRead);
                	}
                },
                {text:'车速(km/h)', dataIndex:'gps', sortable: false, menuDisabled:true, width:'15%',
                	renderer:function(val, x, store){ 
                		return setBlod(val[3], store.data.ifRead);
                	}
                },
                {text:'报警时间', dataIndex:'gps', sortable: false, menuDisabled:true, width:'40%',
                	renderer:function(val, x, store){ 
                		return setBlod(val[2], store.data.ifRead);
                	}
                }
            ],
            bbar: Ext.create('Ext.PagingToolbar', {
                store: store,
                displayInfo: true
            }),
            getCheckedModel : function() {
                var models = this.getSelectionModel().getSelection();
                return models ? models[0] : null;
            }
        });
        
        var setBlod = function(val, ifread){
        	if(ifread == 0){
        		return '<b>'+val+'</b>';
        	}else{
        		return val;
        	}
        };
    }
})

