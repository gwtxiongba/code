/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.Vehicle',{
   config:{
     gps: null,
     map: null,
     size:{w:36,h:38}  
   },
   constructor:function(config){
       var veh = this;
       this.initConfig(config);
       var icon = new BMap.Icon('theme/imgs/online.png',new BMap.Size(this.size.w,this.size.h));
       var marker = new BMap.Marker(this.gps[0],{icon:icon});
       this.map.setCenter(this.gps[0]);
       this.map.addOverlay(marker);
       marker.setZIndex(99);
       setAngle(this.gps[3]);
       
       var lastpt = null;
       this.moveTo = function(gps) {
          marker.setPosition(gps[0]);
          var driveInfo = gps[4];
          var angle = gps[3] ? gps[3] : getAngle(lastpt,gps[0]);
          var speed = gps[2] ? gps[2] : driveInfo.curspeed;
          setAngle(angle);
          getTraceInfo(speed,gps[1],driveInfo);
          veh.map.setCenter(gps[0]);
          lastpt = gps[0];
       }
       
       this.moveToWithoutInfo = function(gps) {
           marker.setPosition(gps[0]);
           setAngle(gps[3]);
           veh.map.setCenter(gps[0]);
       }
       
       this.removeTraceInfo = function() {
           Ext.get('mapInfo').remove();
       }
       
       function setAngle(angle) {
           var i = (36-angle%36)%36; //将顺时针转为逆时针
           icon.setImageOffset(new BMap.Size(-veh.size.w*(i%9),-veh.size.h*Math.floor(i/9)));
           marker.setIcon(icon);
       }
       
        //两点方位角算法
       function getAngle(pt0,pt1) {
           if(pt0 == null) return 0;
           var dx = (pt1.lng - pt0.lng)*3600*24;
           var dy = (pt1.lat - pt0.lat)*3600*24;
           if(dx==0 && dy == 0) return 0;
           if(dx==0 && dy > 0) return 0;
           if(dx==0 && dy < 0) return 18;
           if(dy==0 && dx > 0) return 9;
           if(dy==0 && dx < 0) return 27;
           var angle = Atan2F(dx, dy);        
           if (angle < 0) angle += 360;
           var value = (90 - angle + 360+5) % 360;
           value=Math.round(value / 10);
           return value <= 36 ? (value>=1?value:1) : 36; 
           
       }
       
       function Atan2F(dx, dy) {
            var tanHash = [4, 13, 22, 31, 40, 49, 58, 67, 77, 86, 95, 104, 114, 123, 132, 142, 152, 161, 171, 181, 191, 202, 212, 223, 233, 244, 255, 267, 278, 290, 302, 314, 326, 339, 352, 365, 379, 393, 407, 422, 437, 453, 469, 486, 503, 521, 540, 559, 579, 599, 621, 644, 667, 692, 718, 745, 774, 804, 836, 869, 905, 943, 984, 1027, 1073, 1123, 1178, 1236, 1300, 1369, 1446, 1530, 1624, 1728, 1846, 1980, 2133, 2309, 2517, 2763, 3060, 3426, 3889, 4494, 5317, 6506, 8371, 11727, 19552, 58669 ];
            var i = Math.abs(dy * 512 / dx);
            var j;
            for (j = 0; j < 90; ++j) {
                if (i < tanHash[j]) break;
            }
            if (dx < 0 && dy > 0) return 180 - j;

            if (dx < 0 && dy < 0) return j + 180;

            if (dx > 0 && dy < 0) return 360 - j;

            return j;
        }
       
       function getTraceInfo(s,t,o) {
           var mapInfo = Ext.get('mapInfo');
           var tpstr = '<div id="mapInfo" class="mapInfoZ">{1}<br/>当前行驶里程：{2}km<br/>当前行驶时间：{3}小时<br/>平均速度：{4}km/h<br/>瞬时速度：{0}km/h</div>';
           var speed = s ? s : o.curspeed;
           if(!mapInfo) {
               Ext.DomHelper.insertAfter(Ext.get('mapDiv'),tpstr.Format(speed,t,o.curmileage,o.hours,o.avgspeed));
           } else {
               mapInfo.dom.outerHTML = tpstr.Format(speed,t,o.curmileage,o.hours,o.avgspeed);
           }
           
       }
   } 
});

