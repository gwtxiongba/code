/* 
 * To change this template, choose Tools | Templates
 * 操作员列表集
 */
Ext.define('MyApp.Model.ListCarUser',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['teamName','cars'],
        sortInfo:{field:'teamName', direction:"ASC"}
    }
});

