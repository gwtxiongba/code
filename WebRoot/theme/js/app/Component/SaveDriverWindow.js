/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this template, choose Tools | Templates
 * 新建、修改车辆弹窗
 */
Ext.define('MyApp.Component.SaveDriverWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        var ckModel = Ext.getCmp('DriverGridPanel_id').getCheckedModel();
        
        var driverName = {
            xtype: 'textfield',
            blankText: '请输入司机姓名',
            maxLength:20,
            fieldLabel : '司机名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
           // disabled: !!ckModel,
           // hidden: !!ckModel,
            name:'driverName'
        }
         var userName = {
            xtype: 'textfield',
            blankText: '请输入登录名',
            maxLength:20,
            fieldLabel : '登录名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            readOnly: !ckModel ? false:true,
           // disabled: !!ckModel,
           // hidden: !!ckModel,
            name:'userName'
        }
        var pwd = {
            xtype: 'textfield',
            blankText: '请输入登录密码',
            maxLength:20,
            fieldLabel : '密码',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
           // disabled: !!ckModel,
           // hidden: !!ckModel,
            name:'pwd'
        }
        var remark = {
            xtype: 'textarea',
            blankText: '请输入备注',
            maxLength:20,
            fieldLabel : '备注',
            labelAlign:'right',
            allowBlank : true,
            name:'remark'
        }
        
        var license = {
            xtype: 'textfield',
            blankText: '请输入司机驾照',
            maxLength:20,
            fieldLabel : '驾照编号',
            labelAlign:'right',
            allowBlank : true,
            name:'license'
        }
         var carno = {
            xtype: 'textfield',
            blankText: '请输入身份证号码',
            maxLength:20,
            fieldLabel : '身份证号码',
            labelAlign:'right',
            allowBlank : true,
            name:'cardNo'
        }
        var zjcx = {
            xtype : 'combo',
            fieldLabel : '准驾车型',
            mode : 'local',
            emptytext : '准驾车型',
            displayField : "zjcx",
            valueField : "zjcx",
            labelAlign : 'right',
            editable : false,
            selectOnFocus : true,
            typeAhead : true,
            triggerAction : 'all',
             allowBlank : false,
            name:'zjcx',
            // 数据
            store : new Ext.data.SimpleStore({
                fields : ['Id', 'zjcx'],
                data : [[0,''],[1, 'A1'], [2, 'A2'], [3, 'A3'], [4, 'B1'],[5, 'B2'], [6, 'C1'], [7, 'C2']]
            })
        }
        
            
        var driverTel = {
            xtype: 'textfield',
            maxLength:20,
            fieldLabel : '联系电话',
            labelAlign:'right',
            allowBlank : false,
            regex:/^((?:13\d|15[0-9]|18[0-9])-?\d{5}(\d{3}|\*{3})$|^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$|^400\d{7})?$/,
            regexText: '请输入正确的电话号码!<br/>例如：15876551205 或 027-59361596',
            name:'driverTel'
        }
        
        var key = {
            xtype: 'hiddenfield',
           // type:'hidden',
            name:'driverId'
        }
        
        var formPanel = Ext.create('Ext.form.Panel',{
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items :[driverName,userName,pwd,driverTel,zjcx,license,carno,key]
        });
        
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
        }
        
        this.superclass.constructor.call(this,{
           title: !ckModel ? '添加司机' : ('修改司机信息：' + ckModel.get('driverName')),
           width:300,
           height: !ckModel ? 350 :350,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[formPanel],
           buttonAlign:'center',  
           buttons:[{
                text:'提交',
                handler:function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.SaveDriver',{
                        params:formPanel.getForm().getValues(),
                        target: w
                    }).request();
                }
            },{
                text: '退出',
                handler:function(btn) {
                   formPanel.getForm().reset();
                   w.hide();        
                }
            }]
        });
        this.show();
    }
});


