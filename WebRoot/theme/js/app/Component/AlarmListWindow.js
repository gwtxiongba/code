/* 
 * 警情列表窗口初始化
 */
Ext.define('MyApp.Component.AlarmListWindow',{
    extend: 'Ext.window.Window',
    config:{id:'AlarmListWindow_id'},
    constructor: function(){
        var w = this;
        
        var alarmTab = Ext.create('MyApp.Component.AlarmTabPanel');
        
        this.superclass.constructor.call(this,{
            title: '警情查询',
            width: 800,
            height: 400,
            resizable: false,
            layout: 'border',
            //modal:true,
            items:[alarmTab]
        });
        this.show();
    }
});



