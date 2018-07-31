/* 
 * To change this template, choose Tools | Templates
 * 线路管理
 */
Ext.define('MyApp.Page.Lines',{
   extend:'MyApp.Base.Page',
   onInit:function(){
       var lineMap = null;
       
       var tab = top.Ext.getCmp('apptabs').getActiveTab();
       
       //顶部控制栏
       var tbar = [{
            id:'addLineBtn',
            text:'添加路径',
            handler:function(){
                grid_line.getSelectionModel().deselectAll();
                Ext.create('MyApp.Component.LineEditWindow');
                Ext.getCmp('editLineBtn').disable();
                Ext.getCmp('delLineBtn').disable();
            }
        },{
            xtype: 'tbseparator'
        },{
            id:'editLineBtn',
            text:'编辑路径',
            disabled:true,
            handler:function(){
                Ext.create('MyApp.Component.LineEditWindow');
            }
        },{
            xtype: 'tbseparator'
        },{
            id:'delLineBtn',
            text:'删除路径',
            disabled:true,
            handler:function(){
                Ext.Msg.confirm('系统确认','<span style="color:red">警告：该操作将会解除该路径下所有设置车辆规则</span><p>您确定要删除当前路径吗？</p>',function(opt){
                    if(opt=='yes') {
                        var record = grid_line.getSelectionModel().getSelection()[0];
                        Ext.ajaxModelLoader('MyApp.Model.DelLine',{
                            params:{
                                lineId: record.get('lineId')
                            },
                            success:function(data){
                                if(!data.status) {
                                    grid_line.getStore().reload();
                                    
                                    Ext.define('rule', {
                                        extend: 'Ext.data.Model',
                                        fields: ['ruleId','lineId','identifier','plate']
                                    });
        
                                    grid_rule.bindStore(Ext.create('Ext.data.Store',{
                                       model: 'rule',
                                       data:[]
                                    }));
                                    
                                    lineMap.clearOverlays();
                                    Ext.getCmp('editLineBtn').disable();
                                    Ext.getCmp('delLineBtn').disable();
                                    Ext.getCmp('addRuleBtn').disable();
                                    Ext.getCmp('delRuleBtn').disable();
                                    
                                } else Ext.create('MyApp.Component.LoadingTips',{
                                    lazytime:1000,
                                    msg: '删除失败，请重试'
                                });
                            }
                        }).request();
                    }
                });
            }
        }]
    
       var ruleStore = Ext.ajaxModelLoader('MyApp.Model.GetLineRules',{});
       
       var grid_line = Ext.create('Ext.grid.Panel',{
           id:'gridline_id',
           frame: true,
           border:true,
           autoScroll: false,
           scroll:'vertical',
           store:Ext.ajaxModelLoader('MyApp.Model.GetLines',{}),
           columns:[
               {xtype:'rownumberer'},
               {text:'路径名称', width:'35%',dataIndex:'lineName',sortable:false,menuDisabled:true},
               {text:'里程',dataIndex:'distance',sortable:false , renderer:function(v) {return v + ' 公里';}},
               {text:'偏移半径',dataIndex:'radius',renderer:function(v){
                  return v + ' 米';     
               }},
               {text:'创建时间',dataIndex:'createTime'}
           ],
           listeners:{
               itemclick : function(grid, record, item, index, e, eOpts) {
                   var lineId = record.get('lineId');
                   ruleStore.clearFilter(true);
                   ruleStore.filter('lineId',lineId);
                   grid_rule.bindStore(ruleStore);
                   lineMap.drawSingleLine(record.get('path'));
                   Ext.getCmp('editLineBtn').enable();
                   Ext.getCmp('delLineBtn').enable();
                   Ext.getCmp('addRuleBtn').enable();
                   Ext.getCmp('delRuleBtn').disable();
               }
           }
        });
        
        var grid_rule = Ext.create('Ext.grid.Panel',{
            id:'gridrule_id',
            frame : true,
            border:true,
            autoScroll: false,
            scroll:'vertical',
            height:200,
            tbar:[{
                id:'addRuleBtn',
                text:'添加规则',
                disabled:true,
                handler:function(){
                    grid_rule.getSelectionModel().deselectAll();
                    Ext.create('MyApp.Component.SaveRuleWindow');
                    Ext.getCmp('delRuleBtn').disable();
                }
            },{
                xtype: 'tbseparator'
            },{
                id:'delRuleBtn',
                text:'删除规则',
                disabled:true,
                handler:function(){
                    Ext.Msg.confirm('系统确认','您确定要删除当前规则吗？',function(opt){
                        if(opt=='yes') {
                            var record = grid_rule.getSelectionModel().getSelection()[0];
                            Ext.ajaxModelLoader('MyApp.Model.DelRule',{
                                params:{
                                    ruleId: record.get('ruleId')
                                },
                                success:function(data){
                                   if(!data.status) {
                                       grid_rule.getStore().reload();
                                       Ext.getCmp('delRuleBtn').disable();
                                   } else {
                                       Ext.create('MyApp.Component.LoadingTips',{
                                          lazytime:1000,
                                          msg: '删除失败，请重试'
                                       });
                                   } 
                                }
                        }).request();
                        }
                    });
                }
            }],
            columns:[
                {text:'车牌号', width:'60%',dataIndex:'plate',sortable:false,menuDisabled:true},
                {text:'设备识别码',dataIndex:'identifier',sortable:false,menuDisabled:true}
            ],
            listeners:{
                itemclick : function(grid, record, item, index, e, eOpts) {
                    //Ext.getCmp('editLineBtn').enable();
                    Ext.getCmp('delRuleBtn').enable();
                }
            }
        });
    
       
       //主结构
       var panel = Ext.create('Ext.panel.Panel',{
           width: tab.getEl().down('iframe').dom.scrollWidth,
           height:tab.getEl().down('iframe').dom.scrollHeight,
           border:false,
           layout:'border',
           tbar:tbar,
           items:[{
              region:'center',
              border:false,
              title:'路径轨迹',
              layout: 'border',
              html:'<div class="mapWrap"><div id="mapDiv">地图加载中..</div></div>',
              listeners:{
                  afterrender:function(){
                      lineMap = Ext.create('MyApp.Component.LineMap','mapDiv');
                  }
              }
           },{
               region:'west',
               layout:'border',
               width:500,
               items:[{
                      region:'center',
                      layout:'fit',
                      border:false,
                      items:grid_line
                  },{
                      id:'detail',
                      region:'south',
                      border:false,
                      items:grid_rule
                  }]
           }],
           renderTo:Ext.getBody()
       });
   }
});


