/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.DrivingRep',{
   extend:'MyApp.Base.Page',
   onInit:function(){
       switch(location.hash) {
           case '#0': //行驶报表
               Ext.create('MyApp.Component.DrivingRepPanel');
               break;
           case '#1': //超速报表
               Ext.create('MyApp.Component.OverSpeedRepPanel');
               break;
           case '#2': //位置报表
               Ext.create('MyApp.Component.PositionRepPanel');
               break;
            case '#3': //里程报表
               Ext.create('MyApp.Component.MileageRepPanel')
               break;
            case '#4': //停车报表
                Ext.create('MyApp.Component.ParkRepPanel');
                break;
             case '#5': //点火报表
                Ext.create('MyApp.Component.FireRepPanel');
                break;
       }
   }
});

