/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.ParkingCarListGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'ParkingCarListGridPanel_id'},
    constructor:function(){
        var gp = this;
        var checked = Ext.getCmp('ParkingGridPanel_id').getCheckedModel();
        var driverId = checked.get('driver').driverId;
        var store = Ext.ajaxModelLoader('MyApp.Model.ListParkingCar',{params:{driverId:driverId}});
        this.superclass.constructor.call(this,{
          	frame: true,
            border:true,
            region: 'center',
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns: [
            	{xtype: 'rownumberer', text:'序', width:'6%'},
                {text:'设备码', dataIndex: 'identifier', sortable: false, menuDisabled:true, width:'31%'},
                {text:'车牌', dataIndex:'plate', sortable: false, menuDisabled:true, width:'31%'},
                {text:'车型', dataIndex:'type', sortable: false, menuDisabled:true, width:'31%',renderer:function(v) { return v ? MyApp.Config.Golbal.VehicleTypes[v-1] : '未录入'}}
            ],
          	 bbar:[{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }]
            });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



