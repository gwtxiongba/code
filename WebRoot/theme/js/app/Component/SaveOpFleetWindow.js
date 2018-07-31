/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this template, choose Tools | Templates
 * 新建、修改操作员弹窗
 */
Ext.define('MyApp.Component.SaveOpFleetWindow',{
    extend: 'Ext.window.Window',
    requires:['Ext.form.Panel','Ext.ux.form.MultiSelect','Ext.ux.form.ItemSelector'],
    constructor: function(){
        var w = this;
        var ckModel = Ext.getCmp('OpFleetGridPanel_id').getCheckedModel();

        var operatorName = {
            xtype: 'textfield',
            style: 'margin: 5px 0px 3px 220px',
            emptyText: '命名操作员',
            fieldLabel : '操作员名称',
            width: 180,
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'operatorName'
        };
        
//         var fleetSearch = {
//            xtype: 'textfield',
//            style: 'margin: 5px 0px 3px -45px',
//            emptyText: '查询车队',
//            fieldLabel : '车队搜索',
//            width: 190,
//            labelAlign:'right',
//            id:'fleetSh',
//            listeners:{
//            	change:function(){
//            		var plateIn = Ext.getCmp("fleetSh").getValue();
//            		if(null==plateIn||""==plateIn)
//            		Ext.getCmp('OpTeamsSelect_id').fromField.boundList.getStore().load();
//					var fieldstore = Ext.getCmp('OpTeamsSelect_id').fromField.boundList.getStore();
//					var afterStore = Ext.create('Ext.data.Store', {
//            						fields: ['operatorId','teamName','teamId']});
//            		fieldstore.each(function(dataRecord){
//            			if(dataRecord.get('teamName').indexOf(plateIn)!=-1)
//            			afterStore.add(dataRecord);
//            		})
//            		var allrec = afterStore.getRange();
//					Ext.getCmp('OpTeamsSelect_id').fromField.boundList.getStore().removeAll();
//					Ext.getCmp('OpTeamsSelect_id').fromField.boundList.getStore().add(allrec);
//            	}
//            }
//        };
//        
        var key = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'operatorId'
        };
        
        var selector = Ext.create('MyApp.Component.OpTeamItemSelector',{
            layout:'fit'
        });
        
        var formPanel = Ext.create('Ext.form.Panel',{
            //bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            layout:'border',
            //items :[fleetName, selector]
            items :[key,{
                region: 'north',
				layout: 'table',
				border: false,
				items: [{
	                region: 'north',
	                layout: 'table',
	                border: false,
	                height: 35,
	                items: [operatorName]
				}]
            },{
                region: 'center',
				layout: 'fit',
				border: false,
				items: [selector]
            }]
            
        });
        
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
        }
        
        this.superclass.constructor.call(this,{
           title:'修改操作员：' + ckModel.get('operatorName'),
           width: 430,
           height: 360,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[formPanel],
           buttons:[{
                text:'提交',
                handler:function(btn) {
                	//alert(formPanel.getForm().getValues())
//                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.UpdateOpTeam',{
                        params:formPanel.getForm().getValues(),
                        target: w
                    }).request();
                }
            },{
                text: '退出',
                handler:function(btn) {
                   w.close();        
                }
            }]
        });
        this.show();
    }
});



