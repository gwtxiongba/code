
Ext.define('MyApp.Component.MapLocWindow',{
    extend: 'Ext.window.Window',
    config : {
		id : 'MapLocWindows_id'
	},
    constructor: function(){
    	
    	var w = this;
    	
    	var mapPanel = Ext.create('Ext.panel.Panel',{
    					height:600,
//			           	loader: {
//						loadMask: { 
//						msg: "加载中……" 
//						}, 
//						//renderer: "html ", 
//						scripts: true, 
//						autoLoad: true, 
//						url: "locSend.html" 
//						}
    					html:"<iframe frameborder='0' id='mapframe' Scrolling=no width='100%' height='100%' src='locSend.html'> </iframe>"
					})
			
		
    	var locText = {
	            xtype: 'textfield',
	            blankText: '请输入目标位置',
	            //fieldLabel : '',
	            labelAlign:'right',
	            margin:'10 10 10 10 ',
	            height:23,
	            widht:250,
	            id:'locSearch'
    	}
        
    	var schBn = {
                Width: 45,
                xtype: 'button',
                margin:'10 10 10 0',
                text: '查询',
                handler: function(){
					document.getElementById('mapframe').contentWindow.searchPla(Ext.getCmp('locSearch').getValue());
                }
    	}
    	
    	var label1 ={
        	xtype:'label',
//        	height:15,
//			width: 30,
			margin:'0 0 0 300',
        	text:'选定街道:'
        	}
        
        var label2 ={
        	xtype:'label',
        	id:"selectedloc",
//        	height:15,
//			width: 30,
        	text:'待选'
        	}
        	
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
    	var panels = Ext.create('Ext.panel.Panel',{
		        region:'center',
	           	modal:true,
	            items:[{
		                region: 'north',
						layout: 'table',
						border: false,
						items:[locText,schBn,label1,label2]},mapPanel],
	            listeners:{
	            	render:function(){
	            	},
	            	close:function(){
	            		
	            	}
	            }
    	})
    	
        this.superclass.constructor.call(this,{
           title: '发送位置',
           width: 800,
           height: 700,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[panels],
           buttons:[{
                text:'确认位置',
                handler:function(btn) {
                	pointx = document.getElementById('mapframe').contentWindow.pointX,
                	pointy = document.getElementById('mapframe').contentWindow.pointY,
                	Ext.getCmp('pointx').setValue(pointx) ;
                	Ext.getCmp('pointy').setValue(pointy) ;
                	if(pointx)
                		Ext.getCmp('locbn').setIconCls('loc-on'); 
                    w.close(); 
                }
            },{
                text: '退出',
                handler:function(btn) {
                   w.close();        
                }
            }],
            listeners:{
            	afterrender:function(){
//            		mapPanel.on('render',function(){
//            			alert(222)
//            			if(Ext.getCmp('pointx').getValue()){
//            				alert(111)
//            				document.getElementById('mapframe').contentWindow.addMarkerPoint(Ext.getCmp('pointx').getValue(),Ext.getCmp('pointy').getValue())
//            			}
//            		})
            	}
            }
        });
        this.show();
    }
});



