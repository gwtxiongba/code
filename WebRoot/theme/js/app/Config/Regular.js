/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Config.Regular',{
   statics:{
       account: { regExp:/^[a-zA-Z0-9_@.]{4,20}$/, info: '对不起，用户名由4-20个字符长度的字母、数字和下划线组成！'}
   }
});

