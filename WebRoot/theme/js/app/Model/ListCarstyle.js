/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListCarstyle',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','name'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

