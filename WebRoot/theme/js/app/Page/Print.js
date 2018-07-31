/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Page.Print',{
    extend:'MyApp.Base.Page',
    onInit:function(){
        var _page = this;
        document.title = '车辆结算算单';
        var btn = Ext.get('bt_print');
        Ext.ajaxModelLoader('MyApp.Model.GetPrintData', {params:{orderId:_page.getQuery('key')}}).request();
        btn.addListener('click',function(){
            btn.hide();
            setTimeout(function(){
               window.print(); 
            },500);
        })
    }
});