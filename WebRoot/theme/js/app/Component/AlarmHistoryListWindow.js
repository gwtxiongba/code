/* 
 * 警情列表窗口初始化
 */
Ext.define('MyApp.Component.AlarmHistoryListWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        
        var historyPanel = Ext.create('MyApp.Component.AlarmHistoryListPanel');
        
        var model = Ext.getCmp('AlarmListPanel_id').getSelectionModel().getSelection()[0];
        
        this.superclass.constructor.call(this,{
            title: '车辆报警历史:'+model.get('plate'),
            width: 700,
            height: 520,
            resizable: false,
            layout: 'border',
            //modal:true,
            items:[historyPanel]
        });
        this.show();
    }
});



