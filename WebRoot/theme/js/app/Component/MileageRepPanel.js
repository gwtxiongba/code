/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MileageRepPanel',{
    extend:'Ext.panel.Panel',
    requires:['Ext.ux.TreeCombo'],
    config:{
        
    },
    constructor:function(){
        var panel = this;
        var tab = top.Ext.getCmp('statstabs').getActiveTab();
        
        Ext.define('stats', {
            extend: 'Ext.data.Model',
            fields: ['vehicleId', 'plate', 'mileage']
        });
       
       var myGeo = new BMap.Geocoder();
       
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
                title:['总里程'],
                type:'column',
                highlight: true,
                xField:'plate',
                yField:['mileage'],
                label:{
                    display : 'insideEnd',
                    'text-anchor' : 'middle',
                    field :['mileage'],
                    renderer : function(v) {
                        return v + ' 公里';   
                    }
                },
                tips: {
                    width: 220,
                    renderer:function(storeItem,item) {
                        this.setTitle(storeItem.get('plate') + '<br/>总里程：' + storeItem.get('mileage')+ ' 公里');
                    }
                },
                listeners:{
                    itemmouseup:function(item) {
                        grid.getSelectionModel().select(item.storeItem.data.order-1);
                    }
                }
            }],
            listeners:{
              dblclick:function(e,opts){
                  var frameHeight = tab.getEl().down('iframe').dom.scrollHeight-55;
                  var rate = parseInt(Ext.getCmp('topChart').getHeight()*100/frameHeight);
                  if(rate >30) Ext.getCmp('topChart').setHeight(parseInt(frameHeight*0.3));
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
            },{
                xtype:'tbspacer',
                width:20
            },{
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
                   
                   store = Ext.ajaxModelLoader('MyApp.Model.FormObdList', {
                       params:{
                           date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                           date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                           vehicleIds: ids.join(','),
                           plate:Ext.getCmp('plate').getValue()
                       }
                   });
                   
                   store.on('load',function(){
                       for(var i = 0;i < store.getCount();i++){
                            var pt1 = store.getAt(i).get('pt1').split(',');
                            var bpt1 = new BMap.Point(pt1[0],pt1[1]);
                            //通过地图坐标点获取百度地图位置详细信息
                            getAddress(bpt1,i,store,'pt1');
                        }
                   });
                   
                   mainchart.bindStore(store);
                   grid.bindStore(store);
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
                           var ids = Ext.getCmp('tc').getValue();
                            //var fuel = Ext.getCmp('fuel').getValue();
                           var vehicleIds = ids.join(',');
                            window.open('api.action?cmd=formObdList_excel&date0='+date0+'&date1='+date1+'&vehicleIds='+vehicleIds);
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
                           var ids = Ext.getCmp('tc').getValue();
                            //var fuel = Ext.getCmp('fuel').getValue();
                           var vehicleIds = ids.join(',');
                            window.open('api.action?cmd=formObdList_excel&date0='+date0+'&date1='+date1+'&vehicleIds='+vehicleIds);
                        }
                    });
                }
            }]
        }
        
        var grid =  Ext.create('Ext.grid.Panel',{
           frame:true,
           border:true,
           height:150,
           autoScroll:false,
           scroll:'vertical',
           columns:[
               {'text':'次序', xtype:'rownumberer', width:'10px'},
               {'text':'车牌号',dataIndex:'plate'},
               {text:'所属分组', width:'20%', dataIndex:'team',sortable:false, renderer:function(v){
                   return !v ? '未分组车辆' : v;
               }},
               {'text':'总里程',dataIndex:'mileage', renderer:function(v){
                   return v + ' 公里';       
               }},
               //{'text':'总油耗',dataIndex:'fuel'},
               {'text':'最后状态', renderer:function(){
                   return '';    
               }},
               {'text':'最后位置', width:'30%',dataIndex:'pt1',renderer:function(v){
                   return v;   
               }}
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
                height:'50%',
                border:false,
                items:mainchart
            },{
                region:'center',
                layout:'fit',
                border:false,
                items: grid
            }],
            renderTo:Ext.getBody()
        });
        
        function getAddress(pt,index,store,dataIndex) {
               myGeo.getLocation(pt, function(rs){
                  var addComp = rs.addressComponents;
                  var address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
                  store.getAt(index).set(dataIndex, address);
                 /* if(index == store.getCount()-1){
                      store.sync();
                  }*/
               });
        }        
    }
});




