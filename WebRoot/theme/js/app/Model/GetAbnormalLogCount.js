/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetAbnormalLogCount',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'get',
        quiet:true,
        autoAbort:false
    },
    success:function(data){
        Ext.getCmp('driver_log').setText('考勤报警(' + data.total + ')');
    }
});

