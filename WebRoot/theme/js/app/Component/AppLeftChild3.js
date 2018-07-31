/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AppLeftChild3',{
   extend:'Ext.grid.Panel',
   constructor:function(){
      // var childPanel =  Ext.create('MyApp.Component.ChildPane3');

   	   var store = Ext.ajaxModelLoader('MyApp.Model.ListDriver');
   	   
       this.superclass.constructor.call(this,{
       	    id:'driver_list',
            title: '司机监控',
            layout: 'fit',
   	   		frame: false,
            border:false,
            autoScroll: true,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer', text:'序'},
                {text:'司机', dataIndex: 'driverName', sortable: false, menuDisabled:true, width:'30%'},
                {text:'联系电话', dataIndex: 'driverTel', sortable: false, menuDisabled:true, width:'70%'}
            ],
            listeners:{
            	'itemcontextmenu':function(view,record,items,index,e,obj){
                    e.preventDefault();
                    e.stopEvent();
                    
                    //获取所有选中项
                    var selData = this.getSelectionModel().getSelection();
                    
                    var recmenu = new Ext.menu.Menu({
                        floating:true,
                        items:[{
                            text:'司机详情',
                            handler:function(){
                            	if( Ext.getCmp('detailWindow'+selData[0].get('driverId')));
                            	// Ext.getCmp('detailWindow'+selData[0].get('driverId')).ownerCt.close();
                            	else{
                            	Ext.create('MyApp.Component.DriverDetail');
                            	}
//                            	var identifier = selData[0].get('identifier');
//            	                if(identifier) G_MonitMap.getInfo(identifier);
                            }
                        },{
                            text:'考勤记录',
                            handler:function(){
                            	Ext.create('MyApp.Component.DriverLogListWindow');
                            }
                        },{
                            text:'历史轨迹',
                            handler:function(){
                            	//延时请求历史轨迹
                            	var text = this.text;
                            	var i=0;
                            	function al(){   
                            	   if(i < selData.length){
                            		   var checkData = selData[i];
                            		   setTimeout(function(){
                            			   Ext.getCmp('apptabs').open({title:text+':'+checkData.get('driverName'),url:'driverTrace.html?driverId='+checkData.get('driverId')});
                            			   al();
                            		   },500);
                            	   }
                            	   i++;
                            	}
                            	al();
                            }
                        },{
                            text:'行车统计',
                            handler:function(){
                            }
                        }]
                    });
                    recmenu.showAt(e.getXY());
                    
                    if(selData.length > 1){
                    	recmenu.items.each(function(item){   
                            	item.disable();   
                    	});
                    }
            	},
            	'itemdblclick': function(scope, record, item, index, e, eOpts ){
            		var selData = this.getSelectionModel().getSelection();
                	if( Ext.getCmp('detailWindow'+selData[0].get('driverId')));
                	else{
                	Ext.create('MyApp.Component.DriverDetail');
                	}
                
	            },
	            'expand':function(){
	            	store.load();
	            }
            }
       });
       
   },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});

