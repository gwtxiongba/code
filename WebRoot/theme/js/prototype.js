/* 
 * prototype is a extention of base javascript object
 * edit by alitte ying
 * 2013-4-13 创建
 * 2013-4-25 更新
 * 将Object.Equals和Object.isNum改成String.Equals以及String.isNum 防止与jquery冲突
 */
//清空字符串
String.prototype.Empty = function() {
    return '';    
}

//字符串恒等
String.prototype.Equals = function(str) {
    return this == str;
}

//去空格
String.prototype.Trim = function() { 
    var m = this.match(/^\s*(\S+(\s+\S+)*)\s*$/); 
    return (m == null) ? "" : m[1]; 
}

//首字符大写
String.prototype.toCapitalUpperCase = function() {
    return this.substr(0,1).toUpperCase() + this.substr(1);
}

//首字母小写
String.prototype.toCapitalLowerCase = function() {
    return this.substr(0,1).toLowerCase() + this.substr(1);
}

//字符串是否为数字
String.prototype.isNum = function() {
    return (/^(\+|\-){0,1}\d+$/.test(this.Trim()));
}

//字符串转数字 非数字强转0
String.prototype.toInt = function() {
    return this.isNum() ? parseInt(this.Trim()) : 0;
}

//字符串转Unicode字符集串
String.prototype.toUnicode = function(radix) {
    var arr = new Array();
    radix = typeof(undefined) === typeof(radix) ? 16 : radix;
    for(var i=0; i< this.length;i++) {
        arr[arr.length] = this.charCodeAt(i).toString(radix);
    }
    return arr;
}

//字符串格式化
String.prototype.Format = function(args){
    var result = this;
    var reg = null;
    if(arguments.length > 0) {
        if(arguments.length == 1 && typeof(args) == 'object') {
            var m = this.match(/{(\w+\.?)+}/g); 
            for(var i=0; m != null && i<m.length; i++) {
                var v = args;
                var p = m[i].substr(1, m[i].length-2).split('.');
                for(var n=0; n < p.length; n++) {
                    v = v[p[n]];
                    if(!v) break;
                }
                result = result.replace(m[i],v);
            }
        } else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}

//将<%=%>中的字符串以js执行并返回执行结果
String.prototype.Pack = function() {
   var m = this.match(/<%=(.\s*)+?%>/g);
    var str = this.toString();
    for(var i=0; m != null && i<m.length; i++) {
        try {
            eval("var value = " + m[i].toString().replace(/\n/g,' ').substring(3,m[i].length-2));
        } catch(ex) {
            var value = '<b>内容解析错误!</b>';    
        }
        
        str = str.replace(m[i],value);
    }
    return str;
}

//将html中&lt;&gt;&amp;&quot;&times;&divide;转移成实体字符
String.prototype.toEscape = function() {
    var m = this.match(/&lt;|&gt;|&nbsp;|&amp;|&quot;|&divide;/g);
    var str = this.toString();
    for(var i=0; m!=null&& i<m.length;i++) {
        if(m[i].toString()=='&lt;') str = str.replace(m[i].toString(),'<');
        if(m[i].toString()=='&gt;') str = str.replace(m[i].toString(),'>');
        if(m[i].toString()=='&nbsp;') str = str.replace(m[i].toString(),' ');
        if(m[i].toString()=='&amp;') str = str.replace(m[i].toString(),'&');
        if(m[i].toString()=='&quot;') str = str.replace(m[i].toString(),'"');
        if(m[i].toString()=='&divide;') str = str.replace(m[i].toString(),'/');
    }
    return str;
}

//将字符串中特殊符号转成html码
String.prototype.toHtmlChars = function() {
    var m = this.match(/<\/?.+?>/g);
	var str = this.toString();
	for(var i=0; m!=null&& i<m.length;i++) {
        var oldStr = m[i].toString();
        str = str.replace(oldStr,oldStr.replace('<','&lt;').replace('>','&gt;'));
	}
	return str.replace(/\n/g,'<br/>').replace('/&/g','&amp;').replace('/"/g','&quot;').replace('/\//g','&divide;');
}

//将对象值填充参数 object.paramName -> {paramName} 
String.prototype.fillParams = function(obj) {
    var m = this.match(/{(\w+[\w.]+\w+)}/g);
    var str = this.toString();
    for(var i=0;m!=null&&i<m.length;i++){
        try {
            var v = eval('obj.'+m[i].toString().substring(1,m[i].length-1));
            str = str.replace(m[i], v.toString().replace(/\"/g,'&quot;'));
        } catch(e) {
            str = str.replace(m[i], undefined);
        }
    }
    return str;
}

String.prototype.toDate = function() {
    var format =  arguments.length == 0 ? '%Y-%m-%d %H:%i:%s': arguments[0];
    /*
    var regStr = format.replace(/%Y/, '\\d{4}')
                .replace(/%m/,'([1-9]|0[1-9]|1[0-2])')
                 .replace(/%d/,'([1-9]|0[1-9]|[1-2][0-9]|3[0-1])')
                 .replace(/%H/,'([0-9]|0[0-9]|1\\d|2[0-3])')
                 .replace(/%(i|s)/g,'([0-9]|0[0-9]|[1-5]\\d)');
    */             
    var regStr = format.replace(/%Y/, '\\d{4}')
                .replace(/%m/,'(0[1-9]|1[0-2])')
                 .replace(/%d/,'(0[1-9]|[1-2][0-9]|3[0-1])')
                 .replace(/%H/,'(0[0-9]|1\\d|2[0-3])')
                 .replace(/%(i|s)/g,'(0[0-9]|[1-5]\\d)');
                 
    var m = this.match(new RegExp(regStr,'g'));
    if(m==null) throw new Error('Not a date string');
    var tags = format.match(/%(Y|m|d|H|i|s)/g);
    var r = format.replace(/%(Y|m|d|H|i|s)/g, ',').split(',');

    //2012-11-22 22:21:84
    var tmp = m[0], date = new Date(0,1,1,0,0,0);
    for(var i = 1; i < r.length; i++) {
        var tag  = tags.shift();
        var t = tmp.substr(0, tmp.indexOf(r[i]));
        t = t=='' ? tmp : t;
        if(tag=='%Y') date.setFullYear(t);
        if(tag=='%m') date.setMonth(t-1);
        if(tag=='%d') date.setDate(t);
        if(tag=='%H') date.setHours(t);
        if(tag=='%i') date.setMinutes(t);
        if(tag=='%s') date.setSeconds(t);
        tmp = tmp.substr(tmp.indexOf(r[i])+1);
    }
    return date;
}
//验证手机号
String.prototype.isMobile = function() { 
    return (/^(?:13\d|15\d|14\d|18\d)-?\d{5}(\d{3}|\*{3})$/.test(this.Trim())); 
}

//验证电话
String.prototype.isTel = function() {
    //"兼容格式: 国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)"
    return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(this.Trim()));
}

//将字符串反序列化
String.prototype.decode = function() {
    try {return eval('('+ this.toString() + ')');} catch(ex) {return ''};
}

//将符合x,y字符串转成点对象
String.prototype.toPoint = function() {
    if(!this || this.split(',').length < 2) return null;
    return {x:this.split(',')[0],y:this.split(',')[1]};
}

//将全角字符串转成半角
String.prototype.toHalfWidth = function() {
    var result = '';
    for(var i = 0; i < this.length; i++) {
        var charCode = this.charCodeAt(i);
        if(charCode == 12288) {
            result += String.fromCharCode(charCode-12288);
            continue;
        }
        if(charCode > 65280 && charCode < 65375) 
            result += String.fromCharCode(charCode-65248);
        else result += String.fromCharCode(charCode);
    }
    return result;
}

//数字等于
Number.prototype.Equals = function(obj) {
    return this == obj;
}

//数字大于
Number.prototype.Gt = function (obj) {
    return (this.valueOf() > obj.toString().toInt());
}

//数字小于
Number.prototype.Lt = function (obj) {
    return (this.valueOf() < obj.toString().toInt());
}

//数字大于等于
Number.prototype.EGt = function (obj) {
    return (this.valueOf() >= obj.toString().toInt());
}

//数字小于
Number.prototype.ELt = function (obj) {
    return (this.valueOf() <= obj.toString().toInt());
}

//数字是否在两参数之间
Number.prototype.Between = function(obj1,obj2) {
    obj1 = obj1.toString().toInt();
    obj2 = obj2.toString().toInt();
    if(obj1>obj2) {var tmp = obj1;obj1=obj2;obj2=tmp;}
    return (this < obj2 && this > obj1);
}

//数字是否包含两参数在内
Number.prototype.Range = function(obj1,obj2) {
    obj1 = obj1.toString().toInt();
    obj2 = obj2.toString().toInt();
    if(obj1>obj2) {var tmp = obj1;obj1=obj2;obj2=tmp;}
    return (this <= obj2 && this >= obj1);
}

//数字转布尔
Number.prototype.toBoolean = function() {
    return this.Equal(0) ? false : true;
}

//数字秒转时间格式(hh:mm:ss)
Number.prototype.toTimeFormat = function() {
    var hourFree = arguments.length > 0 ? arguments[0] : false;
    var units = [];
    var hour,minute,second;
    var max = hourFree ? this : this%360000;
    hour = Math.floor(max/3600);
    if(hour < 10) units.push('0'+hour); 
    else units.push(hour);
    minute = Math.floor((max - hour*3600)/60);
    if(minute < 10) units.push('0'+minute);
    else units.push(minute);
    second = max - hour*3600 - minute*60;
    if(second < 10) units.push('0'+second);
    else units.push(second);
    return units.join(':');
}

//将十六进制内容数组转成中英文字符串
Array.prototype.toChars = function(radix) {
    var str = String.prototype.Empty();
    radix = typeof(undefined) === typeof(radix) ? 16 : radix;
    for(var i =0; i<this.length; i++) {
       str+=String.fromCharCode(parseInt(this[i],radix));
    }
    return str;
}

//兼容ie不支持indexOf函数
if(!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(Object) {
        for(var i = 0;i<this.length;i++){  
            if(this[i] == Object){  
                return i;  
            }  
        }  
        return -1;  
    }
}

//数组排重
Array.prototype.distinct = function() {
    var _this = [];
    var _a = this.concat().sort();
    for(var i=0 ; i < _a.length; i++) {
        var len = _this.length ? (_this.length-1) : _this.length;
        if(_this[len] != _a[i]) _this.push(_a[i]);
    }
    return _this;
}
 
Array.prototype.getValuesFromKey = function(key) {
    var arr = [];
    for(var i=0; i< this.length; i++) {
        arr.push(this[i][key]);
    }
    return arr;//.distinct();
}

Array.prototype.getValuesFromKeys = function() {
    var arr = [];
    if(arguments.length == 0) return arr;
    for(var i=0; i< this.length; i++) {
        var obj = null;
        for(var n = 0; n < arguments.length; n++) {
            if(obj == null) obj = {};
            obj[arguments[n]] = this[i][arguments[n]] == undefined ? null : this[i][arguments[n]];
        }
        if(obj != null) arr.push(obj);
    }
    return arr;
}

Array.prototype.contains = function(obj) { 
    var i = this.length; 
    while (i--) { 
        if (this[i] === obj) { 
            return true; 
        } 
    } 
    return false; 
} 

//格式化时间对象
Date.prototype.format = function(format)
{
    var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(),    //day
        "h+" : this.getHours(),   //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
        (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)if(new RegExp("("+ k +")").test(format))
        format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
    return format;
}
//智能化输出格式
Date.prototype.toChineseFormat = function() {
    var now = new Date;
    var full = this.getFullYear() + '年'+ (this.getMonth()+1) + '月' + this.getDate() + '日 ' + this.getHours() + '时' + this.getMinutes() + '分';
    //var format = 'yyyy年MM月dd日 hh时mm分';
    if(this.getFullYear() != now.getFullYear() || this.getMonth() != now.getMonth()) {
        return full;
    }
    
    var dayDiff = now.getDate() - this.getDate();
    var dayCall = ['前天','昨天','今天','明天','后天'];
    if(Math.abs(dayDiff)>2) {
        return full;
    }
    
    var dayPart = '';
    if(this.getHours().Range(0, 5)) {
        dayPart = '凌晨';
    } else if(this.getHours().Range(6, 11)) {
        dayPart = '上午';
    } else if(this.getHours() == 12) {
        dayPart = '中午';
    } else if(this.getHours().Range(13, 18)) {
        dayPart = '下午';
    } else dayPart = '晚上';
    
    return dayCall[2-dayDiff] + dayPart + (this.getHours() > 12 ? this.getHours()-12 : this.getHours()) + '时'+ this.getMinutes() + '分';
}

Date.prototype.addDays = function (dayInterval) {
    this.setDate(this.getDate()+dayInterval);
    return this;
}

//添加月数获得日期 monthCount 为整数
Date.prototype.addMonths = function(monthCount) {
    this.setMonth(this.getMonth()+ monthCount);
    return this;
}

//加载提示
window.loadMask = function(content) {
    var _this = jQuery.dialog({
        id:'loadMask',
        width: '250px',
        height: '100px',
        max: false,
        min: false,
        close:false,
        lock: true,
        title: '数据加载',
        content : '<img src="./theme/imgs/load.gif" style="vertical-align: middle"/> ' + content
    });
    return _this;
}

