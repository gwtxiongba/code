/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.NewOrder',{
    extend:'MyApp.Base.Page',
    onInit: function(){
        Ext.create('MyApp.Component.NewOrderGridPanel');
     //alert(Ext.getCmp('NewOrderGridPanel_id'));
    }
})

