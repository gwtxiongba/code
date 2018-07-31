/* 
 * To change this template, choose Tools | Templates
 * 操作员列表集
 */
Ext.define('MyApp.Model.ListCarDriver',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['carDriverId', 'car','driver','startTime','endTime'],
        sortInfo:{field:'timeInterval', direction:"ASC"}
    }
});

