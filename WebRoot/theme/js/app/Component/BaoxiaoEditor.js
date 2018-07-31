/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.BaoxiaoEditor',{
	requires:['Ext.ux.TreeCombo_s'],
    extend:'Ext.window.Window',
    constructor:function(){
        var w = this;
        //var user =  MyApp.Base.LocalSet.getGolbal();
        var dateTime0 = Ext.create('Ext.ux.form.DateTimeField',{
            fieldLabel:'费用时间',
            format:'Y-m-d H:i',
            width:210,
            labelWidth:80,
            name:'time'
        });
        
//        var dateTime1 = Ext.create('Ext.ux.form.DateTimeField',{
//            fieldLabel:'还车时间',
//            format:'Y-m-d H:i',
//            width:210,
//            labelWidth:70,
//            name:'dateTime1'
//        });
        
        w.superclass.constructor.call(this,{
            width:670,
            height:400,
            modal:true,
            title:'报销信息',
            resizable: false,
            layout: 'border',
            items:[{
                id:'form_id',
                region:'center',
                layout:'border',
                xtype:'form',
                border:false,
                items:[{
                    region:'west',
                    //cls:'win-c-left',
                    width: '50%',
                    layout: {
                        pack: 'center',
                        type: 'hbox'
                    },
                    items:[{
                    	border:false,
                        layout:'vbox',
                        defaults:{
                            labelCls:'label-default',
                            labelWidth:80,
                            allowBlank:false
                        },
                        items:[{
                            xtype:'label',
                            //cls:'label-default',
                            style:'font-weight: bold;color: #4B4B4B; font-size: 14px;padding-bottom:20px',
                            text:'报销基本信息'
                        },
//                        {
//                            id:'team',
//                            xtype : 'hiddenfield',
//                           name:'teamId',
//                        },{
//                            xtype: 'treecombo',
//                            id:'teams',
//                            width:210,
//                           fieldLabel:'公司',
//                           emptyText:'选择公司',
//                           //labelAlign : 'right',
//                          
//                           fields: ["id", "text"],
//                          
//                           valueField: 'id',
//                           //labelWidth : 100,
//                           url: CRMApp.Configuration.servicePath + '?cmd=golbal&key=uteams',
//                           //url: 'data.json?lineId='+this.getCheckedModel('gridline_id').getData().lineId,
//                          typeAhead : true,
//                           selectChildren: true,
//           			   canSelectFolders: true,
//           			    redStar:true,
//                          allowBlank:false,
//                           listeners:{
//                             itemClick: function (me, view, record, item, index, e, eOpts){
//                                //alert(record.data.id);
//                                 Ext.getCmp('team').setValue(record.data.id);
////                                 var st = Ext.ajaxModelLoader('MyApp.Model.ListDeptByTeamId',{
////         			                params:{teamId: record.data.id}});
////         			             	Ext.getCmp("dept").setValue('');
////         			                Ext.getCmp('dept').bindStore(st); 
//                             }
//                           }
//                          
//                       },
                       {
                            fieldLabel:'选择司机',
                            width:210,
                            id:'userId',
                            displayField: 'driverName',
                            valueField: 'driverId',
                            name:'userId',
                            xtype:'combo',
                            editable:false,
                            allowBlank:false,
                            store:Ext.ajaxModelLoader('MyApp.Model.ListDriver',{params:{type:'flag'}})
                        },
                        {
                            fieldLabel:'选择车辆',
                            width:210,
                            id:'carId',
                            displayField:'plate',
                            valueField:'userId',
                            name:'carId',
                            editable:false,
                            xtype:'combo',
                            allowBlank:false,
                            store:Ext.ajaxModelLoader('MyApp.Model.ListPlateAll')
                        },
                        dateTime0,{
                            xtype:'textfield',
                            fieldLabel:'过路费(元)',
                            width:210,
                            allowBlank:true,
                            name:'gl'
                        },{
                            xtype:'textfield',
                            fieldLabel:'加油费(元)',
                            width:210,
                            allowBlank:true,
                            name:'jy'
                        },{
                            xtype:'textfield',
                            fieldLabel:'洗车费(元)',
                            width:210,
                            allowBlank:true,
                            name:'xc'
                        }]
                    }]
                },{
                    region:'east',
                    //cls:'win-c-right',
                    width:'50%',
                    layout: {
                        pack: 'center',
                        
                        type: 'hbox'
                    },//0过路费、1加油费、2保养费、3维修费、4洗车费、5路桥费、6停车费、7年检费、8保险费、9其他费用；
                    items:[{
                        layout:'vbox',
                        border:false,
                        style:' margin-top:20px',
                        defaults:{
                            xtype: 'combo',
                            editable:false,
                            allowBlank:false,
                            labelWidth: 70,
                           
                            labelCls:'label-default'
                        },
                        items:[{
                            xtype:'textfield',
                            fieldLabel:'保养费(元)',
                            allowBlank:true,
                            width:210,
                            name:'by'
                        },{
                            xtype:'textfield',
                            fieldLabel:'维修费(元)',
                            allowBlank:true,
                            width:210,
                            name:'wx'
                        },{
                            xtype:'textfield',
                            fieldLabel:'路桥费(元)',
                            allowBlank:true,
                            width:210,
                            name:'lq'
                        },{
                            xtype:'textfield',
                            fieldLabel:'停车费(元)',
                            allowBlank:true,
                            width:210,
                            name:'tc'
                        },{
                            xtype:'textfield',
                            fieldLabel:'年检费(元)',
                            allowBlank:true,
                            width:210,
                            name:'nj'
                        },{
                            xtype:'textfield',
                            fieldLabel:'保险费(元)',
                            allowBlank:true,
                            width:210,
                            name:'bx'
                        },{
                            xtype:'textfield',
                            fieldLabel:'其他费用',
                            allowBlank:true,
                            width:210,
                            name:'qt'
                        }]
                        
                       
                    }]
                }]
            }],
             buttons:[{
                text: '提   交',
                handler:function(){
                 var formData = Ext.getCmp('form_id').getForm();
                                    if(formData.isValid()){
                                        var data = formData.getValues();
                                        Ext.ajaxModelLoader('MyApp.Model.AddBaoxiao',{
                                            target: w, 
                                            params: data
                                        }).request();   
                   
                   
                   } }
                
            },{
                text: '关  闭',
                handler:function(){
                    w.close();
                }
            }
            ]
        });
        this.show();
    }
});

