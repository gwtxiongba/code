/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this template, choose Tools | Templates
 * 新建、修改操作员弹窗
 */
Ext.define('MyApp.Component.SaveFleetWindow',{
    extend: 'Ext.window.Window',
    requires:['Ext.form.Panel','Ext.ux.form.MultiSelect','Ext.ux.form.ItemSelector'],
    constructor: function(){
        var w = this;
        var ckModel = Ext.getCmp('FleetGridPanel_id').getCheckedModel();

        var fleetName = {
            xtype: 'textfield',
            style: 'margin:15px 0px 3px 30px',
            emptyText: '命名机构',
            fieldLabel : '机构名称',
            width: 260,
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'teamName'
        };
        
         var plateSearch = {
            xtype: 'textfield',
            style: 'margin: 15px 0px 3px -45px',
            emptyText: '添加车牌号',
            fieldLabel : '车牌搜索',
            width: 190,
            labelAlign:'right',
            id:'plateSh',
            listeners:{
            	change:function(){
            		var plateIn = Ext.getCmp("plateSh").getValue();
            		if(null==plateIn||""==plateIn)
            		Ext.getCmp('TeamsSelect_id').fromField.boundList.getStore().load();
					var fieldstore = Ext.getCmp('TeamsSelect_id').fromField.boundList.getStore();
					var afterStore = Ext.create('Ext.data.Store', {
            						fields: ['vehicleId','plate','teamId']});
            		fieldstore.each(function(dataRecord){
            			if(dataRecord.get('plate').indexOf(plateIn)!=-1)
            			afterStore.add(dataRecord);
            		})
            		var allrec = afterStore.getRange();
					Ext.getCmp('TeamsSelect_id').fromField.boundList.getStore().removeAll();
					Ext.getCmp('TeamsSelect_id').fromField.boundList.getStore().add(allrec);
            	}
            }
        };
        
        var order = {
            xtype: 'textfield',
            style: 'margin: 15px 0px 3px -55px',
            blankText: '请输出序号',
            maxLength:2,
            hidden:true,
            width: 130,
            value: 1,
            fieldLabel : '序号',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'order'
        };
        
        var key = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'teamId'
        };
        
        var selector = Ext.create('MyApp.Component.TeamItemSelector',{
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
            	//region: 'north',
                region: 'center',
				layout: 'table',
				border: false,
				items: [{
	                region: 'north',
	                layout: 'table',
	                border: false,
	                height: 50,
	                //items: [plateSearch,fleetName, order]
	                items: [fleetName, order]
				}]
            }
//            ,{
//                region: 'center',
//				layout: 'fit',
//				border: false,
//				items: [selector]
//            }
            ]
            
        });
        
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
        }
        
        this.superclass.constructor.call(this,{
           title: !ckModel ? '添加机构' : ('修改机构：' + ckModel.get('teamName')),
           width: 430,
           height: 150,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[formPanel],
           buttons:[{
                text:'提交',
                handler:function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.SaveTeam',{
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



