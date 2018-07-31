/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListOperator',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['accountId','account','realname','address','phone','email','createTime','logintimes','visitTime','teamName','teamId','deptId','deptName','levelId','levelName'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

