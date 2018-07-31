/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
Ext.define('MyApp.Component.CmdTabPanel', {
			extend : 'Ext.tab.Panel',
			constructor : function() {
				var carInfoPanel = Ext.create('MyApp.Component.CarInfoPanel');
				var comInfoPanel = Ext.create('MyApp.Component.ComInfoPanel');
				this.superclass.constructor.call(this, {
							id : 'cmdtabs',
							region : 'south',
							border : false,
							height : 120,
							items : [{
										title : '车辆信息',
										layout : 'fit',
										itemId : 'carInfo',
										items : [carInfoPanel]
									}, {
										title : '指令信息',
										layout : 'fit',
										itemId : 'comInfo',
										items : [comInfoPanel]
									}]
						});
				Ext.getCmp('appmain').add(this);
			}
		})
