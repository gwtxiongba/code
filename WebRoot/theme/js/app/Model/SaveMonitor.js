/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.SaveMonitor',{
    extend: 'MyApp.Base.Ajax',
    config: {
        method : 'post',
        success: function() {
            //设置监控列表焦点,并刷新列表
            Ext.getCmp('leftTabPanel').setActiveTab(2);
            var st = Ext.getCmp('CarControlPanel_id').getStore();
            st.load(function(){
                var v = [];
                st.each(function(){
                    v.push(this.get('identifier'));
                });
                G_MonitMap.setVisible(v);
            });
        }
    }
});

