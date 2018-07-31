/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MsgSessionInforGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'MsgSessionInfor_id'},
    constructor:function(){
        var gp = this;
        
        var msgModel = Ext.getCmp('MsgSessionGrid_id').getCheckedModel();
        var carId = msgModel.get('carId');
        var msgStore = Ext.ajaxModelLoader('MyApp.Model.GetMsgSession',{params:{carId:carId}});
        
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
                {text:'调度内容', dataIndex: 'content', sortable: false, menuDisabled:true, width:'45%'},
                {text:'发送时间', dataIndex: 'createTime', sortable: false, menuDisabled:true, width:'45%'}
            ],
            bbar: Ext.create('Ext.PagingToolbar', {
				store: msgStore,
				displayInfo: true
	   	    })
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});

