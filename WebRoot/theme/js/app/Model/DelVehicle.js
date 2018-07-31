Ext.define('MyApp.Model.DelVehicle',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'post',
        success:function(){
            this.getStore().reload();
            top.Ext.getCmp('CarTreePanel_id').refreshTree();
        },
        failtrue:function(){
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '车辆删除成功'
            });
        }
    }
})

