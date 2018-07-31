/* 
 * To change this template, choose Tools | Templates
 * 绘制多边形区域显示地图
 */
Ext.define('MyApp.Component.DrawPolygonMap',{
   extend:'Ext.util.Observable',
   config:{},
   constructor: function(){
      var mapDiv = Ext.get('mapCanvus');
      var map = new BMap.Map(mapDiv.id);
      var polygon = null;
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
        polygonOptions: styleOptions //多边形的样式
    });  
    
    drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
    
    this.onPolygonComplete = function(callback){
        drawingManager.addEventListener('polygoncomplete',function(overlay){
            flag = false;
            polygon = overlay;
            callback();
        });
    }
    
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
          polygon = null;
          map.clearOverlays();
      }
      
      //绘制多边形
      this.draw = function(pathStr){
          var pathArr = pathStr.split(';');
           var pts = [];
           for(var i = 0; i < pathArr.length; i++) {
             pts.push(new BMap.Point(pathArr[i].split(',')[0],pathArr[i].split(',')[1])); 
           }
           if(pts.length < 3) return;
           polygon = new BMap.Polygon(pts,styleOptions);
           map.addOverlay(polygon);
           map.setViewport(pts);
      }
      
      //是否存在合法的多边形
      this.hasPolygon = function() {
          if(!polygon) return false;
          return polygon.getPath().length >2;
      }
      
      //获得多边形参数值
      this.getValue = function(){
          var arr = [];
          var bpts = polygon.getPath();
          for(var i=0; i<bpts.length; i++) {
              arr.push(bpts[i].lng + ','+ bpts[i].lat);
          }
          return arr.join(';');
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


