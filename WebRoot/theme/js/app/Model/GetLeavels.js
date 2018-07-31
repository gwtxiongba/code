/* 
 * To change this template, choose Tools | Templates
 * 获取所辖权限的车队分组
 */
Ext.define('MyApp.Model.GetLeavels',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['levelId', 'levelName'],
        sortInfo:{field:'levelId', direction:"ASC"}
    }
});