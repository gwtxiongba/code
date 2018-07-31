/* 
 * To change this template, choose Tools | Templates
 * MyApp.Page.ParkRepPanel
 */
Ext.define('MyApp.Component.ParkRepPanel',{
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
        
        var resizable = false;
        var vehicle_id = 0;
        //停车时间总图
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
                fields: ['stopseconds'],
                minimum: 0,
                grid:true
            },{
                type:'Category',
                position:'bottom',
                fields:['plate']
                //label:{rotate:{degrees:60}}
            }],
            series:[{
                title:'总时长',
                type:'column',
                highlight: true,
                xField:'plate',
                yField:'stopseconds',
                label:{
                    display : 'insideEnd',
                    'text-anchor' : 'middle',
                    field :'stopseconds',
                    renderer: function(v) {
                        return Math.round(v*100/60)/100 + ' 分钟'
                    }
                    //renderer : Ext.util.Format.numberRenderer('0')
                },
                tips: {
                    width: 220,
                    renderer:function(storeItem,item) {
                        var minites = Math.round(storeItem.get('stopseconds')*100/60)/100;
                        this.setTitle(storeItem.get('plate') + '<br/>总时长：' + minites + ' 分钟');
                    }
                },
                listeners:{
                    itemmouseup:function(item) {
                        resizable = true;
                        vehicle_id=item.storeItem.data.vehicleId;
                        var height = parseInt((tab.getEl().down('iframe').dom.scrollHeight-55)*0.3);
                        Ext.getCmp('topChart').setHeight(height);
                        var attachStore = Ext.ajaxModelLoader('MyApp.Model.FormStopList', {
                            params:{
                                date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                                date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                                vehicleId:item.storeItem.data.vehicleId
                            }
                        });
                        
                        attachStore.on('load',function(){
                            for(var i = 0;i < attachStore.getCount();i++){
                                var pt = attachStore.getAt(i).get('pt').split(',');
                                var bpt = new BMap.Point(pt[0],pt[1]);
                                //通过地图坐标点获取百度地图位置详细信息
                                getAddress(bpt,i,attachStore,'pt');
                            }
                        });
                        
                        attchchart.bindStore(attachStore);
                        grid.bindStore(attachStore);
                        //alert(item.storeItem.data.vehicleId + ':' + item.storeItem.data.plate);
                    }
                }
            }],
            listeners:{
              dblclick:function(e,opts){
                  var frameHeight = tab.getEl().down('iframe').dom.scrollHeight-55;
                  var rate = parseInt(Ext.getCmp('topChart').getHeight()*100/frameHeight);
                  if(!resizable) return;
                  if(rate >30) Ext.getCmp('topChart').setHeight(parseInt(frameHeight*0.3));
                  else Ext.getCmp('topChart').setHeight(frameHeight);
              }  
            },
            store:store
        });
        
        //分段停车时长柱状图
        var attchchart = Ext.create('Ext.chart.Chart',{
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
                fields: ['timespan'],
                minimum: 0,
                grid:true
            },{
                type:'Numeric',
                position:'bottom',
                fields:['order'],
                minimum: 1
                //label:{rotate:{degrees:60}}
            }],
            series:[{
                title:'时长',
                type:'column',
                highlight: true,
                xField:'order',
                yField:['timespan'],
                label:{
                    display : 'insideEnd',
                    'text-anchor' : 'middle',
                    field :['timespan'],
                    //renderer : Ext.util.Format.numberRenderer('0')
                    renderer: function(v) {
                        return Math.round(v*100/60)/100 + ' 分钟'
                    }
                },
                tips: {
                    width: 220,
                    renderer:function(storeItem,item) {
                        var minites = Math.round(storeItem.get('timespan')*100/60)/100;
                        this.setTitle('次序：' + storeItem.get('order') + '<br/>时长：' + minites + ' 分钟');
                    }
                },
                listeners:{
                    itemmouseup:function(item) {
                        grid.getSelectionModel().select(item.storeItem.data.order-1);
                    }
                }
            }],
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
                value: new Date().format('yyyy-MM-dd'),
                maxValue:new Date().format('yyyy-MM-dd'),
                name:'date0'
            },{
                xtype:'datefield',
                id:'date1',
                width:120,
                fieldLabel:'到',
                labelWidth: 20,
                labelAlign: 'right',
                format:'Y-m-d',
                value: new Date().format('yyyy-MM-dd'),
                maxValue:new Date().format('yyyy-MM-dd'),
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
                xtype:'textfield',
                id:'outtime',
                fieldLabel:'自定义超时',
                labelAlign: 'right',
                labelWidth:70,
                width:130,
                listeners:{
                    blur:function(o) {
                        var unit = Ext.getCmp('unit').getValue();
                        var v = o.value;
                       if(v=='' || isNaN(v)) return '';
                       if(unit == '分钟') {
                           Ext.getCmp('ot').setValue(v*60);
                       } else if(unit == '小时') {
                           Ext.getCmp('ot').setValue(v*3600);
                       } else {
                           Ext.getCmp('ot').setValue(v*3600*24);
                       }
                    }
                }
            },{
                id:'unit',
                xtype:'combobox',
                displayField:'value',
                width:60,
                editable:false,
                value:'分钟',
                store:Ext.create('Ext.data.SimpleStore',{
                   fields:['key','value'],
                   data:[['0','分钟'],['1','小时'],['2','天']]
                }),
                listeners:{
                   change : function(o,nv,ov){
                       var v = Ext.getCmp('outtime').getValue();
                       if(v=='' || isNaN(v)) return '';
                       if(nv == '分钟') {
                           Ext.getCmp('ot').setValue(v*60);
                       } else if(nv == '小时') {
                           Ext.getCmp('ot').setValue(v*3600);
                       } else {
                           Ext.getCmp('ot').setValue(v*3600*24);
                       }
                   }   
                }
            },{
                id:'ot',
                xtype:'hidden',
                name:'timeout'
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
                   store = Ext.ajaxModelLoader('MyApp.Model.FormStop', {
                       params:{
                           date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                           date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                           vehicleIds: ids.join(','),
                           plate:Ext.getCmp('plate').getValue(),
                           timeout:Ext.getCmp('ot').getValue()
                       }
                   });
                   mainchart.bindStore(store);
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
                           // var fuel = Ext.getCmp('fuel').getValue();
                             var ids = Ext.getCmp('tc').getValue();
                             var timeout=Ext.getCmp('ot').getValue()
                             var vehicleIds=ids.join(',');
                            window.open('api.action?cmd=formStop_excel&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleIds='+vehicleIds+'&timeout='+timeout);
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
                           // var fuel = Ext.getCmp('fuel').getValue();
                            window.open('api.action?cmd=formStopList_excel&summary=summary&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleId='+vehicle_id);
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
               {text:'所属分组',dataIndex:'team',sortable:false, renderer:function(val){
                   return !val ? '未分组车辆' : val;
               }},
               {'text':'起始时间',dataIndex:'time0'},
               {'text':'结束时间',dataIndex:'time1'},
               {'text':'经度',dataIndex:'pt1',renderer:function(v){
                    return v.split(',')[0]
               }},
               {'text':'纬度',dataIndex:'pt1',renderer:function(v){
                    return v.split(',')[1]   
               }},
               {'text':'时长',dataIndex:'timespan'},
               {'text':'位置',dataIndex:'pt'}
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
                height:'100%',
                border:false,
                items:mainchart
            },{
                region:'center',
                layout:'border',
                border:false,
                items:[{
                    id:'botChart',
                    region:'north',
                    height:'50%',
                    layout:'fit',
                    border:false,
                    items: attchchart
                },{
                    region:'center',
                    layout:'fit',
                    height:'50%',
                    items:grid
                }]
            }],
            renderTo:Ext.getBody()
        });

        function getAddress(pt,index,store,dataIndex) {
               myGeo.getLocation(pt, function(rs){
                  var addComp = rs.addressComponents;
                  var address = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
                  store.getAt(index).set(dataIndex, address);
                /*  if(index == store.getCount()-1){
                      store.sync();
                  }*/
               });
        }
    }
});

