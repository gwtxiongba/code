
Ext.define('MyApp.Component.ParkingCarListWindow',{
    extend: 'Ext.window.Window',
    config:{id:'ParkingCarListWindow_id'},
    constructor: function(){
        var w = this;
        
        var parkingCar_gp = Ext.create('MyApp.Component.ParkingCarListGridPanel');
        
        this.superclass.constructor.call(this,{
            title: '车库车辆列表',
            width: 400,
            height: 400,
            resizable: false,
            layout: 'border',
            //modal:true,
            items:[parkingCar_gp]
        });
        this.show();
    }
});



