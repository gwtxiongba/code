/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.UpdateOpTeam',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post', 
        success: function() {
            var w = this;
            w.close();
            Ext.getCmp('OpFleetGridPanel_id').getStore().load();
        }
    }
});