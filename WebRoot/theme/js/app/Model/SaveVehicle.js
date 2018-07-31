/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveVehicle',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            var w = this;
            w.close();
//            if(Ext.getCmp('VehicleGridPanel_id')){
//            	Ext.getCmp('VehicleGridPanel_id').getStore().reload();
//            }else{
//            	Ext.create('MyApp.Component.VehicleGridPanel');
//            }
            Ext.getCmp('VehicleGridPanel_id').getStore().reload();
            top.Ext.getCmp('CarTreePanel_id').refreshTree();
        }
    }
});