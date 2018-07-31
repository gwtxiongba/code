/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SavePolygon',{
   extend:'MyApp.Base.Ajax',
   config:{
       method:'post',
       success:function() {
           var w = this;
           Ext.getCmp('gridpolygon_id').getStore().reload();
           w.close();
       }
   }
});

