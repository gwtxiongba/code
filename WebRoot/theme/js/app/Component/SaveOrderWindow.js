/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.SaveOrderWindow',{
    extend:'Ext.window.Window',
    constructor:function(){
        var w = this;
        var dt = new Date();
        var user =  MyApp.Base.LocalSet.getGolbal();
        var levelId = user.visitor.levelId ;
        var dateTime0 = Ext.create('Ext.ux.form.DateTimeField',{
            fieldLabel:'用车时间',
            format:'Y-m-d H:i',
            width:220,
            labelWidth:70,
            name:'dateTime0',
            allowBlank : false
        });
        
        var dateTime1 = Ext.create('Ext.ux.form.DateTimeField',{
            fieldLabel:'还车时间',
            format:'Y-m-d H:i',
            width:220,
            labelWidth:70,
            name:'dateTime1',
            allowBlank : false
        });
        window.ppNumber = '';
        window.styleId = '';
        window.brandId = '';
        w.superclass.constructor.call(this,{
            width:600,
            height:380,
            modal:true,
            resizable: false,
            layout: 'border',
            items:[{
                id:'form_id',
                region:'center',
                layout:'border',
                xtype:'form',
                items:[{
                    region:'west',
                    //cls:'win-c-left',
                    width: '50%',
                    layout: {
                        pack: 'center',
                        type: 'hbox'
                    },
                    items:[{
                        layout:'vbox',
                        border:false,
                        defaults:{
                            //labelCls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;',
                            labelWidth:80,
                            allowBlank:false
                        },
                        items:[{
                            xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                            text:'乘车人基本信息'
                        },
                        {
                            id:'passenger',
                            name:'car_user_id',
                            xtype:'combo',
                            fieldLabel:'申请用车人',
                            width:200,
                            editable:false,
                            displayField:'name',
                            allowBlank : false,
                            valueField:'id',
                            store:Ext.ajaxModelLoader('MyApp.Model.ListMember', {
                              //params: {
                                  //dwid: user.dwid
                              //}
                          }),
                            listeners:{
                                change:function(me,nv,ov){
                                    var rec = this.getStore().findRecord('id',nv);
                                    if(rec) {
                                        Ext.getCmp('tel').setValue(rec.get('tel'));
                                        Ext.getCmp('car_user_name').setValue(rec.get('name'));
                                    }
                                }
                            }
                        },{
                            id:'tel',
                            xtype:'textfield',
                            fieldLabel:'联系电话',
                            width:200,
                            allowBlank : false,
                            name:'tel'
                        },{
                            xtype:'textfield',
                            fieldLabel:'乘车人数',
                            width:200,
                            allowBlank : false,
                            name:'number'
                        },
                       
                        {
                            xtype:'textfield',
                            allowBlank:true,
                            fieldLabel:'货物',
                            width:200,
                            name:'takes'
                        },{
                            id:'caruses',
                            name:'caruses',
                            xtype:'combo',
                            fieldLabel:'用车事由',
                            width:200,
                            editable:false,
                            displayField:'name',
                            valueField:'id',
                            store:Ext.ajaxModelLoader('MyApp.Model.ListCaruses')
                        },
                        
                        {
                            id:'carsts',
                            name:'carStyleId',
                            xtype:'combo',
                            fieldLabel:'用车类型',
                            width:200,
                            editable:false,
                            displayField:'name',
                            valueField:'id',
                            store:Ext.ajaxModelLoader('MyApp.Model.ListCarstyleSel')
                        },
//                        {
//                            xtype:'textfield',
//                            allowBlank:true,
//                            fieldLabel:'用车事由',
//                            width:200,
//                            name:'use_reason'
//                        },
                        {
                            xtype:'textfield',
                            allowBlank:true,
                            fieldLabel:'备注',
                            width:200,
                            name:'remark'
                        },{
                            xtype:'hidden',
                            id:'car_user_name',
                            name:'car_user_name'
                        },{
                            xtype:'hidden',
                            id:'pos0',
                            name:'pos0'
                        },{
                            xtype:'hidden',
                            id:'pos1',
                            name:'pos1'
                        }]
                    }]
                },{
                    region:'east',
                    //cls:'win-c-right',
                    width:'50%',
                    layout: {
                        pack: 'center',
                        type: 'hbox'
                    },
                    items:[{
                        layout:'vbox',
                        border:false,
                        defaults:{
                            xtype: 'combo',
                            editable:false,
                            allowBlank:false,
                            labelWidth: 70,
                            //labelCls:'label-default'
                            style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;'
                        },
                        items:[{
                            xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                            text:''
                        },dateTime0,dateTime1,{
                            id:'pos0_address',
                            xtype: 'textfield',
                            fieldLabel:'起始地址',
                            width:280,
                            name:'adress0',
                            allowBlank : false,
                            readOnly:true,
//                            listeners:{
//                                focus:function(me,opts){
//                                    Ext.create('MyApp.Component.PosSetting','pos0').setTitle('起始位置');
//                                    this.fireEvent('blur');
//                                }
//                            }
                            listeners :{
                				focus : function(){
                					Ext.create('MyApp.Component.BaiduMapSelectWindow','pos0');
                				}
                			}
                        },{
                            id:'pos1_address',
                            xtype: 'textfield',
                            fieldLabel:'结束地址',
                            width:280,
                            name:'adress1',
                            allowBlank : false,
                            readOnly:true,
//                            listeners:{
//                                focus:function(me,opts){
//                                    Ext.create('MyApp.Component.PosSetting','pos1').setTitle('结束位置');
//                                    this.fireEvent('blur');
//                                }
//                            }
                            listeners :{
                				focus : function(){
                					Ext.create('MyApp.Component.BaiduMapSelectWindow','pos1');
                				}
                			}
                        },{
                            xtype: 'label',   
                            //cls:'label-default',
                            text:'配置车辆',
                            style:'font-weight: bold;text-align: center; line-height: 20px; border-bottom: solid 1px #B5B5B5;margin:10px,auto',
                            hidden:(levelId == 3 || levelId == 5)?false:true
                        },
//                        {
//                            id:'styleId',
//                            fieldLabel:'选择车型',
//                            width:230,
//                            displayField: 'styleName',
//                            valueField: 'styleId',
//                            hidden:(user.level == 1)?true:false,
//                            allowBlank:(user.level == 1)?true:false,
//                            store:Ext.ajaxModelLoader('MyApp.Model.ListStyle'),
//                            listeners:{
//                                change:function(me,nv,ov){
//                                    window.styleId = nv;
//                                    var st = Ext.ajaxModelLoader('MyApp.Model.ListVehicle', {
//                                        params:{styleId:nv, brandId: window.brandId}
//                                    });
//                                    Ext.getCmp('carId').setValue('');
//                                    Ext.getCmp('carId').bindStore(st);
//                                }
//                            }
//                        },

                      {
                            id:'carStyleIds',
                            fieldLabel:'选择车型',
                            width:230,
                            name:'id',
                            displayField: 'name',
                            valueField: 'id',
                            hidden:(levelId == 3 || levelId == 5)?false:true,
                            allowBlank : (levelId == 3 || levelId == 5)?false:true,
                            store:Ext.ajaxModelLoader('MyApp.Model.ListCarstyle'),
                            listeners:{
                                change:function(me,nv,ov){
                                    window.brandId = nv;
                                    var st = Ext.ajaxModelLoader('MyApp.Model.ListBrandStyle', {
                                        params:{styleId:nv}
                                    });
                                    Ext.getCmp('brandId').setValue('');
                                    Ext.getCmp('brandId').bindStore(st);
                                }
                            }
                        },
                        {
                            id:'brandId',
                            fieldLabel:'选择品牌',
                            width:230,
                            name:'name',
                            displayField: 'brand',
                            valueField: 'brand',
                             hidden:(levelId == 3 || levelId == 5)?false:true,
                             allowBlank : (levelId == 3 || levelId == 5)?false:true,
                            listeners:{
                                change:function(me,nv,ov){
                                if(nv){
                                    window.brandId = nv;
                                    var st = Ext.ajaxModelLoader('MyApp.Model.ListPlate', {
                                        params:{brand:nv}
                                    });
                                    Ext.getCmp('carId').setValue('');
                                    Ext.getCmp('carId').bindStore(st);
                                    }
                                }
                            }
                        },{
                            id:'carId',
                            fieldLabel:'选择车辆',
                            width:230,
                            name:'carId',
                            displayField:'plate',
                            valueField:'userId',//车辆id
                             hidden:(levelId == 3 || levelId == 5)?false:true,
                              allowBlank : (levelId == 3 || levelId == 5)?false:true,
                            listeners:{
                                change:function(me,nv,ov){
                                    var rec = this.getStore().findRecord('vehicleId',nv);
                                    if(rec) {
                                       // alert(rec.get('ppNumber'));
                                        Ext.getCmp('ppNumber').setValue(rec.get('ppNumber'));
                                       // Ext.getCmp('car_user_name').setValue(rec.get('car_user_name'));
                                    }
                                }
                            }
                        }
                          ,  
//                          {
//                                id:'ppNumber',
//                                xtype:'textfield',
//                                fieldLabel:'乘车人数',
//                                name:'ppNumber',
////                                hidden:(user.level == 1)?true:false,
//                                allowBlank:true,
//                                width:230,
//
//                            },
                            {
                            id:'driverId',
                            fieldLabel:'选择司机',
                            width:230,
                            name:'driverId',
                            displayField: 'driverName',
                            valueField: 'driverId',
                             hidden:(levelId == 3 || levelId == 5)?false:true,
                             allowBlank : (levelId == 3 || levelId == 5)?false:true,
                            //store:Ext.ajaxModelLoader('MyApp.Model.FormalDrivers_s')
                            store:Ext.ajaxModelLoader('MyApp.Model.ListDriver',{params:{type:'flag'}})
                        }, {
                            xtype:'panel',
                            border:false,
                            width:200,
                            layout: {
                                pack: 'end',
                                type: 'hbox'
                            }
                            
                        }]
                    }]
                }]
            }],
               
            buttons:[{
                text: '提    交',
                handler:function(){
                var formData = Ext.getCmp('form_id').getForm();
                if(!formData.isValid()) return;
                   var data = formData.getValues();
                    Ext.ajaxModelLoader('MyApp.Model.AddOrder', {
                          params: data,
                        target : w
                    }).request();
                    
                   
                    }
                
            
            },{
                text: '取    消',
                handler:function(){
                    w.close();
                }
            }
            
            
            
            ]
        });
        this.show();
    }
});

