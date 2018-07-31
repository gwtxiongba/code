/* 
 * To change this template, choose Tools | Templates
 * 获得机构中车辆与路径的关系
 */
Ext.define('MyApp.Model.GetLimitLines',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['identifier', 'path', 'createTime'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

