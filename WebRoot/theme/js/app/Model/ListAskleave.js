/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListAskleave',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','user_id','start_time','end_time','reason','status','create_time','reason2','name','tel'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

