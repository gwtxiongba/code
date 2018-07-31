/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */ 
Ext.define('MyApp.Model.ListTeamsAndOperator',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['teamId','teamName','operatorId'],
        sortInfo:{field:'order', direction:"ASC"}
    }
});

