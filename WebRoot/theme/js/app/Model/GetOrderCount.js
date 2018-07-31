/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetOrderCount',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method:'get',
        quiet:true,
        autoAbort:false
    },
    success:function(data){
        Ext.getCmp('orders').setText('最新预约(' + data.total + ')');
        if(data.total>0){
        	//Ext.getCmp('orders').setIconCls('alarm-off');
        	Ext.getCmp('orders').setIcon('./theme/imgs/icons/tishi.png');// Ext.BLANK_IMAGE_URL
        }else{
        Ext.getCmp('orders').setIcon(Ext.BLANK_IMAGE_URL);// Ext.BLANK_IMAGE_URL
        }
        if(Ext.getCmp('NewOrderGridPanel_id'))
         Ext.getCmp('NewOrderGridPanel_id').getStore().reload();
    }
});

