/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.FuelDateRepPanel',{
    extend:'Ext.panel.Panel',
    requires:['Ext.ux.TreeCombo'],
    config:{
        
    },
    constructor:function(){
        var panel = this;
        var list = [];
        
        var tab = top.Ext.getCmp('statstabs').getActiveTab();
        
       var store = Ext.create('Ext.data.Store',{
           fields: ['order','plate','date','mileage', 'fuel','overspeedtime','speeduptimes','breaktimes','avgfuel'],
           data:[]
       });
       
       Ext.define('options',{
           extend:'Ext.data.Model',
           fields:['id','text']
       });
       
       var st = Ext.create('Ext.data.Store',{
            model:'options',
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
                fields: ['mileage','fuel'],
                minimum: 0,
                grid:true
            },{
                type:'Category',
                position:'bottom',
                fields:['date']
                //label:{rotate:{degrees:60}}
            }],
            series:[{
                title:['行驶里程（公里）','油耗（升）'],
                type:'column',
                highlight: true,
                xField:'date',
                yField:['mileage','fuel'],
                label:{
                    display : 'insideEnd',
                    'text-anchor' : 'middle',
                    field :['mileage','fuel'],
                    renderer : Ext.util.Format.numberRenderer('0')
                },
                tips: {
                    width: 220,
                    renderer:function(storeItem,item) {
                        this.setTitle(storeItem.get('date') + '<br/>总里程：' + storeItem.get('mileage') + ' 公里' + '<br/>总油耗：' + storeItem.get('fuel') + ' 升');
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
                   store = Ext.ajaxModelLoader('MyApp.Model.DateFuel', {
                       params:{
                           date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                           date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                           plate:Ext.getCmp('plate').getValue()
                       }
                   });
                   mainchart.bindStore(store);
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
                        store = Ext.ajaxModelLoader('MyApp.Model.DateFuel', {
                            params:{
                                date0:Ext.getCmp('date0').getValue().format('yyyy-MM-dd'),
                                date1:Ext.getCmp('date1').getValue().format('yyyy-MM-dd'),
                                vehicleId:list[index].id
                            }
                        });
                        mainchart.bindStore(store);
                        grid.bindStore(store);
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
                            var date0 = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                            var date1 = Ext.getCmp('date1').getValue().format('yyyy-MM-dd');
                            var plate = Ext.getCmp('plate').getValue();
                           var p = Ext.getCmp('vehicleId').getValue();
                             
                           var index= getIndex(p);
                           var  vehicleId=list[index].id;
                            window.open('api.action?cmd=dateFuel_excel&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleId='+vehicleId);
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
                            var date0 = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                            var date1 = Ext.getCmp('date1').getValue().format('yyyy-MM-dd');
                            var plate = Ext.getCmp('plate').getValue();
                           var p = Ext.getCmp('vehicleId').getValue();
                             
                           var index= getIndex(p);
                           var  vehicleId=list[index].id;
                            window.open('api.action?cmd=dateFuel_excel&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleId='+vehicleId);
                        }
                    });
                }
            }]
        }
        
        var grid =  Ext.create('Ext.grid.Panel',{
           frame:true,
           border:true,
           autoScroll:false,
           scroll:'vertical',
           columns:[
               {text:'次序', width:'10px', xtype:'rownumberer'},
               {text:'车牌号',width:'15%',dataIndex:'plate'},
               {text:'日期',width:'10%',dataIndex:'date'},
               {text:'行驶里程',width:'15%',dataIndex:'mileage', renderer:function(v){
                    return v + ' 公里';   
               }},
               {text:'油耗',width:'10%',dataIndex:'fuel', renderer:function(v){
                    return v + ' 升';   
               }},
               {text:'超速时长',width:'10%',dataIndex:'overspeedtime', renderer:function(v){
                    return Math.round(v/60) + ' 分钟';
               }},
               {text:'急加速次数',width:'10%',dataIndex:'speeduptimes'},
               {text:'急减速次数',width:'10%',dataIndex:'breaktimes'},
               {text:'百公里油耗',width:'10%',dataIndex:'avgfuel'}
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
    }
});




