/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.OnlinesDRepPanel',{
    extend:'Ext.panel.Panel',
    requires:['Ext.ux.TreeCombo'],
    constructor:function(){
        var panel = this;
        var tab = top.Ext.getCmp('statstabs').getActiveTab();
        
        var minYear = 2014;
        var maxYear = new Date().getFullYear();
        var maxDays = 0;
        
       var store = Ext.create('Ext.data.Store',{
           fields: ['name','d1','d2','d3','d4','d5','d6','d7','d8','d9','d10','d11','d12','d13','d14','d15','d16','d17','d18','d19','d20','d21','d22','d23','d24','d25','d26','d27','d28','d29','d30','d31','avg'],
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
                fields: ['agv'],
                minimum: 0,
                maximum: 100,
                grid:true
            },{
                type:'Category',
                position:'bottom',
                fields:['name']
                //label:{rotate:{degrees:60}}
            }],
            series:[{
                title:['上线率'],
                type:'column',
                highlight: true,
                xField:'plate',
                yField:['avg'],
                label:{
                    display : 'insideEnd',
                    'text-anchor' : 'middle',
                    field :['avg'],
                    renderer : function(v) {
                        return v + ' %';
                    }
                },
                tips: {
                    width: 220,
                    renderer:function(storeItem,item) {
                        this.setTitle(storeItem.get('name') + '<br/>上线率：' + storeItem.get('avg') + ' %');
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
                xtype:'combobox',
                id:'year',
                width:120,
                editable:false,
                queryMode:false,
                fieldLabel:'年份',
                labelWidth: 40,
                labelAlign: 'right',
                listeners:{
                    render:function(){
                        var data = [];
                        for(var i = minYear; i <= maxYear; i++) {
                            data.push({id:i-minYear+1,text:i});
                        }
                        var yearSt = Ext.create('Ext.data.Store',{
                            fields:['id','text'],
                            data:data
                        });
                        this.bindStore(yearSt);
                        this.setValue(data[data.length-1].text);
                    }
                }
            },{
                xtype:'combobox',
                id:'month',
                width:100,
                editable:false,
                queryMode:false,
                fieldLabel:'月份',
                labelWidth: 40,
                labelAlign: 'right',
                listeners:{
                    render:function(){
                        var data = [];
                        for(var i=1; i <=12; i++){
                            data.push({id:i,text:i});
                        }
                        var monthSt = Ext.create('Ext.data.Store',{
                            fields:['id','text'],
                            data:data
                        });
                        this.bindStore(monthSt);
                        this.setValue(data[new Date().getMonth()].text);
                    }
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
                   
                   var year = Ext.getCmp('year').getValue();
                   var month = Ext.getCmp('month').getValue();
                   maxDays = getDaysOfMonth(year,month);
                   store = Ext.ajaxModelLoader('MyApp.Model.DateOnlines', {
                       params:{
                           date:year + '-' + month + '-1',
                           vehicleIds: ids.join(','),
                           plate:Ext.getCmp('plate').getValue()
                       }
                   });
                   mainchart.bindStore(store);
                   grid.bindStore(store);
                }
            },{
                xtype:'tbfill'
            },{
               // text:'导出',
               // icon:'theme/imgs/ico_excel.png',
                //scale:'medium',
                handler:function(){
                    Ext.Msg.confirm('系统确认','您确定要导出当前报表吗？',function(opt){
                        if(opt=='yes') {
                            //这里添加下载地址
                            var date0 = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
                            var date1 = Ext.getCmp('date1').getValue().format('yyyy-MM-dd');
                            var plate = Ext.getCmp('plate').getValue();
                            var fuel = Ext.getCmp('fuel').getValue();
                             var ids = Ext.getCmp('tc').getValue();
                             var vehicleIds = ids.join(',');
                            window.open('api.action?cmd=monthOnlines_excel&date0='+date0+'&date1='+date1+'&plate='+plate+'&vehicleIds='+vehicleIds);
                        }
                    });
                }
            },{
               // text:'导出(统计)',
               // icon:'theme/imgs/ico_excel.png',
                //scale:'medium',
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
            }]
        }
        
       var columns = [{'text':'设备类型/日期',width:'10%',dataIndex:'name'}];
       for(var i=1; i <= 31; i++) {
           columns.push({'text':i,width:'4%', dataIndex:'d'+i, renderer:function(v){ return v ? v + ' %' : ''}});
       }
       columns.push({'text':'平均上线率',dataIndex:'avg', renderer:function(v){ return v + ' %'}})
                
        var grid =  Ext.create('Ext.grid.Panel',{
           frame:true,
           border:true,
           height:150,
           autoScroll:true,
           //scroll:'vertical',
           columns: columns
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
        
        function getDaysOfMonth(year,month) {
            var d = new Date(year,month,0);
            return d.getDate();
        }
    }
});




