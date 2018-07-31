/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveOperator',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            var w = this;
            w.close();
            if(Ext.getCmp('OperatorGridPanel_id')){
            	Ext.getCmp('OperatorGridPanel_id').getStore().reload();
            }
            if(Ext.getCmp('AdministratorGridPanel_id')){
            	Ext.getCmp('AdministratorGridPanel_id').getStore().reload();
            }
            if(Ext.getCmp('OpFleetGridPanel_id'))
            Ext.getCmp('OpFleetGridPanel_id').getStore().reload();
        }
    }
});

