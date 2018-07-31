/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this template, choose Tools | Templates
 */
Ext.define('MyApp.Component.SaveMemberWindow',{
	requires:['Ext.ux.TreeCombo_s'],
    extend: 'Ext.window.Window',
    constructor: function(){
        var w = this;
        var ckModel = Ext.getCmp('MemberGridPanel_id').getCheckedModel();
        var g = MyApp.Base.LocalSet.getGolbal();
        var userName = {
            xtype: 'textfield',
            blankText: '请输入登录名',
            maxLength:20,
            fieldLabel : '登录名',
            readOnly: !ckModel ? false:true,
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'user_name'
        }
        
        var name = {
            xtype: 'textfield',
            blankText: '请输入真实姓名',
            maxLength:20,
            fieldLabel : '真实姓名',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'name'
        }
        
            
        var tel = {
            xtype: 'textfield',
            maxLength:20,
            fieldLabel : '联系电话',
            labelAlign:'right',
            allowBlank : false,
            regex:/^((?:13\d|15[0-9]|18[0-9])-?\d{5}(\d{3}|\*{3})$|^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$|^400\d{7})?$/,
            regexText: '请输入正确的电话号码!<br/>例如：15876551205 或 027-59361596',
            name:'tel'
        }
        
        var warning = {
                xtype: 'textfield',
                maxLength:20,
                fieldLabel : '费用预警值(元)',
                labelAlign:'right',
                regex: /^\d+$/,
                regexText: '只能输入数字',
                name:'warning'
            }
        
        var team = {
                id:'team',
                xtype : 'hiddenfield',
               name:'team_id'
            }
        
           var teams =  {
                    xtype: 'textfield',
                     id:'teams',
                    fieldLabel:'公司',
                    labelAlign : 'right',
                    value:g.company.name,
                    valueField: 'id',
                    labelWidth : 100,
                    readOnly:true
                    //url: 'data.json?lineId='+this.getCheckedModel('gridline_id').getData().lineId,
                }
        
        var dept = {
                id:"dept",
                xtype : 'combo',
//                hidden:true,
                fieldLabel:'部门',
                blankText:'请选择部门',
                displayField : "deptName",
                valueField : "deptId",
                labelAlign : 'right',
                editable : false,
                selectOnFocus : true,
                typeAhead : true,
                triggerAction : 'all',
                name:'deptName',
                redStar:true,
                allowBlank:true
                
            }
        
        var key = {
            xtype: 'hiddenfield',
           // type:'hidden',
            name:'id'
        }
        
        var formPanel = Ext.create('Ext.form.Panel',{
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items :[userName,name,tel,team,teams,dept,warning,key]
        });
        
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
            Ext.getCmp('teams').setReadOnly(true);
            Ext.getCmp('teams').setValue(ckModel.getData().teamName);
            
            var stt = Ext.ajaxModelLoader('MyApp.Model.ListDeptByTeamId',{
                params:{teamId: ckModel.getData().team_id}});
                
            Ext.getCmp("dept").bindStore(stt); 
            Ext.getCmp("dept").setValue(ckModel.getData().dept_id);
        }else{
           Ext.getCmp('team').setValue(g.company.id);
                          var st = Ext.ajaxModelLoader('MyApp.Model.ListDeptByTeamId',{
  			                params:{teamId: g.company.id}});
  			             	Ext.getCmp("dept").setValue('');
  			                Ext.getCmp('dept').bindStore(st); 
           
        }
        if(g.visitor.levelId == 4){
               Ext.getCmp("dept").setValue(g.company.deptId);
               Ext.getCmp("dept").setReadOnly(true);
           }
        this.superclass.constructor.call(this,{
           title: !ckModel ? '添加用车人' : ('修改用车人信息：' + ckModel.get('user_name')),
           width:300,
           height: !ckModel ? 250 :250,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[formPanel],
           buttonAlign:'center',  
           buttons:[{
                text:'提交',
                handler:function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.SaveMember',{
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


