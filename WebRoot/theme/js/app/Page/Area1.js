/* 
 * To change this template, choose Tools | Templates
 * 多边形区域管理
 */
Ext.define('MyApp.Page.Area1',{
   extend:'MyApp.Base.Page',
   onInit:function(){
       var areaMap = null;
       var tab = top.Ext.getCmp('apptabs').getActiveTab();
       
       var tbar = [{
            id:'addPolygonBtn',
            text:'添加多边形域',
            handler:function(){
                grid_polygon.getSelectionModel().deselectAll();
                Ext.create('MyApp.Component.PolygonEditWindow');
                Ext.getCmp('editPolygonBtn').disable();
                Ext.getCmp('delPolygonBtn').disable();
            }  
       },{
           xtype:'tbseparator'
       },{
           id:'editPolygonBtn',
           text:'编辑多边形域',
           disabled:true,
           handler:function(){
               Ext.create('MyApp.Component.PolygonEditWindow');
           }
       },{
           xtype: 'tbseparator'
       },{
           id:'delPolygonBtn',
           text:'删除多边形域',
           disabled:true,
           handler:function(){
                Ext.Msg.confirm('系统确认','<span style="color:red">警告：该操作将会解除该多边形区域下所有设置车辆规则</span><p>您确定要删除当前多边形区域吗？</p>',function(opt){
                    if(opt=='yes') {
                        var record = grid_polygon.getSelectionModel().getSelection()[0];
                        Ext.ajaxModelLoader('MyApp.Model.DelPolygon',{
                            params:{
                                areaId: record.get('areaId')
                            },
                            success:function(data){
                                if(!data.status) {
                                    grid_polygon.getStore().reload();
                                    Ext.define('rule', {
                                        extend: 'Ext.data.Model',
                                        fields: ['ruleId','areaId','identifier','plate']
                                    });
        
                                    grid_rule.bindStore(Ext.create('Ext.data.Store',{
                                       model: 'rule',
                                       data:[]
                                    }));
                                    
                                    areaMap.clearOverlays();
                                    
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
   
       var ruleStore = Ext.ajaxModelLoader('MyApp.Model.GetPolygonRules',{});
       
       var grid_polygon = Ext.create('Ext.grid.Panel',{
           id:'gridpolygon_id',
           frame:true,
           border:true,
           autoScroll:false,
           scroll:'vertical',
           store:Ext.ajaxModelLoader('MyApp.Model.GetPolygons', {}),
           columns:[
               {xtype:'rownumberer'},
               {text:'围栏名称', width:'50%',dataIndex:'areaName',sortable:false,menuDisabled:true},
               
               {text:'边数',width:'25%',dataIndex:'area',renderer:function(v){
                    var count = v.split(';').length;
                    return count > 3 ? count+'边形' : '三角形';
               }},
               {text:'创建时间',width:'25%',dataIndex:'createTime'}
           ],
           listeners:{
               itemclick : function(grid, record, item, index, e, eOpts) {
                   ruleStore.clearFilter(true);
                   ruleStore.filter('areaId',record.get('areaId'));
                   grid_rule.bindStore(ruleStore);
                   areaMap.clearOverlays();
                   areaMap.drawPolygon(record.get('area'));
                   Ext.getCmp('editPolygonBtn').enable();
                   Ext.getCmp('delPolygonBtn').enable();
                   Ext.getCmp('addRuleBtn').enable();
                   Ext.getCmp('delRuleBtn').disable();
               }
           }
       });
       
       grid_polygon.getStore().on('load',function(){
           Ext.getCmp('editPolygonBtn').disable();
           Ext.getCmp('delPolygonBtn').disable();
           Ext.getCmp('addRuleBtn').disable();
           Ext.getCmp('delRuleBtn').disable();
       });
       
       var grid_rule = Ext.create('Ext.grid.Panel',{
           id:'gridrule_id',
           frame:true,
           border:true,
           autoScroll:false,
           scroll:'vertical',
           height:200,
           tbar:[{
               id:'addRuleBtn',
               text:'添加规则',
               disabled:true,
               handler:function(){
                   grid_rule.getSelectionModel().deselectAll();
                   Ext.create('MyApp.Component.SaveRule1Window');
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
                            Ext.ajaxModelLoader('MyApp.Model.DelRule1',{
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
              title:'围栏区域',
              layout: 'border',
              html:'<div class="mapWrap"><div id="mapDiv">地图加载中..</div></div>',
              listeners:{
                  afterrender:function(){
                      areaMap = Ext.create('MyApp.Component.AreaMap','mapDiv');
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
                      items:grid_polygon
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