/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.FleetGridPanel
 */
Ext.define('MyApp.Component.CarListPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'CarListPanel_id'},
    constructor:function(){
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListCarInfor');
    
        var selModel = Ext.create('Ext.selection.CheckboxModel',{
            injectCheckbox:1
        });
       
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            //width:'100%',
            //maxWidth:'100%',
            autoScroll: false,
            selModel:selModel,
            scroll:'vertical',
            store:store,
            columns: [
                {text:'车牌号码', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'15%'},
                {text:'车牌号码', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'35%'},
                {text:'设备识别码', dataIndex:'identifier', sortable: false, menuDisabled:true, width:'30%'}
            ],
            bbar: Ext.create('Ext.PagingToolbar', {
                store : store,
                displayInfo : true
            })
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});



