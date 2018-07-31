/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Base.AjaxModel',{
    config:{
        fields: [],
        sortInfo: {},
        params:{},
        pageSize: MyApp.Base.LocalSet.getPageSize(),
        getCls:function(){}
    },
    constructor:function(config){
       this.initConfig(config);
       var cmd = this.getCls().split('.')[2].toCapitalLowerCase();
       var vks = []
       for(var key in this.params) {
           vks.push(key+'='+this.params[key]);
       }
       var vkquery = vks.join('&');
       return Ext.create('Ext.data.Store',{
           autoLoad: true,
           autoSync: false,
           pageSize: this.pageSize,
           fields : this.fields,
           proxy:{
               type: 'ajax',
               url:CRMApp.Configuration.servicePath + '?cmd=' + cmd + (vkquery ? '&'+vkquery : ''),
               extractResponseData: function(data) {
               
                   try {
                       var resp = Ext.decode(data.responseText);
                      
                       if(typeof(resp.code) == 'undefined') return data;
                       if(MyApp.Config.Error.No_ACCESS == resp.code){
                       
                           window.top.page.showLoginWindow();
                       } else Ext.Msg.alert('系统提示',resp.data);;
                   } catch(ex) {
                    //alert(ex);
                       if(CRMApp.Configuration.debug_on) console.log('error format output:' + data.responseText);
                       else Ext.Msg.alert('系统提示','服务返回数据异常');
                   }
               },
               reader:{
                   type : 'json',
                   root : 'result',
                   totalProperty: 'totalCount'  
               },
               listeners:{
                   exception:function(proxy, resp, op, opts ) {
                     //  Ext.Msg.alert('系统提示',resp);
                   }
               }
           },
           listeners:{
               load:function(){
               	this.each(function(m){
                	    //console.log(m.get('createTime'));
                });
                   //console.log('cmd:' + cmd);
               }
           },
           sortInfo:this.sortInfo
       });
    }
});

