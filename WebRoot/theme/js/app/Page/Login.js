/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.Login',{
   extend:'MyApp.Base.Page',
   title: '政企车辆管理平台1.0',
   onInit: function(){
       var loginForm = Ext.create('MyApp.Component.LoginFormPanel');
       loginForm.render('loginBox');
       loginForm.setWidth(300);
       loginForm.setHeight(26);
   }
});

