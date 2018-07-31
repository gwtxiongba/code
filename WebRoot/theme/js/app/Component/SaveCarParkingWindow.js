/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/*
 * To change this template, choose Tools | Templates 新建、修改车辆弹窗
 */
Ext.define('MyApp.Component.SaveCarParkingWindow', {
	extend : 'Ext.window.Window',
	id:'SaveCarParkingWindow_id',
	constructor : function() {
		var w = this;
		
		var ckModel = Ext.getCmp('ParkingGridPanel_id').getCheckedModel();
		
		var ds = Ext.ajaxModelLoader('MyApp.Model.UnbindDrivers');
		
		var name = {
			xtype : 'textfield',
			id:'parkingName',
			blankText : '请输入车库名称',
			//readOnly:true,
			maxLength : 20,
			fieldLabel : '车库名称',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'name'
		}
		
		var address = {
			xtype : 'textfield',
			maxLength : 20,
			id:'parkingAddress',
			blankText : '请输入车库地址',
			fieldLabel : '车库地址',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'address'
		}

		var position = {
			xtype : 'textfield',
			maxLength : 20,
			id:'parkingPosition',
			fieldLabel : '车库坐标',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'position',
			listeners :{
				focus : function(){
					Ext.create('MyApp.Component.MapLocParkingWindow');
				}
			}
		}
		
		var tel = {
			xtype : 'textfield',
			id:'parkingTel',
			blankText : '请输停车场电话',
			maxLength : 20,
			fieldLabel : '电话',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'tel'
		}
            
		var admin = {
			xtype : 'textfield',
			id:'admin',
			blankText : '请输入管理员',
			maxLength : 20,
			fieldLabel : '管理员',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'adminName'
		}
		
		var parkingId = {
			id:'parkingId',
			xtype : 'hiddenfield',
			name : 'parkingId'
		}
		
		var driverId = {
			id:'driverId',
			xtype : 'hiddenfield',
			name : 'driverId'
		}
		
        var formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [name,address,position,tel,admin,parkingId,driverId]
        });
        
		this.superclass.constructor.call(this, {
					title : !ckModel ? '添加车库信息' : '修改车库信息：',
					width : 300,
					height :230,
					resizable : false,
					layout : 'border',
					modal : true,
					items : [formPanel],
					buttonAlign : 'center',
					buttons : [{
						text : '提交',
						handler : function(btn) {
							Ext.ajaxModelLoader('MyApp.Model.SaveParking', {
										params : formPanel.getForm()
												.getValues(),
										target : w
									}).request();
						}
					}, {
						text : '退出',
						handler : function(btn) {
							formPanel.getForm().reset();
							w.close();
						}
					}]
				});
		this.show();
	},
	setParkingValue : function(pId, pName,pAddress,pPosition,pTel,admin,driverId) {
		Ext.getCmp('parkingName').setValue(pName);
		Ext.getCmp('parkingAddress').setValue(pAddress);
		Ext.getCmp('parkingPosition').setValue(pPosition);
		Ext.getCmp('parkingTel').setValue(pTel);
		Ext.getCmp('admin').setValue(admin);
		Ext.getCmp('parkingId').setValue(pId);
		Ext.getCmp('driverId').setValue(driverId);
	}
});
