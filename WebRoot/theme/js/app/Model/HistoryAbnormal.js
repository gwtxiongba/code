/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.HistoryAbnormal',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['driver','identifier','reason','reader','time'],
        sortInfo:{field:'time', direction:"DESC"}
    }
});

