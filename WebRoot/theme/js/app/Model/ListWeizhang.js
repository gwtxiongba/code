/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListWeizhang',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','plate','driverName','car_id','driver_id','foul_time','foul_address','foul_reason','foul_mark','foul_price'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

