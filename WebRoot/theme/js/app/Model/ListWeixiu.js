/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListWeixiu',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','plate','fee','time','reason','status','reason2','name','tel'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

