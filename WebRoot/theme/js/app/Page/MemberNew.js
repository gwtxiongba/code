/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.MemberNew',{
    extend:'MyApp.Base.Page',
    onInit: function(){
        Ext.create('MyApp.Component.MemberNewGridPanel');
    }
})

