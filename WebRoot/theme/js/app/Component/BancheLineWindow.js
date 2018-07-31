/* 
 * 路径编辑窗口
 */
 
Ext.define('MyApp.Component.BancheLineWindow',{
    extend:'Ext.window.Window',
    config:{
       getCheckedModel:function(grid_id){
           var models = Ext.getCmp(grid_id).getSelectionModel().getSelection();
           return models ? models[0] : null;
       }
    },
    constructor:function(lingGps){
        var w = this;
        var ckModel = this.getCheckedModel('VehicleBancheGridPanel_id');
        var drawLineMap = null;
        
        var hlinename = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'lineName'
        })
        
        var hdist = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'distance'
        });
        
        var hpath = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'path'
        });
        
        var hradius = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'radius'  
        });
        
        var hlineid = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'lineId',
            value: !!ckModel ? ckModel.getData().lineId : 0,
            disabled: !ckModel
        });
        
        
        var formPanel = Ext.create('Ext.form.Panel',{
            frame:false,
            border : false,
            layout:'border',
            region : 'center',
            items : [hlinename,hdist,hpath,hradius,hlineid],
            html:'<div id="mapCanvus" style=" height:100%;">地图加载中..</div>',
            listeners:{
                afterrender:function(){
                    setTimeout(function(){
                        drawLineMap = Ext.create('MyApp.Component.BancheDrawLineMap');
                        if(ckModel) drawLineMap.drawLine(lingGps);
                        
                    },0);
                }
            }
        });
        
        
        var win = Ext.create('MyApp.Component.LineVarsWindow',{
            id:'LineEditWindow_id',
            click:function(v){
                hlinename.setValue(v.name);
                hradius.setValue(v.radius);
                hdist.setValue(Math.round(drawLineMap.getDistance()/10)/100);
                hpath.setValue(drawLineMap.getPoints());
                Ext.ajaxModelLoader('MyApp.Model.SaveLine',{
                  params:formPanel.getForm().getValues(),
                  target: w
                }).request();
            }
        });
     
     this.superclass.constructor.call(this,{
            title:!ckModel ? '添加路径' : '查看路径',
            width:800,
            height:400,
            resizable:false,
            layout:'fit',
            modal:true,
            maximizable:true, 
           padding:20,
            tbar:[{
                id:'skey_id',
                xtype:'textfield',
                fieldLabel:'关键字',
                labelAlign: 'right',
                labelWidth:40,
                blankText:'省/市/地区/街区/IPO',
                width:160    
            },{
                text:'查询',
                handler:function(){
                    drawLineMap.search(Ext.getCmp('skey_id').getValue());
                }
            },{
                xtype: 'tbfill'
            },{
                text:'绘制',
                 hidden:true,
                handler:function(){
                    var btn = this;
                    drawLineMap.changeModel(function(flag){
                        if(flag) btn.setText('查看');
                        else btn.setText('绘制');
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                text:'回退',
                 hidden:true,
                handler:function(){
                    drawLineMap.redo();
                }
            },{
                xtype: 'tbseparator'
            },{
                text:'清除',
                 hidden:true,
                handler:function(){
                    drawLineMap.clearLine();
                }
            }],
            items: formPanel,
            buttons:[{
                text:'完成',
                hidden:true,
                handler:function(){
                    if(ckModel) {
                        win.setValues({
                           name:ckModel.getData().lineName,
                           radius:ckModel.getData().radius
                        });
                    }
                    if(drawLineMap.getDistance() > 0) {
                    
                     Ext.getCmp('lineGps').setValue(drawLineMap.getPoints());
                      w.close();
                    }
                    else Ext.Msg.alert('系统提示','必须在地图上绘制一条路径');
                }
            },{
                text:'关闭',
                handler:function(){
                    w.close();
                }
            }],
            listeners:{
                close:function(){
                    win.close();
                }
            }
        });
   
        this.show();

    
    }
});


