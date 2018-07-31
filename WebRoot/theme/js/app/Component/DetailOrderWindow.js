/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.DetailOrderWindow',{
    extend:'Ext.window.Window',
    constructor:function(key){
        var w = this;
         var g = MyApp.Base.LocalSet.getGolbal();
          var levelId = g.visitor.levelId ;
          var ckModel;
          if(key == 'OrderHistoryIngGridPanel_id'){
           ckModel = Ext.getCmp('OrderHistoryIngGridPanel_id').getCheckedModel();
          }else{
            ckModel = Ext.getCmp('OrderHistoryOverGridPanel_id').getCheckedModel();
          }
          var time = "";;
         
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
			             //time = times/3600;
			             var m = times;
			            // alert(times);
			             if(m>3600){
			               time += parseInt(m/3600)+"小时";
			               m = m%3600;
			              // alert(time);
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
         
        window.ppNumber = '';
        window.styleId = '';
        window.brandId = '';
        w.superclass.constructor.call(this,{
            title: '订单信息',
            width:670,
            height:430,
            modal:true,
            resizable: false,
            layout: 'border',
            items:[{
                region:'north'
                //cls:'win-top',
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
                            style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;padding:5px',
                            labelWidth:80,
                            allowBlank:false
                        },
                        items:[{
                            xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                            text:'乘车人基本信息'
                        },
//                        
                        {
                             xtype:'label',
                            text:'用车人：'+ckModel.getData().carUserName
                           
                        },{
                            xtype:'label',
                             text:'联系电话：'+ckModel.getData().carUserTel
                        },
                        
                       
                        {
                            xtype:'label',
                             text:(ckModel.getData().status==7)?'用车时间：'+ckModel.getData().startTime:'预约时间：'+ckModel.getData().beginTime
                        },
                        {
                            xtype:'label',
                            fieldLabel:'结束时间',
                           text:(ckModel.getData().status==7)?'还车时间： '+ckModel.getData().overTime:'结束时间：'+ckModel.getData().endTime
                        },
                        {
                            xtype:'label',
                            fieldLabel:'起始地址',
                             text:'起始地址：'+ckModel.getData().beginAddr
                        },
                        
                        
                        {
                            xtype:'label',
                            fieldLabel:'结束地址',
                            name:'endAddr',
                             text:'结束地址：'+ckModel.getData().endAddr
                        },
                        {
                            xtype:'label',
                            fieldLabel:'用车类型',
                            text:'用车类型：'+ckModel.getData().carStyle
                        },
                        
                       {
                            xtype:'label',
                            text:'用车事由：'+ckModel.getData().caruse
                        },
                        {
                            xtype:'label',
                            text:'乘车人数：'+ckModel.getData().pnumber
                        },
                         {
                            xtype:'label',
                             text:'货物：'+ckModel.getData().takes
                        },
                        {
                            xtype:'label',
                            text:'备注：'+ckModel.getData().remark
                        }]
                    }]
                },{
                    region:'east',
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
                            style:'color: #4B4B4B; font-size: 14px;width: 285px; overflow: hidden;padding:5px'
                        },
                        items:[{
                         xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                            text:'订单状态：'+status
                            },
                            {
                         xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                            hidden:(ckModel.getData().status==7)?false:true,
                            text:'行驶里程：'+ckModel.getData().miles/1000.0+'公里'
                            },
                            {
                             xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                            hidden:(ckModel.getData().status==7)?false:true,
                            text:'行驶时间：'+time
                            },
                            {
                         xtype:'label',
                            //cls:'label-default',
                            style:'color: #4B4B4B; font-size: 14px;font-weight:bold;margin:10px',
                             hidden:(ckModel.getData().status==7)?false:true,
                            text:'总费用：'+ckModel.getData().cost+'元'
                            },
                        {
                        xtype:'label',
                        text:'审批员备注：'+ckModel.getData().reason
                        
                    },
                        
                       {
                            xtype: 'label',   
                            //cls:'label-default',
                            text:'车辆信息',
                            style:'font-weight: bold;text-align: center; line-height: 20px; border-bottom: solid 1px #B5B5B5;margin:10px',
                            hidden:(ckModel.getData().status > 3)?false:true
                            
                        },   
                        {
                             xtype: 'label',   
                            //cls:'label-default',
                            text:'车辆品牌：'+ckModel.getData().brand,
                             hidden:(ckModel.getData().status > 3)?false:true
                        }, 
                        {
                             xtype: 'label',   
                            //cls:'label-default',
                            text:'车牌：'+ckModel.getData().plate,
                             hidden:(ckModel.getData().status > 3)?false:true
                        }
                          ,  
                             {
                             xtype: 'label',   
                            //cls:'label-default',
                            text:'司机：'+ckModel.getData().driverName,
                             hidden:(ckModel.getData().status > 3)?false:true
                        },
                         {
                             xtype: 'label',   
                            //cls:'label-default',
                            text:'司机电话：'+ckModel.getData().driverTel,
                             hidden:(ckModel.getData().status > 3)?false:true
                        }
                        ]
                    }]
                }]
            }],
            
            buttons:[
            
            /*{
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
                
            },*/{ 
                text: '查看派车单',
                hidden:(ckModel.getData().status>=4)?false:true,
                handler:function(){
                 //var formData = Ext.getCmp('form_id').getForm();
                   //var data = formData.getValues();
                    //data.status = -1;
                     //data.orderId = ckModel.getData().id;
                     window.open('print.html?key='+ckModel.getData().id, '车辆预算单', 'height=520,width=900,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
                    
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

