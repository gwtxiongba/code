/* 
 * 司机考情报警列表窗口初始化
 */
Ext.define('MyApp.Component.DriverLogWarningWindow',{
    extend: 'Ext.window.Window',
    config:{id:'DriverLogWarningWindow_id'},
    constructor: function(){
        var w = this;
        
        var warningTab = Ext.create('MyApp.Component.DriverLogWarningPanel');
        
        this.superclass.constructor.call(this,{
            title: '考勤详情',
            width: 800,
            height: 400,
            resizable: false,
            layout: 'border',
            //modal:true,
            items:[warningTab]
        });
        this.show();
    }
});



