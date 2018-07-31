/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AppTabPanel',{
   extend:'Ext.tab.Panel',
   constructor:function(){
      var isAlarm = MyApp.Base.LocalSet.getAlarmSwitch();
      var memoryTabs = MyApp.Base.LocalSet.getMemoryTabs();
      var g = MyApp.Base.LocalSet.getGolbal();
      var tabMenu = Ext.create('Ext.ux.TabCloseMenu', {
            closeTabText : '关闭当前页',
            closeOthersTabsText : '关闭其他页',
            closeAllTabsText : '关闭所有页'
        });
        
       var alarmbtn = Ext.create('Ext.button.Button',{
            iconCls: isAlarm ? 'alarm-on' : 'alarm-off'
       });

       this.superclass.constructor.call(this,{
          id:'apptabs',
          region:'center',
          layout:'fit',
          items:[
              {id:'tab_0', title:'监控视区', html:'<div id="mapDiv" style="width:100%">地图控件加载区...</div>',itemId:'mapview'}
          ],
          plugins: tabMenu,
          bbar:[
              {id:"onlines", text:'在线?(?%)'},{xtype: 'tbseparator'},
              {id:"offlines",xtype:'label',text:'不在线?(?%)'},{xtype: 'tbseparator'},
              {id:'alarms',text:'状态报警(0)',icon:Ext.BLANK_IMAGE_URL},{xtype: 'tbseparator'},
              {id:'limits',text:'围栏报警(0)',icon:Ext.BLANK_IMAGE_URL},{xtype: 'tbseparator'},
              {id:'orders',text:'最新预约(0)'},{xtype: 'tbseparator'},
              {id:'weibao',text:'到期提醒(0)', hidden:(g.visitor.levelId != 3)?true:false},
              {xtype: 'tbseparator'},
              alarmbtn
          ],
          listeners:{
              tabchange:function( tabPanel, newCard, oldCard){
                  if(newCard.getId() == 'tab_0') G_AppTimer.excute(true);
                  else G_AppTimer.stop();
              }
          }
       });
       
       Ext.ajaxModelLoader('MyApp.Model.GetAlarmCount').request();
       Ext.ajaxModelLoader('MyApp.Model.GetLimitCount').request();
       Ext.ajaxModelLoader('MyApp.Model.GetOrderCount').request();
       Ext.ajaxModelLoader('MyApp.Model.GetWeibaoEnd').request();
       Ext.getCmp('appmain').add(this);
       
       var tip1 = Ext.create('Ext.tip.ToolTip',{
            target: alarmbtn.el,
            html: '报警声音开启'
        });
        
	    var tip2 = Ext.create('Ext.tip.ToolTip',{
	        target: alarmbtn.el,
	        html: '报警声音关闭' 
        });
        if(isAlarm){
        	tip2.setDisabled(true);}
        else{
        	tip1.setDisabled(true);
        }
       var onlineb = Ext.getCmp('onlines');
       
       onlineb.on('click',function(){
       	var w = Ext.getCmp("VehicleOnlines_id");
        if(w){
          // w.close();
        }else
           Ext.create('MyApp.Component.VehicleOnlines');
           
       });
       var alarmb = Ext.getCmp('alarms');
       alarmb.on('click',function(){
       	var w = Ext.getCmp("AlarmListWindow_id");
        if(w){
           //w.close();
        }else
           Ext.create('MyApp.Component.AlarmListWindow');
       });
       var weibao = Ext.getCmp('weibao');
       
       weibao.on('click',function(){
       	var w = Ext.getCmp("AlarmListWindow_id");
        if(w){
           //w.close();
        }else
           Ext.create('MyApp.Component.WeibaoWarningWindow');
       });
       
       var limitsb = Ext.getCmp('limits');
       limitsb.on('click',function(){
          var w = Ext.getCmp('AlarmLimitWindow_id');
          if(!w) Ext.create('MyApp.Component.AlarmLimitWindow');
       });
       var orders = Ext.getCmp('orders');
       orders.on('click',function(){
      
          Ext.getCmp('apptabs').open({title:"最新预约",url:'newOrder.html'});
       });
       alarmbtn.on('click',function(){
           if(isAlarm) {
               this.setIconCls('alarm-off');
               isAlarm = false;
               tip1.setDisabled(true);
               tip2.setDisabled(false);
           }else {
               this.setIconCls('alarm-on');
               isAlarm = true;
               tip1.setDisabled(false);
               tip2.setDisabled(true);
           }
           MyApp.Base.LocalSet.setAlarmSwitch(isAlarm);
       });
       
       var apptabs = this;
       if(memoryTabs.length > 0) Ext.Msg.confirm('系统提示','是否恢复上一次的标签项',function(btn){
           if(btn == 'yes') {
               for(var i=0; i<memoryTabs.length; i++) {
                    apptabs.open(memoryTabs[i]);
                }
           } else MyApp.Base.LocalSet.clearMemoryTabs();
       });
       
   },
   open:function(item) {
       var tabitems = this.items.items;
       item.id = item.id ? item.id : 'tab_' + (parseInt(tabitems[this.items.length-1].id.split('_')[1])+1);
       var newid = MyApp.Base.LocalSet.setMemoryTab(item);
       var extTab = Ext.getCmp(newid);
       if(!extTab) this.add({
            id: item.id,
            title:item.title,
            closable: true,
            html:'<iframe id="mainViewers" src="'+ item.url +'" scrolling="no" width="100%" height="100%" frameborder="0"></iframe>',
            listeners:{
                'close':function(){
                    MyApp.Base.LocalSet.removeMemoryTab(this.id);
                }
            }
        });
        if(extTab&&item.auto) {
            var iframe = extTab.getEl().down('iframe');
            iframe.dom.src = item.url; 
        }
        this.setActiveTab(newid); 
   } ,
   refresh: function(){
       Ext.ajaxModelLoader('MyApp.Model.GetAlarmCount').request();
       Ext.ajaxModelLoader('MyApp.Model.GetLimitCount').request();
        Ext.ajaxModelLoader('MyApp.Model.GetOrderCount').request();
        Ext.ajaxModelLoader('MyApp.Model.GetWeibaoEnd').request();
       this.items.each(function(tab){
          if(tab.getId() != 'tab_0') tab.getEl().down('iframe').dom.contentWindow.location.reload();
       });
   }
});

