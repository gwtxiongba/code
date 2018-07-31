/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MarkMonitor',{
   extend:'Ext.util.Observable',
   requires:'MyApp.Component.Vehicle',
   config:{
     mapLoaded:function(){}  
   },
   constructor:function(config){
       var me = this;
       me.initConfig(config)
       me.vehs = {};
       me.fveh = null;
       me.menu = null;
       window.BMap_loadScriptTime = (new Date).getTime();
       var _js = new CRMApp.Javascript();
       _js.include('http://api.map.baidu.com/getscript?v=1.4&ak=&services=&t=20141230041930');
       _js.load(function(){
           var map = me.map = new BMap.Map('mapDiv');
           map.centerAndZoom(new BMap.Point(116.331398,39.897445), 8);
           map.enableScrollWheelZoom();
           map.addControl(new BMap.MapTypeControl());
           map.addControl(new BMap.OverviewMapControl({isOpen:true})); 
           map.addControl(new BMap.NavigationControl());
           me.circle = createCircle();
           /*
           me.circle = new BMap.Circle(new BMap.Point(0,0),0.1);
           me.circle.setFillOpacity(0.01);
           me.circle.setStrokeColor("#ff4040");
           me.circle.setStrokeStyle("dashed");
           me.circle.setStrokeWeight(4);  
           me.map.addOverlay(me.circle);
           */
           me.monit();
           G_AppTimer.add(1,function(){
              me.monit(); 
           });
           
           me.map.addEventListener('zoomend',function() {
               if(me.fveh) {
                   //me.circle.setRadius(16*Math.pow(2,18- me.map.getZoom()));
                   me.map.setCenter(me.fveh.getPosition());
               }
           });
           
           me.loseFocus = function() {
               /*
               me.circle.setCenter(new BMap.Point(0,0));
               me.circle.setRadius(0.1);*/
               me.circle.setPosition(new BMap.Point(0,0));
               me.fveh = null;
           }
           
           me.map.addEventListener('rightclick',me.loseFocus);
           
           var myGeo = new BMap.Geocoder();
           me.doFocus = function(veh) {
                var pt = veh.getPosition();
                me.map.setCenter(pt);
                /*
                me.circle.setRadius(16*Math.pow(2,18 - me.map.getZoom()));
                me.circle.setCenter(pt);*/
                me.circle.setPosition(pt);
                me.fveh = veh;
                
                myGeo.getLocation(pt, function (rs) {
                    var addComp = rs.addressComponents;
                    veh.attachInfo.location = addComp.province  + addComp.city + addComp.district+addComp.street + addComp.streetNumber;
                    Ext.getCmp('CarInfoPanel_id').addInfo(veh.attachInfo);
                });
           }
           
           function createCircle() {//创建闪烁效果
               var i = 0;
               var circle = new BMap.Marker(new BMap.Point(0,0),{
                    icon:new BMap.Icon('theme/imgs/circle.png',new BMap.Size(45,45)),
                    offset:new BMap.Size(-2,0)
                });
               circle.setZIndex(90);
               me.map.addOverlay(circle);
               var ico = circle.getIcon();
               setInterval(function(){
                   ico.setImageOffset(new BMap.Size(-45*i,0));
                   circle.setIcon(ico);
                   i = (i+1)%3;
               },300);
               return circle;
           }
           
           me.mapLoaded();
       });
   },
   monit:function() {
       var me = this;
       Ext.ajaxModelLoader('MyApp.Model.MonitData', {
           success:function(d){
               var o = null;
               while((o= d.shift())) {
                   if(!me.vehs[o.identifier]) {
                       me.vehs[o.identifier] = Ext.create('MyApp.Component._Vehicle',{
                            map:me.map,
                            gps:[new BMap.Point(o.gps.x,o.gps.y),o.gps.time,o.gps.speed,o.gps.angle,o.online]
                        });
                        me.vehs[o.identifier].addEventListener('click',function(){
                            me.doFocus(this);
                            me.map.setZoom(19);
                        });
                        
                        me.vehs[o.identifier].addEventListener('rightclick',function(e){
                           //阻止右击marker的时候冒泡
                           if(!me.fveh || this.attachInfo.identifier != me.fveh.attachInfo.identifier) {
                                var pt = this.getPosition();
                                /*
                                me.circle.setRadius(16*Math.pow(2,18 - me.map.getZoom()));
                                me.circle.setCenter(pt);*/
                                me.circle.setPosition(pt);
                                me.fveh = this;
                           }
                           me.map.removeEventListener('rightclick',me.loseFocus);
                           setTimeout(function(){
                               me.map.addEventListener('rightclick',me.loseFocus);
                           },0);
                        });
                        
                   } else {
                       me.vehs[o.identifier].moveTo([new BMap.Point(o.gps.x,o.gps.y),o.gps.time,o.gps.speed,o.gps.angle,o.online]);
                   }
                 
                   me.vehs[o.identifier].attachInfo = {
                      vehid: o.vehicle.vehicleId,
                      plate:o.vehicle.plate,
                      identifier:o.identifier,
                      type:o.vehicle.type,
                      driver: o.vehicle.driver,
                      angle: o.gps.angle,
                      speed: o.gps.speed,
                      ifOff: o.vehicle.ifOff,
                      online:o.online,
                      location: '',
                      locTime : o.gps.time
                   }
               }
               if(me.fveh) {
                   if(me.fveh.movable()) me.doFocus(me.fveh);
               }
           }
       }).request();
   },
   getInfo:function(identifier) {
       var me = this;
       //identifier = 100000001;
       if(me.vehs[identifier]) {
           me.doFocus(me.vehs[identifier]);
           me.map.setZoom(19);
       } else Ext.create('MyApp.Component.LoadingTips',{lazytime:1000,msg: '该车辆未上报位置点信息'});
   },
   remove:function(identifier){
       var me = this;
       if(me.vehs[identifier]) {
          me.vehs[identifier].removeOnMap();
          delete me.vehs[identifier];
          /*me.circle.setCenter(new BMap.Point(0,0));
          me.circle.setRadius(0.1);*/
          me.circle.setPosition(new BMap.Point(0,0));
          me.fveh = null;
       }
   }
});

