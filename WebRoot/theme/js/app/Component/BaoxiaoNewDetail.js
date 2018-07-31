/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.BaoxiaoNewDetail',{
    extend:'Ext.window.Window',
    constructor:function(config){
        var w = this;

        if(config== 0){
            compName = 'NewBaoxiaoGridPanel_id';
        }else if(config== 1){
            compName = 'BaoxiaoGridPanel_id';
        }
//        else{
//            compName = 'AuditBaoxiao_id';
//        }

        var record = Ext.getCmp(compName).getSelectionModel().getSelection()[0];
       
        w.superclass.constructor.call(this,{
            width:670,
            height:400,
            modal:true,
            resizable: false,
            layout: 'border',
            border:false,
            items:[{
                region:'west',
                border:false,
                //cls:'win-c-left',
                style:'margin:20px auto 30px; position: relative;border-right: solid 1px #B5B5B5; line-height: 30px;margin:8px 0;',
                width: '50%',
                layout: {
                	border:false,
                    pack: 'center',
                    type: 'hbox'
                },
                items:[{
                	border:false,
                   layout:'vbox',
                   defaults:{
                      xtype: 'label',
                      //cls:'label-default'
                      style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;'
                   },
                   items:[{
                       style:'font-weight: bold',
                       text:'报销单基本信息'
                   },{
                        text:'报销人姓名：' + record.get('name')
                    },
                    {
                        text:'车牌：' + record.get('plate')
                    }
                    ,{
                       text:'费用日期：' + record.get('time')
                   },{
                        text:'加油费(元)：' + ((record.get('jyf')!=null&&record.get('jyf')!="")?record.get('jyf'):0)
                    },{
                        text:'洗车费(元)：' + ((record.get('xcf')!=null&&record.get('xcf')!="")?record.get('xcf'):0)
                    },{
                        text:'保养费(元)：' + ((record.get('byf')!=null&&record.get('byf')!="")?record.get('byf'):0)
                    },{
                        text:'维修费(元)：' + ((record.get('wxf')!=null&&record.get('wxf')!="")?record.get('wxf'):0)
                    },{
                       text:'过路费(元)：' + ((record.get('glf')!=null&&record.get('glf')!="")?record.get('glf'):0)
                   },{
                       text:'路桥费(元)：' + ((record.get('lqf')!=null&&record.get('lqf')!="")?record.get('lqf'):0)
                   },{
                       text:'停车费(元)：' + ((record.get('tcf')!=null&&record.get('tcf')!="")?record.get('tcf'):0)
                   }]
                }]
            },{
                region:'east',
                id:'form_id',
                xtype:'form',
                //cls:'win-c-right',
                style:'margin:20px auto 30px; position: relative;width: 285px; overflow: hidden;margin:8px 0;width: 87px; height:34px; background: url(./theme/imgs/commit.gif) no-repeat; border:none;',
                width:'50%',
                layout: {
                	border:false,
                    pack: 'center',
                    type: 'hbox'
                },
                items:[{
                    layout:'vbox',
                    border:false,
                    defaults:{
                        xtype: 'label',
                        border:false,
                        //cls:'label-default',
                        style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;',
                        height:'30px'
                    },
                    items:[{
                        text:'年检费(元)：' + ((record.get('njf')!=null&&record.get('njf')!="")?record.get('njf'):0)
                    },{
                        text:'保险费(元)：' + ((record.get('bxf')!=null&&record.get('bxf')!="")?record.get('bxf'):0)
                    },{
                        text:'其他费(元)：' + ((record.get('qtf')!=null&&record.get('qtf')!="")?record.get('qtf'):0)
                    },{
                        text:'总费用(元)：' + ((record.get('fee')!=null&&record.get('fee')!="")?record.get('fee'):0)
                    },
                        {
                        xtype: 'label',
                        //cls:'label-default',
                        style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;',
                        text:config == 0 ? '请输入备注' : '备注信息',
                        style:'font-weight: bold'

                    },{
                        xtype:'textarea',
                        id:'info2',
                        labelHeight:100,
                        height:150,
                        editable:config== 0 ? true : false,
                        readOnly:config == 0 ? false: true,
                        value: record.get('info2')
                    },
                    {
                        xtype:'panel',
                        width:200,
                        hidden:( config== 0) ? false:true,
                        layout: {
                            pack: 'end',
                            type: 'hbox'
                        },
                        items:[{
                            xtype:'button',
                            text:'同意',
                            style:'margin-right:20px;',
                            handler:function(){
                                var formData = Ext.getCmp('form_id').getForm();
                                if(formData.isValid()){
//                                    var data = formData.getValues();
//                                    data.type = 'deal';
//                                    data.baoxiaoId = record.get('id');
                                    Ext.ajaxModelLoader('MyApp.Model.AuditBaoxiao',{
                                        target: {grid:Ext.getCmp(compName),win:w},
//                                        params: data
                                        params: {type:'deal',id:record.get('id'),info2:Ext.getCmp('info2').getValue()}
                                    }).request();
                                }
                            }
                        },{
                            text:'拒绝',
                            xtype:'button',
                            style:'margin-right:20px;',
                            handler:function(){
                                var formData = Ext.getCmp('form_id').getForm();
                                if(formData.isValid()){
//                                    var data = formData.getValues();
//                                    data.type = 'refuse';
//                                    data.baoxiaoId = record.get('id');
                                    Ext.ajaxModelLoader('MyApp.Model.AuditBaoxiao',{
                                        target: {grid:Ext.getCmp(compName),win:w},
//                                        params: data
                                        params: {type:'refuse',id:record.get('id'),info2:Ext.getCmp('info2').getValue()}
                                    }).request();
                                }
                            }
                        }]
                    }]
                }]
            }]
        });
        this.show();
    }
});

