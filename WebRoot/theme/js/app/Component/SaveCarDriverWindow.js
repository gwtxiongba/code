Ext.define('MyApp.Component.SaveCarDriverWindow', {
	extend : 'Ext.window.Window',
	config:{id:'SaveCarDriverWindow_id'},
	constructor : function() {
		var w = this;
		
		var ckModel = Ext.getCmp('CarDriverGridPanel_id').getCheckedModel();

		var ds = Ext.ajaxModelLoader('MyApp.Model.ListDriver');
		
		var dsv = Ext.ajaxModelLoader('MyApp.Model.ListVehicle');
		
		var temp = [];
		
		var plate = Ext.create('Ext.form.field.ComboBox',{
			id : 'plate',
			fieldLabel : '车牌号',
			mode : 'remote',
			displayField : "plate",
			valueField : "vehicleId",
			labelAlign : 'right',
			editable : true,
			typeAhead : true,
			selectOnFocus : true,
			triggerAction : 'all',
			name : 'vehicleId',
			store : dsv,
            listeners:{
            	change:function(){
			    		var plateIn = Ext.getCmp("plate").getValue();
			    		if(plateIn=='')
			    			dsv.load();
			    		dsv.each(function(dataRecord){
			    			if(dataRecord.get('plate').indexOf(plateIn)==-1)
			    			{
			    			temp.push(dataRecord)
			    			}
			    		})
			    		dsv.remove(temp);
			    		if(dsv.getCount()==0)
			    		{
			    			dsv.load();
			    		}
			    }
			}
		});
		
		var driverCom = Ext.create('Ext.form.field.ComboBox',{
			id : 'driverCombox',
			fieldLabel : '司机',
			mode : 'remote',
			displayField : "driverName",
			valueField : "driverId",
			labelAlign : 'right',
			editable : false,
			typeAhead : true,
			selectOnFocus : true,
			triggerAction : 'all',
			name : 'driverId',
			store : ds
		});
		
		var startTime = {
        	id: 'startTime',
			xtype: 'datefield',
			//style: 'margin: 5px 12px 3px 5px',
			//labelWidth: 55,
			format: 'Y-m-d',
			//format: 'Y-m-d H:i:s ',
			labelAlign : 'right',
			fieldLabel: '开始时间',
			name: 'startTime'
        };
        
        var endTime = {
        	id: 'endTime',
			xtype: 'datefield',
			//style: 'margin: 5px 12px 3px 5px',
			//labelWidth: 55,
			//width:180,
			format: 'Y-m-d',
			//format: 'Y-m-d H:i:s ',
			labelAlign : 'right',
			fieldLabel: '结束时间',
			name: 'endTime'
        };
		
		var carDriverId = {
			id : 'carDriverId',
			xtype : 'hiddenfield',
			name : 'carDriverId'
		};

        var formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [plate, driverCom, startTime, endTime, carDriverId]
            
        });

		                
		this.superclass.constructor.call(this, {
			title : !ckModel ? '添加规则' : ('修改规则：' + ckModel.get('car').plate),
			width : 300,
			height :220,
			resizable : false,
			layout : 'border',
			modal : true,
			items : [formPanel],
			buttonAlign : 'center',
			buttons : [{
				text : '提交',
				handler : function(btn) {
					if (!formPanel.getForm().isValid()){
						return;
					}
					Ext.ajaxModelLoader('MyApp.Model.SaveCarDriver', {
						params : formPanel.getForm().getValues(),
						target : w
					}).request();
				}
			}, {
				text : '退出',
				handler : function(btn) {
					w.close();
				}
			}]
		});
		this.show();
	},
	setCarValue : function(carDriverId, vehicleId, driverId, startTime, endTime) {
		Ext.getCmp('carDriverId').setValue(carDriverId);
		Ext.getCmp('plate').setValue(vehicleId);
		Ext.getCmp('driverCombox').setValue(driverId);
		Ext.getCmp('startTime').setValue(startTime);
		Ext.getCmp('endTime').setValue(endTime);
	}
});
