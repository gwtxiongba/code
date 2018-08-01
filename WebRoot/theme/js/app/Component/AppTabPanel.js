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
          listeners:{
              tabchange:function( tabPanel, newCard, oldCard){
                  if(newCard.getId() == 'tab_0') G_AppTimer.excute(true);
                  else G_AppTimer.stop();
              }
          }
       });
       
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
      
       var apptabs = this;
       
       
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
       this.items.each(function(tab){
          if(tab.getId() != 'tab_0') tab.getEl().down('iframe').dom.contentWindow.location.reload();
       });
   }
});

