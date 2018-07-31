/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.StatsTabPanel',{
   extend:'Ext.tab.Panel',
   config:{
       tabcache:[] //标签表
   },
   constructor:function(){
       var tabMenu = Ext.create('Ext.ux.TabCloseMenu', {
            closeTabText : '关闭当前页',
            closeOthersTabsText : '关闭其他页',
            closeAllTabsText : '关闭所有页'
        });
        
        this.superclass.constructor.call(this,{
            id:'statstabs',
            region:'center',
            layout:'fit',
            items:[],
            plugins: tabMenu,
            listeners:{
                
            }
        });
        
        Ext.getCmp('statsmain').add(this);
   },
    open:function(item) {
       var tabitems = this.items.items;
       this.add({
          title:item.title,
          closable: true, 
          html:'<iframe src="'+ item.url +'" scrolling="no" width="100%" height="100%" frameborder="0"></iframe>'
          //loader:{url:item.url,loadMsg:'努力加载中...',autoLoad:true,scripts:true}
       });
       this.setActiveTab(tabitems.length-1);
   }
});

