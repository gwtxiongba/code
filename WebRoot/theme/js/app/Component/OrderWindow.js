/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.OrderWindow',{
    extend:'Ext.window.Window',
    config:{id : 'orderWindowShow_id'},
    constructor:function(){
        var w = this;
        
        var cutpwd = new Ext.form.TextField({
            blankText : '请输入操作密码',
            inputType : 'password',
            maxLength : 20,
            fieldLabel : '操作密码',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'opPwd'
        });
        
        var identifier = {
        	id:'identifier',
			xtype : 'hiddenfield',
			name : 'identifier'
		}
		
		var state = {
			id:'state',
			xtype : 'hiddenfield',
			name : 'state'
		}

		
        var formPanel = new Ext.form.FormPanel({
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [cutpwd, identifier,state]
        });
        
        this.superclass.constructor.call(this,{
            title : '',
            width : 300,
            height : 130,
            resizable : false,
            layout : 'border',
            modal: true,
            items : [formPanel],
            buttons : [{
                text : '提交',
                handler : function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.OperateCar', {
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
    setValue : function(identifier,state) {
		Ext.getCmp('identifier').setValue(identifier);
		Ext.getCmp('state').setValue(state);
	}
});

