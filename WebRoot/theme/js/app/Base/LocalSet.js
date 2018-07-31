/* 
 * To change this template, choose Tools | Templates
 * 本地系统参数持久化
 */
Ext.define('MyApp.Base.LocalSet',{
   statics:{
       cp:Ext.create('Ext.state.CookieProvider', CRMApp.Configuration.CookieSet),
       //登录信息
       setGolbal : function(o) {
           this.cp.set('golbal',o);
       },
       getGolbal : function() {
           var o = this.cp.get('golbal');
           return o ? o : null;
       },
       clearGolbal : function() {
           this.cp.clear('golbal');
       },
       //每页显示数量
       setPageSize : function(v) {
           var account = this.getGolbal().visitor.account;
           var s = this.cp.get('localset');
           if(!s) s = {};
           if(!s[account]) s[account] = {};
           s[account].pageSize = v;
           this.cp.set('localset',s);
       },
       getPageSize : function() {
           var account = this.getGolbal().visitor.account;
           var set = this.cp.get('localset');
           if(set && set[account] && set[account].pageSize) return set[account].pageSize;
           else return 20;
       },
       //报警开关状态
       setAlarmSwitch : function(status) {
           var account = this.getGolbal().visitor.account;
           var s = this.cp.get('localset');
           if(!s) s = {};
           if(!s[account]) s[account] = {};
           s[account].isAlarm = status;
           this.cp.set('localset',s);
       },
       getAlarmSwitch : function() {
           var account = this.getGolbal().visitor.account;
           var set = this.cp.get('localset');
           if(set && set[account] && set[account].isAlarm != undefined) return set[account].isAlarm;
           else return true;
       },
       //标签页打开状态
       setMemoryTab : function(o) {
           var account = this.getGolbal().visitor.account;
           var s = this.cp.get('localset');
           if(!s) s = {};
           if(!s[account]) s[account] = {};
           if(!s[account].tabs) s[account].tabs = [];
           for(var i=0; i< s[account].tabs.length; i++) {
               if(o.url == s[account].tabs[i].url) return s[account].tabs[i].id;
           }
           s[account].tabs.push(o);
           this.cp.set('localset',s);
           return o.id;
       },
       getMemoryTabs : function() {
           var account = this.getGolbal().visitor.account;
           var set = this.cp.get('localset');
           if(set && set[account] && set[account].tabs != undefined) return set[account].tabs;
           else return [];
       },
       removeMemoryTab : function(tabid) {
           var account = this.getGolbal().visitor.account;
           var set = this.cp.get('localset');
           for(var i = 0; i < set[account].tabs.length; i++) {
               if(set[account].tabs[i].id == tabid) {
                   set[account].tabs.splice(i,1);
                   break;
               }
           }
           this.cp.set('localset', set);
       },
       clearMemoryTabs : function() {
           var account = this.getGolbal().visitor.account;
           var set = this.cp.get('localset');
           if(set[account].tabs.length > 0) {
                set[account].tabs = [];
                this.cp.set('localset', set);   
            }
       }
       
   } 
});

