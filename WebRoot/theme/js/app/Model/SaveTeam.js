/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveTeam',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            var w = this;
            w.close();
            Ext.getCmp('FleetGridPanel_id').getStore().reload();
               top.Ext.getCmp('CarTreePanel_id').refreshTree();
                top.Ext.getCmp('DeptTreePanel_id').refreshTree();
              
        }
    }
});