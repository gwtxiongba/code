/* 
 * To change this template, choose Tools | Templates
 * 新建、修改操作员弹窗
 */
Ext.define('MyApp.Component.SaveMenuWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        
        var menuName = {
            xtype: 'textfield',
            blankText: '请输入菜单名',
            maxLength:20,
            fieldLabel : '菜单名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'menuName'
        }
        
        var url = {
            xtype: 'textfield',
            blankText: '请正确输入路径',
            maxLength:20,
            fieldLabel : '路径',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'url'
        }
         var menuId = {
            id:'parentId',
            xtype: 'textfield',
            blankText: '请正确输入路径',
            maxLength:20,
            fieldLabel : '路径',
            labelAlign:'right',
            redStar:true,
            hidden:true,
            name:'parentId'
        }
       
        var parent = {
            xtype : 'combo',
            id:'leavel',
            fieldLabel:'选择父级',
            blankText:'请选择父级',
            displayField : "menuName",
            valueField : "menuId",
            labelAlign : 'right',
            editable : false,
            selectOnFocus : true,
            typeAhead : true,
            triggerAction : 'all',
            name:'parentId',
            redStar:true,
            allowBlank:false
           
        }
        
        
        var buttons = {
            xtype: 'textfield',
            maxLength:20,
            fieldLabel : '按钮',
            labelAlign:'right',
            allowBlank : true,
            name:'address'
        }
      
       
      
        
        var formPanel = Ext.create('Ext.form.Panel',{
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items :[menuName,url,menuId,buttons]
        });
        
      
        this.superclass.constructor.call(this,{
           title: '添加菜单',
           width:300,
           height: 350,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[formPanel],
           buttonAlign:'center',  
           buttons:[{
                text:'提交',
                handler:function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.SaveMenu',{
                        params:formPanel.getForm().getValues(),
                        target: w
                    }).request();
                }
            },{
                text: '退出',
                handler:function(btn) {
                  // alert( formPanel.getForm().teamId);
                   formPanel.getForm().reset();
                   w.hide();        
                }
            }]
        });
        this.show();
    },
    setValue:function(vehid){
        Ext.getCmp('parentId').setValue(vehid);
    }
});

