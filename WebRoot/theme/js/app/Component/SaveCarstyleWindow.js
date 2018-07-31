/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.SaveCarstyleWindow',{
   extend:'Ext.window.Window',
   constructor:function(key) {
       var w = this;
       if(isNaN(key)) return;
       
       var title = key > 0 ? '编辑车型' : '添加车型';
       
       var formPanel = Ext.create('Ext.form.Panel',{
          layout:{
              type:'hbox',
              algin:'center',
              pack:'center'
          },
          items:[{
                xtype:'textfield',
                name:'name',
                width:220,
                allowBlank:false,
                labelWidth:70,
                labelAlign:'right',
                fieldLabel:'车型'
          },{
                xtype:'hidden',
                name:'id',
                value: key,
                disabled:!key
            }]
       });
       
       if(key > 0) {
           var rec = Ext.getCmp('CarstyleGridPanel_id').getSelectionModel().getSelection()[0];
           formPanel.getForm().setValues(rec.getData());
       }
       
       w.superclass.constructor.call(this,{
          width:285,
          height:100,
          title:title,
          modal:true,
          resizable:false,
          items:[formPanel],
          buttonAlign:'center',
          buttons:[{
             text:'提交',
             handler:function(btn) {
            	 if (!formPanel.getForm().isValid())
						return;
                 Ext.ajaxModelLoader('MyApp.Model.SaveCarstyle', {
                        target:w,
					    params:formPanel.getForm().getValues()						
                 }).request();
             }
          },{
              text:'取消',
              handler:function(btn) {
                  w.close();
              }
          }]
       });
       
       w.show();
   }
});

