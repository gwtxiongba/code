/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.RelayCount',{
    extend: 'MyApp.Base.Ajax',
    config:{
        method:'get',
        quiet:true,
        autoAbort:false
    },
    success:function(o) {
            var tmp = '(断{0}/总{1})';
           // var rate = parseInt(o.ons*1000/o.total)/10;
            Ext.getCmp('relay').setText('油电控制' + tmp.Format(o.total-o.ons,o.total));
    }
});
