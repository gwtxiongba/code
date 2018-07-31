/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MsgSessionGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'MsgSessionGrid_id'},
    constructor:function(){
        var gp = this;
        
        var msgStore = Ext.ajaxModelLoader('MyApp.Model.ListMsgSession');
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            region:'center',
            width:'100%',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:msgStore,
            columns: [
                {xtype: 'rownumberer'},
                //{xtype: 'checkcolumn', dataIndex:'accountId',sortable: false, menuDisabled:true, width: '5%', text:'全选'},
                {text:'车牌', dataIndex: 'plate', sortable: false, menuDisabled:true, width:'25%'},
                {text:'调度内容', dataIndex: 'content', sortable: false, menuDisabled:true, width:'45%'},
                {text:'发送时间', dataIndex: 'createTime', sortable: false, menuDisabled:true, width:'25%'}
            ],
            bbar: Ext.create('Ext.PagingToolbar', {
				store: msgStore,
				displayInfo: true
	   	    }),
	   	    listeners:{
	   	    	'itemdblclick': function(grid, rowIndex, e) {
	   	    		var model = Ext.getCmp('MsgSessionGrid_id').getSelectionModel().getSelection()[0];
	   	    		Ext.create('MyApp.Component.MsgSessionInforWindow').setTitle(model.get('plate'));
	   	    	}
	   	    }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});

