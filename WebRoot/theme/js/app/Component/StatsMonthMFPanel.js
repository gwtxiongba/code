/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.StatsMonthMFPanel',{
   extend:'Ext.panel.Panel',
   config:{},
   constructor:function(){
       var panel = this;
       var tab = top.Ext.getCmp('apptabs').getActiveTab();
       
       Ext.define('stats', {
            extend: 'Ext.data.Model',
            fields: ['vehicleId', 'plate', 'mileage','fuel']
        });
        
       var store = Ext.create('Ext.data.Store',{
           model:'stats',
           data:[]
       });
       
       Ext.chart.theme.Browser = Ext.extend(Ext.chart.theme.Base, {
            constructor: function(config) {
                Ext.chart.theme.Base.prototype.constructor.call(this, Ext.apply({
                    colors: ['rgb(47, 162, 223)','rgb(255, 255, 0)']
                }, config));
            }
        });
       
       //矩形图报表
       var chart = Ext.create('Ext.chart.Chart',{
          style: 'background:#fff',
          theme: 'Browser:gradients',
          animate:true,
          legend: {
                position: 'bottom'
          },
          axes:[{
                title:'总里程/总油耗',
                type: 'Numeric',
                position:'left',
                fields: ['mileage','fuel'],
                minimum: 0,
                grid:true
            },{
                type:'Category',
                position: 'bottom',
                fields: ['plate'],
                label: {rotate: {degrees: 90}}
            }],
            series: [
               {
                   type: 'column',
                   xField: 'plate',
                   yField: ['mileage','fuel'],
                   label : {  
                       display : 'insideEnd',  
                       'text-anchor' : 'middle',  
                       field :['mileage','fuel'],  
                       renderer : Ext.util.Format.numberRenderer('0')
                   },
                   tips: {
                       width: 150,
                       renderer:function(storeItem,item) {
                           this.setTitle(storeItem.get('plate') + '<br/>总里程：' + storeItem.get('mileage') + ' 公里' + '<br/>总油耗：' + storeItem.get('fuel') + ' 升');
                       }
                   }
               }
           ],
            store:store
       });
       
       //月统计列表
       var grid_month = Ext.create('Ext.grid.Panel',{
           frame: true,
           border:true,
           autoScroll: false,
           scroll:'vertical',
           columns:[
               {xtype:'rownumberer'},
               {text:'车牌号',dataIndex:'plate',sortable:false,menuDisabled:true},
               {text:'所属分组',dataIndex:'team',sortable:false, renderer:function(val){
                   return !val ? '未分组车辆' : val;
               }},
               {text:'总里程',dataIndex:'mileage', renderer:function(val){
                   return val + ' 公里';    
               }},
               {text:'总油耗',dataIndex:'fuel', renderer:function(val){
                   return val + ' 升';   
               }},
               {text:'超速时间',dataIndex:'overspeedhours',renderer:function(val){
                      return val+' 小时'; 
               }},
               {text:'急加/减速次数',dataIndex:'acctimes'},
               {text:'疲劳驾驶次数',dataIndex:'sleepytimes'}
           ],
           listeners:{
               itemdblclick:function(grid,record,item,index,e){
                   //this, record, item, index, e, eOpts   
               }
           }
       });
       
       this.superclass.constructor.call(this,{
           width: tab.getEl().down('iframe').dom.scrollWidth,
           height:tab.getEl().down('iframe').dom.scrollHeight,
           border:false,
           layout:'border',
           tbar:[{
              xtype:'textfield',
              id:'plate',
              fieldLabel:'车牌',
              labelAlign: 'right',
              labelWidth:40,
              width:120
           },{
               xtype:'textfield',
               id:'fuel',
               fieldLabel:'自定义油耗',
               labelAlign: 'right',
               labelWidth:70,
               width:130
           },{
               xtype:'tbtext',
               text:'升/百公里'
           },{
               xtype:'datefield',
               id:'date',
               fieldLabel:'年月',
               labelWidth: 50,
               labelAlign: 'right',
               width:140,
               format:'Y-m',
               value: new Date().format('yyyy-MM'),
               name:'date'
           },{
                text:'1月',
                handler:function(){
                   setMonth(1);
                }
           },{
                text:'2月',
                handler:function(){
                   setMonth(2);
                }
           },{
                text:'3月',
                handler:function(){
                   setMonth(3);
                }
           },{
                text:'4月',
                handler:function(){
                   setMonth(4);
                }
           },{
                text:'5月',
                handler:function(){
                   setMonth(5);
                }
           },{
                text:'6月',
                handler:function(){
                   setMonth(6);
                }
           },{
                text:'7月',
                handler:function(){
                   setMonth(7);
                }
           },{
                text:'8月',
                handler:function(){
                   setMonth(8);
                }
           },{
                text:'9月',
                handler:function(){
                   setMonth(9);
                }
           },{
                text:'10月',
                handler:function(){
                   setMonth(10);
                }
           },{
                text:'11月',
                handler:function(){
                   setMonth(11);
                }
           },{
                text:'12月',
                handler:function(){
                   setMonth(12);
                }
           },{
                xtype: 'tbseparator'
           },{
               text:'查询',
               icon:'theme/imgs/ico_sea.png',
               scale:'medium',
               handler:function(){
               	   var plate = Ext.getCmp('plate').getValue();
               	   var fuel = Ext.getCmp('fuel').getValue();
               	   var date = Ext.getCmp('date').getValue().format('yyyy-MM');
                   store = Ext.ajaxModelLoader('MyApp.Model.StatsMonthMF',{params:{plate:plate,fuel:fuel,date:date}});
                   chart.bindStore(store);
                   grid_month.bindStore(store);
               }
           },{
               xtype:'tbfill'
           },{
               text:'导出',
               icon:'theme/imgs/ico_excel.png',
               scale:'medium',
               handler:function(){
                   Ext.Msg.confirm('系统确认','您确定要导出当前报表吗？',function(opt){
                       if(opt=='yes') {
                           //这里添加下载地址
                           var plate = Ext.getCmp('plate').getValue();
		               	   var fuel = Ext.getCmp('fuel').getValue();
		               	   var date = Ext.getCmp('date').getValue().format('yyyy-MM');
                           window.open('api.action?cmd=exportCycleMonthSummary&date='+date+'&fuel='+fuel+'&plate='+plate);
                       }
                   });
               }
           }],
           items:[{
               region:'west',
               layout:'border',
               width:400,
               items:chart
           },{
               region:'center',
               border:false,
               layout:'fit',
               items:grid_month
           }],
           renderTo: Ext.getBody()
       });
       
       var setMonth = function(month){
           var date = Ext.getCmp('date').getValue();
           date.setDate(1);
           date.setMonth(month-1);
           Ext.getCmp('date').setValue(date);
       }
   }
});

