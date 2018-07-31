/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/*
 * To change this template, choose Tools | Templates 新建、修改车辆弹窗
 */
Ext.define('MyApp.Component.SaveCarBaseInfoWindow', {
	id:'SaveCarBaseInfoWindow_id',
	extend : 'Ext.window.Window',
	constructor : function() {
		var w = this;

		var ckModel = Ext.getCmp('CarBaseInfoGridPanel_id').getCheckedModel();
		
		var ds = Ext.ajaxModelLoader('MyApp.Model.ListPlate');
		
		var store = Ext.ajaxModelLoader('MyApp.Model.ListBrand');
		
		var plateCom =
		{	xtype : 'combo',
			id : 'plateCombox',
			fieldLabel : '车牌',
			mode : 'remote',
			displayField : "plate",
			valueField : "userId",
			labelAlign : 'right',
			editable : true,
			disableKeyFilter:false,
			typeAhead : true,
			selectOnFocus : true,
			triggerAction : 'all',
			name:'userId',
			store : ds,
			listeners:{
				change : function (){
					var plateIn = Ext.getCmp("plateCombox").getRawValue();
            		if(null==plateIn||""==plateIn){
            			Ext.getCmp('plateCombox').getStore().load();
            		}
            		var fieldstore = Ext.getCmp('plateCombox').getStore();
            		var afterStore = Ext.create('Ext.data.Store', {
            						fields: ['userId','plate']});
            		fieldstore.each(function(dataRecord){
            			if(dataRecord.get('plate').indexOf(plateIn)!=-1)
            			afterStore.add(dataRecord);
            		});
            		var allrec = afterStore.getRange();
					Ext.getCmp('plateCombox').getStore().removeAll();
					Ext.getCmp('plateCombox').getStore().add(allrec);
				}
			}
		};
		
		var brandCom =
		{	xtype : 'combo',
			id : 'brandCombox',
			fieldLabel : '品牌',
			mode : 'local',
			displayField : "brand",
			valueField : "mark",
			labelAlign : 'right',
			editable : true,
			typeAhead : true,
			selectOnFocus : true,
			triggerAction : 'all',
			name:'brand',
			store : store,
			listeners:{
				change : function (){
					var brandIn = Ext.getCmp("brandCombox").getRawValue();
					if(null==brandIn||""==brandIn){
            			Ext.getCmp('brandCombox').getStore().load();
            		}
            		var fieldstore = Ext.getCmp('brandCombox').getStore();
            		var afterStore = Ext.create('Ext.data.Store', {
            						fields: ['brand','mark']});
            		fieldstore.each(function(dataRecord){
            			if(dataRecord.get('mark').toLowerCase ().indexOf(brandIn)!=-1 || dataRecord.get('mark').toUpperCase().indexOf(brandIn)!=-1)
            			afterStore.add(dataRecord);
            		});
            		var allrec = afterStore.getRange();
					Ext.getCmp('brandCombox').getStore().removeAll();
					Ext.getCmp('brandCombox').getStore().add(allrec);
				}
			}
		};
		
		var weight = {
			xtype : 'textfield',
			id:'weight',
			blankText : '请输车辆吨位',
			//readOnly:true,
			maxLength : 20,
			fieldLabel : '吨位',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'weight'
		}

		var totalNumber = {
			id:'totalNumber',
			xtype : 'textfield',
			blankText : '请输入车辆总载人数',
			maxLength : 20,
			fieldLabel : '总载人数',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'totalNumber'
		}
        
		var prcTime ={
	       id : 'prcTime',
	        xtype: 'datefield',
	        anchor: '-30',
	        fieldLabel: '生产时间',
	        name: 'prcTime',
	        format: 'Y-m-d',
	        labelAlign: 'right',
	        selectOnFocus:true, 
	        editable:false
   		}
   		
   		var limTime ={
   			id : 'limTime',
	        xtype: 'datefield',
	        anchor: '-30',
	        fieldLabel: '年审时间',
	        name: 'limTime',
	        format: 'Y-m-d',
	        labelAlign: 'right',
	        selectOnFocus:true, 
	        editable:false
   		}
		

		var baseId = {
			id : 'baseId',
			xtype : 'hiddenfield',
			name : 'baseId'
		}

        var formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [plateCom, brandCom,weight, totalNumber, prcTime,limTime,baseId]
        });
      
		this.superclass.constructor.call(this, {
					title : !ckModel ? '添加车辆信息' : ('修改车辆信息：' + ckModel
							.get('user').plate),
					width : 300,
					height :250,
					resizable : false,
					layout : 'border',
					modal : true,
					items : [formPanel],
					buttonAlign : 'center',
					buttons : [{
						text : '提交',
						handler : function(btn) {
							if (!formPanel.getForm().isValid())
								return;
							var brand = Ext.getCmp('brandCombox').getRawValue();
							if(brand == ''){
								brand = ' ';
							}
							Ext.getCmp('brandCombox').setValue(brand);
							Ext.ajaxModelLoader('MyApp.Model.SaveBaseInfo', {
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
	setBaseValue : function(baseId,userId,plate,weight,totalNumber,prcTime,limTime,brand) {
		Ext.getCmp('plateCombox').setValue(userId);
		Ext.getCmp('weight').setValue(weight);
		Ext.getCmp('totalNumber').setValue(totalNumber);
		Ext.getCmp('prcTime').setValue(prcTime);
		Ext.getCmp('limTime').setValue(limTime);
		Ext.getCmp('baseId').setValue(baseId);
		Ext.getCmp('brandCombox').setValue(brand);
	}
});
