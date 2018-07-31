/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.EditOrder',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            var w = this;
            w.close();
            Ext.getCmp('NewOrderGridPanel_id').getStore().reload();
            
             // top.Ext.getCmp('CarTreePanel_id').refreshTree();
        }
    }
});