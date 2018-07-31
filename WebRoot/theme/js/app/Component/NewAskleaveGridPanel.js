/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.NewAskleaveGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'NewAskleaveGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListAskleave',{params:{status:0}});
        
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
                {text:'姓名', dataIndex: 'name', sortable: false, menuDisabled:true, width:'15%'},
                {text:'联系电话', dataIndex: 'tel', sortable: true, menuDisabled:true, width:'15%'},
                {text:'开始时间', dataIndex: 'start_time', sortable: true, menuDisabled:true, width:'15%'},
                {text:'结束时间', dataIndex: 'end_time', sortable: true, menuDisabled:true, width:'15%'},
                {text:'请假原因', dataIndex:'reason', sortable: true, menuDisabled:true, width:'20%'},
                {text:'操作', dataIndex:'id', sortable: false, menuDisabled:true, width:'20%',
                	renderer:function(v){
                		return '<a class="btn deal">同意</a>　<a class="btn refuse">拒绝</a>';
                	}}
            ],
           
            tbar: [
//                   {
//                id: 'updateBtn_id',
//                text: '修改司机信息',
//                disabled: true,
//                handler: function(){
//                    Ext.create('MyApp.Component.SaveDriverWindow');
//                }
//            },
			{
                id:'ss_name',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '姓名',
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                	var ss_name = Ext.getCmp("ss_name").getValue();
                	//var ss_tel = Ext.getCmp("ss_tel").getValue();
                    store.load({params:{start:0,limit:20, ss_name:ss_name}});
                }
            },
            {
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
            	cellClick: function(thisTab, td, cellIndex,record,tr,rowIndex,event,eventObj) {
                    if(cellIndex == 6) {
                        var extTarget = Ext.get(event.target);
                        if(!extTarget.hasCls('btn')) return;
                        if(extTarget.hasCls('deal')) {
                                Ext.Msg.confirm('系统提示','请确认是否同意该用户申请',function(opt){
                                	if(opt=='yes')
                                		//alert(record.get('id'));
                                    Ext.ajaxModelLoader('MyApp.Model.AuditAskleave',{
                                        target: gp, 
                                        params: {type:'deal',id:record.get('id')}
                                    }).request();
                                });
                             
                        }
                        
                        if(extTarget.hasCls('refuse')) {
//                            Ext.Msg.confirm('系统提示','请确认是否拒绝该用户申请',function(opt){
//                                if(opt=='yes') 
//                                	Ext.ajaxModelLoader('MyApp.Model.AuditAskleave',{
//                                		target: gp, 
//                                		params: {type:'refuse',id:record.get('id')}
//                                	}).request();   
//                            });
                        	Ext.Msg.prompt('系统提示','请输入拒绝原因',function(opt,text){
                        		if(opt=='ok'){
                        			Ext.ajaxModelLoader('MyApp.Model.AuditAskleave',{
                                		target: gp, 
                                		params: {type:'refuse',reason2:text,id:record.get('id')}
                                	}).request(); 
                        		}
                        	})
                        }
                    }
                },
            	
              beforerender:function(){
                  var tab = top.Ext.getCmp('apptabs').getActiveTab();
                  this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                  this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
              }
          }
        });
    },
})



