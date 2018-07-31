/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.SaveWeibaoWindow',{
   extend:'Ext.window.Window',
   constructor:function() {
       var w = this;
       var ckModel = Ext.getCmp('WeibaoGridPanel_id').getCheckedModel();
       
       var title = ckModel ? '编辑维保' : '添加维保';

       var yearTime = Ext.create('Ext.ux.form.DateTimeField',{
           fieldLabel:'年检日期',
           format:'Y-m-d',
           allowBlank: false,
           width:200,
           labelWidth:70,
           labelAlign:'right',
           name:'year_time'
       });

       var keepTime = Ext.create('Ext.ux.form.DateTimeField',{
           fieldLabel:'保养日期',
           format:'Y-m-d',
           width:200,
           labelWidth:70,
           labelAlign:'right',
           name:'keep_time'
       });

       var safeTime = Ext.create('Ext.ux.form.DateTimeField',{
           fieldLabel:'保险日期',
           format:'Y-m-d',
           allowBlank: false,
           width:200,
           labelWidth:70,
           labelAlign:'right',
           name:'safe_time'
       });

       var formPanel = Ext.create('Ext.form.Panel',{
           layout: {
               type:'vbox',
               align:'center'
           },
           defaults:{
               xtype: 'textfield',
               width:200,
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
            },yearTime,
            keepTime,
           safeTime,{
                xtype:'hidden',
                name:'id'
                //value: key
                //disabled:!key
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
          width:285,
          height:200,
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
                        Ext.ajaxModelLoader('MyApp.Model.SaveWeibao', {
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

