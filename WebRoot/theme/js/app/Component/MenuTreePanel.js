/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.MenuTreePanel',{
   extend:'Ext.tree.Panel',
   config:{id:'MenuTreePanel_id', storeModel: null},
   constructor:function(){
        var tree = this;
        //var o = MyApp.Base.LocalSet.getGolbal();
      //  var name = o.company.name;
        var treeStore = Ext.create('Ext.data.TreeStore', {	
          autoLoad : true,
           fields: [
                     { name: 'id', type:'string'},
                     { name: 'text', type:'string'},
                     { name: 'uid', type:'string'},
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
                 text:"",
                 iconCls:'com_ic'
            }
            
        });
        
       
        this.superclass.constructor.call(this,{
            renderTo:Ext.getBody(),
            frame: true,
            border:true,
            width:'100%',
            height:'800px',
            maxWidth:'100%',
            autoScroll: false,
            scroll:'vertical',
            store:treeStore,
            rootVisible: true,
            listeners:{
            	'itemcontextmenu':function(menutree,record,items,index,e){
                    e.preventDefault();
                    e.stopEvent();
                    	var nodemenu = new Ext.menu.Menu({
                            floating:true,
                            items:[{
                                text:'添加子菜单',
                                icon:'./theme/imgs/icons/shuaxin.png',
                                handler:function(){
                                                var id = record.get('id');
                                                Ext.create('MyApp.Component.SaveMenuWindow').setValue(id);
                            		}
                            },
                            {
                                text:'修改子菜单',
                                icon:'./theme/imgs/icons/jrjk.png',
                                handler:function(){
                                     var id = record.get('id');
                                                Ext.create('MyApp.Component.SaveMenuWindow').setValue(id);
                                }
                            }]
                        });
                        nodemenu.showAt(e.getXY());
                    
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

