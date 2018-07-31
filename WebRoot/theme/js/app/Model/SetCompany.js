/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SetCompany',{
    extend:'MyApp.Base.Ajax',
    config:{
        method:'post',
        success:function() {
           var w = Ext.getCmp('updateComWindowShow_id');
           var g = MyApp.Base.LocalSet.getGolbal();
           var o = w.getComponent(0).getForm().getValues();
           for(var p in o) {
               g.company[p] = o[p];
           }
           //以后要推送到其他登录改机构的客户端
           MyApp.Base.LocalSet.setGolbal(g);
           w.getComponent(0).getForm().reset();
           w.hide();
           Ext.getCmp('apptoolbar').refresh();
           Ext.create('MyApp.Component.LoadingTips',{
              lazytime:2000,
              msg:'企业资料修改成功!'
           });
        }
    }
})

