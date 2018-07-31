/* 
 * 警情列表窗口初始化
 */
Ext.define('MyApp.Component.AbnormalHistoryListWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        
        var historyPanel = Ext.create('MyApp.Component.AbnormalHistoryListPanel');
        
        var model = Ext.getCmp('AbnormalListPanel_id').getSelectionModel().getSelection()[0];
        
        this.superclass.constructor.call(this,{
            title: '车辆异常历史:'+model.get('plate'),
            width: 700,
            height: 520,
            resizable: false,
            layout: 'border',
            items:[historyPanel]
        });
        this.show();
    }
});



