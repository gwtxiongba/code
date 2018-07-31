/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.UpdateComWindow',{
    extend:'Ext.window.Window',
    config:{
        id:'updateComWindowShow_id'
    },
    constructor:function(){
        var w = Ext.getCmp(this.id);
        if(w){
            w.show();
            return;
        } 
        w = this;
        var corpname = new Ext.form.TextField({
            blankText: '请输入新的企业名称',
            maxLength:20,
            fieldLabel : '企业名称',
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'name'
        });
        
        var corpadder = new Ext.form.TextField({
            fieldLabel : '办公地址',
            labelAlign:'right',
            maxLength:100,
            name:'address'
        });
        
        var corptelp = new Ext.form.TextField({
            blankText: '请输入一个有效的企业的联系电话',
            fieldLabel : '联系电话',
            labelAlign:'right',
            allowBlank : false,
            name:'phone',
            regex:/^((?:13\d|15[0-9]|18[0-9])-?\d{5}(\d{3}|\*{3})$|^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$|^400\d{7})?$/,
            regexText: '请输入正确的电话号码!<br/>例如：15876551205 或 027-59361596'
        });
        
        var corpemail = new Ext.form.TextField({
            fieldLabel : '电子邮箱',
            labelAlign:'right',
            vtype : 'email',
            name: 'email'
        });
       
        var corpid = new Ext.form.Hidden({
            id:'companyId',
            name:'companyId',
            disabled:true
        });
        
        var formPanel = new Ext.form.FormPanel({
            bodyStyle : 'padding-top:20px',
            region : 'center',
            labelWidth : 100,
            border : false,
            labelAlign : 'right',
            items : [corpname, corptelp, corpadder, corpemail, corpid]
        });
      
       
        var initForm = function() {
            var g = MyApp.Base.LocalSet.getGolbal();
            formPanel.getForm().setValues(g.company);
        }
  
        w.save = function(btn) {
            if(!formPanel.getForm().isValid())return;
            Ext.ajaxModelLoader('MyApp.Model.SetCompany', {
               params: formPanel.getForm().getValues()
            }).request();
        }
        
        w.cancel = function(btn) {
            formPanel.getForm().reset();
            w.hide();
        }
        
        this.superclass.constructor.call(this,{
            title : '企业信息',
            width : 300,
            height : 230,
            resizable : false,
            closeAction : 'hide',
            layout : 'border',
            modal: true,
            items : [formPanel],
            buttons:[{
                text:'保存',
                handler:this.save
                },{
                text:'退出',
                handler: this.cancel
                }],
            listeners:{
                beforeshow: initForm
            }
        });
        w.show();
    }
})

