/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetCircles',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['areaId', 'areaName', 'area', 'createTime'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});