/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DelOperator',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'post',
        success:function(){
            this.getStore().reload();
            if(Ext.getCmp('OpFleetGridPanel_id'))
            Ext.getCmp('OpFleetGridPanel_id').getStore().reload();
        },
        failtrue:function(){
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '信息提交成功'
            });
        }
    }
})

