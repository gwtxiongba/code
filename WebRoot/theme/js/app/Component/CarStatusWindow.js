/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.CarStatusWindow',{
    extend:'Ext.window.Window',
    config:{id : 'carStatusWindow_id'},
    constructor:function(){
        var w = this;
        
        
        var states = Ext.create('Ext.data.Store', {
            fields: ['id', 'name'],
            data : [
                {"id":0, "name":"空闲"},
                {"id":1, "name":"维修中"},
                {"id":2, "name":"已派出"},
                //...
            ]
        });

        var online =Ext.create('Ext.form.ComboBox', {
            fieldLabel: '车辆状态:',
            emptyText:'请选择车辆状态....',
            store: states,
            editable : false,//不能手工输入
            allowBlank : false,// 选项不允许为空
            queryMode: 'local',
            displayField: 'name',//下拉框展示的值 。跟states中的name字段对应
            valueField: 'id',//下拉框实际的值
            name:'online',
            hiddenName : 'online', //name和hiddenName设置为一样，提交表单以后，后台通过request.getParameter("overSpeed")来获取值
            renderTo: Ext.getBody()
        });
        
        var vehicleId = {
        	id:'vehicleId',
			xtype : 'hiddenfield',
			name : 'vehicleId'
		}
		
		
        var formPanel = new Ext.form.FormPanel({
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [online, vehicleId]
        });
        
        this.superclass.constructor.call(this,{
            title : '',
            width : 300,
            height : 150,
            resizable : false,
            layout : 'border',
            modal: true,
            items : [formPanel],
            buttons : [{
                text : '提交',
                handler : function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.SaveVehicle', {
                        params:formPanel.getForm().getValues(),
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
    setValue : function(vehicleId) {
		Ext.getCmp('vehicleId').setValue(vehicleId);
	}
});

