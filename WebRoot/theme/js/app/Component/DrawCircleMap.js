/* 
 * To change this template, choose Tools | Templates
 * 绘制圆形区域显示地图
 */
Ext.define('MyApp.Component.DrawCircleMap',{
   extend:'Ext.util.Observable',
   config:{},
   constructor: function(){
      var mapDiv = Ext.get('mapCanvus');
      var map = new BMap.Map(mapDiv.id);
      var circle = null;
      var flag = false; //正常模式
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
      
      //实例化鼠标绘制工具
      var drawingManager = new BMapLib.DrawingManager(map, {
          isOpen: false, //是否开启绘制模式
          enableDrawingTool: false, //是否显示工具栏
          circleOptions: styleOptions //多边形的样式
      });
      
      drawingManager.setDrawingMode(BMAP_DRAWING_CIRCLE);
      
      this.onCircleComplete = function(callback) {
          drawingManager.addEventListener('circlecomplete',function(overlay){
            flag = false;
            drawingManager.close()
            circle = overlay;
            callback();
        });
      }
      
      /*
      map.addEventListener('click',function(e){
          if(!flag) return; 
          var o = new BMap.Point(e.point.lng,e.point.lat);
          map.clearOverlays();
          map.setCenter(o);
          var level = Math.floor(18-Math.log((4*5000)/800)/Math.log(2));
          circle = new BMap.Circle(o,5000,styleOptions);
          map.centerAndZoom(o,level);
          Ext.getCmp('CircleVarsWindow_id').show();
      });
      
      map.addEventListener('dblclick',function(){
          return !flag;
      });*/
      
      //切换查看/编辑模式
      this.changeModel = function(fn){
            flag = !flag;
            if(flag) {
                this.clear();
                drawingManager.open();
            }
            else drawingManager.close();
            fn(flag);
        }
      
      //清除绘制图形
      this.clear = function() {
          circle = null;
          map.clearOverlays();
      }
      
      //绘制圆形
      this.draw = function(x,y,r){
          var level = Math.floor(18-Math.log((4*r)/800)/Math.log(2));
          var pt = new BMap.Point(x,y);
           circle = new BMap.Circle(pt,r,styleOptions);
           map.addOverlay(circle);
           map.centerAndZoom(pt,level);
      }
      
      this.getValue = function() {
          var bpt = circle.getCenter();
          return bpt.lng + ',' + bpt.lat + ',' + circle.getRadius();
      }
      
      this.hasCircle = function(){
          return !!circle;
      }
      
      //搜索位置
      this.search = function(v){
          var local = new BMap.LocalSearch(map, {
		renderOptions:{map: map}
           });
          local.search(v);
      }
   }
});


