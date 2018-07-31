/* 
 * To change this template, choose Tools | Templates
 * 获取所辖权限的车队分组
 */
Ext.define('MyApp.Model.GetTeams',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['teamId', 'teamName'],
        sortInfo:{field:'teamId', direction:"ASC"}
    }
});