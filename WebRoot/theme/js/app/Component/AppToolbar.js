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

           		 }
	     		
	    		
            
                ]
        };
        menuItems.push(tbcarinfo);
        menuItems.push(tbseparator);
        
       
        
      
        
       
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
                text:'菜单管理',
                icon:'./theme/imgs/icons/jggl.png',
                 hidden:(g.visitor.levelId >= 3)?true:false,
                handler: function(){
                    Ext.getCmp('apptabs').open({title:this.text,url:'menuTree.html'});
                }
            },
            {
                text:'修改密码',
                icon:'./theme/imgs/icons/xgmm.png',
                handler: function(btn){
                    Ext.create('MyApp.Component.UpdatePwdWindow');
                }
                
            }]
        };
       
       
        
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
    
       // var roleName = g.visitor.levelName ;
        var infoText = '{1} ({2})　{0}　';
    
        //信息显示区域
        var zoneinfo = {
            id:'accInfo',
            xtype:'label',
            text: infoText.Format("",g.visitor.account,""),
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