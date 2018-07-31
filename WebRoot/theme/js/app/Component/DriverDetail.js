/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this template, choose Tools | Templates
 * 新建、修改车辆弹窗
 */
Ext.define('MyApp.Component.DriverDetail',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        var ckModel = Ext.getCmp('driver_list').getCheckedModel();
        
        var store = Ext.ajaxModelLoader('MyApp.Model.DriverDetail',{params:{driverId:ckModel.get('driverId')}});
        
        var driverName = {
            xtype: 'displayfield',
            maxLength:20,
            fieldLabel : '司机名',
            readOnly:true,
            labelAlign:'right',
            //redStar:true,
           // allowBlank : false,
           // disabled: !!ckModel,
           // hidden: !!ckModel,
            name:'driverName'
        }
        
        var remark = {
            xtype: 'displayfield',
            readOnly:true,
            maxLength:10,
            fieldLabel : '备注',
            labelAlign:'right',
            allowBlank : true,
            name:'remark'
        }
        
        var carPlate = {
            xtype: 'displayfield',
            id:'plate'+ckModel.get('driverId'),
            readOnly:true,
            maxLength:20,
            fieldLabel : '驾驶车辆',
            labelAlign:'right',
           // allowBlank : false,
            name:'plate'
        }
        
        var license = {
            xtype: 'displayfield',
            readOnly:true,
            maxLength:20,
            fieldLabel : '驾照编号',
            labelAlign:'right',
           // allowBlank : false,
            name:'license'
        }
        
            
        var driverTel = {
            xtype: 'displayfield',
            maxLength:20,
            readOnly:true,
            fieldLabel : '联系电话',
            labelAlign:'right',
            allowBlank : true,
            regex:/^((?:13\d|15[0-9]|18[0-9])-?\d{5}(\d{3}|\*{3})$|^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$|^400\d{7})?$/,
            regexText: '请输入正确的电话号码!<br/>例如：15876551205 或 027-59361596',
            name:'driverTel'
        }
        
        var key = {
            xtype: 'hiddenfield',
           // type:'hidden',
            name:'driverId'
        }
        
        var formPanel = Ext.create('Ext.form.Panel',{
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
          //  labelWidth : 300,
            items :[driverName,driverTel,license,carPlate,remark,key]
        });
        
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
        }
        
        
        this.superclass.constructor.call(this,{
           title: '司机详情：',
           id:'detailWindow'+ckModel.get('driverId'),
           width:300,
           height: 290,
           resizable: false,
           layout: 'border',
          // modal:true,
           items:[formPanel],
           buttonAlign:'center',  
           buttons:[{
                text: '退出',
                handler:function(btn) {
                   w.close();        
                }
            }],
            listeners:{
            	afterrender:function(){
            			store.on('load',function(){
            					if(store.getCount()==0)
            					 Ext.getCmp('plate'+ckModel.get('driverId')).setValue('未使用车辆')
            				store.each(function(dataRecord){
				        	 	 Ext.getCmp('plate'+ckModel.get('driverId')).setValue(dataRecord.get('plate'));
				        	     G_MonitMap.getInfo(dataRecord.get('identifier'));
				       		 })
            			})
            			
            	}
            }
        });
        this.show();
    }
});


