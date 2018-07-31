/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.LoadingTips',{
    config: {
        id:'appLoading',
        lazytime: 0,
        msg:'系统正在处理数据，请稍后',
        lazywork: function() {}
    },
    constructor:function(config) {
        this.initConfig(config);
        var _this = this;
        var mask = new Ext.LoadMask(Ext.getBody(),{msg:this.msg});
        mask.show();
        var task = new Ext.util.DelayedTask(function(){
           mask.hide();
           _this.lazywork();
        });
        if(this.lazytime) task.delay(this.lazytime);
    }
})

