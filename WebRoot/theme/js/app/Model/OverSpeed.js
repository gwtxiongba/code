/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.OverSpeed',{
    extend: 'MyApp.Base.Ajax',
    config:{
        method:'post',
        success:function(o) {
        	var win = Ext.getCmp('speedWindowShow_id');
        	win.close();
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '设置成功！'
            });
            Ext.getCmp('CarTreePanel_id').refreshTree();
            //var cmdPanel = Ext.getCmp('ComInfoPanel_id');
            //cmdPanel.refreshTree();
        }
    }
})

