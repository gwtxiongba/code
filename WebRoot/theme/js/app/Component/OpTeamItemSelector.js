/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.OpTeamItemSelector',{
	config:{id:'OpTeamsSelect_id'},
    extend: 'Ext.ux.form.ItemSelector',
    requires:['Ext.form.Panel','Ext.ux.form.MultiSelect','Ext.ux.form.ItemSelector'],
    constructor:function(){
        var selector = this;
        var ckModel = Ext.getCmp('OpFleetGridPanel_id').getCheckedModel();
        
         Ext.tip.QuickTipManager.init();
         
        var operatorId = "";
        if(ckModel) {
        	operatorId = ckModel.getData().operatorId;
        }
      
        var ds = Ext.create('Ext.data.Store', {
            fields: ['operatorId','teamName','teamId'],
            proxy: {
                type: 'ajax',
                url: CRMApp.Configuration.servicePath + '?cmd=listTeamsAndOperator&operatorId=' + operatorId,
                reader: 'json'
            },
            listeners:{
                load:function(){
                    var bindIds = [];
                    ds.each(function(team){
                        if(team.get('operatorId')) bindIds.push(team.get('teamId'));
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
            allowBlank: true,
            msgTarget: 'side',
            buttons: ['add','remove','bottom'],
            buttonsText:{
            add:"添加",
            remove:"移除",
            bottom:"清空"
            },
            store: ds,
            fromTitle: '可管理车队',
            toTitle: '已管理车队',
            displayField: 'teamName',
            valueField: 'teamId',
            renderTo:Ext.getBody()
        });
        
        ds.load();
    }
})

