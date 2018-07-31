/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.Setpwd',{
    extend: 'MyApp.Base.Ajax',
    config:{
        method:'post',
        success:function(o) {
            var win = Ext.getCmp('updatePwdWindowShow_id');
            win.getComponent(0).getForm().reset();
            win.hide(); 
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '密码修改成功，请牢记新密码!'
            });
        }
    }
})

