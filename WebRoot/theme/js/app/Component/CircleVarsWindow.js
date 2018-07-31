/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.CircleVarsWindow',{
    extend:'Ext.window.Window',
    constructor: function() {
       var win = this;
       var args = arguments[0];

       this.superclass.constructor.call(this,{
            id:args.id,
            title:'圆形域参数',
            width:300,
            height:150,
            resizable: false,
            closable: false,
            //closeAction: 'hide',
            layout: 'border',
            modal:true,
            items:[{
               id:'vars_form',
               xtype:'form',
               layout:'vbox',
               width:300,
               height:100,
               border:false,
               bodyStyle:'padding:30px 30px',
               items:[{
               id:'name_id',
               xtype: 'textfield',
               labelWidth: 80,
               width:220,
               margin:'0 0 20px 0',
               fieldLabel : '区域名称',
               labelAlign:'right',
               allowBlank:false
            }]
            }],
            buttons:[{
                text:'提交',
                handler:function(){
                    if(Ext.getCmp('vars_form').isValid()){
                       args.click({
                            name:Ext.getCmp('name_id').getValue()
                        });   
                    }
                }
            },{
                text:'关闭',
                handler:function(btn){
                    win.hide();
                }
            }]
       });
       
       this.setValues = function(v) {
           Ext.getCmp('name_id').setValue(v.name);
       }
       return this;
    }
});

