/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MonitorSwitch',{
    constructor:function(count){
        var o = MyApp.Base.LocalSet.getGolbal();
        if(o.vehCount > count) {//麻点图
            G_MonitMap = Ext.create('MyApp.Component.MonitMap',10);
            setTimeout(function(){//延时执行防止地图加载失败
                Ext.create('MyApp.Component.CmdTabPanel');
                G_AppTimer.excute();
            },500);
        } else {//marker
            G_MonitMap = Ext.create('MyApp.Component.MarkMonitor',{
                mapLoaded:function(){
                    Ext.create('MyApp.Component.CmdTabPanel');
                    G_AppTimer.excute();
                }
            });
        }
    }
});

