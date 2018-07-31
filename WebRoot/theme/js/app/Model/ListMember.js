/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListMember',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','user_name','name','tel','team_id','leavel','if_del','status','create_time','dept_id','teamName','deptName','warning'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

