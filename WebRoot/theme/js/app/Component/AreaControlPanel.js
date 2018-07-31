/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AreaControlPanel',{
   extend:'Ext.panel.Panel',
   constructor:function(){
       var carStateStore = new Ext.data.SimpleStore({
               fields: ['id', 'state'],
               data : [['1','全部'],['2','在线'],['3','不在线']]
       });

       this.superclass.constructor.call(this,{
           id:'areaControlPanel',
           region: 'north',
           border: false,
           layout: {
               type: 'table',
               pack:'center',
               columns: 2
           },
           items:[{
                colspan:2,
                //height: 25,
                xtype: 'textfield',
                fieldLabel: '车牌号码',
                labelWidth: 60,
                text: 'carNo'	
           },{
                colspan:2,
                //height: 30,
                xtype: 'combo',
                fieldLabel: '车辆状态',   
                labelWidth: 60,
                id: "carState",   			
                store: carStateStore,   
                value: 1,
                triggerAction: "all",//请设置为"all",否则默认为"query"的情况下，你选择某个值后，再此下拉时，只出现匹配选项，如果设为"all"的话，每次下拉均显示全部选项   
                //mode: "local",   
                valueField: "id",   
                displayField: "state",   
                selectOnFocus: true,   
               // resizable: true,//可以改变大小   
                typeAhead: true, //延时查询   
                typeAheadDelay:3000,  
                editable: true
           },{
                xtype:'button',
                anchor: '-5',
                width: 90,
                //height: 20,
                text: '显示区域'
           }, {
                xtype:'button',
                anchor: '-5',
                width: 90,
                //height: 20,
                text: '查询车辆'
           }, {
                xtype:'button',
                anchor: '-5',
                width: 90,
                //height: 20,
                text: '清除区域'
           }]

       });
       
   }
   
});

