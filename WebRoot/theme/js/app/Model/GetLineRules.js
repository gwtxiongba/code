/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetLineRules',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['ruleId', 'lineId', 'identifier', 'plate'],
        sortInfo:{field:'ruleId', direction:"DESC"}
    }
});

