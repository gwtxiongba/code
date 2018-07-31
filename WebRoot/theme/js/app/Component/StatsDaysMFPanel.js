/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.StatsDaysMFPanel',{
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
                label: { rotate: { degrees: 90} }
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
                           this.setTitle(storeItem.get('plate') + '<br/>总里程：' + storeItem.get('mileage') + ' 公里' + '<br/>总油耗：' + storeItem.get('fuel') + ' 升/百公里');
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
                   var vehicleId = record.get('vehicleId');
                   
                   var dateStore = Ext.ajaxModelLoader('MyApp.Model.StatsDateMF',{params:{vehicleId:vehicleId,
                   	  date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                      date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                      plate:Ext.getCmp('plate').getValue(),
                      fuel:Ext.getCmp('fuel').getValue()
                   }});
                   grid_date.bindStore(dateStore);
                   Ext.getCmp('detail').setHeight(150);
                   
               }
           }
       });
       
       //日统计表
       var grid_date = Ext.create('Ext.grid.Panel',{
           frame:true,
           border:true,
           height:150,
           autoScroll:false,
           scroll:'vertical',
           columns:[
               {'text': '日期',dataIndex:'date'},
               {'text':'车牌号',dataIndex:'plate'},
               {'text':'行驶里程',dataIndex:'mileage',renderer:function(val){
                   return val + ' 公里';    
               }},
               {'text':'油耗', dataIndex:'fuel',renderer:function(val){
                   return val + ' 升';   
               }},
               {'text':'超速时间',dataIndex:'overspeedhours'},
               {'text':'急加/减速次数',dataIndex:'acctimes'},
               {'text':'疲劳驾驶次数',dataIndex:'sleepytimes'}
           ]
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
               id:'date0',
               fieldLabel:'从',
               labelWidth: 20,
               labelAlign: 'right',
               format:'Y-m-d',
               value: new Date().format('yyyy-MM-dd'),
               maxValue:new Date().format('yyyy-MM-dd'),
               name:'date0'
           },{
               xtype:'datefield',
               id:'date1',
               fieldLabel:'到',
               labelWidth: 20,
               labelAlign: 'right',
               format:'Y-m-d',
               value: new Date().format('yyyy-MM-dd'),
               maxValue:new Date().format('yyyy-MM-dd'),
               name:'date1'
           },{
                text:'今天',
                handler:function(){
                   Ext.getCmp('date0').setValue(new Date());
                   Ext.getCmp('date1').setValue(new Date());
                }
           },{
                xtype: 'tbseparator'
           },{
                text:'昨天',
                handler:function(){
                   var d = new Date();
                   d.setDate(d.getDate()-1);
                   Ext.getCmp('date0').setValue(d);
                   Ext.getCmp('date1').setValue(d);
                }
           },{
                xtype: 'tbseparator'
           },{
                text:'近三天',
                handler:function(){
                   var d = new Date();
                   Ext.getCmp('date1').setValue(d);
                   d.setDate(d.getDate()-2);
                   Ext.getCmp('date0').setValue(d);
                }
           },{
                xtype: 'tbseparator'
           },{
                text:'近七天',
                handler:function(){
                   var d = new Date();
                   Ext.getCmp('date1').setValue(d);
                   d.setDate(d.getDate()-6);
                   Ext.getCmp('date0').setValue(d);
                }
           },{
               text:'查询',
               icon:'theme/imgs/ico_sea.png',
               scale:'medium',
               handler:function(){
                   Ext.getCmp('detail').setHeight(0);
                   store = Ext.ajaxModelLoader('MyApp.Model.StatsDaysMF',{params:{
                      date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                      date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                      plate:Ext.getCmp('plate').getValue(),
                      fuel:Ext.getCmp('fuel').getValue()
                   }});
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
                           var date0 = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                           var date1 = Ext.getCmp('date1').getValue().format('yyyy-MM-dd');
                           var plate = Ext.getCmp('plate').getValue();
                      	   var fuel = Ext.getCmp('fuel').getValue();
                           window.open('api.action?cmd=statsTotalMF_excel&date0='+date0+'&date1='+date1+'&plate='+plate+'&fuel='+fuel);
                       }
                   });
               }
           },{
               text:'导出(统计)',
               icon:'theme/imgs/ico_excel.png',
               scale:'medium',
               handler:function(){
                   Ext.Msg.confirm('系统确认','您确定要导出当前报表(统计)吗？',function(opt){
                       if(opt=='yes') {
                           //这里添加下载地址
                           var date0 = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                           var date1 = Ext.getCmp('date1').getValue().format('yyyy-MM-dd');
                           var plate = Ext.getCmp('plate').getValue();
                      	   var fuel = Ext.getCmp('fuel').getValue();
                           window.open('api.action?cmd=exportCycleDaySummary&summary=summary&date0='+date0+'&date1='+date1+'&plate='+plate+'&fuel='+fuel);
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
               layout:'border',
               items:[{
                       region:'center',
                       layout:'fit',
                       border:false,
                       items:grid_month
                   },{
                       id:'detail',
                       region:'south',
                       border:false,
                       height:0,
                       items:grid_date
                   }]
           }],
           renderTo: Ext.getBody()
       });
   }
});

