/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListMsgSession',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['carId','msgId','plate','identifier','content','createTime','ifRead'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

