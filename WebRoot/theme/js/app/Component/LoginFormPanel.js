/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.LoginFormPanel',{
   extend: 'Ext.form.FormPanel',
    constructor: function() {
        var formPanel = this;
        var reg = MyApp.Config.Regular;
        var tf_m = Ext.create('Ext.form.TextField',{
            id:'mobile',
            name: 'mobile',
            value:"mobile",
            inputType: 'hidden'
        });
        
        var tf_name = Ext.create('Ext.form.TextField',{
            id:'account',
            name: 'account',
            fieldLabel: '用户名',
            labelAlign: 'right',
            maxLength : 20,
            allowBlank : false,
            blankText: '请输入系统授权的登录用户名',
            regex: reg.account.regExp,
            regexText: reg.account.info
        });
        
        var tf_pwd = Ext.create('Ext.form.TextField', {
            id:'pwd',
            name: 'pwd',
            fieldLabel:'密　码',
            labelAlign: 'right',
            inputType: 'password',
            maxLength : 20,
            allowBlank : false,
            blankText: '登录密码不能为空'
        });

        var tf_vcode = Ext.create('MyApp.Component.VCodeText',{
            id:'vcode',
            fieldLabel : '验证码',
            labelAlign: 'right',
            name : 'vcode',  
            allowBlank : false,  
            isLoader:true,  
            blankText : '验证码不能为空',  
            codeUrl: CRMApp.URI.getHost()+CRMApp.URI.getRelative().substr(1) + 'rand.action',  
            width : 150 ,
            listeners : {  
    			specialkey : function(field, e) {  
						        if (e.getKey() == Ext.EventObject.ENTER) {  
				                    if(!formPanel.getForm().isValid()) return;
				                    Ext.ajaxModelLoader('MyApp.Model.Login',{
				                        params:formPanel.getForm().getValues()
				                    }).request();
						        }  
					    }  
					}
        }); 
       
        var config = {
            bodyStyle : 'padding-top:30px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            buttonAlign:'center',
            items: [tf_name,tf_pwd,tf_vcode,tf_m]

        }
       
        if(CRMApp.URI.getFileName() == 'login') {
            config.buttons = [{
                text:'登录',
                handler:function surely(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.Login',{
                        params:formPanel.getForm().getValues()
                    }).request();
                }
            },{
                text:'重置',
                handler:function(btn) {
                    formPanel.getForm().reset();
                }
            }];
        }
        this.superclass.constructor.call(this, config);
        
    },
    setLabelWidth:function(pix) {
        Ext.getCmp('account').setLabelWidth(pix);
        Ext.getCmp('pwd').setLabelWidth(pix);
    },
    setWidth:function(pix) {
        Ext.getCmp('account').setWidth(pix);
        Ext.getCmp('pwd').setWidth(pix);
    },
    setHeight:function(pix) {
        Ext.getCmp('account').setHeight(pix);
        Ext.getCmp('pwd').setHeight(pix);
        Ext.getCmp('vcode').setHeight(pix);
    }
   
     
});

