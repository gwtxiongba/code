/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetAlarmCount',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'get',
        quiet:true,
        autoAbort:false
    },
    success:function(data){
        Ext.getCmp('alarms').setText('状态报警(' + data.total + ')');
    }
});

