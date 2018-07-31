/* 
 * To change this template, choose Tools | Templates
 * 保存围栏检测规则的车辆窗口
 */
Ext.define('MyApp.Component.SaveRule1Window',{
    extend:'Ext.window.Window',
    requires:['Ext.ux.TreeCombo'],
    config:{
       getCheckedModel:function(grid_id){
           var models = Ext.getCmp(grid_id).getSelectionModel().getSelection();
           return models ? models[0] : null;
       }
    },
    constructor:function(){
        var w = this;
        var ckModel = this.getCheckedModel('gridrule_id');
        
        var areaId = this.getCheckedModel('gridpolygon_id').getData().areaId;
        
        this.superclass.constructor.call(this,{
            title:!ckModel ? '添加规则' : '编辑规则',
            width:300,
            height:100,
            border:false,
            resizable:false,
            layout:'border',
            modal:true,
            items:[{
                xtype: 'treecombo',
                editable:false,
                emptyText:'选择车辆',
                displayField: 'text',
                fields: ["uid", "text"],
                valueField: 'id',
                width: 200,
                url: CRMApp.Configuration.servicePath + '?cmd=guanhuiTeamsCar&lineId='+this.getCheckedModel('gridpolygon_id').getData().areaId,
               // url: 'data.json?areaId='+this.getCheckedModel('gridpolygon_id').getData().areaId,
                id: "tc"
            }],
            buttons:[{
                text:'完成',
                handler:function(){
                    var vehicleIds = Ext.getCmp('tc').getValues();
                    if(vehicleIds.length == 0) {
                        Ext.Msg.alert('系统提示','请选择至少一辆车遵从当前路径规则');
                        return;
                    }
                    Ext.ajaxModelLoader('MyApp.Model.SaveRules1', {
                        params:{
                            areaId: areaId,
                            vehicleIds:vehicleIds.join(',')
                        },
                        target: w
                    }).request();
                }
            },{
                text:'关闭',
                handler:function(){
                    w.close();
                }
            }]
        });
        this.show();
        
    }
});

