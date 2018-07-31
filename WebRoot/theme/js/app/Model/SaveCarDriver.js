/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveCarDriver',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
        	 var w = this;
             w.close();
             Ext.getCmp('CarDriverGridPanel_id').getStore().reload();
        }
    }
});