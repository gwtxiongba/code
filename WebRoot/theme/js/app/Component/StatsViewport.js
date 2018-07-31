Ext.define('MyApp.Component.StatsViewport',{
    extend: 'Ext.container.Viewport',
    constructor:function(){
        var g = MyApp.Base.LocalSet.getGolbal();
         if(g == undefined) window.location.href = 'login.html';
         
        //菜单树
        var menuTree = Ext.create('Ext.tree.Panel',{
            style:'text-indent:20px',
            border:false,
            root:{
                text:'统计菜单根节点',
                expanded:true,
                children:[{
                    text:'行驶报表',
                    children:[/*{
                       text:'行驶报表',
                       leaf:true,
                       hrefTarget:'drivingRep.html#0'
                    },{
                       text:'超速报表',
                       leaf:true,
                       hrefTarget:'drivingRep.html#1'
                    },*/{
                        text:'位置报表',
                        leaf:true,
                        hrefTarget:'drivingRep.html#2'
                    },{
                        text:'里程报表',
                        leaf:true,
                        hrefTarget:'periodRep.html#0'
                    },/*{
                        text:'停车报表',
                        leaf:true,
                        hrefTarget:'drivingRep.html#4'
                    },{
                        text:'点火报表',
                        leaf:true,
                        hrefTarget:'drivingRep.html#5'
                    }*/]
                }/*,{
                    text:'报警统计',
                    children:[{
                        text:'断电报警',
                        leaf:true,
                        hrefTarget:'alarmsRep.html#0'
                    },{
                        text:'碰撞报警',
                        leaf:true,
                        hrefTarget:'alarmsRep.html#1'
                    },{
                        text:'侧翻报警',
                        leaf:true,
                        hrefTarget:'alarmsRep.html#2'
                    },{
                        text:'震动报警',
                        leaf:true,
                        hrefTarget:'alarmsRep.html#3'
                    },/*{
                        text:'超速报警',
                        leaf:true,
                        hrefTarget:'alarmsRep.html#4'
                    }]
                },{
                    text:'周期报表',
                    children:[{
                        text:'日报表',
                        leaf:true,
                        hrefTarget:'periodRep.html#0'
                    },{
                        text:'月报表',
                        leaf:true,
                        hrefTarget:'periodRep.html#1'
                    }]
		                },/*{
                    text:'油耗报表',
                    children:[{
                        text:'日行/油耗报表',
                        leaf:true,
                        hrefTarget:'fuelDateRep.html#0'
                    }]
                },/*{
                    text:'上线统计',
                    children:[{
                        text:'车辆月上线率',
                        leaf:true,
                        hrefTarget:'onlinesRep.html#0'
                    },{
                        text:'设备日上线率',
                        leaf:true,
                        hrefTarget:'onlinesRep.html#1'
                    }]
                }*/]
            },
            listeners: {
                itemdblclick:function(v, record, item, index, e, eOpts){
                    if(record.get('leaf')) {
                        var url = record.get('hrefTarget');
                        if(url) Ext.getCmp('statstabs').open({title:record.get('text'),url:record.get('hrefTarget')});
                    }
                }
            },
            rootVisible:false
        });

        this.superclass.constructor.call(this,{
            layout:'border',
            items:[{
              tbar:[{
                 xtype:'tbtext',
                 text:'<b>企业车辆统计系统1.0</b>',
                 style:'color:#04408c;'
              },{
                  xtype:'tbfill'
              },{
                  xtype:'label',
                  text:'用户名称：' + g.visitor.account,
                  style:'color:#04408c;'
              },{
                  xtype:'tbspacer',
                  width:10
              },{
                  xtype:'label',
                  text:'用户身份：'+ (g.visitor.role ? '操作员' : '管理员'),
                  style:'color:#04408c;'
              },{
                  xtype:'tbspacer',
                  width:10
              },{
                  xtype:'tbseparator'
              },{
                  text:'关闭',
                  handler:function(){
                      Ext.Msg.confirm('系统提示','关闭统计系统页面',function(btn){
                          if(btn=='yes') window.close()
                      });
                  }
              }],
              region:'north',
              border:false,
              height:26
            },{
                title:'统计菜单',
                region:'west',
                collapsible:true,
                width:240,
                maxSize:240,
                layout:'fit',
                items:menuTree
            },{
                id: 'statsmain',
                region:'center',
                layout:'border',
                border:false,
                collapsible:false
            },{
                region: 'south',
                xtype: 'toolbar',
                id: 'main_bottom',
                height: 9
            }]
        });
    }
});

