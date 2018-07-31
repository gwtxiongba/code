LOAD_URL = CRMApp.Configuration.UPLOADER_SERVLET_URL;

Ext.define('MyApp.Component.ImgUploadWindows', {
	extend : 'Ext.window.Window',
	//requires : ['Ext.ux.swfupload.swfupload'],
	config : {
		id : 'ImgUploadWindows_id'
	},
	constructor : function() {

		var w = this;

		var panel = Ext.create('Ext.ux.uploadPanel.UploadPanel', {
			//renderTo:Ext.getCmp('ImgUploadWindows_id'),
			//renderTo:Ext.getBody(),
			id:'uploadpanel',
			header : false,
			addFileBtnText : '选择文件',
			uploadBtnText : '上传',
			removeBtnText : '移除所有',
			cancelBtnText : '取消上传',
			file_types : "*.jpg;*.gif;*.bmp;*.png;*.jpeg",
			file_types_description : "Image Files", 
			file_size_limit : 5,// MB
			file_queue_limit : 1,
			width : 800,
			height : 300,
			flash_url : "theme/js/extjs4.2/src/ux/swfupload/swfupload.swf",
			//flash9_url : "theme/js/extjs4.2/src/ux/swfupload/swfupload_fp9.swf",
			upload_url : LOAD_URL + 'servlet/Uploadify',
			//上传完成之后触发
			upload_complete_handler : function(file){
		        Ext.getCmp('imgbn').setIconCls('picload-on');
			},
			listeners : {
			}
		});
		this.superclass.constructor.call(this, {
					title : '上传图片',
					region : 'center',
					width : 800,
					height : 300,
					resizable : false,
					closeAction : 'hide',
					layout : 'fit',
					modal : true,
				    items:panel,
					buttons : [{
//								text : '提交',
//								handler : function(btn) {
//									// if(!formPanel.getForm().isValid())
//									// return;
//									// Ext.ajaxModelLoader('MyApp.Model.SaveTeam',{
//									// params:formPanel.getForm().getValues(),
//									// target: w
//									// }).request();
//								}
//							}, {
								text : '退出',
								handler : function(btn) {
									w.close();
								}
							}]
				});
		this.on("close",function(){
		Ext.getCmp("uploadpanel").onRemove();
		});
		this.show();
	}
});

//src='theme/js/extjs4.2/src/ux/uploadPanel/icons/ok.png'
//'theme/js/extjs4.2/src/ux/uploadPanel/icons/delete.gif',