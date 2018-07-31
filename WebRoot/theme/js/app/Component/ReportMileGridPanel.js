/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.ReportMileGridPanel',{
    extend:'Ext.panel.Panel',
    requires:['Ext.ux.TreeCombo'],
    constructor:function(type){
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
        
        var mainchart = Ext.create('Ext.chart.Chart',{
            style: 'background:#fff',
            theme: 'Browser:gradients',
            shadow: true, 
            animate:true,
            legend: {
                position: 'right'
            },
            axes:[{
                type: 'Numeric',
                position:'left',
                fields: ['mileage'],
                minimum: 0,
                grid:true
            },{
                type:'Category',
                position:'bottom',
                fields:['plate']
                //label:{rotate:{degrees:60}}
            }],
            series:[{
                title:['总里程(公里)'],
                type:'column',
                highlight: true,
                xField:'plate',
                yField:['mileage'],
                label:{
                    display : 'insideEnd',
                    'text-anchor' : 'middle',
                    field :['times'],
                    renderer : Ext.util.Format.numberRenderer('0')
                },
                tips: {
                    width: 220,
                    renderer:function(storeItem,item) {
                        this.setTitle(storeItem.get('plate') + '<br/>总里程：' + storeItem.get('mileage') + ' 公里') ;
                    }
                },
                listeners:{
                    itemmouseup:function(item) {
                        //item.storeItem.data.vehicleId
                    }
                }
            }],
            listeners:{
              dblclick:function(e,opts){
                  var frameHeight = tab.getEl().down('iframe').dom.scrollHeight-55;
                  var rate = parseInt(Ext.getCmp('topChart').getHeight()*100/frameHeight);
                  if(rate >50) Ext.getCmp('topChart').setHeight(parseInt(frameHeight*0.5));
                  else Ext.getCmp('topChart').setHeight(frameHeight);
              }  
            },
            store:store
        });
        
        
        
        var dt = new Date();
        dt.setDate(dt.getDate()-1);
        
        var tbar1 = {
            xtype:'toolbar',
            dock:'top',
            border:false,
            items:[{
                xtype:'tbspacer',
                width:8
            },{
                xtype:'datefield',
                id:'date0',
                width:120,
                fieldLabel:'从',
                labelWidth: 20,
                labelAlign: 'right',
                format:'Y-m-d',
                value: dt.format('yyyy-MM-dd'),
                maxValue:dt.format('yyyy-MM-dd'),
                name:'date0'
            },{
                xtype:'datefield',
                id:'date1',
                width:120,
                fieldLabel:'到',
                labelWidth: 20,
                labelAlign: 'right',
                format:'Y-m-d',
                value: dt.format('yyyy-MM-dd'),
                maxValue:dt.format('yyyy-MM-dd'),
                name:'date1'
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
                    d.setDate(d.getDate()-1);
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
                    d.setDate(d.getDate()-1);
                    Ext.getCmp('date1').setValue(d);
                    d.setDate(d.getDate()-6);
                    Ext.getCmp('date0').setValue(d);
                }
            }]
        }
        
        var tbar2 = {
            xtype:'toolbar',
            border:false,
            items:[{
                xtype:'tbspacer',
                width:8
            },Ext.create('MyApp.Component.CarTreeCombo'),{
                xtype:'textfield',
                id:'plate',
                fieldLabel:'车牌',
                labelAlign: 'right',
                labelWidth:40,
                width:120
            },
            /*{
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
                xtype:'tbspacer',
                width:20
            },*/{
                text:'查询',
                icon:'theme/imgs/ico_sea.png',
                //scale:'medium',
                handler:function(){
                   var ids = Ext.getCmp('tc').getValues();
                   var plate = Ext.getCmp('plate').getValue();
                   if(ids.length == 0 && plate == '') {
                       Ext.Msg.alert('系统提示','请选择车辆或车牌');
                       return;
                   }
                   
                   store = Ext.ajaxModelLoader('MyApp.Model.StatsTotalMF', {
                       params:{
                           date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                           date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                           vehicleIds: ids.join(','),
                           plate:Ext.getCmp('plate').getValue()
                          // fuel:Ext.getCmp('fuel').getValue()
                       }
                   });
                   mainchart.bindStore(store);
                   grid0.bindStore(store)
                }
            },{
                xtype:'tbfill'
            },{
                text:'导出',
                icon:'theme/imgs/ico_excel.png',
                //scale:'medium',
                handler:function(){
                    Ext.Msg.confirm('系统确认','您确定要导出当前报表吗？',function(opt){
                        if(opt=='yes') {
                            //这里添加下载地址
                            var date0 = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                            var date1 = Ext.getCmp('date1').getValue().format('yyyy-MM-dd');
                            var plate = Ext.getCmp('plate').getValue();
                          //  var fuel = Ext.getCmp('fuel').getValue();
                            var ids = Ext.getCmp('tc').getValues();
                            var vehicleIds=ids.join(',');
                            window.open('api.action?cmd=statsTotalMF_excel&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleIds='+vehicleIds);
                        }
                    });
                }
            },{
                text:'导出(统计)',
                icon:'theme/imgs/ico_excel.png',
                //scale:'medium',
                handler:function(){
                    Ext.Msg.confirm('系统确认','您确定要导出当前报表(统计)吗？',function(opt){
                        if(opt=='yes') {
                            //这里添加下载地址
                            var date0 = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                            var date1 = Ext.getCmp('date1').getValue().format('yyyy-MM-dd');
                            var plate = Ext.getCmp('plate').getValue();
                            //var fuel = Ext.getCmp('fuel').getValue();
                            window.open('api.action?cmd=statsDaysMF_excel&summary=summary&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleId='+vehicle_id);
                        }
                    });
                }
            }]
        }
        var vehicle_id = 0;
        //月统计列表
       var grid0 = Ext.create('Ext.grid.Panel',{
           frame: true,
           border:true,
           autoScroll: false,
           scroll:'vertical',
           columns:[
               {xtype:'rownumberer'},
               {text:'车牌号', width:'15%',dataIndex:'plate',sortable:false,menuDisabled:true},
               {text:'所属分组',width:'15%',dataIndex:'team',sortable:false, renderer:function(v){
                   return !v ? '未分组车辆' : v;
               }},
               {text:'总里程',width:'10%',dataIndex:'mileage', renderer:function(v){
                   return v + ' 公里';    
               }}
              /* {text:'总油耗',width:'10%',dataIndex:'fuel', renderer:function(v){
                   return v + ' 升';   
               }},
               {text:'百公里油耗',width:'10%',dataIndex:'avgfuel',renderer:function(v){
                      return v+' 升'; 
               }},
               {text:'超速时长',width:'10%',dataIndex:'overspeedtime',renderer:function(v){
                   return Math.round(v/60) + ' 分钟';
               }},
               {text:'急加速次数',width:'10%',dataIndex:'speeduptimes'},
               {text:'急减速次数',width:'10%',dataIndex:'breaktimes'}*/
           ],
           listeners:{
               itemdblclick:function(grid,record,item,index,e){
                   //this, record, item, index, e, eOpts 
                   var vehicleId = record.get('vehicleId');
                   vehicle_id = record.get('vehicleId');
                   var dateStore = Ext.ajaxModelLoader('MyApp.Model.StatsDaysMF',{
                                     params:{
                                         vehicleId:vehicleId,
                                         date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                                         date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                                         plate:Ext.getCmp('plate').getValue()
                                         //fuel:Ext.getCmp('fuel').getValue()
                                     }
                                 });
                   grid1.bindStore(dateStore);
               }
           }
       });
       
       //日统计表
       var grid1 = Ext.create('Ext.grid.Panel',{
           frame:true,
           border:true,
           autoScroll:false,
           scroll:'vertical',
           columns:[
               {'text': '日期', width:'15%',dataIndex:'date'},
               {'text':'车牌号', width:'15%',dataIndex:'plate'},
               {text:'所属分组',width:'15%',dataIndex:'team',sortable:false, renderer:function(v){
                   return !v ? '未分组车辆' : v;
               }},
               {'text':'行驶里程', width:'10%',dataIndex:'mileage',renderer:function(val){
                   return val + ' 公里';    
               }}
               /*{'text':'油耗',  width:'10%',dataIndex:'fuel',renderer:function(val){
                   return val + ' 升';   
               }},
               {'text':'超速时长', width:'10%',dataIndex:'overspeedtime',renderer:function(v){
                   return Math.round(v/60) + ' 分钟';
               }},
               {'text':'急加速次数', width:'10%',dataIndex:'speeduptimes'},
               {'text':'急减速次数', width:'10%',dataIndex:'breaktimes'}*/
           ]
       });
                
        this.superclass.constructor.call(this,{
            width: tab.getEl().down('iframe').dom.scrollWidth,
            height:tab.getEl().down('iframe').dom.scrollHeight,
            border:false,
            layout:'border',
            dockedItems :[tbar1,tbar2],
            items:[{
                id:'topChart',
                region:'north',
                layout:'border',
                height:'40%',
                border:false,
                items:mainchart
            },{
                region:'center',
                layout:'border',
                height:'60%',
                border:false,
                items:[{
                   region:'north',
                   layout:'fit',
                   height:'60%',
                   border:true,
                   items:grid0
                },{
                   region:'center',
                   layout:'fit',
                   height:'40%',
                   border:true,
                   items:grid1
                }]
            }],
            renderTo:Ext.getBody()
        });
    }
});




