/* 
 * To change this template, choose Tools | Templates
 * 围栏报警窗口初始化
 */
Ext.define('MyApp.Component.AlarmLimitWindow',{
   extend:'Ext.window.Window',
   config:{id:'AlarmLimitWindow_id'},
   constructor:function(){
       var w = this;
       var areaMap = null;
       
       var grid = Ext.create('MyApp.Component.LimitAlarmsPanel');
       
       grid.addListener('itemclick',function(view,rec,item,index,e,eOpts){
           var d = rec.getData();
           areaMap.clearOverlays();
           for(var i=0; i < d.paths.length;i++) {areaMap.drawPolyline(d.paths[i]);}
           for(var i=0; i < d.areas.length;i++) {
               if(d.areas[i].indexOf(';') == -1) {//绘制圆
                   var vars = d.areas[i].split(',');
                   areaMap.drawCircle(vars[0],vars[1],vars[2]);
               } else {//绘制多边形
                   areaMap.drawPolygon(d.areas[i]);
               }
           }
           areaMap.drawPosition(d.gps,16);
           
           if(d.ifRead) return;
           rec.set('ifRead',true);
           Ext.ajaxModelLoader('MyApp.Model.ReadLimitAlarm',{
                params:{limitId: rec.get('limitId')},
                target: grid 
            }).request();
       });
       
       this.superclass.constructor.call(this,{
           title:'围栏报警',
           width:800,
           height:400,
           resizable:false,
           layout:'border',
           items:[{
               region:'west',
               width:'45%',
               layout:'border',
               html:'<div class="mapWrap"><div id="mapDiv0" style="height:400px">地图加载中..</div></div>',
               listeners:{
                  afterrender:function(){
                      areaMap = Ext.create('MyApp.Component.AreaMap','mapDiv0');
                  }
              }
           },{
               region:'center',
               layout:'fit',
               border:false,
               items:grid
           }]
       });
       this.show();
   }
});


