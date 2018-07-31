/* 
 * To change this template, choose Tools | Templates
 * 操作员列表集
 */
Ext.define('MyApp.Model.Operators',{
   extend:'Ext.data.Store',
   constructor:function(){
       this.superclass.constructor.call(this,{
           autoLoad: true,
           autoSync: true,
           pageSize: 20,
           fields:['accountId','account','realName','address','phone','email','createTime','logintimes','visitTime'],
           proxy: {
               type: 'ajax',
               url: CRMApp.Configuration.servicePath + '?cmd=listOperator',
               reader: {
                   type : 'json',
                   root : 'result',
                   totalProperty: 'totalCount'
               }
           },
           listeners:{
               /*
                load:function(){
                    alert('hihih');
                }*/
            },
           sortInfo: {field:'createTime', direction:"DESC"}
       });
   }
});

