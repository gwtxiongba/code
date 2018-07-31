/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.VehicleCount',{
    extend: 'MyApp.Base.Ajax',
    config:{
        method:'get',
        quiet:true,
        autoAbort:false
    },
    success:function(o) {
            var tmp = '{0}({1}%)';
            var rate = parseInt(o.onlines*1000/o.total)/10;
            Ext.getCmp('onlines').setText('在线' + tmp.Format(o.onlines,o.total ? rate : 0));
            Ext.getCmp('offlines').setText('离线'+ tmp.Format(o.total-o.onlines,o.total ? 100-rate : 0));
    }
});

