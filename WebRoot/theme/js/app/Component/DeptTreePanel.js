/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.DeptTreePanel',{
   extend:'Ext.tree.Panel',
   config:{id:'DeptTreePanel_id', storeModel: null},
   constructor:function(){
        var tree = this;
        //定义初步树结构以及根节点
        var o = MyApp.Base.LocalSet.getGolbal();
        var name = o.company.name;
        var treeStore = Ext.create('Ext.data.TreeStore', {	
           fields: [
                     { name: 'id', type:'string'},
                     { name: 'text', type:'string'},
                     { name: 'uid', type:'string'}
                    ],
             proxy: {
                type: 'ajax',
                url: CRMApp.Configuration.servicePath + '?cmd=getDeptTeam&key=teams',
                reader: {  
		            type: 'json' ,
		             root: 'children'
		        }
            },
             root: {
                 expanded: true,
                 text:name,
                 iconCls:'com_ic' 
                
            }
        });
        
        //从后台获取数据源来添加树节点
       
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
            listeners:{
            	'itemcontextmenu':function(menutree,record,items,index,e){
                    e.preventDefault();
                    e.stopEvent();
                    
                    var nodemenu;
                   
                    //判断是否为叶子结点
                    if(record.data.leaf == true){
                        nodemenu = new Ext.menu.Menu({
                            floating:true,
                            items:[{
                                text:'刷新',
                                handler:function(){
                                	tree.getRootNode().removeAll(false);
                                	treeStore.load();
                                }
                            }]
                        });
                        nodemenu.showAt(e.getXY());
                        
                    }else{//非叶子节点菜单
                    	
                    	nodemenu = new Ext.menu.Menu({
                            floating:true,
                            items:[{
                                text:'刷新',
                                handler:function(){
                                	tree.getRootNode().removeAll(false);
                                	treeStore.reload();
                                }
                            }
                           ]
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
                            	item.disable(); 
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
    	//alert(this.store);
    	this.store.reload();
    }
});

