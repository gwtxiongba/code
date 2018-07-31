/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetLines',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['lineId', 'lineName', 'distance', 'path','radius','createTime'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});