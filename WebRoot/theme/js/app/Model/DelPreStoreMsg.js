/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DelPreStoreMsg',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'post',
        success:function(){
            this.getStore().reload();
        },
        failtrue:function(){
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '删除成功'
            });
        }
    }
})

