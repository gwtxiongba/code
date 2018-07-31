/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.StatsDateMF',{
   extend:'MyApp.Base.AjaxModel',
   config:{
       fields:['date','plate','mileage','fuel','overspeedhours','acctimes','sleepytimes']
   }
});

