/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AdministratorGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'AdministratorGridPanel_id'},
    constructor:function(){
        var gp = this;
        //var store = Ext.create('MyApp.Model.Operators');
        var store = Ext.ajaxModelLoader('MyApp.Model.ListAdmin');
       
        var selModel = Ext.create('Ext.selection.CheckboxModel',{
            injectCheckbox:1,
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
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
                {text:'公司名', dataIndex:'realname', sortable: false, menuDisabled:true, width:'9%'},
                {text:'管理员账号', dataIndex: 'account', sortable: false, menuDisabled:true, width:'9%'},
                {text:'管理员姓名', dataIndex: 'accountRealName', sortable: false, menuDisabled:true, width:'9%'},
                {text:'联系地址', dataIndex:'address', sortable: false, menuDisabled:true, width:'13%'},
                {text:'联系电话', dataIndex:'phone', sortable: false, menuDisabled:true, width:'13%'},
                {text:'电子邮件', dataIndex:'email', sortable: false, menuDisabled:true, width: '13%'},
                {text:'创建时间', dataIndex:'createTime', menuDisabled:true, width:'13%', renderer:function(v){return v ? v.toDate().format('yyyy-M-d h:m') : '未登录'}},
                {text:'登录次数', dataIndex:'logintimes', menuDisabled:true, width:'9%'},
                {text:'最后登录日期',dataIndex:'visitTime', menuDisabled:true, width: '9%', renderer:function(v){return v ? v.toDate().format('yyyy-M-d') : '未登录'}}
            ],
            tbar: [{
                text: '添加机构管理员',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveAdminWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改机构管理员',
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveAdminWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                 id:'seakey',
                 xtype: 'textfield',
                 labelWidth: 40,
                 fieldLabel : '搜索',
                 labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                    //store.filter('realName',Ext.getCmp("seakey").getValue(),false,false);store.reload()
                    store.load({params:{ start:0,limit:20,realname:Ext.getCmp("seakey").getValue() }});
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
                }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})

