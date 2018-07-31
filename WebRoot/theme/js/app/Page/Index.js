/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.Index',{
   extend: 'MyApp.Base.Page',
   title: '政企车辆管理平台1.0',
   onInit: function() {
      G_AppTimer = Ext.create('MyApp.Base.Timer',60000);
      Ext.create('MyApp.Component.AppViewport');
      //Ext.create('MyApp.Component.MessageTriger');
      Ext.create('MyApp.Component.AppTabPanel');
      G_MonitMap = Ext.create('MyApp.Component.MonitMap',{
                mapLoaded:function(){
                    Ext.create('MyApp.Component.CmdTabPanel');
                    G_AppTimer.excute();
                }
      });
      //Ext.create('MyApp.Component.MonitorSwitch',100);
   },
   showLoginWindow:function() {
       Ext.create('MyApp.Component.LoginWindow','loginWindowShow_id');
   },
   showTips:function(msg) {
       Ext.create('MyApp.Component.LoadingTips',{lazytime:2000,msg:msg});
   },
   refresh: function() {
       Ext.getCmp('apptabs').refresh();
       Ext.getCmp('CarTreePanel_id').refreshTree();
       Ext.getCmp('CarControlPanel_id').getStore().load();
   }
});
