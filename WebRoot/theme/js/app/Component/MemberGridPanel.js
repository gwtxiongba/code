/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.MemberGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'MemberGridPanel_id'},
    constructor:function(){
        var gp = this;
       
        var selModelss = Ext.create('Ext.selection.CheckboxModel',{
            injectCheckbox:1,
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
                    Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
                    Ext.getCmp('resetBtn_id').setDisabled(!selections.length);
                }             
            }
        });
        
         var store = Ext.ajaxModelLoader('MyApp.Model.ListMember',{params: {status:1}});
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            selModel:selModelss,
            columns: [
                {xtype: 'rownumberer', text:'序'},
                //{text:'司机ID', dataIndex: 'driverId', sortable: false, menuDisabled:true, width:'15%'},
                {text:'登录名', dataIndex: 'user_name', sortable: false, menuDisabled:true, width:'20%'},
                {text:'真实姓名', dataIndex: 'name', sortable: false, menuDisabled:true, width:'20%'},
                {text:'联系电话', dataIndex: 'tel', sortable: false, menuDisabled:true, width:'20%'},
                {text:'公司', dataIndex: 'teamName', sortable: false, menuDisabled:true, width:'15%'},
                {text:'部门', dataIndex:'deptName', sortable: false, menuDisabled:true, width:'10%',renderer:function(v){return v ? v  : '--'}},
                {text:'费用预警值(元)', dataIndex:'warning', sortable: false, menuDisabled:true, width:'15%',renderer:function(v){return v ? v  : '--'}}
            ],
           
            tbar: [{
                text: '添加用车人',
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveMemberWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_id',
                text: '删除用车人',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选用车人，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while((model = selModels.shift())){keys.push(model.get('id'));}
                            Ext.ajaxModelLoader('MyApp.Model.DelMember',{target: gp, params: {id:keys.join(',')}}).request();   
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改用车人信息',
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveMemberWindow');
                    gp.getSelectionModel().deselectAll();//清空选择框
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'resetBtn_id',
                text: '重置密码',
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将重置所选用车人密码，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while((model = selModels.shift())){keys.push(model.get('id'));}
                            Ext.ajaxModelLoader('MyApp.Model.ResetMember',{target: gp, params: {id:keys.join(',')}}).request(); 
                            gp.getSelectionModel().deselectAll();//清空选择框
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id:'ss_name',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '用车人姓名',
                labelAlign:'right'
            },{
                id:'ss_tel',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '联系电话',
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                	var ss_name = Ext.getCmp("ss_name").getValue();
                	var ss_tel = Ext.getCmp("ss_tel").getValue();
                    store.load({params:{start:0,limit:20, ss_name:ss_name,ss_tel:ss_tel}});
                }
            },{
                xtype:'tbfill'
            },{
                text:'导出报表',
                icon:'theme/imgs/ico_excel.png',
                //scale:'medium',
                handler:function(){
                    Ext.Msg.confirm('系统确认','您确定要导出当前报表吗？',function(opt){
                        if(opt=='yes') {
                            //这里添加下载地址
//                            var date = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
//                           var p = Ext.getCmp('vehicleId').getValue();
//                            var plate = Ext.getCmp('plate').getValue();
//                              var index= getIndex(p);
//                             var  vehicleId= list[index].id;
//                            window.open('api.action?cmd=formdLocation_excel&date='+date+'&plate='+plate+'&vehicleId='+vehicleId);
                        	window.open('api.action?cmd=member_excel');
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                xtype:'tbfill'
            },{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }],
            listeners:{
               // selectionchange : function(sm, selections) {
                   // Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
                    //Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
                //},
                beforerender:function(){
                	//gp.getSelectionModel().deselectAll();
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



