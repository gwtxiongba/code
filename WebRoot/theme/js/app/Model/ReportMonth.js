/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ReportMonth',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['mon','obj','mile','fee','time','obj_id'],
        //sortInfo:{field:'mon', direction:"DESC"}
    }
});

