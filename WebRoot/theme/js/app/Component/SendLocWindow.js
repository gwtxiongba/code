/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
 * To change this template, choose Tools | Templates
 * 发送信息窗口
 */
Ext.define('MyApp.Component.SendLocWindow',{
    extend: 'Ext.window.Window',
    config: {id: 'sendLocWindow_id'},
    constructor: function(){
        var w = this;

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
        
      /*  var genres = new Ext.data.SimpleStore({
			fields: ['id', 'genre'],
			data : [['1','Comedy'],['2','Drama'],['3','Action']]
		});*/
        var store = Ext.ajaxModelLoader('MyApp.Model.ListPrestoreMsg');
        
        var locButton = {
        	xtype: 'button',
			id: 'locbn',
			height:22,
			width: 22,
			iconCls :'loc-off',  
			margin:'0 0 0 10',
			listeners: {
				click:function(){
					Ext.create('MyApp.Component.MapLocWindow')
				}
			}
        };
        
        var stateLabel ={
        	id:"locselect",
        	xtype:'label',
//        	height:15,
//			width: 30,
			margin:'0 -10 0 5',
			padding:'0 0 5 0',
        	text:'待选'
        	}
        
        var msgCombox = {//下拉框
            xtype: 'combo',
            fieldLabel: '预存短语',              
            id: 'combo',
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
            id:'locArea',
            xtype: 'textarea',
            hideLabel: true,
            width: '100%',
            height:80,
            margin:'10 10 10 10',
            name: 'msgs'
           // anchor: '-5 -5'  // anchor width and height
        }; 
        
        //this.msgArea1 = msgArea;
        
        /*
        //图片预览
        var cover = Ext.create('Ext.Img', {
            src: Ext.BLANK_IMAGE_URL,
            width: 200,
            height: 150,
            style: 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);'  //必不可少，否则IE下无法预览
        });
        
        //上传图片
        var upFile = {
    		xtype: 'textfield',
            itemId: 'upload_image',
            id: 'upload_image',
            name: 'file',
            inputType: 'file',
            fieldLabel: '上传照片',    
            emptyText : '选择文件存放路径',
//                allowBlank: false,
            listeners: {
            	'change': function(e, t, obj){
            		var formPanel = panel1;    
            		var uploadImage = e;
            		 //上传图片类型,在前台的拦截
                    var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;
                    //得到选择的图片路径   
                    var path = uploadImage.getValue();
                    if(path!=null && !Ext.isEmpty(path)) {
                        var url = "file://" + path;
                         //是否是规定的图片类型   
                         if (img_reg.test(url)) {
                             var imageShow_box = cover;    //预览的图片框对象
                             if (Ext.isIE) {//IE浏览器
                                 var imageShow_box_dom = imageShow_box.getEl().dom;
                                 imageShow_box_dom.src = Ext.BLANK_IMAGE_URL;// 覆盖原来的图片   
                                 imageShow_box_dom.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url; 
                             } else {
                            	 var file = uploadImage.inputEl.dom.files[0];
                            	 var url = window.URL.createObjectURL(file);
                            	 cover.height = 150;
         						 cover.width = 200;
         						 cover.setSrc(url);
                             }
                        } else {
                            Ext.Msg.alert('提示','请选择图片类型的文件！');
                        }
                    } 
                }
            }
        };*/
        
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
            id: 'iidentifiers'
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
        
        var panel1 = {
            layout: 'vbox',
          	frame:false,
	        border: false,
            defaults:{
                margins:'0 0 5 0'
            },
            items :[key, key2, key3,key4,plateArea, 
            		{
	                region: 'north',
	                layout: 'table',
	                border: false,
	               // height: 35,
	                items: [locButton,stateLabel,msgCombox]
					},msgArea]
        };
        
        this.superclass.constructor.call(this,{
            title: '发送位置',
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
							identifiers: Ext.getCmp('iidentifiers').getValue(),
							msgContent: Ext.getCmp('locArea').getValue()
						},
						target : w
					}).request();
					
					Ext.ajaxModelLoader('MyApp.Model.SendPoint',{
                        params:{
							  identifiers:Ext.getCmp('iidentifiers').getValue(),
							  pointX:Ext.getCmp('pointx').getValue(),
							  pointY:Ext.getCmp('pointy').getValue()
                        },
                        target: w
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
		Ext.getCmp('iidentifiers').setValue(iidentifierVal);
	}
});



