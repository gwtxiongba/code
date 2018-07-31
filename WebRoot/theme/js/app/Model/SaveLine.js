/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveLine',{
   extend:'MyApp.Base.Ajax',
   config:{
       method:'post',
       success:function() {
           var w = this;
           Ext.getCmp('gridline_id').getStore().reload();
           w.close();
       }
   }
});

