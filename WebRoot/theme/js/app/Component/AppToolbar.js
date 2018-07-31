/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AppToolbar',{
   extend: 'Ext.Toolbar',
   constructor: function() {
        var g = MyApp.Base.LocalSet.getGolbal();
        if(g == undefined) window.location.href = 'login.html';
        var menuItems =  [];
        var tbseparator = {
            xtype: 'tbseparator'
        };
        
        var tbfill = {
            xtype:'tbfill'
        };
    
      var tipimg = {
           xtype: 'box', 
           width: 225, 
           height: 43,
           autoEl: {  
           tag: 'img',   
           src: './theme/imgs/logo.png'
         }  
        };
        menuItems.push(tipimg);
        var tipinfo = {
            id:'tipinfo',
            xtype:'label',
            text:'中寰政企车辆管理平台',
            style:'color:#04408c;font-size:24px'
        };
        //  menuItems.push(tipinfo);
      var tbmsg = {
            text:'预约用车',
            icon:'./theme/imgs/icons/yyyc.png',
            menu:[{
                text:'最新预约列表',
                icon:'./theme/imgs/icons/zxyy.png',
                handler:function(){
                	Ext.getCmp('apptabs').open({title:this.text,url:'newOrder.html'});
                }
            },
            {
                text:'未完成预约',
                icon:'./theme/imgs/icons/wwcyy.png',
                handler:function(){
                	Ext.getCmp('apptabs').open({title:this.text,url:'orderHistoryIng.html'});
                }
            },
            {
                text:'已完成预约',
                icon:'./theme/imgs/icons/ywcyy.png',
                handler:function(){
                	Ext.getCmp('apptabs').open({title:this.text,url:'orderHistoryOver.html'});
                }
            }
            ]
        };
        
        //车辆安全
       var tbsafe = {
            text:'乘客管理',
            icon:'./theme/imgs/icons/ckgl.png',
            menu:[
               {
              text:'乘客注册审批',
              icon:'./theme/imgs/icons/ckzcsp.png',
              handler:function(){
               		Ext.getCmp('apptabs').open({title:this.text,url:'memberNew.html'});
             	 }
              },
              
               {
              text:'乘客信息管理',
              icon:'./theme/imgs/icons/ckxxgl.png',
              handler:function(){
                 Ext.getCmp('apptabs').open({title:this.text,url:'member.html'});
              }
             }
            ]
        };
        var tbsafesj = {
            text:'司机管理',
            icon:'./theme/imgs/icons/sjgl.png',
            menu:[
               
              {
              text:'司机注册审批',
              icon:'./theme/imgs/icons/sjzcsp.png',
              handler:function(){
               		Ext.getCmp('apptabs').open({title:this.text,url:'driverNew.html'});
             	 }
              },
               {
                text:'司机信息管理',
                icon:'./theme/imgs/icons/sjxxgl.png',
                handler:function(){
                    Ext.getCmp('apptabs').open({title:this.text,url:'drivers.html'});
                	}
            	}
            ]
        };
        menuItems.push(tbseparator);
        menuItems.push(tbmsg);
        menuItems.push(tbseparator);
        menuItems.push(tbsafe);
         menuItems.push(tbseparator);
        menuItems.push(tbsafesj);
        
          //司机请假
         var tbaskleave = {
            text:'司机请假管理',
            icon:'./theme/imgs/icons/sjqjgl.png',
           	hidden:(g.visitor.levelId != 3 &&  g.visitor.levelId != 5)?true:false,
            menu:[{
                  text:'司机请假审批',
                  icon:'./theme/imgs/icons/sjqjsp.png',
                   handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'newAskleave.html'});}   
              },
            	{
                text:'司机请假列表', 
                icon:'./theme/imgs/icons/sjqjlb.png',
                handler: function(){
                    			  Ext.getCmp('apptabs').open({title:this.text,url:'askleave.html'});
                						}
            }]
        };
        menuItems.push(tbaskleave);
        menuItems.push(tbseparator);
         var tbbaoxiao = {
            text:'费用报销管理',
            icon:'./theme/imgs/icons/fybxgl.png',
           	hidden:(g.visitor.levelId == 4)?true:false,
            menu:[{
                  text:'费用报销审批',
                  icon:'./theme/imgs/icons/fybxsp.png',
                  hidden:(g.visitor.levelId == 4)?true:false,
                   handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'newBaoxiao.html'})}   
              },
              {
                text:'费用报销列表', 
                icon:'./theme/imgs/icons/fybxlb.png',
                hidden:(g.visitor.levelId == 4)?true:false,
                handler: function(){
                    			  Ext.getCmp('apptabs').open({title:this.text,url:'baoxiao.html'});
                						}
            }]
        };
        menuItems.push(tbbaoxiao);
        menuItems.push(tbseparator);
         var tbcarinfo = {
            text:'车辆信息管理',
            icon:'./theme/imgs/icons/clxxgl.png',
           	hidden:(g.visitor.levelId > 5)?true:false,
            menu:[{
                text:'车辆类型管理',
                icon:'./theme/imgs/icons/cllxgl.png',
                hidden:(g.visitor.levelId != 4)?false:true,
                handler: function(btn){
                     Ext.getCmp('apptabs').open({title:this.text,url:'carstyle.html'});
                	}
                },
                {
                 text:'品牌管理',
                 icon:'./theme/imgs/icons/ppgl.png',
                 hidden:(g.visitor.levelId != 4)?false:true,
               	handler: function(btn){
                     Ext.getCmp('apptabs').open({title:this.text,url:'brand.html'});
                	}
                },
                {
                text:'车辆管理', 
                icon:'./theme/imgs/icons/clgl.png',
               // hidden:(g.visitor.levelId == 4)?true:false,
                handler: function(){
                    			   Ext.getCmp('apptabs').open({title:this.text,url:'vehicle.html'});
                						}

           		 },
	    		{
                text:'班车管理', 
                icon:'./theme/imgs/icons/clgl.png',
                //hidden:(g.visitor.levelId == 4)?true:false,
                handler: function(){
                    			   Ext.getCmp('apptabs').open({title:this.text,url:'vehicleBanche.html'});
                						}
            	},
	     		{
                text:'车辆维保', 
                icon:'./theme/imgs/weibao.png',
                hidden:(g.visitor.levelId != 3 && g.visitor.levelId != 5)?true:false,
                handler: function(){
                    			   Ext.getCmp('apptabs').open({title:this.text,url:'weibao.html'});
                						}

            	},
	    		{
                text:'违章管理', 
                icon:'./theme/imgs/weizhan.png',
                hidden:(g.visitor.levelId != 3 && g.visitor.levelId != 5)?true:false,
                handler: function(){
                    			   Ext.getCmp('apptabs').open({title:this.text,url:'weizhang.html'});
                						}

            	}
            
                ]
        };
        menuItems.push(tbcarinfo);
        menuItems.push(tbseparator);
         var tbweilan = {
            text:'围栏管理',
            icon:'./theme/imgs/icons/wlgl.png',
           	hidden:(g.visitor.levelId == 4)?true:false,
            menu:[{
                      text:'路径管理',
                      icon:'./theme/imgs/icons/ljgl.png',
                      handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'lines.html'});}     
                   },{
                     text:'圆形域管理',
                     icon:'./theme/imgs/icons/yxygl.png',
                     handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'area0.html'});}     
                   },{
                     text:'多边形域管理',
                     icon:'./theme/imgs/icons/dbxygl.png',
                     handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'area1.html'});}
                   }
                ]
        };
        menuItems.push(tbweilan);
        menuItems.push(tbseparator);
        var tbstat = {
                text:'数据统计',
                icon:'./theme/imgs/icons/sjtj.png',
                hidden:(g.visitor.levelId == 4)?true:false,
                		 menu:[{
                             text:'车辆费用统计',
                             icon:'./theme/imgs/chefei.png',
                             handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'reportCar.html'});}     
                          },{
                            text:'司机费用统计',
                            icon:'./theme/imgs/sjf.png',
                            handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'reportDriver.html'});}     
                          },{
                            text:'乘客费用统计',
                            icon:'./theme/imgs/ckf.png',
                            handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'reportMember.html'});}
                          },{
                              text:'里程报表',
                              icon:'./theme/imgs/lc.png',
                              handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'reportMile.html'});}     
                          },{
                               text:'位置报表',
                               icon:'./theme/imgs/lc.png',
                               handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'reportPosition.html'});}     
                          },{
                              text:'断电报警报表',
                              icon:'./theme/imgs/lc.png',
                              hidden:true,
                              handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'alarmsRep.html#0'});}     
                          },{
                              text:'碰撞报警报表',
                              icon:'./theme/imgs/lc.png',
                               hidden:true,
                              handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'alarmsRep.html#1'});}     
                          },{
                              text:'侧翻报警报表',
                              icon:'./theme/imgs/lc.png',
                               hidden:true,
                              handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'alarmsRep.html#2'});}     
                          },{
                              text:'震动报警报表',
                              icon:'./theme/imgs/lc.png',
                               hidden:true,
                              handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'alarmsRep.html#3'});}     
                          },{
                              text:'超速报警报表',
                              icon:'./theme/imgs/lc.png',
                               hidden:true,
                              handler:function(){Ext.getCmp('apptabs').open({title:this.text,url:'alarmsRep.html#4'});}     
                          }
                       ]
            };
        
        menuItems.push(tbstat);
        menuItems.push(tbseparator);
        
        if(g.visitor.ifAdmin == 1){
        	tbfiles.menu.push({
                text:'机构管理',
                icon:'./theme/imgs/icons/jggl.png',
                handler: function(){
                    Ext.getCmp('apptabs').open({title:this.text,url:'administrator.html'});
                }
            });
        }
    
        //系统设置项
        var tbsets = {
            text:'系统管理',
            icon:'./theme/imgs/icons/xtgl.png',
            menu:[
            
            {
                text:'账户管理',
                icon:'./theme/imgs/icons/zhgl.png',
                 hidden:(g.visitor.levelId >= 3)?true:false,
                handler:function(){
                    				Ext.getCmp('apptabs').open({title:this.text,url:'operators.html'});
                }
            },{
                text:'机构管理',
                icon:'./theme/imgs/icons/jggl.png',
                 hidden:(g.visitor.levelId >= 3)?true:false,
                handler: function(){
                    Ext.getCmp('apptabs').open({title:this.text,url:'companyDept.html'});
                }
            },
            {
                text:'修改密码',
                icon:'./theme/imgs/icons/xgmm.png',
                handler: function(btn){
                    Ext.create('MyApp.Component.UpdatePwdWindow');
                }
                },
                
                {
                 text:'用车类型管理',
                 icon:'./theme/imgs/icons/yclxgl.png',
                 hidden:(g.visitor.levelId != 4)?false:true,
                	handler: function(btn){
                     Ext.getCmp('apptabs').open({title:this.text,url:'caruses.html'});
                }
            }]
        };
       
        if(g.visitor.levelId <= 2) {
            tbsets.menu.push({
                text:'修改企业资料',
                icon:'./theme/imgs/icons/xgqyzl.png',
                handler:function(btn) {
                    Ext.create('MyApp.Component.UpdateComWindow');
                }
            });
        }
        
        menuItems.push(tbsets);
        menuItems.push(tbseparator);
        
        //帮助项
        var tbhelp = {
            text:'帮助',
            icon:'./theme/imgs/icons/bangzhu.png',
            menu:[{
                text:'联机帮助',
                icon:'./theme/imgs/icons/bangzhu.png',
                handler:function(btn) {
                    var url = "help/help.doc";
                    window.open(url, "_blank");
                }
            },{
                text:'关于',
                icon:'./theme/imgs/icons/guanyu.png',
                handler:function(btn) {
                    Ext.MessageBox.alert('中寰政企车辆安全管理系统','版本 1.0(Build 250)<br/>Copyright&copy;2014~2015 chanceit All Rights Reserved.<br/>畅讯网络版权所有<br/><a href="http://www.chanceit.cn" target="_blank">www.chanceit.cn</a>');
                }
            }]
        };
    
        //退出按钮
        var tblogout = {
            text: '退出',
            handler:function() {
                Ext.Msg.confirm('系统询问', '您确定要退出本系统吗?', function(opt){
                    if(opt == 'yes') Ext.ajaxModelLoader('MyApp.Model.Logout').request();
                });
            }
        };
    
        var roleName = g.visitor.levelName ;
        var infoText = '{1} ({2})　{0}　';
    
        //信息显示区域
        var zoneinfo = {
            id:'accInfo',
            xtype:'label',
            text: infoText.Format(g.company.name,g.visitor.account,roleName),
            style:'color:#04408c;width:180px;height:40px'
        };
        
        menuItems.push(tbhelp);
        menuItems.push(tbfill);
        menuItems.push(zoneinfo);
        menuItems.push(tbseparator);
        menuItems.push(tblogout);
        
        this.superclass.constructor.call(this, {id:'apptoolbar',items:menuItems});
        
        this.refresh = function() {
            Ext.getCmp('accInfo').setText(infoText.Format(g.company.name,g.visitor.account,roleName));
        }
   }
});