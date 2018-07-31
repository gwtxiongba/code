/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('MyApp.Model.DateEvent',{
   extend:'MyApp.Base.Ajax',
   config: {
       method:'get',
       quiet:false
   },
   success:function(data) {
       var bmap = this;
       var myGeo = new BMap.Geocoder();
       for(var i=0; i < data.length; i++) {
           var size = new BMap.Size(42,42);
           var icon = new BMap.Icon('theme/imgs/events.png',size,{imageOffset:new BMap.Size(-size.width*(data[i].eventType-1),0)});
           var marker = new BMap.Marker(new BMap.Point(data[i].x,data[i].y),{icon:icon});
           var tmpstr = '{0} <br/>位于"{1}"';
           marker.time = data[i].time;
           marker.info = new BMap.InfoWindow('',{title:"<b>{0}报警</b>".Format(MyApp.Config.Golbal.EventTypes[data[i].eventType-1])});
           marker.addEventListener('click',function(){
               var mk = this;
               myGeo.getLocation(mk.getPosition(), function (rs) {
                    var addComp = rs.addressComponents;
                    mk.info.setContent(tmpstr.Format(mk.time,addComp.province  + addComp.city + addComp.district+addComp.street + addComp.streetNumber));
                    bmap.openInfoWindow(mk.info,mk.getPosition());
                });
           });
           bmap.addOverlay(marker);
       }
   }
});

