/* 
 * To change this template, choose Tools | Templates
 * 绘制路径显示地图
 */
Ext.define('MyApp.Component.BancheDrawLineMap',{
   extend:'Ext.util.Observable',
   config:{},
   constructor: function(){
      var mapDiv = Ext.get('mapCanvus');
      var map = new BMap.Map(mapDiv.id);
      var points = [];
      var polylines = [];
      var flag = false; //正常模式
      var _this = this;
      map.centerAndZoom(new BMap.Point(116.331398,39.897445), 12);
      map.enableScrollWheelZoom();
      map.addControl(new BMap.NavigationControl());
      
      map.addEventListener('click',function(e){
         if(!flag) return; 
         points.push(new BMap.Point(e.point.lng,e.point.lat));
         if(points.length > 1) {
             var polyline = new BMap.Polyline([points[points.length-1],points[points.length-2]],{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5});
             polylines.push(polyline);
             map.addOverlay(polyline);
         }
      });
      
      map.addEventListener('dblclick',function(){
          return !flag;
      });
      
      this.changeModel = function(fn){
          flag = !flag;
          fn(flag);
      }
      
      //回退上次绘制的路径
      this.redo = function() {
          var polyline = polylines.pop();
          points.pop();
          map.removeOverlay(polyline);
      }
      
      //清除绘制路径
      this.clearLine = function() {
          points = [];
          map.clearOverlays();
      }
      
      //获取位置点
      this.getPoints = function() {
          var pts = [];
          for(var i=0; i < points.length; i++) {
              pts.push(points[i].lng+','+points[i].lat);
          }
          return pts.join(';');
      }
      
      this.getDistance = function() {
          var dist = 0;
          for(var i=0; i < points.length-1; i++) {
              dist += map.getDistance(points[i],points[i+1]);
          }
          return dist;
      }
      
      this.drawLine = function(path) {
          var ptStrArr = path.split(';');
          points = [];
         
          for(var i=0; i < ptStrArr.length-1;i++) {
                var x = ptStrArr[i].split(',')[0];
                var y = ptStrArr[i].split(',')[1];
                	 points.push(new BMap.Point(x,y));
                if(points.length > 1) {
                    var polyline = new BMap.Polyline([points[points.length-1],points[points.length-2]],{strokeColor:"blue", strokeWeight:4, strokeOpacity:0.5});
                    polylines.push(polyline);
                    map.addOverlay(polyline);
                }                 
          }
          map.setViewport(points);
      }
      
      this.search = function(v){
          var local = new BMap.LocalSearch(map, {
		renderOptions:{map: map}
           });
          local.search(v);
      }
   }
});


