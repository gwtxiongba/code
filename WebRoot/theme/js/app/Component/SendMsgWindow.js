/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this template, choose Tools | Templates
 * 发送信息窗口
 */
Ext.define('MyApp.Component.SendMsgWindow',{
    extend: 'Ext.window.Window',
    config: {id: 'sendMsgWindow_id'},
    constructor: function(){
        var w = this;
		
        //this.flag=true;
        
        var plateArea = {
            id:'plateArea',
            xtype: 'textarea',
            hideLabel: true,
            width: '100%',
            height:60,
            margin:'10 10 10 10',
            name: 'plates'
           // anchor: '-5 -5'  // anchor width and height
        };
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListPrestoreMsg');
        
        
        var imgButton = {
        	xtype: 'button',
			id: 'imgbn',
			height:22,
			width: 22,
			margin:'0 0 0 10',
			iconCls :'picload-off',
			//padding:'-5 -10 -10 -5',
			listeners: {
				click:function(){
					if(Ext.getCmp('ImgUploadWindows_id'))
						Ext.getCmp('ImgUploadWindows_id').show();
					else{
					Ext.create('MyApp.Component.ImgUploadWindows')
					}
				} 
			}
        };
        
        var locButton = {
        	xtype: 'button',
			id: 'locbn',
			height:22,
			width: 22,
			iconCls :'loc-off',  
			margin:'0 -15 0 10',
			listeners: {
				click:function(){
						Ext.create('MyApp.Component.MapLocWindow');
				}
			}
        };
       // var picPanel= Ext.create('MyApp.Component.PicLoadPanel');
        
//        var stateLabel ={
//        	id:"imgselect",
//        	xtype:'label',
////        	height:15,
////			width: 30,
//			margin:'0 -10 0 5',
//			padding:'0 0 5 0',
//        	text:'待选'
//        	}
        	
        var msgCombox = {//下拉框
            xtype: 'combo',
            fieldLabel: '预存短语',              
            id: 'msgcombo',
            name: 'combo',       
            labelAlign:'right',
            width: 235,
            store: store,      
            margin:'0 10 0 -10',
            displayField: 'preMsgContent',                
            valueField: 'preMsgId',                
            mode: 'local',                
            emptyText: '请选择',
            listeners: {
                select: function(msgCombox, records, obj){
                    //下来框的值
                    //msgCombox.getRawValue();
                    //获取下拉框显示的值
                    var msg = msgCombox.getRawValue();
                    Ext.getCmp('msgArea').setRawValue(msg);
                }
            }
        };
        
        var msgArea = {
            id:'msgArea',
            xtype: 'textarea',
            hideLabel: true,
            width: '100%',
            height:80,
            margin:'10 10 10 10',
            name: 'msgs'
           // anchor: '-5 -5'  // anchor width and height
        }; 
        
        var key = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'vehicleIds',
            id: 'vehicleIds'
        };
        
        var key2 = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'iidentifiers',
            id: 'identifiers'
        };
        
        var key3 = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'pointx',
            id: 'pointx'
        };
        
        var key4 = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'pointy',
            id: 'pointy'
        };
        //this.key1 = key;
        
        var key5 = {
            xtype: 'hiddenfield',
            type:'hidden',
            name:'imgUrl',
            id: 'imgUrl'
        };
        
        var panel1 = {
            //region: 'north',
          	layout: 'vbox',
          	frame:false,
	        border: false,
            defaults:{
                margins:'0 0 5 0'
            },
            items :[key,key2,key3,key4,key5,plateArea, 
            		{
	                region: 'north',
	                layout: 'table',
	                border: false,
	               // height: 35,
	                items: [imgButton,locButton,msgCombox]
					},msgArea]
        };

//        var panel1 = {
//            //region: 'north',
//          	layout: 'column',
//          	frame:false,
//	        border: false,
//            defaults:{
//                margins:'0 0 5 0'
//            },
//            items :[{
//            		columnWidth: .55,
//		            layout: 'vbox',
//	                items: [key,key2,plateArea,msgCombox,msgArea]}
//	                ,{
//				    columnWidth: .45,
//				    items:picPanel
//					}]
//        };
//        [key,key2,plateArea, 
//            		{
//	                region: 'north',
//	                layout: 'table',
//	                border: false,
//	               // height: 35,
//	                items: [imgButton,stateLabel,msgCombox]
//					},msgArea]
					
        this.superclass.constructor.call(this,{
            title: '发送调度',
            width: 300,
            height: 285,
            resizable: false,
            layout: 'fit',
            //modal:true,
            items:[panel1],
            buttons: [{
                text: '发  送',
                handler:function(){
                    Ext.ajaxModelLoader('MyApp.Model.SendMsgCenter', {
                        params : {
                            vehicleIds: Ext.getCmp('vehicleIds').getValue(),
                            identifiers: Ext.getCmp('identifiers').getValue(),
                            msgContent: Ext.getCmp('msgArea').getValue(),
                            image:Ext.getCmp('imgUrl').getValue()
                        },
                        target : w
                    }).request();
                    
                    if(Ext.getCmp('pointx').getValue()){
                    Ext.ajaxModelLoader('MyApp.Model.SendPoint',{
                        params:{
							  identifiers:Ext.getCmp('identifiers').getValue(),
							  pointX:Ext.getCmp('pointx').getValue(),
							  pointY:Ext.getCmp('pointy').getValue()
                        },
                        target: w
                    }).request();
                    }
                }
            },{
                text: '保存为预存短语',
                handler:function(){
                    Ext.ajaxModelLoader('MyApp.Model.SavePreStoreMsg', {
                        params : {
                            preMsgContent: Ext.getCmp('msgArea').getValue()
                        },
                        target : w
                    }).request();
                }
            },{
                text: '关  闭',
                handler:function(){
                    w.close();
                }
            }]
        });
        this.show();
    },

    setKeyAreaValue : function(keyVal, plateVal, iidentifierVal) {
        Ext.getCmp('vehicleIds').setValue(keyVal);
        Ext.getCmp('plateArea').setValue(plateVal);
        Ext.getCmp('identifiers').setValue(iidentifierVal);
    }
});



