/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */ 
Ext.define('MyApp.Model.ListOpTeam',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['teams','operatorId','operatorName'],
        sortInfo:{field:'teamId', direction:"ASC"}
    }
});