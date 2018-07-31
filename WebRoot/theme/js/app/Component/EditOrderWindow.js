/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.EditOrderWindow',{
    extend:'Ext.window.Window',
    constructor:function(){
        var w = this;
        var g = MyApp.Base.LocalSet.getGolbal();
        var levelId = g.visitor.levelId ;
        var ckModel = Ext.getCmp('NewOrderGridPanel_id').getCheckedModel();
        window.ppNumber = '';
        window.styleId = '';
        window.brandId = '';
         var status = '';
          switch(ckModel.getData().status ){
		          case -5:
			           status = '已取消';
			          break;
		           case -4:
			            status = '车队长已拒绝';
			          break;
		           case -3:
			            status = '部门领导已拒绝';
			          break;
		           case -2:
			            status = '公司领导已拒绝';
			          break;
		           case -1:
			            status = '管理员已拒绝';
			          break;
		           case 0:
			            status = '待审批';
			          break;
		           case 1:
			            status = '管理员已同意';
			          break;
		           case 2:
			            status = '公司领导已同意';
			          break;
		           case 3:
			            status = '部门领导已同意';
			          break;
		           case 4:
			            status = '已派车';
			          break;
		           case 5:
			            status = '已发车';
			          break;
		           case 6:
			            status = '乘客已上车';
			          break;
		          
		           case 7:
			            status = '已完成';
			            var t1 = ckModel.getData().startTime.replace(/-/g,'/');
			            var t2 = ckModel.getData().overTime.replace(/-/g,'/');
			            var times = (new Date(t2).getTime()-new Date(t1).getTime())/1000; 
			             var m = times;
			             if(m>3600){
			               time += parseInt(m/3600)+"小时";
			               m = m%3600;
			             }
			             if(m>60){
			              time +=  parseInt(m/60)+"分钟";
			              m = m%60;
			             }
			             if(m>0){
			              time += m+"秒";
			             }
			             
			          break;
          
          }
        w.superclass.constructor.call(this,{
            title: '订单信息',
            width:620,
            height:400,
            modal:true,
            resizable: false,
            layout: 'border',
            items:[{
                region:'north'
            },{
                region:'center',
                layout:'border',
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
                            style:'color: #4B4B4B; font-size: 13px;width: 285px; overflow: hidden;padding:5px',
                            labelWidth:80,
                            allowBlank:false
                        },
                        items:[{
                            xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                            text:'乘车人基本信息'
                        },{
                             xtype:'label',
                            text:'用车人:'+ckModel.getData().carUserName
                           
                        },{
                            xtype:'label',
                             text:'联系电话:'+ckModel.getData().carUserTel
                        },
                        {
                            xtype:'label',
                             text:'货物:'+ckModel.getData().takes
                        },
                        {
                            xtype:'label',
                             text:'开始时间:'+ckModel.getData().beginTime
                        },
                        {
                            xtype:'label',
                            fieldLabel:'结束时间',
                           text:'结束时间:'+ckModel.getData().endTime
                        },
                        {
                            xtype:'label',
                            fieldLabel:'起始地址',
                             text:'起始地址:'+ckModel.getData().beginAddr
                        },
                        {
                            xtype:'label',
                            fieldLabel:'结束地址',
                            name:'endAddr',
                             text:'结束地址:'+ckModel.getData().endAddr
                        },
                        {
                            xtype:'label',
                            fieldLabel:'用车类型',
                            text:'用车类型:'+ckModel.getData().carStyle
                        },
                       	{
                            xtype:'label',
                            text:'用车事由:'+ckModel.getData().caruse
                        },
                        {
                            xtype:'label',
                            text:'备注:'+ckModel.getData().remark
                        }]
                    }]
                },{
                    region:'east',
                    width:'50%',
                    id:'form_id',
                    xtype:'form',
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
                            style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;'
                        },
                        items:[{
                         xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin-bottom:10px;margin-top:10px',
                            text:'订单操作'
                            },
                            {
                         	xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 10px;font-weight:bold;margin-bottom:6px',
                            text:'订单状态：'+status
                            },
	                        {
	                        xtype:'textarea',
	                        labelHeight:150,
	                        height:140,
	                         width:230,
	                        readOnly:false,
	                        name:'reason',
	                        fieldLabel:'备注信息',
	                         emptyText: '如拒绝，请输入原因',
	                           allowBlank : true,
	                        value: ''
	                    	},
                      		{
                            xtype: 'label',   
                            //cls:'label-default',
                            text:'配置车辆',
                            style:'font-weight: bold;text-align: center; line-height: 20px; border-bottom: solid 1px #B5B5B5;margin:10px,auto',
                            hidden:(levelId == 3)?false:true
                        	},
                         	{
                            id:'carStyleId',
                            fieldLabel:'选择车型',
                            width:230,
                            name:'styleId',
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
                            name:'brand',
                            displayField: 'brand',
                            valueField: 'brand',
                            hidden:(levelId == 3 || levelId == 5)?false:true,
                            allowBlank : (levelId == 3 || levelId == 5)?false:true,
                          //  store:Ext.ajaxModelLoader('MyApp.Model.ListBrand'),
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
                            valueField:'userId',
                            hidden:(levelId == 3 || levelId == 5)?false:true,
                            allowBlank : (levelId == 3 || levelId == 5)?false:true,
                            listeners:{
                                change:function(me,nv,ov){
                                    var rec = this.getStore().findRecord('vehicleId',nv);
                                    if(rec) {
                                        Ext.getCmp('ppNumber').setValue(rec.get('ppNumber'));
                                    }
                                }
                            }
                        },  
                            {
                            id:'driverId',
                            fieldLabel:'选择司机',
                            width:230,
                            name:'driverId',
                            displayField: 'driverName',
                            valueField: 'driverId',
                            hidden:(levelId == 3 || levelId == 5)?false:true,
                            allowBlank : (levelId == 3 || levelId == 5)?false:true,
                            store:Ext.ajaxModelLoader('MyApp.Model.ListDriver', {params:{type:'flag'}})
                        }]
                    }]
                }]
            }],
            buttons:[{
                text: '同  意',
                handler:function(){
                var formData = Ext.getCmp('form_id').getForm();
                   var data = formData.getValues();
                     data.status = 1;
                     data.orderId = ckModel.getData().id;
                    Ext.ajaxModelLoader('MyApp.Model.EditOrder', {
                          params: data,
                        target : w
                    }).request();
                    
                    }
                
            },{ 
                text: '拒  绝',
                handler:function(){
                 var formData = Ext.getCmp('form_id').getForm();
                   var data = formData.getValues();
                    data.status = -1;
                     data.orderId = ckModel.getData().id;
                    Ext.ajaxModelLoader('MyApp.Model.EditOrder', {
                         params: data,
                        target : w
                    }).request();
                }
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

