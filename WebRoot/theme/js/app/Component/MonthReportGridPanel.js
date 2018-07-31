/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MonthReportGridPanel',{
   extend:'Ext.grid.Panel',
   constructor:function(id){
       var me = this;
       
       me.superclass.constructor.call(this,{
           id:id,
           //baseCls:'grid',
           frame:false,
           border:true,
           autoScroll:false,
           scroll:'vertical',
           renderTo:Ext.getBody(),
           width: '100%',
           maxWidth:'100%',
           columns:[{
                text:'年月',
                dataIndex:'mon',
                draggable:false,
                align:'center',
                width: '20%',
                renderer:function(v) {
                    return v;
                }
            },{
                text:'统计对象',
                dataIndex:'obj',
                draggable:false,
                align:'left',
                width: '16%'
            },{
                text:'总里程',
                dataIndex:'mile',
                draggable:false,
                align:'center',
                width: '16%',
                renderer:function(v) {
                    return v + '公里';
                }
            },{
                text:'总时长',
                dataIndex:'time',
                draggable:false,
                align:'center',
                width: '16%',
                renderer:function(v) {
                    return v + '小时';
                }
            },{
                text:'总费用',
                dataIndex:'fee',
                draggable:false,
                align:'center',
                width: '16%',
                renderer:function(v) {
                    return '￥' + v;
                }
            }
            ,{
                text:'操作',
                draggable:false,
                align:'center',
                width:'16%',
                renderer:function(value, metaData, record, rowIndex, colIndex, store) {
                    return '<a class="btn print">下载明细</a>';
                }
            }
       ]
       ,
            listeners:{
                cellClick: function(thisTab, td, cellIndex,record,tr,rowIndex,event,eventObj) {
                    if(cellIndex == 5) {
                        var extTarget = Ext.get(event.target);
                        if(!extTarget.hasCls('btn')) return;
                        if(extTarget.hasCls('print')) {
                            //打开对话框显示
                            Ext.Msg.confirm('系统提示','请确认是否下载该明细',function(opt){
                            	if(opt=='yes'){
                            		var begin_time=null;
                            		var end_time=null;
                            		//alert(me.id);
                            		if(Ext.getCmp('begin_time_report1')){
                            			begin_time=Ext.util.Format.date(Ext.getCmp('begin_time_report1').getValue(), 'Y-m-d');
                            			end_time=Ext.util.Format.date(Ext.getCmp('end_time_report1').getValue(), 'Y-m-d');
                            		}else if(Ext.getCmp('begin_time_report2')){
                            			begin_time=Ext.util.Format.date(Ext.getCmp('begin_time_report2').getValue(), 'Y-m-d');
                            			end_time=Ext.util.Format.date(Ext.getCmp('end_time_report2').getValue(), 'Y-m-d');
                            		}else if(Ext.getCmp('begin_time_report3')){
                            			begin_time=Ext.util.Format.date(Ext.getCmp('begin_time_report3').getValue(), 'Y-m-d');
                            			end_time=Ext.util.Format.date(Ext.getCmp('end_time_report3').getValue(), 'Y-m-d');
                            		}
                            		//alert(begin_time);
                            		//alert(end_time);
//                            		window.open('api.action?cmd=reportMonthDetail&type='+id+'&id='+record.get('obj_id') + '&date=' + record.get('mon'));
                                     window.open('api.action?cmd=reportMonthDetail&type='+id+'&id='+record.get('obj_id') + '&date=' + record.get('mon')+ '&begin_time=' + begin_time+ '&end_time=' + end_time);
                            	}
                            });
                        }
                    }
                }
            }
       });
   }
});

