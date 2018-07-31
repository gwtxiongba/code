/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.TeamItemSelector',{
	config:{id:'TeamsSelect_id'},
    extend: 'Ext.ux.form.ItemSelector',
    requires:['Ext.form.Panel','Ext.ux.form.MultiSelect','Ext.ux.form.ItemSelector'],
    constructor:function(){
        var selector = this;
        var ckModel = Ext.getCmp('FleetGridPanel_id').getCheckedModel();
        
        Ext.tip.QuickTipManager.init();
        
        var teamId = "";
        if(ckModel) {
        	teamId = ckModel.getData().teamId;
        }
      
        var ds = Ext.create('Ext.data.Store', {
            fields: ['vehicleId','plate','teamId'],
            proxy: {
                type: 'ajax',
                url: CRMApp.Configuration.servicePath + '?cmd=teamVehicles&teamId=' + teamId,
                reader: 'json'
            },
            listeners:{
                load:function(){
                    var bindIds = [];
                    ds.each(function(vehicle){
                        if(vehicle.get('teamId')) bindIds.push(vehicle.get('vehicleId'));
                    });
                    selector.setValue(bindIds);
                }
            }
        });
        
//        this.selectStore = ds;
        
        Ext.override(Ext.ux.form.ItemSelector, {
//	        onTopBtnClick: function () {       //将top的事件改成全选的事件
//	            var me = this,
//	                fromList = me.fromField.boundList,
//	                allRec = fromList.getStore().getRange();
//	
//	            fromList.getStore().remove(allRec);
//	            me.toField.boundList.getStore().add(allRec);
//	        },
	        
	        onBottomBtnClick: function () {    //将bottom的事件改成清空的事件
	            var me = this,
	                toList = me.toField.boundList,
	                allRec = toList.getStore().getRange();
	            me.moveRec(false, allRec);
	        }
		});
        
        this.superclass.constructor.call(this,{
            name: 'bindIds',
            anchor: '100%',
            hideLabel: true,
            //hideNavIcons:true,
            allowBlank: true,
            msgTarget: 'side',
            buttons: ['add','remove','bottom'],
            buttonsText:{
            add:"添加",
            remove:"移除",
            bottom:"清空"
            },
            store: ds,
            fromTitle: '未编队车辆',
            toTitle: '已编队车辆',
            displayField: 'plate',
            valueField: 'vehicleId',
            renderTo:Ext.getBody()
        });
        
        ds.load();
    }
})

