/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.Logout',{
   extend: 'MyApp.Base.Ajax',
   config:{
       success:function(o) {
            MyApp.Base.LocalSet.clearGolbal();
            window.location.href = 'login.html';
        }
   }
});

