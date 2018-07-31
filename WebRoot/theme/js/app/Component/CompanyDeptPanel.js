/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.CompanyDeptPanel',{
    extend:'Ext.panel.Panel',
    constructor:function(){
        var panel = this;
       
        var tab = top.Ext.getCmp('apptabs').getActiveTab();
        var companyGrid = Ext.create('MyApp.Component.FleetGridPanel');	
          var deptGrid = Ext.create('MyApp.Component.DeptGridPanel');	
        this.superclass.constructor.call(this,{
            width: tab.getEl().down('iframe').dom.scrollWidth,
            height:tab.getEl().down('iframe').dom.scrollHeight,
            frame:false,
            border:false,
            layout:'border',
            items:[{
            	region:'west',
            	width:'50%',
            	//style:'margin:15px 0.5%',
            	border : false,
            	items:[companyGrid]
            },{
            	
            	region:'east',
            	width:'50%',
            	//style:'margin:15px 0.5%',
            	border : false,
            	items:[deptGrid]
            }],
            renderTo: Ext.getBody()
        });
    }
    
       
});

