/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.ReportMemberGridPanel',{
    extend:'Ext.panel.Panel',
	constructor:function(){
    var panel = this;
   
    var tab = top.Ext.getCmp('apptabs').getActiveTab();
    this.superclass.constructor.call(this,{
        width: tab.getEl().down('iframe').dom.scrollWidth,
        height:tab.getEl().down('iframe').dom.scrollHeight,
        frame:false,
        border:false,
        layout:'border',
//       style:'margin: 7px auto; border:solid 1px #B5B5B5;',
        items:[{
        	region:'west',
        	width:'40%',
        	style:'margin: 0px 3px; border:solid 1px #B5B5B5;',
        	border : false,
        	items:Ext.create('MyApp.Component.ReportMemberleft')
        },{
        	
        	region:'east',
        	width:'60%',
        	style:'margin: 0px 3px; border:solid 1px #B5B5B5;',
        	border : false,
        	items:Ext.create('MyApp.Component.MonthReportGridPanel','monthStats3_id')
        }],
        renderTo: Ext.getBody()
    });
}
});

