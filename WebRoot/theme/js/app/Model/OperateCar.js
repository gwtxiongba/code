/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.OperateCar',{
    extend: 'MyApp.Base.Ajax',
    config:{
        method:'post',
        success:function(o) {
        	var win = Ext.getCmp('orderWindowShow_id');
        	win.close();
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '操作成功！'
            });
            Ext.getCmp('CarTreePanel_id').refreshTree();
            //var cmdPanel = Ext.getCmp('ComInfoPanel_id');
            //cmdPanel.refreshTree();
        }
    }
})

