/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ReportCar',{
    extend:'MyApp.Base.AjaxModel',
    config:{
//        fields:['brand','mark']
    	fields:['id','plate','mile','fee','time']
    }
});

