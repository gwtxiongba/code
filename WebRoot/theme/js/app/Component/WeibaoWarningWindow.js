/* 
 * 司机考情报警列表窗口初始化
 */
Ext.define('MyApp.Component.WeibaoWarningWindow',{
    extend: 'Ext.window.Window',
    config:{id:'DriverLogWarningWindow_id'},
    constructor: function(){
        var w = this;
        
        var warningTab = Ext.create('MyApp.Component.WeibaoWarningPanel');
        
        this.superclass.constructor.call(this,{
            title: '保养/保险/年检 预到期车辆',
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



