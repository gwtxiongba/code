/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Base.Timer',{
    constructor:function(baseTime) {
        var _t = null;
        var _times = 0;
        var _e = [];
        this.add = function(multi,fn) {
            _e.push({
                multi:multi,
                fn:fn
            });
            return this;
        }
  
        this.excute = function() {
            if(_t != null) {
                this.stop();
            }
      
            var _times = acd();
      
            var _excute = function() {
                for(var i=0; i < _e.length; i++) {
                    //console.log(_times %_e[i].multi);
                    if(_times %_e[i].multi == 0) _e[i].fn();
                }
                _times = _times <=0 ? (acd()-1) : (_times-1);
            }
      
            if(arguments[0]) _excute();
            _t = setInterval(function(){
                _excute();
            },baseTime);
        }
  
        //停止执行心跳
        this.stop = function() {
            if(_t != null) {
                clearTimeout(_t);
                _t = null;
            }
        }
  
        //清除
        this.clear = function() {
            _e = [];
            this.stop();
        }
  
        //获得最大公倍数
        var acd = function() {
            var max = 1;
            for(var i = 0; i < _e.length; i++) {
                max *= _e[i].multi;
            }
            return max;
        }
    }
});

