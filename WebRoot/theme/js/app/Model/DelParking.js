Ext.define('MyApp.Model.DelParking',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'post',
        success:function(){
            this.getStore().reload();
        },
        failtrue:function(){
            Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '司机删除成功'
            });
        }
    }
})

