/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DateTrace',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'get'
    }/*,
    success:function(data){
        var player = this;
        player.ready(data);
        
        
       
        var panel = this;
        var traceMap = this.getTraceMap();
        traceMap.drawTrace(packData(data));
        setTimeout(function(){panel.playReady()},1000);
       
    }*/
});

