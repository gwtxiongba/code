/* 
 * To change this template, choose Tools | Templates
 * 路径显示地图
 */
Ext.define('MyApp.Component.BancheLineMap',{
   extend:'Ext.util.Observable',
   constructor: function(id){
      var mapDiv = Ext.get(id);
      var map = new BMap.Map(mapDiv.id);
      var _this = this;
      map.centerAndZoom(new BMap.Point(116.331398,39.897445), 12);
      map.enableScrollWheelZoom();
      map.addControl(new BMap.NavigationControl());
      
      //绘制单条路线
      this.drawSingleLine = function(pathStr) {
          map.clearOverlays();
          var polyline = this.drawLine(pathStr);
          map.setViewport(polyline.getPath());
      }
      
      //绘制路线
      this.drawLine = function(pathStr) {
          var pathArr = pathStr.split(';');
          var pts = [];
          for(var i = 0; i < pathArr.length; i++) {
             pts.push(new BMap.Point(pathArr[i].split(',')[0],pathArr[i].split(',')[1])); 
          }
          var polyline = new BMap.Polyline(pts,{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
          map.addOverlay(polyline);
          return polyline;
      }
      
      //清理图层
      this.clearOverlays = function(){
          map.clearOverlays();
      }
      
      //绘制位置点
      this.drawPosition = function(gps,zoom) {
          var bgps = [new BMap.Point(gps[0],gps[1]),gps[2],gps[3],gps[4]];
          var vehicle = Ext.create('MyApp.Component.Vehicle',{map:map,gps:bgps});
          vehicle.moveToWithoutInfo(bgps);
          if(zoom) map.setZoom(zoom);
      }
   }
});

