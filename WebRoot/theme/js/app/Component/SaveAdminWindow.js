/* 
 * To change this template, choose Tools | Templates
 * 新建、修改操作员弹窗
 */
Ext.define('MyApp.Component.SaveAdminWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        var ckModel = Ext.getCmp('AdministratorGridPanel_id').getCheckedModel();
        
        var reg = MyApp.Config.Regular;
         
        var account = {
            xtype: 'textfield',
            blankText: '请输入想注册的登录名',
            maxLength:20,
            fieldLabel : '登录名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            disabled: !!ckModel,
            hidden: !!ckModel,
            regex: reg.account.regExp,
            name:'account'
        };
        
        var realName = {
            xtype: 'textfield',
            blankText: '请正确输入操作员真实姓名',
            maxLength:20,
            fieldLabel : '管理员姓名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'realname'
        };
        
        var pwd = Ext.create('Ext.form.TextField',{
            blankText : '请输入登录密码',
            inputType : 'password',
            maxLength : 20,
            fieldLabel : '密码',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            disabled: !!ckModel,
            hidden: !!ckModel,
            name:'pwd'
        });
        
        var cpwd = {
            xtype: 'textfield',
            inputType : 'password',
            maxLength : 20,
            redStar:true,
            fieldLabel : '确认密码',
            labelAlign:'right',
            allowBlank : true,
            disabled: !!ckModel,
            hidden: !!ckModel,
            validator: function(value) {
                return pwd.getValue().Equals(value) ?  true : '两次输入的密码不一致';
            }
        };
        
        var address = {
            xtype: 'textfield',
            maxLength:20,
            fieldLabel : '联系地址',
            labelAlign:'right',
            allowBlank : true,
            name:'address'
        };
        
        var phone = {
            xtype: 'textfield',
            maxLength:20,
            fieldLabel : '联系电话',
            labelAlign:'right',
            allowBlank : true,
            name:'phone'
        };
        
        var email = {
            xtype: 'textfield',
            maxLength:30,
            fieldLabel : '电子邮件',
            labelAlign:'right',
            allowBlank : true,
            name:'email'
        };
        
        var key = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'accountId'
        };
        
        var ifAdmin = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'ifAdmin',
            value:'1'
        };
        
        var formPanel = Ext.create('Ext.form.Panel',{
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items :[realName,account,pwd,cpwd,address,phone,email,key,ifAdmin]
        });
        
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
        }
        this.superclass.constructor.call(this,{
           title: !ckModel ? '添加机构管理员' : ('修改机构管理员：' + ckModel.get('account')),
           width:300,
           height: !ckModel ? 270 :210,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[formPanel],
           buttonAlign:'center',  
           buttons:[{
                text:'提交',
                handler:function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.SaveOperator',{
                        params:formPanel.getForm().getValues(),
                        target: w
                    }).request();
                }
            },{
                text: '退出',
                handler:function(btn) {
                   formPanel.getForm().reset();
                   w.hide();        
                }
            }]
        });
        this.show();
    }
});

