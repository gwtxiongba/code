/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.TraceMap',{
   extend:'Ext.util.Observable',
   requires:'MyApp.Component.Vehicle',
   constructor: function(){
        var mapDiv = Ext.get('mapDiv');
        var map = new BMap.Map(mapDiv.id,{minZoom:4,maxZoom:16}); 
        var _this = this;
        map.centerAndZoom(new BMap.Point(116.331398,39.897445), 12);
        map.enableScrollWheelZoom();
        map.addControl(new BMap.NavigationControl());
        var polylines = [];
        var trace = null;
        var tick = 0, maxtick = 0 //全局播放帧
        var vehicle = null;
        var slider = null;
        var timer = null;
        var playMulti = 5;
       
        //绘制当天轨迹
        this.drawTrace = function(btrace) {
            trace = btrace;
            btrace = null;
            if(!trace || !trace.length) {
                Ext.Msg.alert('系统提示','当前时间没有车辆轨迹信息');
                return;
            }
            
            for(var i=0; i<trace.length; i++) {
                var sgps = trace[i][0];
                var egps = trace[i][trace[i].length-1];
                var points = trace[i].getValuesFromKey(0);
                maxtick += points.length;
                var polyline =  new BMap.Polyline(points, {
                    strokeColor:"blue", 
                    strokeWeight:3, 
                    strokeOpacity:0.6,
                    strokeStyle:'dashed'
                });
                map.addOverlay(polyline);
                map.setViewport(points);
                polylines.push(polyline);
            }
            
            slider = Ext.getCmp('slider');
            slider.setMaxValue(maxtick);
            vehicle = Ext.create('MyApp.Component.Vehicle',{map:map,gps:trace[0][0]});
            
            //setTimeout(function(){vehicle.moveTo(trace[0][100])},1000);
        }
        
//      //设置播放倍率
        this.setPlayMulti = function(muti) {
            playMulti = muti;
        }
        
        //车辆移动动画效果
        this.animate = function(){
            if(tick < maxtick){
                var gps = getGPS(tick);
                vehicle.moveTo(gps);
                slider.setValue(tick);
                timer = setTimeout(function(){_this.animate();},1000);
                tick ++;
            } else clearTimeout(timer);
        }   
        
        //根据帧来获取gps数据
        function getGPS(tick) {
            var gtick = 0;
            for(var i=0; i<trace.length; i ++) {
                 gtick += trace[i].length;
                 if(gtick < tick) continue;
                 polylines[i].setStrokeColor('red');
                 polylines[i].setStrokeStyle('solid');
                 return trace[i][trace[i].length - gtick+tick];
            }
            return null;
        }
   }
});

