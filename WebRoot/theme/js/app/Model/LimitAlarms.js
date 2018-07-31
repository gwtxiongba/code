/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.LimitAlarms',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['limitId','plate','identifier','gps','ifRead','paths','areas']
   }
});

