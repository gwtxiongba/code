/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveDriver',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            var w = this;
            w.close();
            Ext.getCmp('DriverGridPanel_id').getStore().reload();
        }
    }
});