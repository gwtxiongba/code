/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.SaveDeptWindow',{
    extend:'Ext.window.Window',
    config:{id : 'saveDeptWindow_id'},
    constructor:function(key){
        var w = this;
        
//       
         var ckModel = Ext.getCmp('DeptGridPanel_id').getCheckedModel();
        // alert();
         var deptId = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'deptId'
        };
         var deptName = {
            xtype: 'textfield',
            //style: 'margin:15px 0px 3px 30px',
            emptyText: '添加部门',
            fieldLabel : '部门名称',
            width: 260,
            labelAlign:'right',
            redStar:true,
            allowBlank : false,
            name:'deptName'
        };

        var teamId = {
        	id:'teamId',
			xtype : 'hiddenfield',
			name : 'teamId'
			
		}
		
		
        var formPanel = new Ext.form.FormPanel({
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [deptId,deptName, teamId]
        });
        if(ckModel) {
            formPanel.getForm().setValues(ckModel.getData());
        }else{
         Ext.getCmp('teamId').setValue(key);
        }
        this.superclass.constructor.call(this,{
            title : '',
            width : 300,
            height : 150,
            resizable : false,
            layout : 'border',
            modal: true,
            items : [formPanel],
            buttons : [{
                text : '提交',
                handler : function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.SaveDept', {
                        params:formPanel.getForm().getValues(),
                        target: w
                    }).request();
                    
                }
            }, {
                text : '退出',
                handler : function(btn) {
                    formPanel.getForm().reset();
                    w.close();
                }
            }]
        });
        this.show();
    },
    setValue : function(identifier) {
		Ext.getCmp('identifier').setValue(identifier);
	}
});

