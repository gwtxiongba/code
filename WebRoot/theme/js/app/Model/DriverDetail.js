/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DriverDetail',{
    extend:'MyApp.Base.AjaxModel',
    config:{
		 fields:['vehicleId','identifier','plate','type','driver'],
	     sortInfo:{field:'vehicleId', direction:"DESC"}
        
    }
});

