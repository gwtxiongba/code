/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.AppLeftPanel',{
   extend:'Ext.panel.Panel',
   constructor:function(){
       var g = MyApp.Base.LocalSet.getGolbal();
       // var child1 = Ext.create('MyApp.Component.AppLeftChild1');
          var carTree =  Ext.create('MyApp.Component.CarTreePanel');
          this.superclass.constructor.call(this,{
          id:'appleft',
          layout:'accordion',                 //指定面板的布局风格
		  layoutConfig:{
		      bodyStyle:'padding:5px 5px 5px 5px',
		      animate:false ,                   //是面板代开具有动画效果
		      titleCollapse:true,              //允许通过单击面板标题来展开或收缩面板
		      activeOnTop:false             //设置打开指定的面板置顶
		  },
		  //监控面板
		  items:[carTree]
          
       });
       
   }
});

