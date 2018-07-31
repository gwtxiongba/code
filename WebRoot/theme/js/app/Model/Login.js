/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.Login',{
    extend: 'MyApp.Base.Ajax',
    config:{
        method:'post',
        success:function(o){
            o.visitor.tick = new Date().getTime(); //记录本地登录时间
            MyApp.Base.LocalSet.setGolbal(o);
            if(CRMApp.URI.getFileName().Equals('index')) {
                Ext.getCmp('loginWindowShow_id').hide();
                G_AppTimer.excute(true);
                top.window.page.refresh();
                //top.window.location.reload();
               // top.Ext.getCmp('CarTreePanel_id').refreshTree();
                //这里打开定时器继续监听监控心跳包
            } else window.location.href = 'index.html';
        },
        failture:function(code,msg) {
            if(MyApp.Config.Error.LOGIN_NOT_MATCH == code) {
                Ext.create('MyApp.Component.LoadingTips',{lazytime:2000,msg:'密码错误或者账号不存在'});    
            } else if(MyApp.Config.Error.VALIDATE_FAILED == code) {
                Ext.create('MyApp.Component.LoadingTips',{lazytime:2000,msg:'您输入的验证码有误',lazywork:function(){
                        Ext.getCmp('vcode').loadCodeImg();
                }});    
            } else Ext.create('MyApp.Component.LoadingTips',{lazytime:2000,msg:msg});    
        }
    }
});

