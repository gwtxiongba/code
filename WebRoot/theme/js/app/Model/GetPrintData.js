/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetPrintData',{
    extend: 'MyApp.Base.Ajax',
    config: {
       method:'get',
       success:function(o){
           for(var p in o) {
               var domEl = document.getElementById(p);
               if(domEl) domEl.innerHTML = o[p];
           }
       },
       failtrue:function(){
           Ext.create('MyApp.Component.LoadingTips',{
                lazytime:2000,
                msg: '请求操作失败，请重新尝试'
            });
       }
    }
});

