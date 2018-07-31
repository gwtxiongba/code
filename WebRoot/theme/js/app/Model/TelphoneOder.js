/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.TelphoneOder',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['identifier','iccid','phoneno'],
        sortInfo:{field:'identifier', direction:"DESC"}
    }
});

