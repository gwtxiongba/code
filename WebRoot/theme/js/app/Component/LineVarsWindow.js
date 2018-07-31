/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.LineVarsWindow',{
    extend:'Ext.window.Window',
    constructor: function() {
       var win = this;
       var args = arguments[0];

       this.superclass.constructor.call(this,{
            id:args.id,
            title:'路径参数',
            width:300,
            height:200,
            resizable: false,
            closable: false,
            //closeAction: 'hide',
            layout: 'border',
            modal:true,
            items:[{
               layout:'vbox',
               width:300,
               height:200,
               border:false,
               bodyStyle:'padding:30px 30px',
               items:[{
               id:'name_id',
               xtype: 'textfield',
               labelWidth: 80,
               width:220,
               margin:'0 0 20px 0',
               fieldLabel : '路径名称',
               labelAlign:'right',
               allowBlank:false
            },{
                id:'radius_id',
                xtype: 'numberfield',
                labelWidth: 80,
                width:220,
                fieldLabel : '偏移半径(米)',
                labelAlign:'right',
                minValue:0,
                value:150,
                allowBlank:false
            }]
            }],
            buttons:[{
                text:'提交',
                handler:function(){
                    args.click({
                       name:Ext.getCmp('name_id').getValue(),
                       radius:Ext.getCmp('radius_id').getValue()
                    });
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
           Ext.getCmp('radius_id').setValue(v.radius);
       }
       return this;
    }
});

