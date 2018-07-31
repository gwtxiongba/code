/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MessageTriger',{
   requires : 'MyApp.Base.WebSocket',
   constructor:function(){
       var cmdPanel = null;
       var o = MyApp.Base.LocalSet.getGolbal();
      // var audio = Ext.create('MyApp.Component.AudioPlayer');
      console.log('WebSocket ');
       Ext.create('MyApp.Base.WebSocket',{
          success:function(o){
           console.log('WebSocket '+o.type);
              switch(parseInt(o.type)) {
                  case 1:{//消息提醒
                      break;
                  }
                  case 2:{//状态报警提醒
                      Ext.ajaxModelLoader('MyApp.Model.GetAlarmCount').request();
                      var alarmPanel = Ext.getCmp('AlarmListPanel_id');
                      if(alarmPanel) alarmPanel.getStore().load();
                     // audio.play('theme/audio/120.wav');
                      
                      //添加指令列表
                      cmdPanel = Ext.getCmp('ComInfoPanel_id');
                      if(cmdPanel) cmdPanel.addCmdInfo();
                      break;
                  }
                  case 3:{//刷卡提醒
                	  Ext.create('MyApp.Component.LoadingTips',{
                          lazytime:2000,
                          msg: '消息发送成功！'
                       });
                   	  break;
                  }
                  case 4:{//断油电
                  	  cmdPanel = Ext.getCmp('CarTreePanel_id');
                  	  var selectPanel = Ext.getCmp('CarSelectPanel_id');
                  	  var controPanle = Ext.getCmp('CarControlPanel_id');
                  	  selectPanel.getStore().load();
                  	  controPanle.getStore().load();
                  	  cmdPanel.refreshTree();
                  	  
                  	  Ext.create('MyApp.Component.LoadingTips',{
                         lazytime:2000,
                         msg: '油电操作成功！'
                      });
                  	  break;
                  }
                  case 5:{//断油电执行命令失败
                  	  Ext.create('MyApp.Component.LoadingTips',{
                              lazytime:2000,
                              msg: o.data
                          });
                  	  break;
                  }


                  case 6: {//围栏报警
                          Ext.ajaxModelLoader('MyApp.Model.GetLimitCount').request();
                          var panel = Ext.getCmp('LimitListPanel_id');
                          if(panel) panel.getStore().load();
                         // audio.play('theme/audio/120.wav');
                          break;
                  }
                  case 10: {//删除车辆
                       if(G_MonitMap.remove) G_MonitMap.remove(o.data);
                  }
                  
                  case 11: {
                   var a = document.getElementById('mainViewers').contentWindow.Ext;
                   a.getCmp('NewOrderGridPanel_id').getStore().reload();
                  Ext.ajaxModelLoader('MyApp.Model.GetOrderCount').request();
                   break;
                  }
                  case 12: {
                   var a = document.getElementById('mainViewers').contentWindow.Ext;
                   a.getCmp('MemberNewGridPanel_id').getStore().reload();
                 // Ext.ajaxModelLoader('MyApp.Model.GetOrderCount').request();
                   break;
                  }
                  case 13: {
                   var a = document.getElementById('mainViewers').contentWindow.Ext;
                   a.getCmp('DriverNewGridPanel_id').getStore().reload();
                 // Ext.ajaxModelLoader('MyApp.Model.GetOrderCount').request();
                   break;
                  }
                  default:;
              } 
          }
      }).addCompanyListener(o.appid,o.company.id);
   }
});

