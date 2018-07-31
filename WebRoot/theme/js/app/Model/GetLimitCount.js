/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetLimitCount',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'get',
        quiet:true,
        autoAbort:false
    },
    success:function(data){
        Ext.getCmp('limits').setText('围栏报警(' + data.total + ')');
    }
});

