/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.CarTreePanel',{
   extend:'Ext.tree.Panel',
   config:{id:'CarTreePanel_id', storeModel: null},
   constructor:function(){
        var tree = this;
        var o = MyApp.Base.LocalSet.getGolbal();
        var name = o.company.name;
        var treeStore = Ext.create('Ext.data.TreeStore', {	
           fields: [
                     { name: 'id', type:'string'},
                     { name: 'text', type:'string'},
                     { name: 'uid', type:'string'},
                     { name: 'checked', type:'boolean'},
                     { name: 'ifOff', type:'string'},
                     { name: 'iconCls', type:'string'}
                      ],
           proxy: {
                type: 'ajax',
                url: CRMApp.Configuration.servicePath + '?cmd=golbal&key=teams',
                reader: {  
		            type: 'json' ,
		            root: 'children'
		        }
            },
             root: {
                 //expanded: true,
                 text:name,
                 iconCls:'com_ic'
            }
        });
        
       
        this.superclass.constructor.call(this,{
            layout:'fit',
            store: treeStore,
            rootVisible: true,
            tbar:[{
                text:'展开全部',
                handler:function(){
                    tree.expandAll();
                }
            },'-',{
                text:'收起全部',
                handler:function(){
                    tree.collapseAll();
                }
            }],
            viewConfig : {   //checkbox联动
                onCheckboxChange : function(e, t) {
                    var item = e.getTarget(this.getItemSelector(), this.getTargetEl()), record;
                    if (item){
                        record = this.getRecord(item);
                        var check = !record.get('checked');
                        record.set('checked', check);
                        if (check) {
                            record.bubble(function(parentNode) {
                                parentNode.set('checked', true);
                                parentNode.expand(false, true);
                            });
                            record.cascadeBy(function(node) {
                                node.set('checked', true);
                                node.expand(false, true);
                            });
                        } else {
                            record.cascadeBy(function(node) {
                                node.set('checked', false);
                            });
                        }
                    }
                },
                plugins :{  
                    ptype:'treeviewdragdrop',  
                    appendOnly:true        
                },  
                listeners:{
                    drop:function(node,data,overModel,dropPosition,options){  
                       
                    	if(data.records[0].get('uid')){
                    		Ext.ajaxModelLoader('MyApp.Model.UpdateTeam',{target: tree, params: {uid:data.records[0].get('uid'),teamId:overModel.get("id")}}).request();
                    	}
                    },  
                    beforedrop:function(node,data,overModel,dropPosition,dropFunction,options){  
                         tree.collapseAll();
                    	if(data.records[0].get('uid')){
                    	
                      } else{
                    	  return false;
                      }
                    }  
                }  
            },
            listeners:{
            	'itemcontextmenu':function(menutree,record,items,index,e){
                    e.preventDefault();
                    e.stopEvent();
                    var nodemenu;
                    if(record.data.leaf == true){
                        nodemenu = new Ext.menu.Menu({
                            floating:true,
                            items:[{
                                text:'刷新',
                                icon:'./theme/imgs/icons/shuaxin.png',
                                handler:function(){
                                	tree.getRootNode().removeAll(false);
                                	treeStore.load();
                                }
                            },{
                                text:'车辆定位',
                                icon:'./theme/imgs/icons/cldw.png',
                                handler:function(){
                                	var identifier = record.get('id');
                	                if(identifier) G_MonitMap.getInfo(identifier);
                                }
                            },{
                                text:'历史回放',
                                icon:'./theme/imgs/icons/lshf.png',
                                handler:function(){
                                	var model = menutree.getSelectionModel().getSelection()[0];
                                	var carid = model.get('uid');
                                	var text = model.get('text');
                                	Ext.getCmp('apptabs').open({title:'历史回放:'+text,url:'trace.html?vehicleId='+carid});
                                	
                                }
                            },{
                            	text:'超速设置',
                            	icon:'./theme/imgs/icons/cssz.png',
                            	handler:function(){
                            		var selNodes = tree.getChecked();
	                            	var identifier = record.get('id');
	                            	if(identifier) G_MonitMap.getInfo(identifier);
	                            	if(!Ext.getCmp('speedWindowShow_id')){
	                            		Ext.create('MyApp.Component.SpeedWindow');
	                            		Ext.getCmp('speedWindowShow_id').setTitle("超速报警的速度设置");
                            			Ext.getCmp('speedWindowShow_id').setValue(identifier);
	                            	}
                                }
//                            
                           },/*{
                            	text:'位置点上传时间间隔设置',
                            	handler:function(){
                            		var selNodes = tree.getChecked();
	                            	var identifier = record.get('id');
	                            	if(identifier) G_MonitMap.getInfo(identifier);
	                            	if(!Ext.getCmp('posTimeWindowShow_id')){
	                            		Ext.create('MyApp.Component.PosTimeWindow');
	                            		Ext.getCmp('posTimeWindowShow_id').setTitle("位置点上传时间间隔设置");
                            			Ext.getCmp('posTimeWindowShow_id').setValue(identifier);
	                            	}
                            	}
                            },*/{
                            	text:'实时车况监测',
                            	icon:'./theme/imgs/icons/jrjk.png',
                            	handler:function(){
                            		if(!Ext.getCmp('OBDMonitorWindow_id')){
                                                var vehid = record.get('uid');
                                                var plate = record.get('text');
	                            		var identifier = record.get('id');
                                                Ext.create('MyApp.Component.OBDMonitorWindow').setValue(vehid,plate,identifier);
                            		}
                            	}
                            },{
                                text:'加入监控列表',
                                icon:'./theme/imgs/icons/jrjk.png',
                                handler:function(){
                                    var vehicleIds = [];
                                    var selNodes = tree.getChecked();
	                                Ext.each(selNodes, function(node){
	                                    if(node.data.leaf == true){
	                                    	vehicleIds.push(node.data.uid);
	                                    }
                                    	node.set('checked', false);
                                    });
	                            if(vehicleIds.length == 0) vehicleIds.push(record.get('uid'));    
                                    Ext.ajaxModelLoader('MyApp.Model.SaveMonitor',{
                                        params:{
                                        	vehicleIds: vehicleIds.join(','),
                                        	ifMonitor:1
                                        }
                                    }).request();
                                    
                                }
                            }]
                        });
                        nodemenu.showAt(e.getXY());
                        
                    }else{//非叶子节点菜单
                    	
                    	record.cascadeBy(function(node) {
                            node.set('checked', true);
                            node.expand(false, true);
                        });
                    	nodemenu = new Ext.menu.Menu({
                            floating:true,
                            items:[{
                                text:'刷新',
                                icon:'./theme/imgs/icons/shuaxin.png',
                                handler:function(){
                                	tree.getRootNode().removeAll(false);
                                	treeStore.reload();
                                }
                            },
                            {
                                text:'加入监控列表',
                                icon:'./theme/imgs/icons/jrjk.png',
                                handler:function(){
                                    var vehicleIds = [];
                                    var selNodes = tree.getChecked();
	                                Ext.each(selNodes, function(node){
	                                    if(node.data.leaf == true){
	                                    	vehicleIds.push(node.data.uid);
	                                    }
                                    	node.set('checked', false);
                                    });
                                    
                                    Ext.ajaxModelLoader('MyApp.Model.SaveMonitor',{
                                        params:{
                                        	vehicleIds: vehicleIds.join(','),
                                        	ifMonitor:1
                                        }
                                    }).request();
                                }
                            }]
                        });
                        nodemenu.showAt(e.getXY());
                    }
                    
                    //树置灰逻辑添加
                    var selNodes = tree.getChecked();
                    var i = 0;
                    Ext.each(selNodes, function(node){
                        if(node.data.leaf == true){
                        	i++;
                        }
                    });
                    
                    if(record.get('replay') =="" || record.get('replay')== null || record.get('replay') == undefined || record.get('replay')== 0){
                    	nodemenu.items.each(function(item){   
                    		if(item.text =='油电操作'){
                    		}
                    	});
                    }
                    
                    if(i > 1){
                    	nodemenu.items.each(function(item){   
                    		if(item.text == '车辆定位' || item.text == '车辆司机规则信息' || item.text == '实时车况监测'|| item.text =='油电操作'
                    			|| item.text == '历史回放' || item.text =='超速设置' || item.text =='位置点上传时间间隔设置'){
                            	item.disable(); 
                    		}
                    	});
                    }
            	},
	            'itemdblclick': function(scope, record, item, index, e, eOpts ){
	                if(record.data.leaf == true){
	                var identifier = record.data.id;
	                if(identifier) {
                            Ext.getCmp('apptabs').setActiveTab(0);
                            G_MonitMap.getInfo(identifier);
                        }
                        }
	            }
            }
            
       })
    },
    getCheckedModel : function() {
        var sel = this.getSelectionModel().getSelection();
        return sel[0] ? sel[0] : null;
        
    },
    refreshTree: function() {
    	this.getRootNode().removeAll(false);
    	this.store.reload();
    },
    refreshSt: function(onlinC,total) {
    	var o = MyApp.Base.LocalSet.getGolbal();
        var name = o.company.name;
    	 if(onlinC != undefined){
    	this.getRootNode().set('text',name+"("+onlinC+"/"+total+")");
    	}
    }
});

