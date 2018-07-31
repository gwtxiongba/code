/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.NewWeixiuGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'NewWeixiuGridPanel_id'},
    constructor:function(){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListWeixiu',{params:{status:0}});
        
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
                {text:'车牌', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'10%'},
                {text:'维修费用', dataIndex: 'fee', sortable: false, menuDisabled:true, width:'10%'},
                {text:'维修时间', dataIndex: 'time', sortable: false, menuDisabled:true, width:'15%'},
                {text:'维修原因', dataIndex: 'reason', sortable: false, menuDisabled:true, width:'15%'},
                {text:'维修人', dataIndex:'name', sortable: false, menuDisabled:true, width:'15%'},
                {text:'电话', dataIndex:'tel', sortable: false, menuDisabled:true, width:'15%'},
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
                id:'ss_plate',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '车牌',
                labelAlign:'right'
            },
			{
                id:'ss_name',
                xtype: 'textfield',
                labelWidth: 70,
                fieldLabel : '维修人',
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                	var ss_name = Ext.getCmp("ss_name").getValue();
                	var ss_plate = Ext.getCmp("ss_plate").getValue();
                    store.load({params:{start:0,limit:20,ss_plate:ss_plate, ss_name:ss_name}});
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
                    if(cellIndex == 7) {
                        var extTarget = Ext.get(event.target);
                        if(!extTarget.hasCls('btn')) return;
                        if(extTarget.hasCls('deal')) {
                                Ext.Msg.confirm('系统提示','请确认是否同意该用户申请',function(opt){
                                	if(opt=='yes')
                                		//alert(record.get('id'));
                                    Ext.ajaxModelLoader('MyApp.Model.AuditWeixiu',{
                                        target: gp, 
                                        params: {type:'deal',id:record.get('id')}
                                    }).request();
                                });
                             
                        }
                        
                        if(extTarget.hasCls('refuse')) {
//                            Ext.Msg.confirm('系统提示','请确认是否拒绝该用户申请',function(opt){
//                                if(opt=='yes') 
//                                	Ext.ajaxModelLoader('MyApp.Model.AuditWeixiu',{
//                                		target: gp, 
//                                		params: {type:'refuse',id:record.get('id')}
//                                	}).request();   
//                            });
                        	Ext.Msg.prompt('系统提示','请输入拒绝原因',function(opt,text){
                        		if(opt=='ok'){
                        			Ext.ajaxModelLoader('MyApp.Model.AuditWeixiu',{
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



