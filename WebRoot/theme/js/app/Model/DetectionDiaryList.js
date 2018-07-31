/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DetectionDiaryList',{
    extend:'MyApp.Base.AjaxModel',
    config:{
        fields:['identifier','plate','info'],
        sortInfo:{field:'identifier', direction:"DESC"}
    }
});

