/* 
 * 消息中心窗口初始化
 */
Ext.define('MyApp.Component.MsgCenterWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        /*
        var startDate = {
        	id: 'startDate',
			xtype: 'datefield',
			style: 'margin: 5px 12px 3px 5px',
			labelWidth: 55,
			format: 'Y-m-d',
			//format: 'Y-m-d H:i:s ',
			fieldLabel: '开始时间',
			name: 'startDate'
        };
        
        var endDate = {
        	id: 'endDate',
			xtype: 'datefield',
			style: 'margin: 5px 12px 3px 5px',
			labelWidth: 55,
			format: 'Y-m-d',
			//format: 'Y-m-d H:i:s ',
			fieldLabel: '结束时间',
			name: 'endDate'
        };
        
        var btn = {
    		 minWidth: 60,
             xtype: 'button',
             text: '查询',
             handler: function(){
            	 var startDate = Ext.getCmp('startDate').getValue();
            	 var endDate = Ext.getCmp('endDate').getValue();
            	 startDate = Ext.Date.format(new Date(startDate),'Y-m-d H:i:s'); 
            	 endDate = Ext.Date.format(new Date(endDate),'Y-m-d H:i:s'); 
            	 var grid = Ext.getCmp('MsgSessionGrid_id');
            	 grid.getStore().load({params:{startDate:startDate, endDate:endDate }});
             }
        };
        
        var panel1 = {
            xtype: 'panel',
            region: 'north',
            layout: 'table',
            border: false,
            height: 35,
            items :[startDate, endDate ,btn]
        };*/
        
        var msgGrid = Ext.create('MyApp.Component.MsgSessionGridPanel');
        
        this.superclass.constructor.call(this,{
            title: '调度中心',
            width: 600,
            height: 410,
            resizable: false,
            layout: 'border',
            //modal:true,  //是否遮罩
            items:[msgGrid]
        });
        this.show();
    }
});



