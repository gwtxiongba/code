/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component._Vehicle',{
    config:{
       gps: null,
       map: null,
       size:{w:36,h:38},
       icoUrl:{
          on:'theme/imgs/online.png',
          off:'theme/imgs/offline.png'
       }
    },
    constructor:function(config) {
        var veh = this;
        veh.initConfig(config);
        
        var lstOn = veh.gps[4] == undefined || veh.gps[4];
        var marker = new BMap.Marker(veh.gps[0],{
            icon:new BMap.Icon(lstOn? veh.icoUrl.on:veh.icoUrl.off,new BMap.Size(veh.size.w,veh.size.h))
        });
        var menu = new BMap.ContextMenu();
        veh.map.addOverlay(marker);
           
        menu.addItem(new BMap.MenuItem('历史回放',function(){
            var vehInfo = veh.attachInfo;
            Ext.getCmp('apptabs').open({title:'历史回放:'+vehInfo.plate,url:'trace.html?vehicleId='+vehInfo.vehid});
        }));
        
        /*
        menu.addItem(new BMap.MenuItem('实时车况',function(){
            var vehInfo = veh.attachInfo;
            var w = Ext.getCmp('carConditionWindow_id');
            if(!w) Ext.create('MyApp.Component.CarConditionWindow');
            else Ext.getCmp('carConditionWindow_id').setValue(vehInfo.id,vehInfo.plate,vehInfo.identifier);
            alert(vehInfo.vehid+','+vehInfo.plate+','+vehInfo.identifier);
        }));*/
        
        menu.addItem(new BMap.MenuItem('加入监控列表',function(){
             Ext.ajaxModelLoader('MyApp.Model.SaveMonitor',{
                params:{
                    vehicleIds: veh.attachInfo.vehid,
                    //修改是否监控属性,0为不监控,1为监控
                    ifMonitor:1
                }
            }).request();
        }));
        
        marker.addContextMenu(menu);
        marker.setZIndex(99);
        
        //移动到新的位置
        var movable = false;
        veh.moveTo = function(gps) {
            var thOn = gps[4] == undefined || gps[4];
            movable = lstOn || thOn;
            if(!movable) return;
            setIcon(gps[3],thOn);
            marker.setPosition(gps[0]);
            lstOn = thOn;
        }
        
        //删除地图上的车辆
        veh.removeOnMap = function() {
            veh.map.removeOverlay(marker);
        }
        
        veh.addEventListener = function(event,handler) {
            marker.addEventListener(event,function(e){
                handler.call(veh,e);
            });
        }
        
        //获取车辆位置点
        veh.getPosition = function() {
            return marker.getPosition();
        }
        
        //判断当前车辆是否可移动
        veh.movable = function() {
            return movable;
        }
        
        veh.addMenu = function(menu) {
            marker.addContextMenu(menu);
        }
        
        veh.removeMenu = function(menu) {
            marker.removeContextMenu(menu);
        }
        
        var setIcon = function(angle,online) {
            var ico = marker.getIcon();
            var i = (36-angle%36)%36; //将顺时针转为逆时针
            ico.setImageUrl(online? veh.icoUrl.on : veh.icoUrl.off);
            ico.setImageOffset(new BMap.Size(-veh.size.w*(i%9),-veh.size.h*Math.floor(i/9)));
            marker.setIcon(ico);
        }
        
        setIcon(veh.gps[3],lstOn);
    }
});

