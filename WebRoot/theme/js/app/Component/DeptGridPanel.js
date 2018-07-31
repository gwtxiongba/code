/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.DeptGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'DeptGridPanel_id'},
    constructor:function(key){
        var gp = this;
        var teamId = "";
        var store = Ext.ajaxModelLoader('MyApp.Model.ListDeptByTeamId');
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: false,
            border:true,
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                 {xtype: 'rownumberer', text:'序'},
                {text:'部门名称', dataIndex: 'deptName', sortable: false, menuDisabled:true, width:'100%'}
               
            ],
            tbar: [{
                text: '添加部门',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveDeptWindow',gp.teamId);
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_ids',
                text: '删除部门',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选部门，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while(model = selModels.shift()){
								keys.push(model.get('deptId'));
							}
                            Ext.ajaxModelLoader('MyApp.Model.DelDept',{
								target: gp, 
								params: {deptId:keys.join(',')}}
							).request();   
							
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_ids',
                text: '修改部门',
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveDeptWindow');
                }
            }],
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteBtn_ids').setDisabled(!selections.length);
                    Ext.getCmp('updateBtn_ids').setDisabled(selections.length != 1);
                },
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                }
            }
        });
       
    },
     setValue:function(teamIds){
        this.teamId = teamIds;
     },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



