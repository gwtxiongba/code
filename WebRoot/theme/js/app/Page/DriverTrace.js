Ext.define('MyApp.Page.DriverTrace',{
   extend:'MyApp.Base.Page',
   title:'司机考勤轨迹回放',
   onInit:function(){
       Ext.create('MyApp.Component.DriverTracePanel');
   }
});

