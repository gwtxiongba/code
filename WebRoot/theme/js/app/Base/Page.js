/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define("MyApp.Base.Page",{
    config:{
        title:'', 
        onInit:function(){}
    },
    statics:{
      getQuery: function(k) {
            if(!k && !!window.location.search) {
                var obj = {};
                var k_v = window.location.search.substr(1).split('&');
                for(var i = 0; i< k_v.length; i++) {
                    obj[k_v[i].split('=')[0]] = decodeURI(k_v[i].split('=')[1]);
                }
                return obj;
            }
            var reg = new RegExp("(^|&)" + k + "=([^&]*)(&|$)", "i");
            var r = decodeURI(window.location.search.substr(1)).match(reg);
            if (r != null) return unescape(r[2]);
            return null;   
        }  
    },
    constructor : function(o) {
        this.initConfig(o);
        if(this.title) document.title = this.title;
        Ext.ajaxModelLoader = function(cls,args){
            if(typeof(args) != 'object') args = {};
            args.getCls = function() {return cls;};
            return Ext.create(cls,args);
        }
        Ext.override(Ext.form.field.Base,{
            initComponent:function(){
                if(this.allowBlank!==undefined && !this.allowBlank){
                    this.afterSubTpl = this.afterSubTpl ? (this.afterSubTpl+'<font class="redStar">*</font>') : '<font class="redStar">*</font>';
                }
                this.callParent(arguments);
            }
        });
        this.onInit();
        window.page = this;
    },
    //获取页面中的对象
    getCmp : function(a) {
        return Ext.getCmp(a);
    },
    //获取页面id元素
    get : function(b) {
        return Ext.get(b);
    },
    //获取RUI参数
    getQuery: function(k) {
        if(!k && !!window.location.search) {
            var obj = {};
            var k_v = window.location.search.substr(1).split('&');
            for(var i = 0; i< k_v.length; i++) {
                obj[k_v[i].split('=')[0]] = decodeURI(k_v[i].split('=')[1]);
            }
            return obj;
        }
        var reg = new RegExp("(^|&)" + k + "=([^&]*)(&|$)", "i");
        var r = decodeURI(window.location.search.substr(1)).match(reg);
        if (r != null) return unescape(r[2]);
        return null;   
    },
    getTitle: function() {
        return document.title;
    }    
});  

