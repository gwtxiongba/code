/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.ParkingPanel',{
    extend:'Ext.panel.Panel',
    constructor:function(){
        var panel = this;
        var tab = top.Ext.getCmp('apptabs').getActiveTab();
        var parkingGrid = Ext.create('MyApp.Component.ParkingGridPanel');
		var mapPanel = Ext.create('Ext.panel.Panel',{
						frame:false,
						border:false,
    					height:600,
    					margin:'-15 0 0 -10',
    					html:"<iframe frameborder='0' id='map_frame' Scrolling=no width='100%' height='100%' src='locSend.html'> </iframe>"

					})
        
        this.superclass.constructor.call(this,{
            width: tab.getEl().down('iframe').dom.scrollWidth,
            height:tab.getEl().down('iframe').dom.scrollHeight,
            frame:false,
            border:false,
            layout:'border',
          // style:'padding:0 0.5%',
            items:[{
            	region:'west',
            	width:'50%',
            	//style:'margin:15px 0.5%',
            	border : false,
            	items:[parkingGrid]
            },{
            	title:'当前位置',
            	region:'east',
            	width:'50%',
            	//style:'margin:15px 0.5%',
            	border : false,
            	items:[mapPanel]
            }],
            renderTo: Ext.getBody()
        });
    }
});

