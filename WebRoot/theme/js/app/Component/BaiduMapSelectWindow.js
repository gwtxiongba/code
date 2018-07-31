
Ext.define('MyApp.Component.BaiduMapSelectWindow',{
    extend: 'Ext.window.Window',
    constructor: function(id){
    	
    	var w = this;
    	
    	var mapPanel = Ext.create('Ext.panel.Panel',{
    					height:500,
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
                text: '搜索地图',
                handler: function(){
					document.getElementById('mapframe').contentWindow.searchPla(Ext.getCmp('locSearch').getValue());
					
                }
    	}
        	
    	var panels = Ext.create('Ext.panel.Panel',{
		        region:'center',
	           	modal:true,
	            items:[{
		                region: 'north',
						layout: 'table',
						border: false,
						items:[locText,schBn]},mapPanel],
	            listeners:{
	            	render:function(){
	            	},
	            	close:function(){
	            	}
	            }
    	})
    	
        this.superclass.constructor.call(this,{
           title: '发送位置',
           width: 600,
           height: 450,
           resizable: false,
           layout: 'border',
           modal:true,
           items:[panels],
           buttons:[{
                text:'确认位置',
                handler:function(btn) {
                	pointx = document.getElementById('mapframe').contentWindow.pointX,
                	pointy = document.getElementById('mapframe').contentWindow.pointY,
//                	Ext.getCmp('parkingPosition').setValue(pointx+','+pointy) ;
//                	Ext.getCmp('parkingAddress').setValue(selecstreet) ;
                	Ext.getCmp(id).setValue(pointx+','+pointy) ;
                	Ext.getCmp(id+'_address').setValue(selecstreet) ;
                    w.close(); 
                }
            },{
                text: '退出',
                handler:function(btn) {
                   w.close();        
                }
            }]
        });
        this.show();
    }
});



