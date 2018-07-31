/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.OperatorGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'OperatorGridPanel_id'},
    constructor:function(){
        var gp = this;
        //var store = Ext.create('MyApp.Model.Operators');
        var store = Ext.ajaxModelLoader('MyApp.Model.ListOperator');
       
        var selModel = Ext.create('Ext.selection.CheckboxModel',{
            injectCheckbox:1,
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
                    Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
                    Ext.getCmp('resetBtn_id').setDisabled(!selections.length);
                }             
            }
        });
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            selModel:selModel,
            columns: [
                {xtype: 'rownumberer', text:'序'},
                //{xtype: 'checkcolumn', dataIndex:'accountId',sortable: false, menuDisabled:true, width: '5%', text:'全选'},
                {text:'登录名', dataIndex: 'account', sortable: true, menuDisabled:true, width:'10%'},
                {text:'姓名', dataIndex:'realname', menuDisabled:true, menuDisabled:true, width:'10%'},
                {text:'角色', dataIndex:'levelName',menuDisabled:true, menuDisabled:true, width:'14%'},
                {text:'所属机构', dataIndex:'teamName',menuDisabled:true, menuDisabled:true, width:'14%'},
                {text:'所属部门', dataIndex:'deptName',menuDisabled:true, menuDisabled:true, width: '14%'},
                {text:'联系电话', dataIndex:'phone', menuDisabled:true, menuDisabled:true, width:'14%'},
                {text:'登录次数', dataIndex:'logintimes', menuDisabled:true, width:'10%'},
                {text:'最后登录日期',dataIndex:'visitTime', menuDisabled:true, width: '10%', renderer:function(v){return v ? v.toDate().format('yyyy-M-d') : '未登录'}}
            ],
            tbar: [{
                text: '添加管理员',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveOpWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_id',
                text: '删除管理员',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选操作员账号，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while((model = selModels.shift())){keys.push(model.get('accountId'));}
                            Ext.ajaxModelLoader('MyApp.Model.DelOperator',{target: gp, params: {accountIds:keys.join(',')}}).request();   
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改管理员',
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveOpWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'resetBtn_id',
                text: '重置密码',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将重置所选账户密码，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while((model = selModels.shift())){keys.push(model.get('accountId'));}
                            Ext.ajaxModelLoader('MyApp.Model.ResetAccount',{target: gp, params: {ids:keys.join(',')}}).request();   
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                 id:'seakey',
                 xtype: 'textfield',
                 labelWidth: 40,
                 fieldLabel : '姓名',
                 labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                    //store.filter('realName',Ext.getCmp("seakey").getValue(),false,false);store.reload()
                    store.load({params:{ start:0,limit:20,name:Ext.getCmp("seakey").getValue() }});
                }
            },{
                xtype:'tbfill'
            },{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }],
            listeners: {
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                },
                headerClick:function(g,index,e){
                  // alert(index.dataIndex);
                   
                 //Ext.ajaxModelLoader('MyApp.Model.ListVehicle');
                //this.store.setParams("sortF", "p11");
                // this.store.reload({params:{sortF:index.dataIndex}});
                }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})

