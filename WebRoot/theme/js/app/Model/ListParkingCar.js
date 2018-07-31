Ext.define('MyApp.Model.ListParkingCar',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['vehicleId','identifier','plate','type','driver','createTime','updateTime'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

