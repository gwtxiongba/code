/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
Ext.define('MyApp.Component.SaveCarWindow', {
requires:['Ext.ux.TreeCombo_s'],
	extend : 'Ext.window.Window',
	constructor : function() {
		var w = this;
                
                var g = MyApp.Base.LocalSet.getGolbal();
                if(g == undefined) window.location.href = 'login.html';

		var ckModel = Ext.getCmp('VehicleGridPanel_id').getCheckedModel();

		var ds = Ext.ajaxModelLoader('MyApp.Model.UnbindDrivers');
		var st = Ext.ajaxModelLoader('MyApp.Model.GetTeams');
        var teamId = 0;
        var dt = new Date();
	    dt.setDate(dt.getDate()-1);
		var identifiers = {
			xtype : 'textfield',
			id:'idfiers',
			blankText : '请输入设备号',
			maxLength : 20,
			fieldLabel : '设备号',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'identifier'
		}

		var plate = {
			xtype : 'textfield',
			blankText : '请输入车牌号',
			maxLength : 20,
			fieldLabel : '车牌号',
			labelAlign : 'right',
			redStar : true,
			allowBlank : false,
			name:'plate'
		}
        
		var team = {
           id:'team',
           xtype : 'hiddenfield',
           name:'teamId'
        }
       	
       	var teams =  {
                xtype: 'treecombo',
                 id:'teams',
                fieldLabel:'车辆分组',
                emptyText:'选择分组',
                labelAlign : 'right',
               
                fields: ["id", "text"],
               
                valueField: 'id',
                labelWidth : 100,
                url: CRMApp.Configuration.servicePath + '?cmd=golbal&key=uteams',
               	typeAhead : true,
                selectChildren: true,
			   	canSelectFolders: true,
			    redStar:true,
              	allowBlank:false,
                listeners:{
                  itemClick: function (me, view, record, item, index, e, eOpts){
                      Ext.getCmp('team').setValue(record.data.id);
                  }
                }
            }
            
        var brandCom =	{	
        	xtype : 'combo',
			id : 'brandCombox',
			fieldLabel : '品牌',
			mode : 'local',
			displayField : "name",
			valueField : "name",
			labelAlign : 'right',
			editable : false,
			typeAhead : true,
			selectOnFocus : true,
			triggerAction : 'all',
			allowBlank : false,
			name:'brand',
			store : Ext.ajaxModelLoader('MyApp.Model.ListBrand')
			
		};
		 var type = {
            xtype : 'combo',
            id:'type',
            fieldLabel : '车型',
            mode : 'local',
            emptytext : '车型',
            displayField : "name",
            valueField : "id",
            labelAlign : 'right',
            editable : false,
            selectOnFocus : true,
            typeAhead : true,
            triggerAction : 'all',
            name:'type',
            allowBlank : false,
            store:Ext.ajaxModelLoader('MyApp.Model.ListCarstyleSel')
          
        }
		
		
		var buyTime={
                xtype:'datefield',
                id:'buyTime',
                fieldLabel:'购入时间',
                labelAlign: 'right',
                format:'Y-m-d',
                name:'buyTime'
            };
		
		var regTime={
                xtype:'datefield',
                id:'regTime',
                fieldLabel:'入网时间',
                labelAlign: 'right',
                format:'Y-m-d',
                name:'regTime'
            };
		
		var userNumber={
				xtype: 'textfield',
                fieldLabel:'准乘人数',
                labelAlign : 'right',
                name:'userNumber',
                regex: /^\d+$/,
                regexText: '只能输入数字'
            };
		
		var tel={
				xtype: 'textfield',
                fieldLabel:'电话号码',
                labelAlign : 'right',
                name:'tel',
                regex: /^\d+$/,
                regexText: '只能输入数字'
            };
		
		var iccid={
				xtype: 'textfield',
                fieldLabel:'iccid',
                labelAlign : 'right',
                name:'iccid'
//                regex: /^\d+$/,
//                regexText: '只能输入数字'
            };
		
		var startPrice={
				xtype: 'textfield',
                fieldLabel:'起步价',
                labelAlign : 'right',
                name:'startPrice',
                regex: /^\d+(\.\d{1,2})?$/,
                regexText: '只能输入整数或保留两位小数'
            };
		
		var kmPrice={
				xtype: 'textfield',
                fieldLabel:'每公里单价',
                labelAlign : 'right',
                name:'kmPrice',
                regex: /^\d+(\.\d{1,2})?$/,
                regexText: '只能输入整数或保留两位小数'
            };
		
		var lowPrice={
				xtype: 'textfield',
                fieldLabel:'低速费用',
                labelAlign : 'right',
                name:'lowPrice',
                regex: /^\d+(\.\d{1,2})?$/,
                regexText: '只能输入整数或保留两位小数'
            };
            
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
			name:'driverId',
			store : ds
		});

       
        var cc = {
            xtype:'numberfield',
            fieldLabel:'排量(升)',
            labelAlign : 'right',
            minValue:0,
            step:0.1,
            name:'discc'
        }
        
      
        var relay = {
            xtype : 'combo',
            id:'relayCombox',
            fieldLabel : '是否有继电器',
            mode : 'local',
            displayField : "type",
            valueField : "Id",
            labelAlign : 'right',
            editable : false,
            selectOnFocus : true,
            typeAhead : true,
            triggerAction : 'all',
            name:'ifRelay',
            store : new Ext.data.SimpleStore({
                fields : ['Id', 'type'],
                data : [[0, '无'], [1, '有']]
            }),
            listeners:{
        		afterrender:function(){
        			if(null== Ext.getCmp("relayCombox").getValue())
        				Ext.getCmp("relayCombox").setValue(0)
        		},
            	change:function(){
					var id = Ext.getCmp("relayCombox").getValue();
					var cutpwd = Ext.getCmp("cutpwdText");
					var idfiers = Ext.getCmp("idfiers").getValue();	
					if(id == 0){
						cutpwd.setValue('');
					}else{
						cutpwd.setValue(idfiers.substr(5,4));
					}
            	}
            }
        }
        
       var cutpwd = {
			xtype : 'hiddenfield',
			id:'cutpwdText',
			name:'cutpwd'
		}
        
		var vehicleId = {
			xtype : 'hiddenfield',
			name : 'vehicleId'
		}

        var formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle : 'padding-top:2px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            layout: {
                pack: 'center',
                type: 'hbox'
            },
            //items : [identifiers, plate,team,teams, brandCom, type,buyTime,regTime,userNumber,startPrice,kmPrice,lowPrice,cc,cutpwd,vehicleId,tel,driverCom],
            items:[{
                region:'west',
                width: '50%',
                    items:[identifiers, plate,team,teams, brandCom, type,buyTime,regTime]
            },{
                region:'east',
                width:'50%',
                    items:[userNumber,startPrice,kmPrice,lowPrice,cc,cutpwd,vehicleId,tel,driverCom]
            }]
        });

	if (ckModel) {
            var md = ckModel.getData();
            Ext.getCmp('idfiers').setReadOnly(true);
            Ext.getCmp('teams').setValue(md.teamName);
            if(md.driverId!=null){
            	ds.on('load',function(ds){
            		ds.insert(0,{
            			'driverId':md.driverId,
            			'driverName':md.driverName
            		});
            		driverCom.setValue(md.driverId);
            		ds.add({
            			'driverId':'',
            			'driverName':'[解除绑定]'
            		});
            	});
            }
                formPanel.getForm().setValues(md);
                
           if(md.regTime){
            Ext.getCmp('regTime').setValue(md.regTime.substr(0,10));
           }
          if(md.buyTime){
            Ext.getCmp('buyTime').setValue(md.buyTime.substr(0,10));
           }
           Ext.getCmp('team').setValue(md.teamId);
            if(null==md.ifRelay){
                Ext.getCmp('relayCombox').setValue(0)
            }
           Ext.getCmp('type').setValue(md.typeId);  
        }
                
		this.superclass.constructor.call(this, {
					title : !ckModel ? '添加车辆' : ('修改车辆：' + ckModel
							.get('plate')),
					width : 650,
					height :255,
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
							
							Ext.ajaxModelLoader('MyApp.Model.SaveVehicle', {
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
                
                
              
	}
});
