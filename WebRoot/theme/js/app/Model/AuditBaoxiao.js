/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.AuditBaoxiao',{
    extend: 'MyApp.Base.Ajax',
    config: {
       method:'post',
       success:function(){
           if(this.win) {
               this.grid.getStore().reload();
               this.win.close();
           } else this.getStore().reload();
           Ext.getCmp('BaoxiaoGridPanel_id').getStore().reload();

       },
       failtrue:function(){
           Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '请求操作失败，请重新尝试'
            });
       }
    }
});

