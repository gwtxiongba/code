/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.LoginWindow',{
    extend:'Ext.window.Window',
    constructor: function(id) {
        G_AppTimer.stop();
        var cmp = Ext.getCmp(id);
        if(cmp) {
            cmp.show();
            return cmp;
        }
        var o = MyApp.Base.LocalSet.getGolbal();
        var formPanel = Ext.create('MyApp.Component.LoginFormPanel');
        var account = formPanel.getComponent(0);
        account.setValue(o.visitor.account);
        account.setReadOnly(true);
        account.setFieldStyle('color:gray');
        
        this.superclass.constructor.call(this,{
            //id:'loginWindowShow_id',
            id: id,
            title:'系统超时登录',
            width:300,
            height:200,
            resizable: false,
            closable: false,
            closeAction: 'hide',
            layout: 'border',
            modal:true,
            items:[formPanel],
            buttons:[{
            text:'登录',
            handler:function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.Login',{
                        params:formPanel.getForm().getValues()
                    }).request();
                }
            },{
                text:'退出',
                handler:function(btn) {
                    Ext.ajaxModelLoader('MyApp.Model.Logout').request();
                }
            }],
            listeners:{
                hide:function(){
                    formPanel.getForm().reset();
                    account.setValue(o.visitor.account);
                }
            }
        });
        this.show();
        this.getComponent(0).getComponent(2).loadCodeImg();
        return this;
    }
})

