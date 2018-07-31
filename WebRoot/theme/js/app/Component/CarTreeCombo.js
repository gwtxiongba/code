/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.CarTreeCombo',{
    extend:'Ext.ux.TreeCombo',
    constructor:function(){
        this.superclass.constructor.call(this,{
            xtype: 'treecombo',
            editable:false,
            emptyText:'选择车辆',
            displayField: 'text',
            fields: ["id", "text","uid","checked"],
            valueField: 'id',
            width: 200,
            url: CRMApp.Configuration.servicePath + '?cmd=formTeamsCar',
            id: "tc"
        });
        
    }
});

