/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Component.TraceHistoryPanel',{
   extend:'Ext.panel.Panel',
   config:{
       map:null,
       atrace:[]
   },
   constructor:function(opts){
       this.initConfig(opts);
       var table = Ext.get('tracerpt');
       for(var i = 0; i < this.atrace.length; i++) {
           var trace = this.atrace[i];
           var tmp = '<tbody><tr><td class="col0">{0}<br/>位置解析中...</td><td class="col1" rowspan="2">{2}公里<br/>{3}</td><tr><tr><td class="col0">{1} 终点<br/>位置解析中...</td></tr></tbody>'
           var stime = trace[0][1].toDate().format('hh:mm:ss');
           var etime = trace[trace.length-1][1].toDate().format('hh:mm:ss');
           var distance = 0;
           var pertime = 0;
           for(var n=0; n < trace.length-1; n++) {
                distance += parseInt(this.map.getDistance(trace[n][0],trace[n+1][0]));
                pertime += trace[n+1][1].toDate().getTime() - trace[n][1].toDate().getTime();
            }
            distance = Math.ceil(distance/100)/10;
            pertime = pertime < 3600000 ? Math.ceil(pertime/60000) + '分钟' : Math.ceil(pertime/360000)/10 + '小时';
            Ext.DomHelper.append(table,tmp.Format(stime,etime,distance,pertime));
       }
       
       this.superclass.constructor.call(this,{
           renderTo:'tracehistory'
       });
       
       function getAddress(pt,fn) {
          var gc = new BMap.Geocoder();    
          gc.getLocation(pt,function(rs){
             var addComp = rs.addressComponents;
             fn(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
          }); 
       }
   }
});

