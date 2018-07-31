/* 
 * To change this template, choose Tools | Templates
 * 保存路径检测规则的车辆窗口
 */
Ext.define('MyApp.Component.SaveRuleWindow',{
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
        
        var lineId = this.getCheckedModel('gridline_id').getData().lineId;
        var uids = '';
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
                url: CRMApp.Configuration.servicePath + '?cmd=guanhuiTeamsCar&lineId='+this.getCheckedModel('gridline_id').getData().lineId,
                //url: 'data.json?lineId='+this.getCheckedModel('gridline_id').getData().lineId,
                id: "tc",
                listeners:{
                  checkChange: function (node, state){
                     alert(record.data.id);
                     uids += record.data.uid;
                      Ext.getCmp('tc').setValue(uids);
                  }
                }
            }],
            buttons:[{
                text:'完成',
                handler:function(){
                    var vehicleIds = Ext.getCmp('tc').getValues();
                    alert(vehicleIds)
                    if(vehicleIds.length == 0) {
                        Ext.Msg.alert('系统提示','请选择至少一辆车遵从当前路径规则');
                        return;
                    }
                    Ext.ajaxModelLoader('MyApp.Model.SaveRules', {
                        params:{
                            lineId: lineId,
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

