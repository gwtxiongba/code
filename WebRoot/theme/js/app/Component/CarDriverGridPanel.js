Ext.define('MyApp.Component.CarDriverGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'CarDriverGridPanel_id'},
    constructor:function(){
        var gp = this;
     
        var store = Ext.ajaxModelLoader('MyApp.Model.ListCarDriver');
        
        var temp = [];
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: false,
            border:true,
            width:'100%',
//            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer',text:'序'},
                {text:'车牌', dataIndex: 'car', sortable: false, menuDisabled:true, width:'25%',
                	renderer:function(val){
                		return val.plate;
                	}},
                {text:'司机', dataIndex: 'driver', sortable: false, menuDisabled:true, width:'25%',
            		renderer:function(val){
                		return val.driverName;
                	}},
                {text:'开始时间', dataIndex: 'startTime', sortable: false, menuDisabled:true, width:'25%',
            		renderer:function(val){
                		return Ext.Date.format(new Date(val),'Y-m-d');
                	}},
                {text:'结束时间', dataIndex: 'endTime', sortable: false, menuDisabled:true, width:'25%',
            		renderer:function(val){
                		return Ext.Date.format(new Date(val),'Y-m-d');
                	}}
            ],
            tbar: [{
                text: '添加规则',
                handler: function(){
                    Ext.create('MyApp.Component.SaveCarDriverWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_id',
                text: '删除规则',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选规则，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while(model = selModels.shift()){
								keys.push(model.get('carDriverId'));
							}
                            Ext.ajaxModelLoader('MyApp.Model.DelCarDriver',{
								target: gp, 
								params: {carDriverIds:keys.join(',')}}
							).request();   
							
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改规则',
                disabled: true,
                handler: function(){
                	 Ext.create('MyApp.Component.SaveCarDriverWindow');
                	 var selModels = gp.getSelectionModel().getSelection();
                	 var carDriverId = selModels[0].get('carDriverId');
                	 var driverId = selModels[0].get('driver').driverId;
                	 var vehicleId = selModels[0].get('car').vehicleId;
                	 var startTime = Ext.Date.format(new Date(selModels[0].get('startTime')),'Y-m-d');
                	 var endTime = Ext.Date.format(new Date(selModels[0].get('endTime')),'Y-m-d');
                	 Ext.getCmp('SaveCarDriverWindow_id').setCarValue(carDriverId, vehicleId,  
                			 driverId, startTime, endTime);
                }
            },{
                xtype: 'tbseparator'
            },{
            xtype: 'textfield',
            style: 'margin: 5px 0px 3px -20px',
            emptyText: '查询车牌号',
            fieldLabel : '车牌搜索',
            width: 190,
            labelAlign:'right',
            id:'plateSh',
            listeners:{
            	change:function(){
            		var plateIn = Ext.getCmp("plateSh").getValue();
            		if(plateIn=='')
            			store.load();
            		store.each(function(dataRecord){
            			if(dataRecord.get('car').plate.indexOf(plateIn)==-1)
            			{
            			temp.push(dataRecord)
            			}
            		})
            		store.remove(temp);
            		if(store.getCount()==0)
            		{
            			Ext.MessageBox.alert('提示','无匹配车辆')
            			store.load();
            		}
            }
        }},{
            xtype:'tbfill'
        },{
            xtype:'pagingtoolbar',
            border:false,
            store:store,
            displayInfo : true
        }],
		   	listeners:{
	            selectionchange : function(sm, selections) {
	                Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
	                Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
	            },
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                }
	         }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});

