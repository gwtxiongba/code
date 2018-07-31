/* 
 * To change this template, choose Tools | Templates
 * 显示路径区域地图
 */
Ext.define('MyApp.Component.AreaMap',{
   extend:'Ext.util.Observable',
   constructor: function(id){
       var mapDiv =Ext.get(id);
       var map = new BMap.Map(mapDiv.id)
       var _this = this;
       map.centerAndZoom(new BMap.Point(116.331398,39.897445), 12);
       map.enableScrollWheelZoom();
       map.addControl(new BMap.NavigationControl());
       
       var styleOptions = {
            strokeColor:"red",    //边线颜色。
            fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
            strokeWeight: 3,       //边线的宽度，以像素为单位。
            strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
            fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
            strokeStyle: 'solid' //边线的样式，solid或dashed。
        }
       
       //绘制多边形
       this.drawPolygon = function(pathStr){
           var pathArr = pathStr.split(';');
           var pts = [];
           for(var i = 0; i < pathArr.length; i++) {
             pts.push(new BMap.Point(pathArr[i].split(',')[0],pathArr[i].split(',')[1])); 
           }
           if(pts.length < 3) return;
           var polygon = new BMap.Polygon(pts,styleOptions);
           map.addOverlay(polygon);
           map.setViewport(pts);
       }
       
       //绘制圆形区域
       this.drawCircle = function(x,y,r) {
           var level = Math.floor(18-Math.log((4*r)/mapDiv.getWidth())/Math.log(2));
           var pt = new BMap.Point(x,y);
           var circle = new BMap.Circle(pt,r,styleOptions);
           map.addOverlay(circle);
           map.centerAndZoom(pt,level);
       }
       
       //绘制路径
       this.drawPolyline = function(pathStr) {
           var pathArr = pathStr.split(';');
           var pts = [];
           for(var i = 0; i < pathArr.length; i++) {
             pts.push(new BMap.Point(pathArr[i].split(',')[0],pathArr[i].split(',')[1])); 
           }
           var polyline = new BMap.Polyline(pts,styleOptions);
           map.addOverlay(polyline);
       }
       
       //绘制位置点
      this.drawPosition = function(gps,zoom) {
          var bgps = [new BMap.Point(gps[0],gps[1]),gps[2],gps[3],gps[4]];
          var vehicle = Ext.create('MyApp.Component.Vehicle',{map:map,gps:bgps});
          vehicle.moveToWithoutInfo(bgps);
          if(zoom) map.setZoom(zoom);
      }
       
       //清理图层
       this.clearOverlays = function(){
          map.clearOverlays();
       }
   }
});

