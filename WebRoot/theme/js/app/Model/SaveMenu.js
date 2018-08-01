/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveMenu',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            //设置监控列表焦点,并刷新列表
          
           alert("成功");
        }
    }
});

