/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveCircle',{
   extend:'MyApp.Base.Ajax',
   config:{
       method:'post',
       success:function() {
           var w = this;
           Ext.getCmp('gridcircle_id').getStore().reload();
           w.close();
       }
   }
});

