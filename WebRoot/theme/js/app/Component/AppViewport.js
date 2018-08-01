Ext.define('MyApp.Component.AppViewport', {
   extend: 'Ext.container.Viewport',
   constructor: function() {
        var mytbar = Ext.create('MyApp.Component.AppToolbar');
       // var leftPanel = Ext.create('MyApp.Component.AppLeftPanel');
          var carTree =  Ext.create('MyApp.Component.CarTreePanel');
        this.superclass.constructor.call(this,{
            id:'appViewport',
            layout:'border',
            items:[{
                tbar: mytbar,
                region:'north',
                border:false,
                height:40
            },{
                id: 'leftPanel',
                title:'功能导航',
                region:'west',
                collapsible:true,
                width: 240,
                maxSize:240,
                layout:'fit',
                items:[carTree]
            
            },{
                id: 'appmain',
                region:'center',
                layout:'border',
                border:false,
                collapsible:false
            },{
                region: 'south',
                xtype: 'toolbar',
                id: 'main_bottom',
                height: 9
            }]
        });
    }
});