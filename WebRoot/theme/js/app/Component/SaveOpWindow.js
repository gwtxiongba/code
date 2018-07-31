/* 
 * To change this template, choose Tools | Templates
 * 新建、修改操作员弹窗
 */
Ext.define('MyApp.Component.SaveOpWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        var ckModel = Ext.getCmp('OperatorGridPanel_id').getCheckedModel();
        
        var reg = MyApp.Config.Regular;
         var st = Ext.ajaxModelLoader('MyApp.Model.ListTeam');
          var lev = Ext.ajaxModelLoader('MyApp.Model.GetLeavels');
                var teamId = 0;
        var account = {
            xtype: 'textfield',
            blankText: '请输入想注册的登录名a',
            maxLength:20,
            fieldLabel : '登录名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            disabled: !!ckModel,
            hidden: !!ckModel,
            regex: reg.account.regExp,
            name:'account'
        }
        
        var realName = {
            xtype: 'textfield',
            blankText: '请正确输入操作员真实姓名',
            maxLength:20,
            fieldLabel : '操作员姓名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'realname'
        }
        
        var pwd = Ext.create('Ext.form.TextField',{
            blankText : '请输入登录密码',
            inputType : 'password',
            maxLength : 20,
            fieldLabel : '密码',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            disabled: !!ckModel,
            hidden: !!ckModel,
            name:'pwd'
        });
        
        var cpwd = {
            xtype: 'textfield',
            inputType : 'password',
            maxLength : 20,
            redStar:true,
            fieldLabel : '确认密码',
            labelAlign:'right',
            allowBlank : true,
            disabled: !!ckModel,
            hidden: !!ckModel,
            validator: function(value) {
                return pwd.getValue().Equals(value) ?  true : '两次输入的密码不一致';
            }
        }
        var leavel = {
            xtype : 'combo',
            id:'leavel',
            fieldLabel:'选择角色',
            blankText:'请选择角色',
            displayField : "levelName",
            valueField : "levelId",
            labelAlign : 'right',
            editable : false,
            selectOnFocus : true,
            typeAhead : true,
            triggerAction : 'all',
            name:'levelId',
            redStar:true,
            allowBlank:false,
            listeners:{
             change:function(me,nv,ov){
               var name = Ext.getCmp("leavel").getRawValue();
               if("部门领导"==name){
                 Ext.getCmp("dept").setVisible(true);
               }else{
                Ext.getCmp("dept").setVisible(false); 
               }
                
             }
           }
        }
        var team = {
            xtype : 'combo',
            id:'team',
            fieldLabel:'选择机构',
            blankText:'请选择所属机构',
            displayField : "teamName",
            valueField : "teamId",
            labelAlign : 'right',
            editable : false,
            selectOnFocus : true,
            typeAhead : true,
            triggerAction : 'all',
            name:'teamId',
            redStar:true,
            allowBlank:false,
            listeners:{
             change:function(me,nv,ov){
               var name = Ext.getCmp("leavel").getRawValue();
                Ext.getCmp("dept").setValue('');
               if("部门领导"==name){
                 //Ext.getCmp("dept").setVisible(true);
                  var st = Ext.ajaxModelLoader('MyApp.Model.ListDeptByTeamId',{
                   params:{teamId: nv}});
                   
                   var teamCtr = Ext.getCmp('dept');
                   teamCtr.bindStore(st); 
               }else{
               // Ext.getCmp("dept").setVisible(false); 
               }
                
             }
           }
           
        }
        
        
        var address = {
            xtype: 'textfield',
            maxLength:20,
            fieldLabel : '联系地址',
            labelAlign:'right',
            allowBlank : true,
            name:'address'
        }
        var dept = {
            id:"dept",
            xtype : 'combo',
            hidden:true,
            fieldLabel:'选择部门',
            blankText:'请选择部门',
            displayField : "deptName",
            valueField : "deptId",
            labelAlign : 'right',
            editable : false,
            selectOnFocus : true,
            typeAhead : true,
            triggerAction : 'all',
            name:'deptId',
            redStar:true,
            allowBlank:true
            
        }
        var phone = {
            xtype: 'textfield',
            maxLength:20,
            fieldLabel : '联系电话',
            labelAlign:'right',
            allowBlank : true,
            name:'phone'
        }
        
        var email = {
            xtype: 'textfield',
            maxLength:30,
            fieldLabel : '电子邮件',
            labelAlign:'right',
            allowBlank : true,
            name:'email'
        }
        
        var key = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'accountId'
        }
        
        var formPanel = Ext.create('Ext.form.Panel',{
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items :[realName,account,pwd,cpwd,leavel,team,dept,address,phone,email,key]
        });
        
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
            if(ckModel.getData().deptId){
            var deptcm = Ext.getCmp("dept");
            deptcm.setVisible(true);
             var stt = Ext.ajaxModelLoader('MyApp.Model.ListDeptByTeamId',{
                   params:{teamId: ckModel.getData().teamId}});
                   
                   deptcm.bindStore(stt); 
                   deptcm.setValue(ckModel.getData().deptId);
            }
             var teamCtr = Ext.getCmp('team');
           
        }
        this.superclass.constructor.call(this,{
           title: !ckModel ? '添加管理员' : ('修改管理员：' + ckModel.get('account')),
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
                    Ext.ajaxModelLoader('MyApp.Model.SaveOperator',{
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
              st.on('load',function(){
                  // if(g.visitor.role < 1) {//非操作员
                     //  st.insert(0,{teamId:0,teamName:'未分组车辆'});    
                  // }
                   var teamCtr = Ext.getCmp('team');
                   teamCtr.bindStore(st); 
                   
                   if(ckModel) teamCtr.setValue(ckModel.getData().teamId);
                });
              lev.on('load',function(){
                  // if(g.visitor.role < 1) {//非操作员
                     //  st.insert(0,{teamId:0,teamName:'未分组车辆'});    
                  // }
                   var levCtr = Ext.getCmp('leavel');
                   levCtr.bindStore(lev); 
                   
                   if(ckModel) levCtr.setValue(ckModel.getData().levelId);
                });
    }
});

