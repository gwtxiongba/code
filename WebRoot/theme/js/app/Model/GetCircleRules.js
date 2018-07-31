/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.GetCircleRules',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['ruleId', 'areaId', 'identifier', 'plate'],
        sortInfo:{field:'ruleId', direction:"DESC"}
    }
});

