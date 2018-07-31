Ext.define('MyApp.Component.CarDriverWindow',{
    extend: 'Ext.window.Window',
    config:{id:'CarDriverWindow_id'},
    requires:['Ext.form.Panel','Ext.ux.form.MultiSelect','Ext.ux.form.ItemSelector'],
    constructor: function(){
        var w = this;
        
        var grid = Ext.create('MyApp.Component.CarDriverGridPanel');
        
        this.superclass.constructor.call(this,{
            title: '司机车辆规则关系',
            width: 600,
            height: 400,
            resizable: false,
            layout: 'border',
            //modal:true,  //是否遮罩
            items:[grid]
        });
        this.show();
    }
});



