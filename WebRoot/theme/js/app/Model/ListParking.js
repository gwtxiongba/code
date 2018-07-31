/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListParking',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['parkingId','adminName','parkingPosition','parkingTel','driver','x','y'],
        sortInfo:{field:'parkingId', direction:"DESC"}
    }
});
