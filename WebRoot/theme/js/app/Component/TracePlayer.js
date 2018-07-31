/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.TracePlayer',{
    extend:'Ext.util.Observable',
    requires:'MyApp.Component.Vehicle',
    statics:{
        //根据帧来获取gps数据
        getGPS : function(tick) {
            var gtick = 0;
            var length = this.atrace.length;
            for(var i=0; i<length; i ++) {
                 gtick += this.atrace[i].length;
                 if(gtick-1 < tick) continue;
                 
                 for(var n = 0; n < length; n++) {
                     if(n == i) {
                         this.polylines[n].setStrokeColor('red');
                         //this.polylines[n].setZIndex(90);
                         this.polylines[n].setStrokeOpacity(1);
                         this.polylines[n].setStrokeStyle('solid');
                     } else {
                         this.polylines[n].setStrokeColor('blue');
                         //this.polylines[n].setZIndex(0);
                         this.polylines[n].setStrokeOpacity(0.4);
                         this.polylines[n].setStrokeStyle('dashed');
                     }
                 }
                 return this.atrace[i][this.atrace[i].length - gtick + tick];
            }
            return null;
            
            function colorPolyline(index,polylines) {
                for(var n=0; n< this.atrace.length; n ++) {
                    if(n == index) {
                        polylines[i].setStrokeColor('red');
                        //polylines[i].setZIndex(90);
                        polylines[i].setStrokeOpacity(1);
                        polylines[i].setStrokeStyle('solid');
                    } else {
                        polylines[i].setStrokeColor('blue');
                        //polylines[i].setZIndex(0);
                        polylines[i].setStrokeOpacity(0.4);
                        polylines[i].setStrokeStyle('dashed');
                    }
                }
            }
        }
    },
    config:{
        mapId: 'mapDiv',
        slider : Ext.getCmp('slider'),
        multi : Ext.getCmp('multi'),
        btnplay : Ext.getCmp('play'),
        btnstop : Ext.getCmp('stop'),
        playMuiti:5,
        polylines:[],
        vehicle:null,
        atrace:[]
    },
    constructor: function() {  
        var player = this;
        //var map = new BMap.Map(this.mapId);
      var map = new BMap.Map(this.mapId,{minZoom:4,maxZoom:19}); 
        map.centerAndZoom(new BMap.Point(116.331398,39.897445), 12);
       //var mapType1 = new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});
	    var mapType2 = new BMap.MapTypeControl({anchor: BMAP_ANCHOR_TOP_LEFT,offset: new BMap.Size(50, 10)});
	      map.addControl(mapType2)
	     map.setCurrentCity("北京");  
        map.enableScrollWheelZoom();
        map.addControl(new BMap.NavigationControl());
        map.addControl(new BMap.OverviewMapControl({isOpen:true}));
        this.getMap = function() {return map;}
        this.openDis = function(){
        var myDis = new BMapLib.DistanceTool(map);
	       map.addEventListener("load",function(){
		    myDis.open();  
	    });
        myDis.open();
        }
        this.slider.on('changecomplete',function(){
            if(player.atrace.length) {
                player.animate();
            }
        });
        
        this.slider.on('dragstart',function(){
            if(this.data.isPlay) {
                player.btnplay.fireEvent('click');
            }
        });
        
        this.btnplay.on('click',function(){
            if(player.atrace.length){
                player.animate();
            }
        });
    },
    load : function(params,fn) {
        var player = this;
        Ext.ajaxModelLoader('MyApp.Model.DateTrace',{
            params:params,
            success:function(data){
                player.atrace = packData(data);
                player.polylines = drawTrace.call(player,player.atrace);
                fn.call(player);
            }
        }).request();
        
        //上一个位置点,当天当前里程，当天行驶时间
        var lastgps = null,curmileage = 0,drivetime = 0;
        
        function packData(data) {
            var atrace = [], btrace = [], gps = null,t = 0;
            var num = 0;
            if(data.length > 1)
           // alert(data.length);
            while((gps = data.shift())){
                var bgps = [new BMap.Point(gps[0],gps[1]),gps[2],gps[3],gps[4]];
                var gpsDriveInfo = getGPSDriveInfo(bgps);
               // if(gpsDriveInfo != 1){
                var dt = new Date(gps[2]);
                if(!data.length || (t && dt.getTime() - t > 300000)) {
                    //上报时间超过五分钟则分段
                   atrace.push(btrace);   
                   btrace = [];
                }
               
                bgps.push(gpsDriveInfo);
                btrace.push(bgps);
                t = dt.getTime();  
                //}
            }
            return atrace;
        }
        
        //获取GPS行车信息
        function getGPSDriveInfo(gps) {
            var time = 0,dist = 0,avgspeed = 0,curspeed = 0;
            
            if(lastgps) {
                time = gps[1].toDate().getTime() - lastgps[1].toDate().getTime(); //单位毫秒
                dist = player.getMap().getDistance(lastgps[0],gps[0]); //单位米
                dist = isNaN(dist) ? 0 : dist;//同位置点返回可能为NaN
               
                curmileage += dist;//累加当天里程
                if(time < 180000) {
                    drivetime += time; //累加当天驾驶时间
                    curspeed =  time ? Math.round(dist*36000/time)/10 : 0;
                   
                } 
                avgspeed = time ? Math.round(curmileage*36000/drivetime)/10 : 0; //小数点后一位
            }
            lastgps = gps;
            return {
                curmileage:Math.round(curmileage/100)/10,
                curspeed:curspeed,
                avgspeed:avgspeed,
                hours: Math.round(drivetime/360000)/10
            };
        }
        
        function drawTrace(atrace) {
            var polylines = [];
            this.getMap().clearOverlays();
            if(this.polylines.length) {
                this.vehicle.removeTraceInfo();
            }
            this.vehicle = null;
            if(!atrace || !atrace.length) {
                Ext.Msg.alert('系统提示','当前时间没有车辆轨迹信息');
                this.slider.setMaxValue(0);
                this.btnplay.disable(true);
                this.btnstop.disable(true);
                this.slider.disable(true);
                return polylines;
            }
            
            
            var map = this.getMap();
            var maxtick = 0;
            for(var i=0; i<atrace.length; i++) {
                var points = atrace[i].getValuesFromKey(0);
                maxtick += points.length;
                var polyline =  new BMap.Polyline(points, {
                    strokeColor:"blue", 
                    strokeWeight:3, 
                    strokeOpacity:0.4,
                    strokeStyle:'dashed'
                });
                map.addOverlay(polyline);
                drawParkMark(atrace,i);
                drawStartStopMark(atrace);
                //map.setViewport(points);
                polylines.push(polyline);
            }
            if(atrace[0] &&atrace[0][0]) setTimeout(function(){map.centerAndZoom(atrace[0][0][0],14);},5);
            this.slider.setMaxValue(maxtick-1);
            return polylines;
        }
        
        function drawStartStopMark(atrace) {
            var spt = atrace[0][0][0];
            var ept = atrace[atrace.length-1][atrace[atrace.length-1].length-1][0];
            var marker = new BMap.Marker(spt,{
                icon:new BMap.Icon("theme/imgs/marker0.png", new BMap.Size(40,32)),
                offset:new BMap.Size(5,-15)
            });
            player.getMap().addOverlay(marker);
            marker = new BMap.Marker(ept,{icon:new BMap.Icon("theme/imgs/marker0.png", new BMap.Size(40,32),{imageOffset:new BMap.Size(-40,0)})});
            player.getMap().addOverlay(marker);
        }
        
        function drawParkMark(atrace,index) {
            var max = atrace.length-1;
            if(index > max-1) return;
            var icon = new BMap.Icon("theme/imgs/marker.png", new BMap.Size(35,34),{
                imageOffset: new BMap.Size(-35*index,0)
            });
            var marker = new BMap.Marker(atrace[index][atrace[index].length-1][0],{
                icon:icon,
                offset:new BMap.Size(6,-20)
            });
            
            player.getMap().addOverlay(marker);
            //marker.setAnimation(BMAP_ANIMATION_BOUNCE);
        }
    },
    animate: function() {
        var player = this;
        if(!player.vehicle) {
            player.vehicle = Ext.create('MyApp.Component.Vehicle',{
                map:player.getMap(),
                gps:player.atrace[0][0]
            });
        } 
        
        var timer = null;
        var tick = player.slider.getValue();
        var muti = player.multi.getValue();
        if(tick <= player.slider.maxValue){
            var gps = MyApp.Component.TracePlayer.getGPS.call(player,tick);
            player.vehicle.moveTo(gps);
            Ext.getCmp('slider').setValue(tick);
            timer = setTimeout(function(){
                player.animate();
            },parseInt(5000/muti.speed));
            
            if(!player.slider.data.isPlay || tick == player.slider.maxValue) {
                player.vehicle.moveTo(gps);
                clearTimeout(timer);
            }
            
            if(player.slider.data.isPlay && tick == player.slider.maxValue) {
                player.btnplay.fireEvent('click');
            }
            
            player.slider.setValue(tick+1);
        }
    },
    getTickTime:function(tick) {
        var gps = MyApp.Component.TracePlayer.getGPS.call(this,tick);
        return gps[1];
    }
});

