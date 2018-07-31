/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListOrder',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','carUserName','beginTime','endTime','beginAddr','endAddr','carUserTel','status','reason','plate','takes','caruse','carStyle','brand','remark','driverName','driverTel','pnumber','cost','miles','startTime','overTime'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

