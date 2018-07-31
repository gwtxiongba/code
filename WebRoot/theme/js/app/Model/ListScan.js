/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListScan',{
   extend:'Ext.data.Store',
   config:{
      fields:['vehicleId','identifier','plate','codeCounts']
   },
   constructor:function(){
       Ext.ajaxModelLoader('MyApp.Model.DoScan',{
          success:function(data) {
              console.log(Ext.encode(data));
          }
       });
       
   }
});
