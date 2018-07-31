Ext.define('MyApp.Component.CarScanDiaryPanel',{
    extend:'Ext.grid.Panel',
    config:{id:'CarScanDiaryPanel_id'},
    constructor:function(){
        
        var store = Ext.ajaxModelLoader('MyApp.Model.DetectionDiaryList');
    
        var checkOptions = ['动力总成','底盘悬挂系统','车身系统','网络通讯系统'];
        
        var setBlod = function(val, ifread){
        	return '<b>'+val+'</b>';
        };
        
        this.superclass.constructor.call(this,{
            frame: true,
            border:true,
            //width:'100%',
            //maxWidth:'100%',
            height:295,
            autoScroll: true,
            scroll:'vertical',
            store:store,
            columns: [
                {xtype: 'rownumberer'},    
                {text:'错误码相关信息', dataIndex: 'info', sortable: false, menuDisabled:true, width:'100%',
                	renderer:function(val, meta, store){ 
                		meta.attr = 'style=" white-space:normal!important;"';     
                		var str = '';
                		str += '<h3>' + store.data.plate +'</h3><br>';
                		if(val.pcount != null && val.pcount != undefined){
                			str += '<h5>' + checkOptions[0] + '(' + val.pcount + ')</h5><br>';
                			var pdata = val.p;
                			for(var i = 0;i<pdata.length;i++){
                				var data = pdata[i];
                				str += '错误码: ' + data.pcode + '<br><br>';
                				str += '说明: ' + data.title + '<br><br>';
                				str += '描述: ' + data.info + '<br><br><br>';
                			}
                		}
                		if(val.bcount != null && val.bcount != undefined){
                			str += '<h5>' + checkOptions[1] + '(' + val.bcount + ')</h5><br>';
                			var bdata = val.b;
                			for(var i = 0;i<bdata.length;i++){
                				var data = bdata[i];
                				str += '错误码: ' + data.pcode + '<br><br>';
                				str += '说明: ' + data.title + '<br><br>';
                				str += '描述: ' + data.info + '<br><br><br>';
                			}
                		}
                		if(val.ccount != null && val.ccount != undefined){
                			str += '<h5>' + checkOptions[2] + '(' + val.ccount + ')</h5><br>';
                			var cdata = val.c;
                			for(var i = 0;i<cdata.length;i++){
                				var data = cdata[i];
                				str += '错误码: ' + data.pcode + '<br><br>';
                				str += '说明: ' + data.title + '<br><br>';
                				str += '描述: ' + data.info + '<br><br><br>';
                			}
                		}
                		if(val.ucount != null && val.ucount != undefined){
                			str += '<h5>' + checkOptions[3] + '(' + val.ucount + ')</h5><br>';
                			var udata = val.u;
                			for(var i = 0;i<udata.length;i++){
                				var data = udata[i];
                				str += '错误码: ' + data.pcode + '<br><br>';
                				str += '说明: ' + data.title + '<br><br>';
                				str += '描述: ' + data.info + '<br><br><br>';
                			}
                		}
                		
                		return str;
                	}
                }
            ],
            tbar: [{
                id:'date',
                xtype: 'datefield',
    			format: 'Y-m-d',
                labelWidth: 40,
                labelAlign:'right'
            },{
                text: '查询',
                border: true,
                handler: function(){
                    store.load({params:{date:Ext.getCmp("date").getValue() }});
                }
            }]
        });
    },
    getCheckedModel : function() {
        var models = this.getSelectionModel().getSelection();
        return models ? models[0] : null;
    }
});



