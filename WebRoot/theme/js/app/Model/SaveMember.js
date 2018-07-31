/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveMember',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            var w = this;
            w.close();
            Ext.getCmp('MemberGridPanel_id').getStore().reload();
             top.Ext.getCmp('DeptTreePanel_id').refreshTree();
        }
    }
});