/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListAbnormaLog',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['logId','driver','car','ifLegal','signInTime','unbindTime','logInfo','x','y'],
        sortInfo:{field:'logId', direction:"DESC"}
    }
});
