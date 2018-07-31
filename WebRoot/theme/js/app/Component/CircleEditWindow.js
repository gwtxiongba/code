/* 
 * 圆路径编辑窗口
 */
Ext.define('MyApp.Component.CircleEditWindow',{
    extend:'Ext.window.Window',
    config:{
       getCheckedModel:function(grid_id){
           var models = Ext.getCmp(grid_id).getSelectionModel().getSelection();
           return models ? models[0] : null;
       }
    },
    constructor:function(){
        var w = this;
        var ckModel = this.getCheckedModel('gridcircle_id');
        var drawCircleMap = null;
        
        var hareaname = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'areaName',
            value: !!ckModel ? ckModel.getData().areaName : ''
        });
        
        var harea = Ext.create('Ext.form.field.Hidden',{
            xtype:'hiddenfield',
            name:'area',
            value: !!ckModel ? ckModel.getData() : ''
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
                        drawCircleMap = Ext.create('MyApp.Component.DrawCircleMap');
                         drawCircleMap.onCircleComplete(function(){
                            Ext.getCmp('btn_draw').setText('绘制');
                        });
                        if(ckModel) {
                            var vars = ckModel.get('area').split(',');
                            drawCircleMap.draw(vars[0],vars[1],vars[2]);
                        }
                    },0);
                }
            }
        });
        
        
        var win = Ext.create('MyApp.Component.CircleVarsWindow',{
            id:'CircleVarsWindow_id',
            click:function(v){
                hareaname.setValue(v.name);
                harea.setValue(drawCircleMap.getValue());
                Ext.ajaxModelLoader('MyApp.Model.SaveCircle',{
                   params: formPanel.getForm().getValues(),
                   target:w
                }).request();
            }
        });
        /*
        win.on('hide',function(){
            drawCircleMap.clear();
        });*/
        
        this.superclass.constructor.call(this,{
            title:!ckModel ? '添加圆形域' : '编辑圆形域',
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
                    drawCircleMap.search(Ext.getCmp('skey_id').getValue());
                }
            },{
                xtype: 'tbfill'
            },{
                id:'btn_draw',
                text:'绘制',
                handler:function(){
                    var btn = this;
                    drawCircleMap.changeModel(function(flag){
                        if(flag) btn.setText('查看');
                        else btn.setText('绘制');
                    });
                }
            },{
                xtype: 'tbseparator'
            },{
                text:'清除',
                handler:function(){
                    drawCircleMap.clear();
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
                    if(drawCircleMap.hasCircle()) win.show();
                    else Ext.Msg.alert('系统提示','必须在地图上绘制一块圆形区域');
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


