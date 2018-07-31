/* 
 * 预存短信窗口
 */
Ext.define('MyApp.Component.PrestoreMsgWindow',{
    extend: 'Ext.window.Window',
    constructor: function(){

        var msgGrid =  Ext.create('MyApp.Component.PrestoreMsgListPanel');
        
        this.superclass.constructor.call(this,{
            title: '预存短语',
            width: 400,
            height: 300,
            resizable: false,
            layout: 'border',
            //modal:true,
            items:[msgGrid],
            buttons: [{
                text: '删除预存短语',
                handler:function(){
                	 Ext.Msg.confirm('系统确认','您将删除所选预存信息，是否继续？',function(btn){
                         if(btn=='yes') {
                             var keys = [];
                             var selModels = msgGrid.getSelectionModel().getSelection();
                             var model = null;
                             while(model = selModels.shift()){
 								 keys.push(model.get('preMsgId'));
 							 }
                             Ext.ajaxModelLoader('MyApp.Model.DelPreStoreMsg',{
 								 target: msgGrid, 
 								 params: {preMsgIds:keys.join(',')}}
 							 ).request();   
 							
                         }
                     });
                }
            }]
        });
        this.show();
    }
});



