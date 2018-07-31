/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.DriverTracePanel',{
   extend:'Ext.panel.Panel',
   config:{
      map : null  
   },
   constructor: function(){
       var panel = this;
       var player = null;
       var tab = top.Ext.getCmp('apptabs').getActiveTab();
       var driverId = MyApp.Base.Page.getQuery('driverId');
       var dataDiary = Ext.create('MyApp.Component.DiaryTabPanel');
       
       this.superclass.constructor.call(this,{
           width: tab.getEl().down('iframe').dom.scrollWidth,
           height:tab.getEl().down('iframe').dom.scrollHeight,
           border:false,
           layout:'border',
           tbar:[{
                xtype:'datefield',
                id:'date',
                fieldLabel: '历史日期',
                format:'Y-m-d',
                value: new Date().format('yyyy-MM-dd'),
                maxValue:new Date().format('yyyy-MM-dd'),
                name:'date',
                listeners:{
                    change:function(h,nd){
                        Ext.getCmp('stop').fireEvent('click');
                        player.load({
                            driverId:driverId,
                            date:nd.format('yyyy-MM-dd')
                        },function(){
                            //panel.map.clearOverlays();
                            panel.playReady.call(this);
                            panel.showHistoryData();
//                            panel.showDateEvent();
                        });
//                        Ext.getCmp('mdiarypanel').addLogs(vehicleId,nd.format('yyyy-MM-dd'));
//						Ext.getCmp('ydiarypanel').addLogs(vehicleId,nd.format('yyyy-MM-dd'));
                    }
                }
            },{
                xtype: 'radiogroup',
                id:'multi',
                fieldLabel:'播放速度',
                defaultType: 'radiofield',
                layout: 'hbox',
                items:[{
                    boxLabel:'5倍',
                    name: 'speed',
                    inputValue:'5',
                    checked:true
                },{
                    boxLabel:'20倍',
                    name: 'speed',
                    inputValue:'20'
                },{
                    boxLabel:'50倍',
                    name: 'speed',
                    inputValue:'50'
                }]
            },{
                id:'play',
                xtype:'button',
                text:'播放',
                disabled:true,
                listeners:{
                    click:function(){
                        var slider = Ext.getCmp('slider');
                        slider.data.isPlay = !slider.data.isPlay;
                        this.setText(slider.data.isPlay ? '暂停' : '播放');
                    }
                }
            },{
                id:'slider',
                xtype:'slider',
                width: 214,
                minValue: 0,
                maxValue: 0,
                useTips: true,
                disabled:true,
                data:{isPlay:false},
                tipText: function(thumb){
                    //这里把数组换成时间显示
                    return Ext.String.format('<b>上报时间：{0}</b>', player.getTickTime(thumb.value));
                }
            },{
                id:'stop',
                xtype:'button',
                disabled:true,
                text:'停止',
                listeners: {
                    click : function(){
                        var slider = Ext.getCmp('slider');
                        slider.setValue(0);
                        slider.data.isPlay = true;
                        Ext.getCmp('play').fireEvent('click');
                    }
                }
            }],
           items:[/*{
                id:'drivelogs',
                title:'行车日记',
                region:'east',
                collapsible:true,
                collapsed:true,
                hideCollapseTool:true,
                style:'opacity:0.9; filter:alpha(opacity=90);',
                layout:'border',
                width:240,
                items:[dataDiary]
            },*/{
                id:'tracehistory',
                title:'轨迹信息',
                region:'east',
                collapsible:true,
                collapsed:true,
                hideCollapseTool:true,
                layout:'border',
                autoScroll : true,
                width:280,
                html:'<table id="tracerpt" class="trace"></table>'
            },{
                region:'center',
                collapsible:false,
                layout:'border',
                border:false,
                html:'<div class="mapWrap"><div id="mapDiv">地图加载中...</div></div>',
                listeners:{
                    afterrender:function(){
                        player = Ext.create('MyApp.Component.DriverTracePlayer');
                        player.load({
                            driverId:driverId,
                            date:Ext.getCmp('date').getValue().format('yyyy-MM-dd')
                        },function(){
                            panel.playReady.call(this);
                            panel.showHistoryData();
//                            panel.showDateEvent();
                        });
                    }
                }
            }],
           renderTo: Ext.getBody()
       });
       
       
       this.showHistoryData = function() {
            var table = Ext.get('tracerpt');
            table.setHTML('');
            var map = player.getMap();
            var adds = [];
            var ticks = [];
            var keytick = 0;
            var myGeo = new BMap.Geocoder();
            for(var i = 0; i < player.atrace.length; i++) {
                var trace = player.atrace[i];
                var tmp = '<tbody><tr><td class="col0"><b>车牌 {4}</b></td></tr><tr><td class="col0"><b>{0} 起点</b><br/><span>地址解析中...</span></td><td class="col1" rowspan="3">{2}公里<br/>{3}</td></tr><tr><td class="col0"><b>{1} 终点</b><br/><span>地址解析中...</span></td></tr></tbody>';
                var stime = trace[0][1].toDate().format('hh:mm:ss');
                var etime = trace[trace.length-1][1].toDate().format('hh:mm:ss');
                var plate = trace[i][5];
                var distance = 0;
                var pertime = 0;
                adds.push(trace[0][0]);
                adds.push(trace[trace.length-1][0]);
                ticks.push(keytick);
                keytick += trace.length;
                //ticks.push(i*trace.length-1)
                for(var n=0; n < trace.length-1; n++) {
                    var dist = map.getDistance(trace[n][0],trace[n+1][0]);
                    dist = isNaN(dist) ? 0 : dist;
                    distance += dist;
                    pertime += trace[n+1][1].toDate().getTime() - trace[n][1].toDate().getTime();
                }
                distance = Math.ceil(distance/100)/10;
                pertime = pertime < 3600000 ? Math.ceil(pertime/60000) + '分钟' : Math.ceil(pertime/360000)/10 + '小时';
                Ext.DomHelper.append(table,tmp.Format(stime,etime,distance,pertime,plate));
            }
            
            var addrSpans = table.select('span');
            var items = table.select('tbody');
            items.on('click',function(){
                if(player.slider.data.isPlay) player.btnplay.fireEvent('click');
                player.slider.setValue(ticks[items.indexOf(this)]);
                player.slider.fireEvent('changecomplete');
            });
            var index = 0;
            bdGEO();
            function bdGEO(){
                var pt = adds[index];
                geocodeSearch(pt);
                index++;
            }
            function geocodeSearch(pt){
                if(index < adds.length-1){
                    setTimeout(bdGEO,300);
                } 
                (function(i){
                    myGeo.getLocation(pt, function(rs){
                        var addComp = rs.addressComponents;
                        addrSpans.item(i).setHTML(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
                    });
                })(index);
            }
        }
       
       //显示当天时间
       this.showDateEvent = function() {
            setTimeout(function(){
                Ext.ajaxModelLoader('MyApp.Model.DateEvent',{
                    target:player.getMap(),
                    params:{
                        driverId:driverId,
                        date:Ext.getCmp('date').getValue().format('yyyy-MM-dd')
                    }
                }).request();
            },2000);
        }
       
       //播放状态就绪
       this.playReady = function() {
           Ext.getCmp('play').enable(true);
           Ext.getCmp('stop').enable(true);
           Ext.getCmp('slider').enable(true);
           Ext.getCmp('stop').fireEvent('click');
       }
       
       window.onresize = function(){
           panel.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
           panel.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
       }
   }
});

