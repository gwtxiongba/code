/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListBaoxiao',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['id','user_id','start_time','order_id','car_id','time','create_time',
                'glf','jyf','byf','wxf','xcf','lqf','tcf','njf','bxf','qtf','fee','status','info1','info2','team_id',
                'name','plate'],
        sortInfo:{field:'id', direction:"DESC"}
    }
});

