/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.MemberNewGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'MemberNewGridPanel_id'},
    constructor:function(){
        var gp = this;
       
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListMember',{params: {status:0}});
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer', text:'序'},
                //{text:'司机ID', dataIndex: 'driverId', sortable: false, menuDisabled:true, width:'15%'},
                {text:'登录名', dataIndex: 'user_name', sortable: false, menuDisabled:true, width:'18%'},
                {text:'真实姓名', dataIndex: 'name', sortable: false, menuDisabled:true, width:'10%'},
                {text:'联系电话', dataIndex: 'tel', sortable: false, menuDisabled:true, width:'22%'},
                {text:'公司', dataIndex: 'teamName', sortable: false, menuDisabled:true, width:'20%'},
                {text:'部门', dataIndex:'deptName', sortable: false, menuDisabled:true, width:'15%'},
                {text:'操作', dataIndex:'id', sortable: false, menuDisabled:true, width:'15%',
                	renderer:function(v){
                		return '<a class="btn deal">同意</a>　<a class="btn refuse">拒绝</a>';
                	}}
            ],
           
           
            listeners:{
              cellClick: function(thisTab, td, cellIndex,record,tr,rowIndex,event,eventObj) {
                    if(cellIndex == 6) {
                        var extTarget = Ext.get(event.target);
                        if(!extTarget.hasCls('btn')) return;
                        if(extTarget.hasCls('deal')) {
                                Ext.Msg.confirm('系统提示','请确认是否同意该用户申请',function(opt){
                                	if(opt=='yes')
                                		//alert(record.get('id'));
                                    Ext.ajaxModelLoader('MyApp.Model.AuditMember',{
                                        target: gp, 
                                        params: {status:'1',id:record.get('id')}
                                    }).request();
                                });
                             
                        }
                        
                        if(extTarget.hasCls('refuse')) {
                            Ext.Msg.confirm('系统提示','请确认是否拒绝该用户申请',function(opt){
                                if(opt=='yes') 
                                	Ext.ajaxModelLoader('MyApp.Model.AuditMember',{
                                		target: gp, 
                                		params: {status:'-1',id:record.get('id')}
                                	}).request();   
                            });
                        }
                    }
                },
                beforerender:function(){
                	//gp.getSelectionModel().deselectAll();
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                }
            }
        });
    }
  
})



