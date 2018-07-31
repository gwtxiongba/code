/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.FleetGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'FleetGridPanel_id'},
    constructor:function(){
        var gp = this;
        var store = Ext.ajaxModelLoader('MyApp.Model.ListTeam');
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: false,
            border:true,
            width:'50%',
            maxWidth:'50%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer', text:'序'},
                {text:'机构名称', dataIndex: 'teamName', sortable: false, menuDisabled:true, width:'50%'},
                {text:'创建时间', dataIndex: 'createTime', sortable: false, menuDisabled:true, width:'50%'}
            ],
            tbar: [{
                text: '添加机构',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveFleetWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_id',
                text: '删除机构',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选机构，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while(model = selModels.shift()){
								keys.push(model.get('teamId'));
							}
                            Ext.ajaxModelLoader('MyApp.Model.DelTeam',{
								target: gp, 
								params: {teamIds:keys.join(',')}}
							).request();   
							
                            //Ext.ajaxModelLoader('MyApp.Model.DelOperator',{target: gp, params: {accountId:keys.join(',')}}).request();   
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改机构',
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveFleetWindow');
                }
            }],
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
                    Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
                },
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                },
                'itemclick' : function(view,rec,item,index,e,eOpts){
                   //  Ext.getCmp('DeptGridPanel_id').getStore().reload();
                    Ext.getCmp('DeptGridPanel_id').setValue(rec.get('teamId'));
                     var store = Ext.getCmp('DeptGridPanel_id').getStore();
                     
                     var request = {teamId:rec.get('teamId')};

                       store.reload({params:request});
                     //store.load({params: {teamId:rec.get('teamId')}});
          			// var deptGrid = Ext.create('MyApp.Component.DeptGridPanel',rec.get('teamId'));	
          		}
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



