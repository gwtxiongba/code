/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AlarmsRepPanel',{
    extend:'Ext.panel.Panel',
    requires:['Ext.ux.TreeCombo'],
    constructor:function(type){
        var panel = this;
        var tab = top.Ext.getCmp('statstabs').getActiveTab();
        
        Ext.define('stats', {
            extend: 'Ext.data.Model',
            fields: ['vehicleId', 'plate', 'times']
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
        
        var isStoreLoaded = false;
        
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
                fields: ['times'],
                minimum: 0,
                grid:true
            },{
                type:'Category',
                position:'bottom',
                fields:['plate']
                //label:{rotate:{degrees:60}}
            }],
            series:[{
                title:['报警次数'],
                type:'column',
                highlight: true,
                xField:'plate',
                yField:['times'],
                label:{
                    display : 'insideEnd',
                    'text-anchor' : 'middle',
                    field :['times'],
                    renderer : Ext.util.Format.numberRenderer('0 次')
                },
                tips: {
                    width: 220,
                    renderer:function(storeItem,item) {
                        this.setTitle(storeItem.get('plate') + '<br/>报警次数：' + storeItem.get('times') + ' 次');
                    }
                },
                listeners:{
                    itemmouseup:function(item) {
                    vehicle_id = item.storeItem.data.vehicleId;
                    
                        var st = Ext.ajaxModelLoader('MyApp.Model.DetailAlarms', {
                            params:{
                                date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                                date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                                vehicleId: item.storeItem.data.vehicleId,
                                type:type
                            }
                        });
                       
                        if(type=='overspeed') {
                            st.on('load',function(){
                                for(var i = 0;i < st.getCount();i++){
                                    var pt = st.getAt(i).get('pt').split(',');
                                    var bpt = new BMap.Point(pt[0],pt[1]);
                                    getAddress(bpt,i,st,'pt');
                                }
                            });
                        } else {
                            st.on('load',function(){
                                for(var i = 0;i < st.getCount();i++){
                                    var pt = st.getAt(i).get('address').split(',');
                                    var bpt = new BMap.Point(pt[0],pt[1]);
                                    getAddress(bpt,i,st,'address');
                                }
                            });
                        }
                        grid.bindStore(st);
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
                   
                   store = Ext.ajaxModelLoader('MyApp.Model.TotalAlarms', {
                       params:{
                           date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                           date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                           vehicleIds: ids.join(','),
                           plate:Ext.getCmp('plate').getValue(),
                           type:type
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
                           var ids = Ext.getCmp('tc').getValues();
                           var  vehicleIds = ids.join(',');
                            window.open('api.action?cmd=totalAlarms_excel&type='+type+'&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleIds='+vehicleIds);
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
                            window.open('api.action?cmd=detailAlarms_excel&type='+type+'&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleId='+vehicle_id);
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
               {text:'次序', xtype:'rownumberer', width:'5%'},
               {text:'车牌号', width:'15%',dataIndex:'plate'},
               {text:'所属分组', width:'20%', dataIndex:'team',sortable:false, renderer:function(v){
                   return !v ? '未分组车辆' : v;
               }},
               {text:'经度', dataIndex:'pt',sortable:false, renderer:function(v){
                   return v.split(',')[0];    
               }},
               {text:'纬度', dataIndex:'pt',sortable:false, renderer:function(v){
                   return v.split(',')[1];        
               }},
               {text:'报警时间', width:'15%', dataIndex:'time',sortable:false},
               {text:'位置', dataIndex:'address', width:'20%'}
           ]
        });
        
        if(type=='overspeed') {
            grid = Ext.create('Ext.grid.Panel',{
                frame:true,
                border:true,
                height:150,
                autoScroll:false,
                scroll:'vertical',
                columns:[
                    {text:'次序', xtype:'rownumberer'},
                    {text:'车牌号', width:'20%',dataIndex:'plate'},
                    {text:'所属分组', width:'20%', dataIndex:'team',sortable:false, renderer:function(v){
                         return !v ? '未分组车辆' : v;
                     }},
                    {text:'时间', dataIndex:'gps',renderer:function(v){
                         return v[2];   
                    }},
                    {text:'速度', dataIndex:'gps',renderer:function(v){
                         return v[3] + ' 公里/小时';   
                    }},
                    {text:'方向', dataIndex:'gps',renderer:function(v){
                         v=v[4]/90.0;
                        if(0<v&&v<1) return '北偏东';
                        else if(v==0) return '北方';
                        else if(v==1) return '东方';
                        else if(1<v&&v<2) return '南偏东';
                        else if(v==2) return '南方';
                        else if(2<v&&v<3) return '南偏西';
                        else if(v==3) return '西方';
                        else if(3<v&&v<4) return '北偏西'; 
                    }},
                    {text:'位置', dataIndex:'pt'}
                    
                ]
            });
        }
        
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
                  /*
                  if(index == store.getCount()-1){
                      store.sync();
                  }*/
               });
        }                
    }
});




