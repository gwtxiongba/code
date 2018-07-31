/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var CRMApp;

if(!CRMApp) CRMApp = {};
else if(typeof(CRMApp)!= 'object') throw new Error('CRMApp has already exists but not an object.');

//应用框架配置
CRMApp.Configuration = {
    version:'1.0',
    debug_on:false,
    jsPath:'carmonitorali/theme/js',
    extJs:'extjs4.2',
    servicePath:'http://192.168.0.126:8080/carmonitorali/api.action',
    CookieSet: {
        domain: "192.168.0.126"
    },
    UserSet: {isAlarm : true , pageSize: 20},
	UPLOADER_SERVLET_URL : "http://www.chanceit.cn:8081/fileload/"
};

//获取版本号
CRMApp.getVersion = function() {
    return CRMApp.Configuration.version;
};

/*******************************************************************************
 * URI对象
 */
CRMApp.URI = {
    toString : function() {
        var pathName = window.location.pathname.substring(1);
        var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/')+1);
        return window.location.protocol + '//' + window.location.host + '/'+ webName;
    },
    //获得域
    getHost : function() {
        var m  = window.location.href.match(/^http:\/\/([^\/]+)\//i);
        return m == null ? '' : m[0];
    },
    //获取页面相对位置
    getRelative : function() {
        var index = location.pathname.lastIndexOf('/');
        return location.pathname.substr(0,index+1);
    },
    getFileName:function() {
        var page = arguments.length >0 ? arguments[0].location.href : location.href ;
        var pageName = page.substring(page.lastIndexOf("\/")+1).split('.')[0];
        pageName = pageName.split('?')[0];
        return pageName==''? 'index': pageName;    
    }
}

/*******************************************************************************
 * 脚本对象
 */
CRMApp.Javascript = function() {
    var _jsUrls = [];
    
    //引用js资源
    this.include = function(jsUrl) {
        _jsUrls.push(jsUrl);
        return this;
    }
    
    //加载js文件
    this.load = function(fn) {
        fn = fn ==undefined ? function(){} : fn;
        _loadJs(fn);
       
    }
    
    var _loadJs = function (fn) {
        
        var url = _jsUrls.shift();
        var baseURI = CRMApp.URI.getHost();
        if(url!=undefined && url!='') {
            var head = document.head || document.getElementsByName("head")[0] || document.documentElement;
            var el = document.createElement("script");
            el.setAttribute("type", "text/javascript");
            if(url.indexOf('http://') != -1)
                el.setAttribute("src",url);
            else el.setAttribute("src",baseURI + url.replace(baseURI,''));
            if(document.all){
                el.onreadystatechange = function() {
                    if(this.readyState == "loaded" || this.readyState == "complete"){
                        //console.log(this.readyState);
                        _loadJs(fn);
                    }
                }
                if(navigator.userAgent.indexOf("MSIE 9.0")>0 || navigator.userAgent.indexOf("MSIE 10.0")>0)
                   head.appendChild(el);
                else head.firstChild.appendChild(el);
            } else {
                el.onload = function() {
                    _loadJs(fn);
                }
                head.appendChild(el); 
            }
        } else fn();
    }
  
}

CRMApp.Javascript.getJsPath = function(type) {
    return CRMApp.URI.getHost() + 
           CRMApp.Configuration.jsPath + '/' +
           type + '/' +
           (type=='pages' ? CRMApp.URI.getRelative().substr(1) : '');
}

CRMApp.Javascript.loadExtJs = function() {
    var _js = new CRMApp.Javascript();
    _js.include(CRMApp.Configuration.jsPath + '/' + 'prototype.js');
    _js.include(CRMApp.Javascript.getJsPath('extjs4.2')+'ext-all.js');
    _js.include(CRMApp.Javascript.getJsPath('extjs4.2')+'ext-lang-zh_CN.js');
    _js.load(function(){
        Ext.Loader.setConfig({enabled: true,paths: {'MyApp':'theme/js/app','Ext.ux':'theme/js/extjs4.2/src/ux'},disableCaching:CRMApp.Configuration.debug_on});
        Ext.create('MyApp.Config.Golbal');
        Ext.create('MyApp.Config.Error');
        Ext.create('MyApp.Config.Regular');
        Ext.create('MyApp.Base.LocalSet');
        Ext.onReady(function(){
            Ext.create('MyApp.Page.' + CRMApp.URI.getFileName().substr(0, 1).toUpperCase() + CRMApp.URI.getFileName().substr(1));
        });
    });
}

CRMApp.Javascript.loadExtJs();