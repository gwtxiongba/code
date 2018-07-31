/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.ListDriverLog',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['logId','driver','car','ifLegal','signInTime','unbindTime','logInfo'],
        sortInfo:{field:'logId', direction:"DESC"}
    }
});
