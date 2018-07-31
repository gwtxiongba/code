/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.DriverLogListWindow',{
    extend:'Ext.window.Window',
    config:{id:'DriverLogListWindow_id'},
    constructor:function(){
       var w = this;
       var selData = Ext.getCmp('driver_list').getSelectionModel().getSelection();
       var driverId = selData[0].get('driverId');
       var store = Ext.ajaxModelLoader('MyApp.Model.ListDriverLog',{params:{driverId:driverId}});
       
	   var gridPanel = Ext.create('Ext.grid.Panel',{
	    	 id:'driverLogId',
//	    	 height:368,
	    	 frame: false,
             border:false,
             region:'center',
             width:'100%',
           	 maxWidth:'100%',
             autoScroll: true,
             scroll:'vertical',
	    	 store: store,
             columns: [
                {xtype: 'rownumberer', text:'序'},
                {text:'车牌', dataIndex:'car', sortable: false, menuDisabled:true, width:'25%',renderer:function(v){return v.plate}},
                {text:'绑定时间', dataIndex: 'signInTime', sortable: false, menuDisabled:true, width:'25%'},
                {text:'解绑时间', dataIndex: 'unbindTime', sortable: true, menuDisabled:true, width:'25%'},
                {text:'使用详情', dataIndex: 'ifLegal', sortable: true, menuDisabled:true, width:'25%',renderer:function(v){return v == 1? "正常使用":"非法使用"}}
              ],
               bbar:[{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }]
              
        });
        
        this.superclass.constructor.call(this,{
            title: '考勤列表',
            height: 400,
            width:800,
		    resizable: false,
		    layout: 'border',
            items:[gridPanel],
            listeners:{
            	'afterlayout': function(){
              		Ext.getCmp('DriverLogListWindow_id').center();
              	}
            }
        });
         this.show();
    }
});



