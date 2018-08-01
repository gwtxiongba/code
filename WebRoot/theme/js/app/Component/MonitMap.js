/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MonitMap',{
    extend:'Ext.util.Observable',
    config:{
        mapLoaded:function(){}
    },
    constructor:function(config) {
        var me = this;
        me.initConfig(config);
        window.BMap_loadScriptTime = (new Date).getTime();
        var _js = new CRMApp.Javascript();
        _js.include('http://api.map.baidu.com/getscript?v=2.0&ak=LNaNGfGVvrVpRlsBC5j7rbr1&services=&t=20141230041605"');
       
        _js.load(function(){
            var map = me.map = new BMap.Map('mapDiv');
            map.centerAndZoom(new BMap.Point(116.331398,39.897445),6);
            map.enableScrollWheelZoom();
            map.addControl(new BMap.MapTypeControl());
            map.addControl(new BMap.OverviewMapControl({
                isOpen:true
            })); 
            map.addControl(new BMap.NavigationControl());
            map.setMinZoom(6);
            map.enableContinuousZoom();
            map.enableInertialDragging();
            //map.disableScrollWheelZoom();
            var circle = createCircle();
            var BW  = 0,    //canvas width
            BH            = 0,    //canvas height
            ctx           = null,
            cars         = {},   //存储所有星星对象的数组
            focusId = 0, //获得焦点的设备号
            rs            = [],   //最新的结果
            py            = null, //偏移
            gridWidth   = 1,//网格的大小
            size = {
                w:36,
                h:38
            },
            canvas        = null; //偏移
           
            var infoStr = '{locTime}<br/><b>当前车辆</b><br/>车牌：{plate}<br/>车型：<%=MyApp.Config.Golbal.VehicleTypes[~~{type}]%><br/>车速：{speed} 公里/小时<br/>油电：<%={ifOff} ? "断开":"连通"%><br/><b>司机信息</b><br/>姓名：--<br/>电话：--';
            var infoOverlay = new InfoOverlay(infoStr,{
                x:10,
                y:40
            });
     
            function doFocus(identifier) {
                if(cars[identifier]) {
                    var data = cars[identifier].data;
                    focusId = data.identifier;
                    var bpt = new BMap.Point(data.gps.x,data.gps.y);
                    map.centerAndZoom(bpt,18);
                    circle.setPosition(bpt);
                    cars[focusId].formatLog();
                }
            }
           
            function loseFocus() {
                focusId = 0;
                circle.setPosition(new BMap.Point(0,0));
                infoOverlay.hide();
                renderAction();
            }
               
            map.addEventListener('hotspotclick',function(e){
                var data = e.spots[0].getUserData();
                if(!data) return;
                doFocus(data.identifier);
            });
            
            map.addEventListener('rightclick',loseFocus);
            
            me.monit = function() {
                var me = this;
                Ext.ajaxModelLoader('MyApp.Model.MonitData', {
                    success:function(d){
                    var tmp = '{0}({1}%)';
            var rate = ((d.onlineCount*1000/d.total)/10).toFixed(1);
             top.Ext.getCmp('CarTreePanel_id').refreshSt(d.onlineCount,d.total);
            Ext.getCmp('onlines').setText('在线' + tmp.Format(d.onlineCount,d.total ? rate : 0));
            Ext.getCmp('offlines').setText('离线'+ tmp.Format(d.total-d.onlineCount,d.total ? 100-rate : 0));
                        rs = d.res;
                        var lstOn = (focusId>0 && cars[focusId]) ? cars[focusId].data.online : false;
                             map.clearOverlays();
                        showCars(rs);
                        if(focusId>0 && cars[focusId]) {
                            var info = cars[focusId].data;
                            var bpt = new BMap.Point(info.gps.x,info.gps.y)
                            circle.setPosition(bpt);
                            map.setCenter(bpt);
                            if(info.online || lstOn) {
                                cars[focusId].formatLog();
                            //infoOverlay.render(info);
                            }
                        }
                       // Ext.ajaxModelLoader('MyApp.Model.VehicleCount').request();
                    }
                }).request();
            }
            
            me.getInfo = function(identifier) {
                if(cars[identifier]) {
                    doFocus(identifier);
                } else Ext.create('MyApp.Component.LoadingTips',{
                    lazytime:1000,
                    msg: '该车辆未上报位置点信息'
                });
            }
            
            me.remove = function(identifier) {
                if(cars[identifier]) {
                    delete cars[identifier];
                    focusId = 0;
                    circle.setPosition(new BMap.Point(0,0));
                    renderAction();
                }
            }
            
            //设置显示车辆
            var visibles = null;
            me.setVisible = function(identifiers){
                if(identifiers.length > 0) {	
                    visibles = {};
                    for(var i=0; i < identifiers.length; i++) {
                        visibles[identifiers[i]] = true;
                    }
                } else visibles = null;
                loseFocus();
                showCars(rs);
            }
            
            function Car(options){
                this.init(options);
            }
                
            Car.prototype.init = function(options) {
                this.x   = ~~(options.x);
                this.y   = ~~(options.y);
                this.data = options.data;
            }
            
            Car.prototype.formatLog = function() {
                var info = this.data;
                var bdpt = new BMap.Point(info.gps.x,info.gps.y);
                var o = {
                    vehid: info.vehicle.vehicleId,
                    plate:info.vehicle.plate,
                    identifier:info.identifier,
                    type:info.vehicle.type,
                    driver: info.vehicle.driver,
                    angle: info.gps.angle,
                    speed: info.gps.speed,
                    ifOff: info.vehicle.ifOff,
                    online:info.online,
                    location: '地址解析失败...',
                    locTime : info.gps.time
                }
                infoOverlay.render(o);
                var myGeo = new BMap.Geocoder();
                myGeo.getLocation(bdpt, function (rs) {
                    if(rs){
                        var addComp = rs.addressComponents;
                        o.location = addComp.province  + addComp.city + addComp.district+addComp.street + addComp.streetNumber;
                    }
                    Ext.getCmp('CarInfoPanel_id').addInfo(o);   
                });
            }
            
            Car.prototype.render = function() {
                var m = this;

                if(m.x < 0 || m.y <0 || m.x > BW || m.y > BH) {
                    return;
                }

                var url = 'theme/imgs/' + (m.data.online > 0 ? 'online' : 'offline') + '.png';
                preImage(url,function(){
                    ctx.globalCompositeOperation = 'source-over';
                    var bdpt = new BMap.Point(m.data.gps.x,m.data.gps.y);
                    var hotSpot = new BMap.Hotspot(bdpt, {
                        offsets:[size.h/2,size.w/2,size.h/2,size.w/2], 
                        minZoom: 1, 
                        maxZoom: 19, 
                        userData:m.data
                    });

                    map.addHotspot(hotSpot);
                        
                    var i =(36-m.data.gps.angle%36)%36;
                    ctx.drawImage(this,size.w*(i%9),size.h*Math.floor(i/9),size.w,size.h,m.x-size.w/2,m.y-size.h/2,size.w,size.h);
                });				
            }

            function preImage(url,callback){
                var img = new Image(); //创建一个Image对象，实现图片的预下载
                img.src = url;

                if (img.complete) { // 如果图片已经存在于浏览器缓存，直接调用回调函数
                    callback.call(img);
                    return; // 直接返回，不用再处理onload事件
                }

                img.onload = function () { //图片下载完毕时异步调用callback函数。
                    callback.call(img);//将回调函数的this替换为Image对象
                };
            }

            function renderAction() {
                map.clearHotspots();
                ctx.clearRect(0, 0, BW, BH);
                ctx.globalCompositeOperation = "lighter";
                for(var identifier in cars) {
                    if(visibles == null || visibles[identifier]) 
                        cars[identifier].render();
                }
            }


            // 复杂的自定义覆盖物
            function MapOverlay(point){
                this._point = point;
            }
            MapOverlay.prototype = new BMap.Overlay();
            MapOverlay.prototype.initialize = function(map){
                this._map = map;
                canvas = this.canvas = document.createElement("canvas");
                document.body.appendChild(canvas);
               if((navigator.userAgent.indexOf('MSIE') >= 0) 
    && (navigator.userAgent.indexOf('Opera') < 0)){
                canvas=window.G_vmlCanvasManager.initElement(canvas);
                  }
                canvas.style.cssText = "position:absolute;left:0;top:0;";
                ctx = canvas.getContext("2d");
                ctx.globalCompositeOperation="source-in";
                var size = map.getSize();
                canvas.width = BW = size.width;
                canvas.height = BH = size.height;
                map.getPanes().labelPane.appendChild(canvas);
                return this.canvas;
            }
            MapOverlay.prototype.draw = function(){
                var map = this._map;
                var bounds = map.getBounds();
                var sw = bounds.getSouthWest();
                var ne = bounds.getNorthEast();
                var pixel = map.pointToOverlayPixel(new BMap.Point(sw.lng, ne.lat));
                py = pixel;
                if (rs.length > 0) {
                    //console.log('draw');
                    showCars(rs);
                }
            }
            var myMapOverlay = new MapOverlay(new BMap.Point(116.407845,39.914101));
            map.addOverlay(myMapOverlay);
            var project = map.getMapType().getProjection();
            var bounds = map.getBounds();
            var sw = bounds.getSouthWest();
            var ne = bounds.getNorthEast();
            sw = project.lngLatToPoint(new BMap.Point(sw.lng, sw.lat));
            ne = project.lngLatToPoint(new BMap.Point(ne.lng, ne.lat));

            //左上角墨卡托坐标点
            var original = {
                x : sw.x,
                y : ne.y
            }

            //显示星星
            function showCars(rs) {
                for (var i = 0, len = rs.length; i < len; i++) {
                    var item = rs[i];
                    var addNum = gridWidth / 2;
                    //114.413268:30.460982
                    var mct = project.lngLatToPoint(new BMap.Point(item.gps.x,item.gps.y));
                    var x = mct.x * gridWidth + addNum;
                    var y = mct.y * gridWidth + addNum;
                    var point = project.pointToLngLat(new BMap.Pixel(x, y));
                    var px = map.pointToOverlayPixel(point);
                    var s = new Car({
                        x: px.x - py.x, 
                        y: px.y - py.y,
                        data:item
                    });
                    cars[item.identifier] = s;
                }
                canvas.style.left = py.x + "px";
                canvas.style.top = py.y + "px";
                renderAction();
            }

            function createCircle() {//创建闪烁效果
                var i = 0;
                var circle = new BMap.Marker(new BMap.Point(0,0),{
                    icon:new BMap.Icon('theme/imgs/circle.png',new BMap.Size(45,45)),
                    offset:new BMap.Size(-2,0)
                });
                circle.setZIndex(90);
                circle.addEventListener('rightclick',function(e){
                    map.removeEventListener('rightclick',loseFocus);
                    setTimeout(function(){
                        map.addEventListener('rightclick',loseFocus);
                    },0);
                });
                var menu = new BMap.ContextMenu();
                menu.addItem(new BMap.MenuItem('历史回放',function(){
                    var vehInfo = cars[focusId].data.vehicle;
                    Ext.getCmp('apptabs').open({
                        title:'历史回放:'+vehInfo.plate,
                        url:'trace.html?vehicleId='+vehInfo.vehicleId
                        });
                }));

                menu.addItem(new BMap.MenuItem('加入监控列表',function(){
                    var vehInfo = cars[focusId].data.vehicle;
                    Ext.ajaxModelLoader('MyApp.Model.SaveMonitor',{
                        params:{
                            vehicleIds: vehInfo.vehicleId,
                            //修改是否监控属性,0为不监控,1为监控
                            ifMonitor:1
                        }
                    }).request();
                }));
               // circle.addContextMenu(menu);
                map.addOverlay(circle);
                var ico = circle.getIcon();
                setInterval(function(){
                    ico.setImageOffset(new BMap.Size(-45*i,0));
                    circle.setIcon(ico);
                    i = (i+1)%3;
                },300);
                return circle;
            }

            //地图信息框
            function InfoOverlay(format,offset) {
                this.format = format;
                this.offset = offset;
                this.mapInfo = null;
            }
            
            //呈现新数据
            InfoOverlay.prototype.render = function(data){
                this.mapInfo = Ext.get('mapInfo');
                if(!this.mapInfo) {
                    var tpStr = '<div id="mapInfo" class="mapInfoZ">' + this.format + '</div>';
                    Ext.DomHelper.insertAfter(Ext.get('mapDiv'),tpStr.Format(data).Pack());
                    this.mapInfo = Ext.get('mapInfo');
                    this.mapInfo.dom.style.top = this.offset.y;
                    this.mapInfo.dom.style.right = this.offset.x;
                } else {
                    this.mapInfo.removeCls('hidden');
                    this.mapInfo.dom.innerHTML = this.format.Format(data).Pack();
                }
            }
            
            InfoOverlay.prototype.hide = function() {
                if(this.mapInfo) this.mapInfo.addCls('hidden');
            }
            
            setTimeout(me.monit,1000);

            G_AppTimer.add(1,function(){
            //    me.monit(); 
              //   top.Ext.getCmp('CarTreePanel_id').refreshTree();
                  //Ext.ajaxModelLoader('MyApp.Model.GetLimitCount').request();
            });
            me.mapLoaded();
        });
    }
});

