/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListVehicle',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['vehicleId','identifier','plate','teamId','teamName','type','typeId','discc','ifRelay','cutpwd','brand','createTime','updateTime','regTime','buyTime','startPrice','userNumber','kmPrice','lowPrice','driverTel','driverName','online','lineGps','carModel','tel','driverId'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

