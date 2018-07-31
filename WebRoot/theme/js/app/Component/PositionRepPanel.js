/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.PositionRepPanel',{
    extend:'Ext.panel.Panel',
    requires:['Ext.ux.TreeCombo'],
    config:{
        
    },
    constructor:function(){
        var panel = this;
        var list = [];
var store = null;
        var tab = top.Ext.getCmp('statstabs').getActiveTab();
        var myGeo = new BMap.Geocoder();
        Ext.define('stats', {
            extend: 'Ext.data.Model',
            fields: ['vehicleId', 'plate', 'mileage']
        });
        
      // var store = Ext.create('Ext.data.Store',{
      //     model:'stats',
      //     data:[]
      // });
       
       Ext.define('options',{
           extend:'Ext.data.Model',
           fields:['id','text']
       });
       
       var st = Ext.create('Ext.data.Store',{
            model:'options',
            data:[]
        });
      
        var dt = new Date();
        dt.setDate(dt.getDate()-1);
        
        var tbar = {
            xtype:'toolbar',
            border:false,
            items:[{
                xtype:'tbspacer',
                width:8
            },{
                xtype:'datefield',
                id:'date0',
                width:130,
                fieldLabel:'日期',
                labelWidth: 30,
                labelAlign: 'right',
                format:'Y-m-d',
                value: dt.format('yyyy-MM-dd'),
                maxValue:dt.format('yyyy-MM-dd'),
                name:'date0'
            },{
                xtype:'tbspacer',
                width:8
            },Ext.create('MyApp.Component.CarTreeCombo'),{
                xtype:'textfield',
                id:'plate',
                fieldLabel:'车牌',
                labelAlign: 'right',
                labelWidth:40,
                width:140
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
                   loadVehicleComboCtrl();
                   
                   if(ids.length == 0 && plate == '') {
                       Ext.Msg.alert('系统提示','请选择车辆或车牌');
                       return;
                   }
                   
                   if(ids.length > 0) return; 
                   //没有选择车辆则提交车牌信息
                   store = Ext.ajaxModelLoader('MyApp.Model.FormdLocation', {
                       params:{
                           date:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                           plate:Ext.getCmp('plate').getValue()
                       }
                   });
                   //alert(store.getCount());
                   grid.bindStore(store);
                }
            },{
                xtype:'combobox',
                id:'vehicleId',
                fieldLabel:'已选车辆',
                hidden:true,
                editable:false,
                queryMode:'local',
                labelAlign: 'right',
                labelWidth:60,
                width:160,
                listeners:{
                    change:function(o,nv,ov){
                        if(list.length == 0) return;
                        var index= getIndex(nv);
                        store = Ext.ajaxModelLoader('MyApp.Model.FormdLocation', {
                            params:{
                                date:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                                vehicleId:list[index].id
                            }
                        });
                       
                        grid.bindStore(store);
                        
                        store.on('load',function(){
                                    for(var i = 0;i < store.getCount();i++){
                                        var bd = store.getAt(i).get('gps');
                                       var bdpt = new BMap.Point(bd[0],bd[1]);
                                        //通过地图坐标点获取百度地图位置详细信息
                                        geocodeSearch(bdpt, i);
                                    }
                                });
                    }
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
                            var date = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                           var p = Ext.getCmp('vehicleId').getValue();
                            var plate = Ext.getCmp('plate').getValue();
                              var index= getIndex(p);
                             var  vehicleId= list[index].id;
                            window.open('api.action?cmd=formdLocation_excel&date='+date+'&plate='+plate+'&vehicleId='+vehicleId);
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
                            var date = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                           var p = Ext.getCmp('vehicleId').getValue();
                            var plate = Ext.getCmp('plate').getValue();
                              var index= getIndex(p);
                             var  vehicleId= list[index].id;
                            window.open('api.action?cmd=formdLocation_excel&date='+date+'&plate='+plate+'&vehicleId='+vehicleId);
                        }
                    });
                }
            }]
        }
        
        var grid =  Ext.create('Ext.grid.Panel',{
           frame:true,
           border:true,
           autoScroll:false,
           store: store,
           scroll:'vertical',
           columns:[
               {text:'次序', xtype:'rownumberer', width:'10px'},
               {text:'车牌号',dataIndex:'plate'},
               {text:'所属分组',dataIndex:'team',sortable:false, renderer:function(val){
                   return !val ? '未分组车辆' : val;
               }},
               {text:'时间',dataIndex:'gps', renderer:function(v) {
                    return v[2];   
               }},
               {text:'经度',dataIndex:'gps', renderer:function(v){
                    return v[0];   
               }},
               {text:'纬度',dataIndex:'gps', renderer:function(v){
                    return v[1];   
               }},
               {text:'速度',dataIndex:'gps', renderer:function(v){
                    return v[3] + ' 公里/小时';
               }},
               {text:'方向',dataIndex:'gps', renderer:function(v){
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
               {text:'状态',dataIndex:'gps', renderer:function(v){
                    return v[3] > 0 ? '行驶中' : '已停止';   
               }},
               {text:'位置',dataIndex:'addr', width:'36%'}
           ]
        });
        
        this.superclass.constructor.call(this,{
            width: tab.getEl().down('iframe').dom.scrollWidth,
            height:tab.getEl().down('iframe').dom.scrollHeight,
            border:false,
            layout:'fit',
            tbar :tbar,
            items:grid,
            store:store,
            renderTo:Ext.getBody()
        });
        
        function loadVehicleComboCtrl() {
            var combobox = Ext.getCmp('vehicleId');
            list = Ext.getCmp('tc').getLists();
            st.removeAll();
            combobox.clearValue();
            for(var i = 0; i < list.length; i++) {
                st.add(list[i]);
            }
            combobox.bindStore(st);

            if(list.length == 0) {//隐藏
                combobox.setVisible(false);
            } else {
            
                combobox.setVisible(true);
                combobox.setValue(list[0].text);
            }
        }
        
        function getIndex(value) {
            for(var i=0; i < list.length; i++) {
                if(list[i].text == value) return i;
            }   
            return 0;
        }
        
        function geocodeSearch(pt, index){
        	/*
            if(index < adds.length){
                setTimeout(window.bdGEO1,300);
            }*/
            //alert(store.getAt(index).get('x'));
            myGeo.getLocation(pt, function(rs){
                var addComp = rs.addressComponents;
                var address = addComp.province + 
                 			  addComp.city + 
                 			  addComp.district + 
                 			  addComp.street + 
                 			  addComp.streetNumber;
             // alert(grid.getStore().getCount());
                grid.getStore().getAt(index).set('addr', address);
                
                if(index == grid.getStore().getCount()-1){
                	grid.getStore().sync();
                }
            });
             
        };
    }
});




