/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.UpdatePwdWindow',{
    extend:'Ext.window.Window',
    config:{
      id : 'updatePwdWindowShow_id'  
    },
    constructor:function(){
        var w = Ext.getCmp(this.id);
        if(w){w.show();return;} 
        w = this;
        var opwd = new Ext.form.TextField({
            blankText : '请输入当前账号登录密码',
            inputType : 'password',
            maxLength : 20,
            fieldLabel : '原始密码',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'oldpwd'
        });
        var pwd = new Ext.form.TextField({
            blankText : '请输入您将要修改的新密码',
            inputType : 'password',
            maxLength : 20,
            fieldLabel : '新密码',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'newpwd'
        });
        var cpwd = new Ext.form.TextField({
            blankText : '请再次输入一遍新密码',
            inputType : 'password',
            maxLength : 20,
            redStar:true,
            fieldLabel : '确认密码',
            labelAlign:'right',
            allowBlank : false,
            validator: function(value) {
                return pwd.getValue().Equals(value) ?  true : '两次输入的密码不一致';
            }
        });

        var formPanel = new Ext.form.FormPanel({
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [opwd, pwd, cpwd]
        });
        
        w.superclass.constructor.call(w,{
            title : '修改密码',
            width : 300,
            height : 200,
            resizable : false,
            closeAction : 'hide',
            layout : 'border',
            modal: true,
            items : [formPanel],
            buttons : [{
                text : '修改',
                handler : function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.Setpwd', {
                        params:formPanel.getForm().getValues()
                    }).request();
                    
                }
            }, {
                text : '退出',
                handler : function(btn) {
                    formPanel.getForm().reset();
                    w.hide();
                }
            }]
        });
        w.show();
        
    }
});

