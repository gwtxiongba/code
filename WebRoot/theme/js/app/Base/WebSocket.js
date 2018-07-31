/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Base.WebSocket',{
    requires:'MyApp.socket_io',
    config:{
        url:'http://120.25.195.12:3000',
        success:function(o){}
    },
    constructor:function(opts){
        var websocket = this;
        this.initConfig(opts);
        var socket = io.connect(this.url);
        socket.on('encodekey',function(key){
            socket.on(key,function(o){websocket.success(o)});
        });
        
        this.addCompanyListener = function(appid,companyId) {
            socket.emit('listener',{
                appid:appid,
                companyId:companyId
            });
        }
        
        this.addAccountListener = function(appid,account) {
            socket.emit('listener',{
                appid:appid,
                account:account
            });
        }
    }
});

