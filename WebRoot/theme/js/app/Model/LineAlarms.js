/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.LineAlarms',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['limitId','plate','identifier','gps','ifRead','paths']
   }
});

