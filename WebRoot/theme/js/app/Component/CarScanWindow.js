/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.CarScanWindow',{
    extend: 'Ext.window.Window',
    config:{
        id:'CarScanWindow_id'/*,
        minimizable: true*/
    },
    constructor:function(){
        var w = this;
        var errorCount = 0;
        var startTime = 0;
        
        var g = MyApp.Base.LocalSet.getGolbal();
        
        //var scanDate = MyApp.Base.LocalSet.getGolbal().scanDate;
        var scanStore = Ext.create('Ext.data.Store',{
            fields:['vehicleId','identifier','plate','codeCounts']
        });
        
        var imgConfig = {
            xtype:'image',
            width:102,
            height:102,
            style:'position:absolute',
            x:24,
            y:24,
            src:'theme/imgs/scan.gif'
                  
        }
        
        var markConfig = {
            id:'markLabel',
            xtype:'label',
            style:'position:absolute;font-size:30px;text-align:center;line-height:102px',
            cls:'ALevel',
            width:102,
            height:102,
            x: 24,
            y: 24,
            text:'100分'
        }
        
        var processBarConfig = {
            layout:'hbox',
            border:false,
            items:[{
                id:'processbar',
                xtype:'progressbar',
                layout:'column',
                text:'点击按钮开始...',
                width:450
            },{
                id:'seabtn',
                xtype:'button',
                text:'扫描',
                style:'width: 50px; height:20px; border:solid 1px #6594CF; margin:0 10px;',
                //disabled:true,
                handler:function(){
                    this.disable();
                    errorCount = 0;
                    startTime = new Date().getTime();
                    //缓存检测日期
                    g.scanDate = new Date().format('yyyy-MM-dd');
                    MyApp.Base.LocalSet.setGolbal(g);
                    processBar.updateProgress(0);
                    Ext.ajaxModelLoader('MyApp.Model.DoScan',{
                        success:function(o) {
                            scanStore.removeAll();
                            doScan(o);
                        }
                    }).request();
                }
            }]
        }
        
        var infoLabelConfig = {
            id:'infoLabel',
            xtype:'label',
            //text:'车队已经连续3天没有进行体检了，赶快开始吧！',
            style:'display:block; font-size:16px; color:#04468C; margin-top:20px;'
        }
        
        if(!g.scanDate) infoLabelConfig.text = '您的车队还没有做过车辆安全检测，快来试试吧！';
        else {
            var t = (g.scanDate + ' 00:00:00').toDate().getTime();
            var day = Math.floor((new Date().getTime() - t)/(24*3600000));
            if(day > 0) infoLabelConfig.text = '车队已经连续' + day + '天没有进行安全检测了，赶快开始吧！';
            else infoLabelConfig.text = '您的车队今天已经做过检测，可以查看检测报告或重新检测';
        }
        
        
        var checkOptions = ['动力总成','底盘悬挂系统','车身系统','网络通讯系统'];
        
        var scanGridPanel = Ext.create('Ext.grid.Panel',{
            layout:'fit',
            border:false,
            height: 295,
            autoScroll:true,
            columns: [
                {text:'车牌', dataIndex:'plate', width:100},
                {text:'设备码', dataIndex:'identifier',width:100},
                {
                    text:'结果', 
                    dataIndex:'codeCounts',
                    width:480,
                    renderer:function(v){
                        var arr = [];
                        for(var i = 0; i < v.length; i++) {
                            if(v[i]) arr.push('<span style="color:red">{0}({1})×</span>'.Format(checkOptions[i],v[i]));
                            else arr.push('<span style="color:green">{0}({1})√</span>'.Format(checkOptions[i],v[i]));
                        }
                        return arr.join(',');
                    }
                }
            ],
            store: scanStore
        });
        
        var diaryPanel =  Ext.create('MyApp.Component.CarScanDiaryPanel');
        
        var tabConfig = {
            xtype:'tabpanel',
            layout:'border',
            items:[{
                title:'扫描详情',
                items:[scanGridPanel]
            },{
                title:'检测报告',
                items:[diaryPanel]
            }]
        }
        
        w.superclass.constructor.call(w,{
           title:'车辆安全检测',
           width:730,
           height: 500,
           layout:'border',
           items:[{
               region:'west',
               width: 180,
               height:200,
               border:false,
               style:'line-height:20px;',
               items:[imgConfig,markConfig]
           },{
               region:'center',
               layout:{
                   type:'vbox',
                   pack:'center',
                   align:'center'
               },
               height:180,
               border:false,
               items:[processBarConfig,infoLabelConfig]
           },{
               region:'south',
               height:320,
               border:false,
               items:[tabConfig]
           }],
           listeners:{
               minimize:function(){
                   w.hide();
               }
           }
        });
        w.show();
        
        var processBar = Ext.getCmp('processbar');
        var markLabel = Ext.getCmp('markLabel');
        var infoLabel = Ext.getCmp('infoLabel');
        var level = '优秀';
        //检测执行函数
        var doScan = function(list) {
            var item = list.shift();
            if(item) {
                scanStore.insert(0,item);
                var totalCount = scanStore.getCount() + list.length;
                var step = parseInt(Math.floor(scanStore.getCount()*10)/totalCount)/10;
                for(var i=0; i<item.codeCounts.length;i++) {
                    if(item.codeCounts[i] > 0) {
                        errorCount ++;
                        break;
                    }
                }
                
                var timespan = parseInt((new Date().getTime() - startTime)/100)/10;
                var mark = 100-parseInt(errorCount*100/totalCount);
                infoLabel.setText('车队车辆安全检测中，请耐心等候...');
                if(mark>=90 && !markLabel.hasCls('ALevel')) {
                    markLabel.removeCls('DLevel');
                    markLabel.addCls('ALevel');
                    level = '优秀';
                } else if(mark <90 && mark >=80 && !markLabel.hasCls('BLevel')) {
                    markLabel.removeCls('ALevel');
                    markLabel.addCls('BLevel');
                    level = '良好';
                }
                else if(mark < 80 && mark >= 70 && !markLabel.hasCls('CLevel')) {
                    markLabel.removeCls('BLevel');
                    markLabel.addCls('CLevel');
                    level = '一般';
                }
                else if(mark < 70 && !markLabel.hasCls('DLevel')){
                    markLabel.removeCls('CLevel');
                    markLabel.addCls('DLevel');
                    level = '糟糕';
                }
                markLabel.setText(100-parseInt(errorCount*100/totalCount) + '分');
                
                var progressText = '已扫描 {0}/{1} 辆车，故障车辆 {2} 耗时{3}秒'.Format(scanStore.getCount(),totalCount,errorCount, timespan);
                processBar.updateProgress(step,progressText,true);
                setTimeout(function(){doScan(list)},parseInt(Math.random()*500));
            } else {
                Ext.getCmp('seabtn').enable();
                infoLabel.setText('检测完成，您的车队今天的评级为"' + level + '"');
            }
        }
    }
});

