/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.CarTreePanel',{
   extend:'Ext.tree.Panel',
   config:{id:'CarTreePanel_id', storeModel: null},
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
            }
            
        });
        
       
        this.superclass.constructor.call(this,{
            layout:'fit',
            store:treeStore,
            rootVisible: false
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

