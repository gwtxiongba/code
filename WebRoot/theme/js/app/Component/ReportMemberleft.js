/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.ReportMemberleft',{
   extend:'Ext.grid.Panel',
   constructor:function(){
       var me = this;
       var store = Ext.ajaxModelLoader('MyApp.Model.ReportMember');
       var begin_time=null;
       var end_time=null;
       
       me.superclass.constructor.call(this,{
           id:'reportMemberleft_id',
           //baseCls:'grid',
           frame:false,
           border:true,
           autoScroll:false,
           scroll:'vertical',
           renderTo:Ext.getBody(),
           width: '100%',
           maxWidth:'100%',
           columns:[{
                text:'乘客姓名',
                dataIndex:'name',
                draggable:false,
                align:'left',
                width: '25%'
            },{
                text:'总里程',
                dataIndex:'mile',
                draggable:false,
                align:'center',
                width: '25%',
                renderer:function(v) {
                    return v + '公里';
                }
            },{
                text:'总时长',
                dataIndex:'time',
                draggable:false,
                align:'center',
                width: '25%',
                renderer:function(v) {
                    return v + '小时';
                }
            },{
                text:'总费用',
                dataIndex:'fee',
                draggable:false,
                align:'center',
                width: '25%',
                renderer:function(v) {
                    return '￥' + v;
                }
            }],
            tbar:[{
            	text:'开始时间:'
    		},{
                id:'begin_time_report3',
                xtype: 'datefield',
                format: 'Y-m-d',
                 width:100,
                labelAlign:'right'
            },{
            	text:'结束时间:'
    		},{
                id:'end_time_report3',
                xtype: 'datefield',
                format: 'Y-m-d',
                 width:100,
                labelAlign:'right'
            },{
                text : '查询',
                handler : function(){
                     begin_time=Ext.util.Format.date(Ext.getCmp('begin_time_report3').getValue(), 'Y-m-d');
                    //var begin_time=(begin==null?'':begin.getTime()/1000-1);
                     end_time=Ext.util.Format.date(Ext.getCmp('end_time_report3').getValue(), 'Y-m-d');
                    //var end_time=(end==null?'':end.getTime()/1000+1);

//                    var tabPanel = Ext.getCmp('baoxiaoTab');
//                    var cmp = tabPanel.getActiveTab().items.items[0].id;//获取当前活动的tab的id
//                    Ext.getCmp(cmp).store.currentPage = 1;//搜索时重置页码为1
                    me.store.load({params: {begin_time:begin_time,end_time:end_time}});
                    // alert(Ext.getCmp('nickname').getValue());
                }
            }
                  ],
            listeners:{
                itemclick:function(view, record, item, index, e, eOpts){
                    var grid = Ext.getCmp('monthStats3_id');
                    var st = Ext.ajaxModelLoader('MyApp.Model.ReportMonth', {
                            params:{id:record.get('id'),type:3,begin_time:begin_time,end_time:end_time}
                         });
                    grid.bindStore(st);
                }
            },
            store:store
       });
   }
});

