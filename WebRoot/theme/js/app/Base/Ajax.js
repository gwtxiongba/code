/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Base.Ajax',{
    config:{
        method: 'get',
        quiet: false,
        target: null,
        autoAbort:true,
        params:{},
        success: function(o) {},
        failture: function(c,m) {Ext.Msg.alert('系统提示', m)},
        getCls : function(){}
    },
    constructor: function(config){
        this.initConfig(config);
    },
    done: function(o){},
    request: function(fn) {
        var _this = this;
        var cmd = this.getCls().split('.')[2].toCapitalLowerCase();
        var url = CRMApp.Configuration.servicePath + '?cmd=' +cmd;
        var mk = new Ext.LoadMask(Ext.getBody(), {msg: '数据请求中，请稍候！',  removeMask: true}); 
        if(!this.quiet) mk.show();
        Ext.Ajax.request({
            url : url,
            params:_this.params,
            method:_this.method,
            autoAbort:_this.autoAbort,
            success: function(resp) {
                mk.hide();
                try{
                    var obj = Ext.decode(resp.responseText);
                    
                    if(obj.code) {
                        if(MyApp.Config.Error.No_ACCESS == obj.code){
                            window.top.page.showLoginWindow();
                            return;
                        }
                        _this.failture.call(_this.target,obj.code,obj.data);
                        return;
                    } else _this.success.call(_this.target,obj);
                }catch(ex){
                    if(CRMApp.Configuration.debug_on) 
                        console.log('error format output:' + resp.responseText);
                   // else Ext.Msg.alert('系统提示','服务返回数据异常');
                }
            },
            failture: function(resp) {
                mk.hide();
                Ext.create('MyApp.Component.LoadingTips',{laytime:5000,msg:Ext.util.JSON.decode(resp.responseText).error});    
            }
        });
    }
});

