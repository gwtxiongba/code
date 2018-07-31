/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.SaveWeizhangWindow',{
   extend:'Ext.window.Window',
   constructor:function() {
       var w = this;
       var ckModel = Ext.getCmp('WeizhangGridPanel_id').getCheckedModel();
       
       var title = ckModel ? '编辑违章' : '添加违章';

       var foul_time = Ext.create('Ext.ux.form.DateTimeField',{
           fieldLabel:'发生时间',
           format:'Y-m-d',
           width:260,
           labelWidth:70,
           labelAlign:'right',
           name:'foul_time'
       });

       var formPanel = Ext.create('Ext.form.Panel',{
           layout: {
               type:'vbox',
               align:'center'
           },
           defaults:{
               xtype: 'textfield',
               width:260,
               allowBlank: false,
               labelWidth: 70,
               labelAlign:'right',
               margin:'5 0'
           },
          items:[{
                xtype:'combo',
                fieldLabel:'车牌',
                name:'car_id',
                editable:false,
                displayField: 'plate',
                valueField: 'userId',
                store:Ext.ajaxModelLoader('MyApp.Model.ListPlateAll')
            },foul_time,
            //{
             // fieldLabel:'发生地点',
             // name:'foulAddress'
          //},
            {
              id:'happenAddress_address',
              fieldLabel:'发生地点',
              name:'foul_address',
              readOnly:true,
             //   grow:true,//大小是否可变
             // growMin:200,//在大小可变的情况下设置最小宽度
              listeners:{
                    focus:function(me,opts){
                        Ext.create('MyApp.Component.BaiduMapSelectWindow','happenAddress').setTitle('起始位置');
                        this.fireEvent('blur');
                    }
                }
            },
            {
                xtype:'combo',
                fieldLabel:'责任司机',
                name:'driver_id',
                editable:false,
                displayField: 'driverName',
                valueField: 'driverId',
                store:Ext.ajaxModelLoader('MyApp.Model.ListDriver',{params:{type:'flag'}})
            },{
              fieldLabel:'罚款金额',
              name:'foul_price',
              regex: /^\d+(\.\d{1,2})?$/,
              regexText: '只能输入整数或保留两位小数'
          },{
              fieldLabel:'扣分',
              name:'foul_mark',
              regex: /^\d+$/,
              regexText: '只能输入数字'
          },{
              fieldLabel:'违章原因',
              name:'foul_reason'
          },{
               xtype:'hidden',
               id:'happenAddress',
               name:'happenAddress'
          },{
                xtype:'hidden',
                name:'id'
//                value: key,
//                disabled:!key
            }]
       });
       
       if(ckModel) {
           formPanel.getForm().setValues(ckModel.getData());
//           Ext.getCmp('teams').setReadOnly(true);
//           Ext.getCmp('teams').setValue(ckModel.getData().teamName);
//           
//           var stt = Ext.ajaxModelLoader('MyApp.Model.ListDeptByTeamId',{
//               params:{teamId: ckModel.getData().team_id}});
//               
//           Ext.getCmp("dept").bindStore(stt); 
//           Ext.getCmp("dept").setValue(ckModel.getData().dept_id);
       }
       
       w.superclass.constructor.call(this,{
          width:305,
          height:300,
          title:title,
          modal:true,
          resizable:false,
          items:[formPanel],
          buttonAlign:'center',
          buttons:[{
                text:'提交',
                handler:function(btn) {
                    var formData = formPanel.getForm();
                    if(formData.isValid()){
                        Ext.ajaxModelLoader('MyApp.Model.SaveWeizhang', {
                            params:formData.getValues(),
                            target : w
                        }).request();
                    }
                }
            },{
                text:'取消',
                handler:function(btn){
                    w.close();
                }
            }]
       });
       
       w.show();
   }
});

