/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/*
 * To change this template, choose Tools | Templates 新建、修改车辆弹窗
 */
Ext.define('MyApp.Component.SaveBancheWindow', {
requires:['Ext.ux.TreeCombo_s'],
	extend : 'Ext.window.Window',
	constructor : function() {
		var w = this;
                var g = MyApp.Base.LocalSet.getGolbal();
                if(g == undefined) window.location.href = 'login.html';
		var ckModel = Ext.getCmp('VehicleBancheGridPanel_id').getCheckedModel();
		var ds = Ext.ajaxModelLoader('MyApp.Model.UnbindDrivers');
		var st = Ext.ajaxModelLoader('MyApp.Model.GetTeams');
                var teamId = 0;
		var identifiers = {
			xtype : 'textfield',
			id:'idfiers',
			blankText : '请输入设备号',
			//readOnly:true,
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
                //url: 'data.json?lineId='+this.getCheckedModel('gridline_id').getData().lineId,
               typeAhead : true,
               selectChildren: true,
			   canSelectFolders: true,
			   redStar:true,
               allowBlank:false,
                listeners:{
                  itemClick: function (me, view, record, item, index, e, eOpts){
                     //alert(record.data.id);
                      Ext.getCmp('team').setValue(record.data.id);
                  }
                }
               
            }
        var brandCom =
		{	xtype : 'combo',
			id : 'brandCombox',
			fieldLabel : '品牌',
			mode : 'local',
			displayField : "name",
			//valueField : "mark",
			valueField : "name",
			labelAlign : 'right',
			editable : false,
			typeAhead : true,
			selectOnFocus : true,
			triggerAction : 'all',
			name:'brand',
			store : Ext.ajaxModelLoader('MyApp.Model.ListBrand'),
			listeners:{
				change : function (){
					/*var brandIn = Ext.getCmp("brandCombox").getRawValue();
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
					//Ext.getCmp('brandCombox').getStore().removeAll();
					Ext.getCmp('brandCombox').getStore().add(allrec);
					*/
				}
			}
		};
		var carName={
				xtype: 'textfield',
                fieldLabel:'班车序号',
                labelAlign : 'right',
                name:'carModel'
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
            // 数据
             store:Ext.ajaxModelLoader('MyApp.Model.ListCarstyleSel')
          
        }
		 var dt = new Date();
	        dt.setDate(dt.getDate()-1);
		
		var buyTime={
                xtype:'datefield',
                id:'buyTime',
                //width:130,
                fieldLabel:'购入时间',
                //labelWidth: 70,
                labelAlign: 'right',
                format:'Y-m-d',
                //value: dt.format('yyyy-MM-dd'),
                //maxValue:dt.format('yyyy-MM-dd'),
                name:'buyTime'
            };
		
		var regTime={
                xtype:'datefield',
                id:'regTime',
                //width:130,
                fieldLabel:'入网时间',
                //labelWidth: 30,
                labelAlign: 'right',
                format:'Y-m-d',
               // value: dt.format('yyyy-MM-dd'),
                //maxValue:dt.format('yyyy-MM-dd'),
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
		
		var line={
		        
				xtype: 'textfield',
				id: 'lineGps',
                fieldLabel:'班车线路',
                labelAlign : 'right',
                name:'line',
                allowBlank : false,
                readOnly:true,
//                regex: /^\d+$/,
//                regexText: '只能输入数字'
           listeners:{
                focus:function(){
                  Ext.create('MyApp.Component.BancheLineEditWindow');
                 }
                
                 }
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
        
//        var weixiu = {
//            xtype : 'combo',
//            id:'weixiu',
//            fieldLabel : '是否有故障',
//            mode : 'local',
//            displayField : "type",
//            valueField : "Id",
//            labelAlign : 'right',
//            editable : false,
//            selectOnFocus : true,
//            typeAhead : true,
//            hidden:!ckModel ?true:false,
////            redStar : true,
////            allowBlank : false,
//            triggerAction : 'all',
//            name:'online',
//            // 数据
//            store : new Ext.data.SimpleStore({
//                fields : ['Id', 'type'],
//                data : [[0, '无'], [1, '有']]
//            }),
//            
//        }
      
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
//            redStar : true,
//            allowBlank : false,
            triggerAction : 'all',
            name:'ifRelay',
            // 数据
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
     var banche = {
			xtype : 'hiddenfield',
			name : 'banche',
			value:'1'
		}
        var formPanel = Ext.create('Ext.form.Panel', {
            bodyStyle : 'padding-top:2px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,

            items : [banche,identifiers, plate,team,teams,line, carName,brandCom,buyTime,regTime,userNumber,cc,cutpwd,vehicleId,driverCom],

            listeners:{
            	afterrender:function(){
                 //  ds.on('load',function(ds){
                   //	if (null==driverCom.value)
                   		//ds.add({'driverId':'','driverName':'暂不绑定'})
                  // })
            	}     
            }
        });

	if (ckModel) {
            var md = ckModel.getData();
           // teamId = md.team ? md.team.teamId : 0;
            Ext.getCmp('idfiers').setReadOnly(true);
            Ext.getCmp('teams').setValue(md.teamName);
           // alert(md.team.teamId);
            // Ext.getCmp('team').setValue(md.team.teamId);
           /* if (null==md.driver) {
                formPanel.getForm().setValues(md);
                ds.add({
                    'driverId':'',
                    'driverName':'暂不绑定'
                })
            } 
            else {
                ds.on('load',function(ds){
                    ds.insert(0,{
                        'driverId':md.driver.driverId,
                        'driverName':md.driver.driverName
                        });
                    driverCom.setValue(md.driver.driverId);
                    ds.add({
                        'driverId':'',
                        'driverName':'解除绑定'
                    });
                });
                   */
                formPanel.getForm().setValues(md);
           // }
           if(md.regTime){
            Ext.getCmp('regTime').setValue(md.regTime.substr(0,10));
           }
          if(md.buyTime){
            Ext.getCmp('buyTime').setValue(md.buyTime.substr(0,10));
           }
           Ext.getCmp('team').setValue(md.teamId);
           // if(null==md.ifRelay){
             //   Ext.getCmp('relayCombox').setValue(0)
           // }
         //  Ext.getCmp('type').setValue(md.typeId);  
        }
                
		this.superclass.constructor.call(this, {
					title : !ckModel ? '添加车辆' : ('修改车辆：' + ckModel
							.get('plate')),
					width : 300,
					height :440,
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
