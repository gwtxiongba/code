/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DoScan',{
   extend: 'MyApp.Base.Ajax',
   config:{
        method:'post',
        quiet:true,
        autoAbort:true
    }
});

