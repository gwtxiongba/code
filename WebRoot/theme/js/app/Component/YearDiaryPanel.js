/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.YearDiaryPanel',{
    extend:'Ext.panel.Panel',
    config:{id:'ydiarypanel',storeModel:null,thisvehicleId:null,thisDateTime:null},
    constructor:function(){
   
	   this.thisvehicleId = MyApp.Base.Page.getQuery('vehicleId');;
       this.thisDateTime = new Date().format('yyyy-MM-dd');
       var thisMonth = this.thisDateTime.substring(0,7);

       var store = Ext.create('Ext.data.Store', {
    		fields:['code','total'],
    		proxy: {
                type: 'ajax',
                url : CRMApp.Configuration.servicePath + '?cmd=yearDriveLogs&date=' + 
		    		  thisMonth+ '&vehicleId=' + this.thisvehicleId,
                reader: 'json'
            },
    		autoLoad: true,
            listeners:{
            	load:function(){
            	var i = 1;
            	var yearTime = Ext.getCmp('ydiarypanel').thisDateTime.substring(0,4);
            	var monthTime = Ext.getCmp('ydiarypanel').thisDateTime.substring(5,7);
            	var localTime = new Date().format('yyyy-MM-dd').substring(0,7);
            	var localYear = localTime.substring(0,4);
            	var timePlus;
            	store.each(function(dataRecord){
            	if(i<10){
            			timePlus = yearTime+'-0'+i;
            			}
            			else{
            			timePlus = yearTime+'-'+i;
            			}
            	var dataModel = dataRecord.get('total');
            	var noModel = dataRecord.get('code');
            	if(dataModel){
						var loghtml ='<B>'+timePlus+'</B><br>行驶里程 : '+dataModel.mile+' km<br>行驶时间 : '+dataModel.driveTime+
									   ' 小时<br> 油耗 : '+dataModel.fule+' '+' 升/百公里<br>最高速度 : '+dataModel.highestSpeed+
									   ' km/h<br>平均速度 : '+dataModel.tvspeed+' km/h<br>急加速 : '+dataModel.acceleration+
									   ' 次<br>急减速 : '+dataModel.brakes+' 次<br>超速行驶时间 : '+dataModel.speedU120+' 小时<br>晚上行驶时间 : '
									   +dataModel.driveTimeNight+' 小时<br>晚上行驶里程 : '+dataModel.mileNight+' km<br>疲劳驾驶 : '+dataModel.fatigueDriving;
	
		      	    	var ychildpanel = Ext.create('Ext.panel.Panel',{
		      	    		id:'ychildpanel'+timePlus,
		      	    		height:175,
        					margins : '0 4 4 0',
		    			    html:loghtml
		    		    });
						Ext.getCmp('ydiarypanel').add(ychildpanel);
						i++;
		            	}
            	 else if(noModel){
					    var nullychild = Ext.create('Ext.panel.Panel',{
							id:'nullychild'+timePlus,
							height:175,
        					margins : '0 4 4 0',
            			    html:'<B>'+timePlus+'</B><br>行驶里程 : '+0+' km<br>行驶时间 : '+0+
									   ' 小时<br> 油耗 : '+0+' '+'升/百公里<br>最高速度 : '+0+
									   ' km/h<br>平均速度 : '+0+' km/h<br>急加速 : '+0+
									   ' 次<br>急减速 : '+0+' 次<br>超速行驶时间 : '+0+' 小时<br>晚上行驶时间 : '
									   +0+' 小时<br>晚上行驶里程 : '+0+' km<br>疲劳驾驶 : '+0
		    		    });
		    		    Ext.getCmp('ydiarypanel').add(nullychild);
		    		    i++;
            		}
            	else{return}
            	})//each
            	}
            }
        });
    		
        this.storeModel = store;
        
        this.superclass.constructor.call(this,{
            frame: true,
            autoScroll:true,
            scroll:'vertical',
            width:'100%',
            maxWidth:'100%',
            layout: {
                type: 'vbox',
                align: 'stretch'
	        }
        });
        
    },
    addLogs: function(vehicleId,dateTimeIn){
    	if(this.thisDateTime.substring(0,4)==dateTimeIn.substring(0,4)&&this.thisDateTime.substring(5,7)>=dateTimeIn.substring(5,7))
    	{ }else{
    	this.thisvehicleId = vehicleId;
    	this.thisDateTime = dateTimeIn;
    	var thisMonth = this.thisDateTime.substring(0,7);
    	Ext.getCmp('ydiarypanel').removeAll();
    	this.storeModel.proxy.url = CRMApp.Configuration.servicePath + '?cmd=yearDriveLogs&date=' + 
		    						thisMonth+ '&vehicleId=' + this.thisvehicleId;
		this.storeModel.load();
    	}
    }
})