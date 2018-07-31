/* 
 * 路径编辑窗口
 */
Ext.define('MyApp.Component.PolygonEditWindow',{
    extend:'Ext.window.Window',
    config:{
       getCheckedModel:function(grid_id){
           var models = Ext.getCmp(grid_id).getSelectionModel().getSelection();
           return models ? models[0] : null;
       }
    },
    constructor:function(){
        var w = this;
        var ckModel = this.getCheckedModel('gridpolygon_id');
        var drawPolygonMap = null;
        
        var hareaname = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'areaName'
        })
        
        var harea = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'area'
        });
        
        var hareaid = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'areaId',
            value: !!ckModel ? ckModel.getData().areaId : 0,
            disabled: !ckModel
        });
        
        
        var formPanel = Ext.create('Ext.form.Panel',{
            frame:false,
            border : false,
            layout:'border',
            region : 'center',
            items : [hareaname,harea,hareaid],
            html:'<div id="mapCanvus" style=" height:100%;">地图加载中..</div>',
            listeners:{
                afterrender:function(){
                    setTimeout(function(){
                        drawPolygonMap = Ext.create('MyApp.Component.DrawPolygonMap');
                        drawPolygonMap.onPolygonComplete(function(){
                            Ext.getCmp('btn_draw').setText('绘制');
                        });
                        if(ckModel) drawPolygonMap.draw(ckModel.getData().area);
                    },0);
                }
            }
        });
        
        
        var win = Ext.create('MyApp.Component.PolygonVarsWindow',{
            id:'PolygonEditWindow_id',
            click:function(v){
                hareaname.setValue(v.name);
                harea.setValue(drawPolygonMap.getValue());
                Ext.ajaxModelLoader('MyApp.Model.SavePolygon',{
                  params:formPanel.getForm().getValues(),
                  target: w
                }).request();
            }
        });
        
        this.superclass.constructor.call(this,{
            title:!ckModel ? '添加多边形域' : '编辑多边形域',
            width:800,
            height:450,
            resizable:false,
            layout:'border',
            modal:true,
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
                    drawPolygonMap.search(Ext.getCmp('skey_id').getValue());
                }
            },{
                xtype: 'tbfill'
            },{
                id:'btn_draw',
                text:'绘制',
                handler:function(){
                    var btn = this;
                    drawPolygonMap.changeModel(function(flag){
                        if(flag) btn.setText('查看');
                        else btn.setText('绘制');
                    });
                    
                    
                }
            },{
                xtype: 'tbseparator'
            },{
                text:'清除',
                handler:function(){
                    drawPolygonMap.clear();
                }
            }],
            items: formPanel,
            buttons:[{
                text:'完成',
                handler:function(){
                    if(ckModel) {
                        win.setValues({
                           name:ckModel.getData().areaName
                        });
                    }
                    if(drawPolygonMap.hasPolygon()) win.show();
                    else Ext.Msg.alert('系统提示','必须在地图上绘制一块多边形区域');
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


