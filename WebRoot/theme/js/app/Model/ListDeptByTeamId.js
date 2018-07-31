/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListDeptByTeamId',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['deptId','deptName','teamId'],
        sortInfo:{field:'deptId', direction:"ASC"}
    }
});

