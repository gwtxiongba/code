/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.PosTimeWindow',{
    extend:'Ext.window.Window',
    config:{id : 'posTimeWindowShow_id'},
    constructor:function(){
        var w = this;
        
//        var cutpwd = new Ext.form.TextField({
//            blankText : '请输入时间间隔(秒)',
//            inputType : 'number',
//            maxLength : 2,
//            fieldLabel : '时间间隔(秒)',
//            labelAlign:'right',
//            redStar:true,
//            allowBlank : false,
//            name:'pos_time'
//        });
        
//        var cities=[
//                    [1,'120km/h'],
//                    [2,'110km/h'],
//                    [3,'100km/h'],
//                    [4,'90km/h'],
//                    [5,'80km/h'],
//                    [6,'70km/h']
//                    
//                   ];
//                   var proxy=new Ext.data.MemoryProxy(cities);
//                   var reader=new Ext.data.ArrayReader({},[
//                                                           {name:'cid',type:'int',mapping:0},
//                                                           {name:'cname',type:'string',mapping:1}
//                                                           ]);
//                   var store=new Ext.data.Store({
//                    proxy:proxy,
//                    reader:reader,
//                    autoLoad:true
//                   });
//        
//        var cutpwd = new Ext.form.ComboBox({  
//        	renderTo:Ext.getBody(),
//        	 triggerAction : 'all',
//        	  store:store,
//        	  editable : false,
//        	  displayField:'cname',
//        	  valueField:'cid',
//        	  mode:'local',
//        	  emptyText:'请选择最大速度....'
//        	 });
        
        var states = Ext.create('Ext.data.Store', {
            fields: ['id', 'name'],
            data : [
                {"id":5, "name":"5s"},
                {"id":10, "name":"10s"},
                {"id":15, "name":"15s"},
                {"id":20, "name":"20s"},
                {"id":25, "name":"25s"},
                {"id":30, "name":"30s"}
            ]
        });

        var cutpwd =Ext.create('Ext.form.ComboBox', {
            fieldLabel: '时间间隔(秒)',
            emptyText:'请选择时间间隔....',
            store: states,
            editable : false,//不能手工输入
            allowBlank : false,// 选项不允许为空
            queryMode: 'local',
            displayField: 'name',//下拉框展示的值
            valueField: 'id',//下拉框实际的值
            name:'pos_time',
            hiddenName : 'pos_time', //name和hiddenName设置为一样，提交表单以后，后台通过request.getParameter("pos_time")来获取值
            renderTo: Ext.getBody()
        });
        
        var identifier = {
        	id:'identifier',
			xtype : 'hiddenfield',
			name : 'identifier'
		}
		
		
        var formPanel = new Ext.form.FormPanel({
            bodyStyle : 'padding-top:20px',
            border : false,
            region : 'center',
            labelAlign : 'right',
            labelWidth : 100,
            items : [cutpwd, identifier]
        });
        
        this.superclass.constructor.call(this,{
            title : '',
            width : 300,
            height : 150,
            resizable : false,
            layout : 'border',
            modal: true,
            items : [formPanel],
            buttons : [{
                text : '提交',
                handler : function(btn) {
                    if(!formPanel.getForm().isValid()) return;
                    Ext.ajaxModelLoader('MyApp.Model.PosTime', {
                        params:formPanel.getForm().getValues()
                    }).request();
                    
                }
            }, {
                text : '退出',
                handler : function(btn) {
                    formPanel.getForm().reset();
                    w.close();
                }
            }]
        });
        this.show();
    },
    setValue : function(identifier) {
		Ext.getCmp('identifier').setValue(identifier);
	}
});

