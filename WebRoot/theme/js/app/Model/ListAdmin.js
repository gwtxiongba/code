/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListAdmin',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['accountId','account','accountRealName','realname','address','phone','email','createTime','logintimes','visitTime'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

