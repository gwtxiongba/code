/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.HistoryAlarm',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['driver','identifier','reason','reader','time','x','y'],
        sortInfo:{field:'time', direction:"DESC"}
    }
});

