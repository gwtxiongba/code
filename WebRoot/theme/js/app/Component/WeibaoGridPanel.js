/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.WeibaoGridPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'WeibaoGridPanel_id'},
    constructor:function(key){
        var gp = this;
        
        var store = Ext.ajaxModelLoader('MyApp.Model.ListWeibao',{params: {flag:key}});
        
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            maxWidth:'100%',
            //height: tab.getEl().down('iframe').dom.scrollHeight,
            autoScroll: false,
            scroll:'vertical',
            store:store,
            columns:[
               {xtype: 'rownumberer', text:'序'},
               {
                text:'车牌',
                align:'center',
                width:'25%',
                dataIndex:'plate'
            },{
                text:'年检日期',
                align:'center',
                width:'25%',
                menuDisabled:true,
                dataIndex:'year_time'
            },
             {
                 text:'保养日期',
                 align:'center',
                 width:'25%',
                 menuDisabled:true,
                 dataIndex:'keep_time'
             }, 
            {
                text:'保险日期',
                align:'center',
                width:'25%',
                menuDisabled:true,
                dataIndex:'safe_time'
            }
//             ,{
//                text:'操作',
//                align:'center',
//                width:'40%',
//                 dataIndex:'weibaoId',
//                 renderer:function(v) {
//                     return '<a class="btn edit">编辑</a>　<a class="btn del">删除</a>'
//                 }
//             }
             ],
           
            tbar: [{
                text: '添加维保',
                hidden:key==1?true:false,
                handler: function(){
                    gp.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveWeibaoWindow');
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'deleteBtn_id',
                text: '删除维保',
                 hidden:key==1?true:false,
                disabled: true,
                handler: function(){
                    Ext.Msg.confirm('系统确认','您将删除所选记录，是否继续？',function(btn){
                        if(btn=='yes') {
                            var keys = [];
                            var selModels = gp.getSelectionModel().getSelection();
                            var model = null;
                            while((model = selModels.shift())){keys.push(model.get('id'));}
                            Ext.ajaxModelLoader('MyApp.Model.DelWeibao',{target: gp, params: {id:keys.join(',')}}).request();   
                        }
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                id: 'updateBtn_id',
                text: '修改维保',
                 hidden:key==1?true:false,
                disabled: true,
                handler: function(){
                    Ext.create('MyApp.Component.SaveWeibaoWindow');
                    gp.getSelectionModel().deselectAll();//清空选择框
                }
            },{
                xtype: 'tbseparator'
            },{
                id:'ss_plate',
                xtype: 'textfield',
                labelWidth: 40,
                fieldLabel : '车牌',
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                	var ss_plate = Ext.getCmp("ss_plate").getValue();
                    store.load({params:{start:0,limit:20, ss_plate:ss_plate}});
                }
            },
//            {
//                xtype:'tbfill'
//            },{
//                text:'导出报表',
//                icon:'theme/imgs/ico_excel.png',
//                //scale:'medium',
//                handler:function(){
//                    Ext.Msg.confirm('系统确认','您确定要导出当前报表吗？',function(opt){
//                        if(opt=='yes') {
//                            //这里添加下载地址
////                            var date = Ext.getCmp('date0').getValue().format('yyyy-MM-dd');
////                           var p = Ext.getCmp('vehicleId').getValue();
////                            var plate = Ext.getCmp('plate').getValue();
////                              var index= getIndex(p);
////                             var  vehicleId= list[index].id;
////                            window.open('api.action?cmd=formdLocation_excel&date='+date+'&plate='+plate+'&vehicleId='+vehicleId);
//                        	window.open('api.action?cmd=car_excel');
//                        }
//                    });
//                }
//            },
            {
                xtype:'tbfill'
            },{
                xtype:'pagingtoolbar',
                border:false,
                store:store,
                displayInfo : true
            }],
            listeners:{
                selectionchange : function(sm, selections) {
                    Ext.getCmp('deleteBtn_id').setDisabled(!selections.length);
                    Ext.getCmp('updateBtn_id').setDisabled(selections.length != 1);
                },
                beforerender:function(){
                    var tab = top.Ext.getCmp('apptabs').getActiveTab();
                    if(key!=1){
                    this.setHeight(tab.getEl().down('iframe').dom.scrollHeight);
                    this.setWidth(tab.getEl().down('iframe').dom.scrollWidth);
                    }
                },
                 headerClick:function(g,index,e){
                  // alert(index.dataIndex);
                   
                 //Ext.ajaxModelLoader('MyApp.Model.ListVehicle');
                //this.store.setParams("sortF", "p11");
                //if(index.dataIndex == 'teamName' || index.dataIndex == 'type' || index.dataIndex == 'brand' || index.dataIndex == 'driverName'){
                 this.store.reload({params:{sortF:index.dataIndex}});
                 //}
                }
            }
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
})



