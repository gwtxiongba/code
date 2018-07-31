/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetMsgSession',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['carId','msgId','content','createTime','ifRead','sendFlag'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

