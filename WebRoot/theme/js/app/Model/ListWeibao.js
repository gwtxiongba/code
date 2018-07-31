/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListWeibao',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','plate','car_id','year_time','keep_time','safe_time'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

