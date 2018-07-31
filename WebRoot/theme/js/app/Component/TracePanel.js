/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.TracePanel',{
   extend:'Ext.panel.Panel',
   config:{
      map : null  
   },
   constructor: function(){
       var panel = this;
       var player = null;
       var tab = top.Ext.getCmp('apptabs').getActiveTab();
       var vehicleId = MyApp.Base.Page.getQuery('vehicleId');
       var dataDiary = Ext.create('MyApp.Component.DiaryTabPanel');
       var dateTime0 = Ext.create('Ext.ux.form.DateTimeField',{
            fieldLabel:'开始时间',
            id:'date1',
            format:'Y-m-d H:i:s',
            width:220,
            labelWidth:70,
            name:'dateTime0',
            allowBlank : false,
            value:Ext.Date.clearTime(new Date()),//设置初始值为当天0时
            editable:false
        });
        
        var dateTime1 = Ext.create('Ext.ux.form.DateTimeField',{
            fieldLabel:'结束时间',
            format:'Y-m-d H:i:s',
            width:220,
             id:'date2',
            labelWidth:70,
            name:'dateTime1',
            allowBlank : false,
            value:new Date(),
            editable:false,
            listeners:{
                    change:function(h,nd){
                        Ext.getCmp('stop').fireEvent('click');
                        Ext.getCmp('mdiarypanel').addLogs(vehicleId,nd.format('yyyy-MM-dd'));
						Ext.getCmp('ydiarypanel').addLogs(vehicleId,nd.format('yyyy-MM-dd'));
                    }
                }
        });
       this.superclass.constructor.call(this,{
           width: tab.getEl().down('iframe').dom.scrollWidth,
           height:tab.getEl().down('iframe').dom.scrollHeight,
           border:false,
           layout:'border',
           tbar:[dateTime0,dateTime1,
            {
                id:'pl',
                xtype:'button',
                text:'查询',
                width:'50',
                listeners: {
                    click : function(){
                     Ext.getCmp('stop').fireEvent('click');
                      player.load({
                            vehicleId:vehicleId,
                            date:Ext.getCmp('date1').getValue().format('yyyyMMddhhmmss'),
                            date1:Ext.getCmp('date2').getValue().format('yyyyMMddhhmmss')
                        },function(){
                            //panel.map.clearOverlays();
                            panel.playReady.call(this);
                            panel.showHistoryData();
                           // panel.showDateEvent();
                        });
                    }
                }
            },
            {
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
            },
            {
                id:'dis',
                xtype:'button',
                text:'测距',
                listeners: {
                    click : function(){
                       player.openDis();
                    }
                }
            },{
                xtype:'tbfill'
            },{
                text:'导出轨迹',
                //icon:'theme/imgs/ico_excel.png',
                listeners: {
                    click : function(){
                    	top.Ext.getCmp('apptabs').open({title:this.text,url:'reportPosition.html'});
                    }
                }
            }],
           items:[{
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
            },{
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
                        player = Ext.create('MyApp.Component.TracePlayer');
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
            if(player.atrace.length == 0) return;
            for(var i = 0; i < player.atrace.length; i++) {
                var trace = player.atrace[i];
                var tmp = '<tbody><tr><td class="col0"><b>{0} 起点</b><br/><span>地址解析中...</span></td><td class="col1" rowspan="2">{2}公里<br/>{3}</td></tr><tr><td class="col0"><b>{1} 终点</b><br/><span>地址解析中...</span></td></tr></tbody>';
                var stime = trace[0][1].toDate().format('hh:mm:ss');
                var etime = trace[trace.length-1][1].toDate().format('hh:mm:ss');
                var distance = 0;
                var pertime = 0;
                adds.push(trace[0][0]);
                adds.push(trace[trace.length-1][0]);
                ticks.push(keytick);
                keytick += trace.length;
                for(var n=0; n < trace.length-1; n++) {
                    var dist = map.getDistance(trace[n][0],trace[n+1][0]);
                    dist = isNaN(dist) ? 0 : dist;
                    distance += dist;
                    pertime += trace[n+1][1].toDate().getTime() - trace[n][1].toDate().getTime();
                }
                distance = Math.ceil(distance/100)/10;
                pertime = pertime < 3600000 ? Math.ceil(pertime/60000) + '分钟' : Math.ceil(pertime/360000)/10 + '小时';
                Ext.DomHelper.append(table,tmp.Format(stime,etime,distance,pertime));
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
            //setTimeout(function(){
                Ext.ajaxModelLoader('MyApp.Model.DateEvent',{
                    target:player.getMap(),
                    params:{
                        vehicleId:vehicleId,
                        date:Ext.getCmp('date').getValue().format('yyyy-MM-dd')
                    }
                }).request();
            //},2000);
        }
       
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

