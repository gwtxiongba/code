/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SavePreStoreMsg',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
           alert('保存预存短语成功!');
        }
    }
});

