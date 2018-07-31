Ext.define('MyApp.Model.DelBaseInfo',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'post',
        success:function(){
            this.getStore().reload();
        },
        failtrue:function(){
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '车辆信息删除成功'
            });
        }
    }
})

