/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.SpeedWindow',{
    extend:'Ext.window.Window',
    config:{id : 'speedWindowShow_id'},
    constructor:function(){
        var w = this;
        
//        var cutpwd = new Ext.form.TextField({
//            blankText : '请输入速度值',
//            inputType : 'number',
//            maxLength : 10,
//            fieldLabel : '最大速度(km/h)',
//            labelAlign:'right',
//            redStar:true,
//            allowBlank : false,
//            name:'overSpeed'
//        });
        
        var states = Ext.create('Ext.data.Store', {
            fields: ['id', 'name'],
            data : [
                {"id":120, "name":"120km/h"},
                {"id":110, "name":"110km/h"},
                {"id":100, "name":"100km/h"},
                {"id":90, "name":"90km/h"},
                {"id":80, "name":"80km/h"},
                {"id":70, "name":"70km/h"},
                {"id":60, "name":"60km/h"}
                //...
            ]
        });

        var cutpwd =Ext.create('Ext.form.ComboBox', {
            fieldLabel: '最大速度(km/h)',
            emptyText:'请选择最大速度....',
            store: states,
            editable : false,//不能手工输入
            allowBlank : false,// 选项不允许为空
            queryMode: 'local',
            displayField: 'name',//下拉框展示的值 。跟states中的name字段对应
            valueField: 'id',//下拉框实际的值
            name:'overSpeed',
            hiddenName : 'overSpeed', //name和hiddenName设置为一样，提交表单以后，后台通过request.getParameter("overSpeed")来获取值
            renderTo: Ext.getBody()
        });
        
        var identifier = {
        	id:'identifier',
			xtype : 'hiddenfield',
			name : 'identifier'
		}
		
		
        var formPanel = new Ext.form.FormPanel({
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [cutpwd, identifier]
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
                    Ext.ajaxModelLoader('MyApp.Model.OverSpeed', {
                        params:formPanel.getForm().getValues()
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
    setValue : function(identifier) {
		Ext.getCmp('identifier').setValue(identifier);
	}
});

