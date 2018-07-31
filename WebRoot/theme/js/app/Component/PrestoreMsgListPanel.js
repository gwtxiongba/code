/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.PrestoreMsgListPanel
 * 预存短信列表
 */
Ext.define('MyApp.Component.PrestoreMsgListPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'PrestoreMsgPanel_id'},
    constructor:function(){
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListPrestoreMsg');
    
        this.superclass.constructor.call(this,{
        	frame: true,
            border:true,
            region:'center',
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},
                {text:'短语内容', dataIndex: 'preMsgContent', sortable: false, menuDisabled:true, width:'90%'}
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



