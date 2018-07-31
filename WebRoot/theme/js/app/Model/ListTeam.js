/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListTeam',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['teamId','teamName','amount','order','createTime'],
        sortInfo:{field:'createTime', direction:"ASC"}
    }
});

