/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AudioPlayer',{
   constructor:function(){
       var player = top.Ext.get('audio');
       if(!player) {
           top.Ext.DomHelper.append(Ext.getBody().dom,'<audio id="audio" autoplay="1" src="#"></audio>');
           player = top.Ext.get('audio');
       }
       
       this.play = function(src) {
          if(!MyApp.Base.LocalSet.getAlarmSwitch()) return;
           player.set({src:src});
           player.on('ended',function(){
               player.set({src:'#'});
           });
       }
   } 
});

