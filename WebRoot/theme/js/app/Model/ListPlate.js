/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListPlate',{
    extend:'MyApp.Base.AjaxModel',
 
    config:{
       
        fields:['userId','plate'],
        sortInfo:{field:'plate', direction:"DESC"},
        pageSize:0
    }
});

