/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListPrestoreMsg',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['preMsgId','preMsgContent'],
        sortInfo:{field:'preMsgId', direction:"ASC"}
    }
});

