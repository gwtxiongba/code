/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.CarAndDriver',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['user_id','identifier','plate','type','driver','createTime','updateTime'],
        sortInfo:{field:'createTime', direction:"DESC"}
    }
});

