/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListBaseInfo',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['baseInfoId','brand','weight','totalNumber','prcTime','limTime','user'],
        sortInfo:{field:'baseInfoId', direction:"DESC"}
    }
});
