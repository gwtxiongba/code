/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListCarInfor',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['Id','plate','identifier'],
        sortInfo:{field:'Id', direction:"ASC"}
    }
});

