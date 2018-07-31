/* 
 * To change this template, choose Tools | Templates
 * MyApp.Component.VehicleGridPanel
 */
Ext.define('MyApp.Component.MonthDiaryPanel',{
    extend:'Ext.panel.Panel',
    config:{id:'mdiarypanel',last_Mtime:null},
    constructor:function(){
        var mpp = this;
        
        var thisvehicleId = MyApp.Base.Page.getQuery('vehicleId');;
		var thisDateTime = new Date().format('yyyy-MM-dd');
		
		var fieldTime = thisDateTime.substring(0,7)+'-01';
		var totalTime = thisDateTime.substring(0,7);
		mpp.last_Mtime = totalTime;
            	
    	var store = Ext.create('Ext.data.Store', {
    		fields:[fieldTime],
            proxy: {
                type: 'ajax',
                url: CRMApp.Configuration.servicePath + '?cmd=monthDriveLogs&date=' + 
    		thisDateTime + '&vehicleId=' + thisvehicleId,
                reader: 'json'
            },
            autoLoad: true,
            sortInfo: {
                field: 'dateTime',
                direction: 'ASC'
            },
            listeners:{
            	load:function(){
            	store.each(function(dataRecord){
            			var dataModels = dataRecord.get(fieldTime);
             			var timePlus;
             			//日视图
            			for(i=1,k=0;i<=Ext.getCmp('mdiarypanel').getMDays(thisDateTime);i++){
            			if(i<10){
            				timePlus = totalTime+'-0'+i;
            			}
            			else{
            				timePlus = totalTime+'-'+i;
            			}
            			if(timePlus<=thisDateTime){
            			var dataModel = dataModels[k];
            			if(dataModel&&timePlus == dataModel.dateTime){
						var loghtml ='<B>'+dataModel.dateTime+'</B><br>行驶里程 : '+dataModel.mile+' km<br>行驶时间 : '+dataModel.driveTime+
									   ' 小时<br> 油耗 : '+dataModel.fule+' '+' 升/百公里<br>最高速度 : '+dataModel.highestSpeed+
									   ' km/h<br>平均速度 : '+dataModel.tvspeed+' km/h<br>急加速 : '+dataModel.acceleration+
									   ' 次<br>急减速 : '+dataModel.brakes+' 次<br>超速行驶时间 : '+dataModel.speedU120+' 小时<br>晚上行驶时间 : '
									   +dataModel.driveTimeNight+' 小时<br>晚上行驶里程 : '+dataModel.mileNight+' km<br>疲劳驾驶 : '+dataModel.fatigueDriving;
            		    var mchildpanels = Ext.create('Ext.panel.Panel',{
            		    	id:'mchildpanels'+timePlus,
            			    html:loghtml,
            			    height:175,
            			    margins : '0 4 4 0'
            		    });
             			Ext.getCmp('mdiarypanel').add(mchildpanels);
             			k++;
                  	    }
            			else{
            				var nullmchild = Ext.create('Ext.panel.Panel',{
            			    id:'mchildpanels'+timePlus,
            			    height:175,
            				margins : '0 4 4 0',
            			    html:'<B>'+timePlus+'</B><br>行驶里程 : '+0+' km<br>行驶时间 : '+0+
									   ' 小时<br> 油耗 : '+0+' '+'升/百公里<br>最高速度 : '+0+
									   ' km/h<br>平均速度 : '+0+' km/h<br>急加速 : '+0+
									   ' 次<br>急减速 : '+0+' 次<br>超速行驶时间 : '+0+' 小时<br>晚上行驶时间 : '
									   +0+' 小时<br>晚上行驶里程 : '+0+' km<br>疲劳驾驶 : '+0
            		    });
            			Ext.getCmp('mdiarypanel').add(nullmchild);
            			}
            			}//if
            			else{
							return;            			
            			}
            			}//for
             			Ext.getCmp('mchildpanels'+thisDateTime).focus();
            		});
            	}
            }
        });
        //this.storeModel = store;
        
        this.superclass.constructor.call(this,{
            frame: true,
            autoScroll:true,
            scroll:'right',
            width:'100%',
            maxWidth:'100%',
            layout: {
                type: 'vbox',
                align: 'stretch'
	        },
	        listeners: {
	        }
        });
        
    },
    getMDays: function(gdateTime){
    	var year = gdateTime.substring(0,4)*1;
    	var month = gdateTime.substring(5,7)*1;
	    switch(month){
	    case 1:    case 3:    case 5:    case 7:
	    case 8:    case 10:   case 12:
	 return 31;
	    case 4:    case 6:    case 9:    case 11:
	 return 30;
	    case 2:
	 return (year%4==0&&year%100!=0||year%400==0)?29:28;
	  }
	  return 0;
 	},
 
    addLogs: function(vehicleId,dateTimeIn){
    	var lastTime = Ext.getCmp('mdiarypanel').last_Mtime;
    	var fieldTime =dateTimeIn.substring(0,7)+'-01';
    	var totalTime =  dateTimeIn.substring(0,7);
    	var thisDateTime = dateTimeIn;
    	var localTime = new Date().format('yyyy-MM-dd');
    	if(lastTime != totalTime){
    	Ext.getCmp('mdiarypanel').last_Mtime = dateTimeIn.substring(0,7);
    	Ext.getCmp('mdiarypanel').removeAll();
    	//store参数修改
    	var url = CRMApp.Configuration.servicePath + '?cmd=monthDriveLogs&date=' + 
    		dateTimeIn + '&vehicleId=' + vehicleId;

    	var store = Ext.create('Ext.data.Store', {
    		fields:['total',fieldTime],
            proxy: {
                type: 'ajax',
                url: url,
                reader: 'json'
            },
            autoLoad: true,
            sortInfo: {
                field: 'dateTime',
                direction: 'ASC'
            },
            listeners:{
            	load:function(){
            	store.each(function(dataRecord){
            			//var dataModel = this.storeModel.getAt(i);
            			//var dateTimeStr = "total";
            			//var fieldTime = thisDateTime.substring(0,7)+'-01';
            			var dataModels = dataRecord.get(fieldTime);
            			var ydataModel = dataRecord.get('total');
            			var timePlus;
             			//日视图
            			for(i=1,k=0;i<=Ext.getCmp('mdiarypanel').getMDays(thisDateTime);i++){
            			var dataModel = dataModels[k];
            			if(i<10){
            				timePlus = totalTime+'-0'+i;
            			}
            			else{
            				timePlus = totalTime+'-'+i;
            			}
            			if(timePlus<=localTime){
            			if(dataModel&&timePlus == dataModel.dateTime){
						var loghtml ='<B>'+dataModel.dateTime+'</B><br>行驶里程 : '+dataModel.mile+' km<br>行驶时间 : '+dataModel.driveTime+
									   ' 小时<br> 油耗 : '+dataModel.fule+' '+' 升/百公里<br>最高速度 : '+dataModel.highestSpeed+
									   ' km/h<br>平均速度 : '+dataModel.tvspeed+' km/h<br>急加速 : '+dataModel.acceleration+
									   ' 次<br>急减速 : '+dataModel.brakes+' 次<br>超速行驶时间 : '+dataModel.speedU120+' 小时<br>晚上行驶时间 : '
									   +dataModel.driveTimeNight+' 小时<br>晚上行驶里程 : '+dataModel.mileNight+' km<br>疲劳驾驶 : '+dataModel.fatigueDriving;
            		    var mchildpanels = Ext.create('Ext.panel.Panel',{
            		    	id:'mchildpanels'+timePlus,
            			    html:loghtml,
            			    height:175,
            			    margins : '0 4 4 0'
            		    });
             			Ext.getCmp('mdiarypanel').add(mchildpanels);
             			k++;
                  	    }
            			else{
            				var nullmchild = Ext.create('Ext.panel.Panel',{
            				id:'mchildpanels'+timePlus,
            				height:175,
            				margins : '0 4 4 0',
            			    html:'<B>'+timePlus+'</B><br>行驶里程 : '+0+' km<br>行驶时间 : '+0+
									   ' 小时<br> 油耗 : '+0+' '+'升/百公里<br>最高速度 : '+0+
									   ' km/h<br>平均速度 : '+0+' km/h<br>急加速 : '+0+
									   ' 次<br>急减速 : '+0+' 次<br>超速行驶时间 : '+0+' 小时<br>晚上行驶时间 : '
									   +0+' 小时<br>晚上行驶里程 : '+0+' km<br>疲劳驾驶 : '+0
            		    });
            			Ext.getCmp('mdiarypanel').add(nullmchild);
            			}
            			}//if
            			else{
            				return;
            			}
            			}//for
            		});
            	}
            }
        });
    }//if
        else{//只改变滚动条定位
        	//Ext.getCmp('mchildpanels'+thisDateTime).focus();
    }
    }
})


//选中显示
//             			if(flag)
//             			{	for(i=0;i<dataModels.length;i++){
//             				var dataModel = dataModels[i];
//            		    	if(dataModel.dateTime==dateTimeIn){
//            		    		var loghtml ='<B>'+dataModel.dateTime+'</B><br>油耗 : '
//						               +dataModel.fule+' '+'升/百公里<br>夜间行驶里程 : '+dataModel.mileNight+
//									   ' 公里<br>夜间行驶时间 : '+dataModel.driveTimeNight+' 小时<br>加速 : '+dataModel.acceleration+
//									   ' 次<br>高速行驶 : '+dataModel.speedU120+' 小时<br>行驶里程 : '+dataModel.mile+
//									   ' 公里<br>平均时速 : '+dataModel.tvspeed+'公里/小时<br>最高时速 : '+dataModel.highestSpeed+
//									   ' 公里/小时<br>刹车次数 : '+dataModel.brakes+' 次<br>行驶时间 : '+dataModel.driveTime+
//									   ' 小时<br>疲劳驾驶 : '+dataModel.fatigueDriving;
//            		    		var childpanel = Ext.create('Ext.panel.Panel',{
//	            			    html:loghtml
//	            		    });
//            		    	Ext.getCmp('mdiarypanel').add(childpanel);
//            		    	flag = 0;
//            		    	break;
//             				}
//             			}
//             			}
