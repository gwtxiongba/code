/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DelCaruses',{
    extend: 'MyApp.Base.Ajax',
    config: {
       method:'post',
       success:function(){
           this.getStore().reload();
       },
       failtrue:function(){
           Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '请求操作失败，请重新尝试'
            });
       }
    }
});

