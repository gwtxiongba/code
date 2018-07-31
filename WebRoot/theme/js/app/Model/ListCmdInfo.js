/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListCmdInfo',{
    extend:'MyApp.Base.AjaxModel',
    config:{
    	 fields:['vehicleId','identifier','plate','type','reason','ifRead','time','createTime','x','y','address'],
         sortInfo:{field:'createTime', direction:"DESC"}
    }
});

