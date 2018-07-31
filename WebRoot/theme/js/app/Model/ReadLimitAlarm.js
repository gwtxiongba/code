/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ReadLimitAlarm',{
    extend:'MyApp.Base.Ajax',
    config:{
        method:'get',
        quiet:true,
        autoAbort:false
    }
});
