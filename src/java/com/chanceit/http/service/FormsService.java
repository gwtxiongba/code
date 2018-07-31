package com.chanceit.http.service;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.client.Client;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.ClientFactory;
import com.chanceit.framework.utils.Converter;
import com.chanceit.http.dao.IBoxreminderDao;
import com.chanceit.http.dao.IPointsFormDao;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Point;
import com.chanceit.http.pojo.PointInfo;
import com.chanceit.http.pojo.Team;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @author gwt 2015-01-02 报表接口处理逻辑
 */
/**
 * @author Administrator
 * 
 */
@Transactional
@Component("formService")
@SuppressWarnings("unchecked")
public class FormsService implements IFormsService {
	private String charSet="UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	private String pointUrl = "http://127.0.0.1/GetBaiduPoints/getDayPointsInfo?";
	private String obdUrl = " http://localhost:8081/shopHttp/services/UserRecorderService?wsdl";
	private String locationUrl = "http://127.0.0.1/citapi/getlocbatch";
    private String breakURL = "http://127.0.0.1:8080/shopHttp/services/UserOBDInfoService?wsdl";
	@Autowired
	@Qualifier("pointsFormDao")
	private IPointsFormDao pointsFormDao;
	@Autowired
	@Qualifier("boxreminderDao")
	private IBoxreminderDao boxreminderDao;
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	
	private List<Map<String,String>> mileMap ;
    @Override
	public String getObdMile(String uids, String beginTime, String endTime,int mile,float fule)
			throws Exception {
		// TODO Auto-generated method stub
    	//String ss = "http://localhost:8080/shopHttp/services/UserRecorderService?wsdl";
		Client client = ClientFactory.getClient(obdUrl);
		Object[] objs = new Object[5];
		objs[0] = uids;
		objs[1] = beginTime;
		objs[2] = endTime;
		objs[3] = mile;
		objs[4] = fule;
		String result =client.invoke("getNumData", objs)[0].toString();
		return result;
		// return null;
	}
    
    /**
     * @param uids
     * @param beginTime
     * @param endTime
     * @return  根据时间段获取该时间段内的最后位置点和状态
     */
    public List getLastLocationByTime(String uids,String beginTime,String endTime){
    	String sql = "select a.uid,a.x_ ,a.y_ from status_log as a,(select uid,max(time2) as m from status_log where uid in "+(uids)+"  and UNIX_TIMESTAMP(data)>'1417363200'  and UNIX_TIMESTAMP(data)<'1419868800'     group by data,uid ) as b where a.time2 = b.m and a.uid = b.uid";
    	List list =  pointsFormDao.findSumList(sql);
    	return list;
    }
    
    @Override
	public List getMileFromDB(String uids,String beginTime, String endTime,int mile)throws Exception{
    	String sql = "select sum(a.miles),a.uid,b.plate from status_log as a,user as b where a.uid in( "+uids+") and a.time1>="+getTime(beginTime)+" and a.time2<="+getTime(endTime)+" and a.uid = b.identifier and a.miles>"+mile+" and  a.flag = 1 group by a.uid";
       System.out.println(sql);
    	List list =  pointsFormDao.findSumList(sql);
    	return list;
    }
    
	/***********************************************
	 * 获取obd每天的详细数据明细
	 ********************************************/
    @Override
    public String getObdMileList(int uid, String beginTime, String endTime)
			throws Exception {
		// TODO Auto-generated method stub
    	String ss = "http://localhost:8080/shopHttp/services/UserRecorderService?wsdl";
    	//obdUrl
		Client client = ClientFactory.getClient(obdUrl);
		Object[] objs = new Object[3];
		objs[0] = uid;
		objs[1] = beginTime;
		objs[2] = endTime;
		String result = client.invoke("getListForForm", objs)[0].toString() ;
		
		return result;
	}

	/************************************************
	 * 获取车队车辆
	 ********************************/
    @Override
    public String getTeamCars(Account account,int accountId) throws Exception{
		// TODO Auto-generated method stub
      // String sql = "select t.team_name,t.team_id,u.user_id,u.plate from team as t,user as u where t.team_id=u.team_id and u.account_id="+accountId;
		JSONObject json = teamService.getJson_x(account, accountId,null);
       return json.toString();
	}
	@Override
	public String getPlate(int uid){
		String sql = "select plate from user where identifier = "+uid;
		List list = pointsFormDao.findSumList(sql);
		String plate = "";
		if(list != null){
		  if(list.size() > 0){
			  plate = list.get(0).toString();
		  }
		}
	  return plate;	
	}
	
	@Override
	public String getTeam(int uid){
		String sql = "";
		if(uid<100000000){
			sql = "select team_name from team as t,user as u where t.team_id=u.team_id and u.user_id = "+uid;	
		}else{
		 sql = "select team_name from team as t,user as u where t.team_id=u.team_id and u.identifier = "+uid;
		}
		List list = pointsFormDao.findSumList(sql);
		String name = "";
		if(list != null){
		  if(list.size() > 0){
			  name = list.get(0).toString();
		  }
		}
	  return name;	
	}
	
	@Override
	public List getTeamAndPlate(String uid){
		String sql = "select u.identifier,u.plate , t.team_name from user as u left join  team as t on t.team_id=u.team_id where  u.user_id in ("+uid+")";
		List list = pointsFormDao.findSumList(sql);
	  return list;	
	}
	
	/*****************************************
	 * 获取行驶报表列表数据 单人
	 **********************/
    
	@Override
    public String getDrivingList(int uid, String beginTime, String endTime,String obdmile,double fule)
			throws Exception {
		// TODO Auto-generated method stub
		mileMap = new ArrayList<Map<String,String>>();
		String sql = "select a.*,u.plate,u.account_id,u.team_id from status_log as a,user as u where a.uid = "+uid+" and u.identifier = a.uid and a.time1>="+getTime(beginTime)+" and a.time2<="+getTime(endTime)+" and a.flag = 1 order by time2";
		//List<PointsForm> list = pointsFormDao.findList(sql);
		String sqlh = "select sum(miles) as mile from status_log where uid = "+uid+" and time1>="+getTime(beginTime)+" and time2<="+getTime(endTime)+" and flag = 1";
		List list = pointsFormDao.findSumList(sql);
		List listh = pointsFormDao.findSumList(sqlh);
		int m  = 0;
		if(listh != null && listh.size()>0){
			if(listh.get(0) != null){
		 m = Integer.parseInt(listh.get(0).toString());
			}
		}
		
		//String udi_list =  getObdMile(uid+"", beginTime, endTime,0,0);
		List list_json = new ArrayList();
		JSONObject json = new JSONObject();
		int i = 1;
		for (Iterator it=list.iterator();it.hasNext();){
			Map<String,String> map = new HashedMap();
			 Object[] obj = (Object[]) it.next();
			 Point point = converter.wgs2bd(Double.parseDouble(String.valueOf(obj[3]))/(3600*24),Double.parseDouble(String.valueOf(obj[2]))/(3600*24));
			 Point point2 = converter.wgs2bd(Double.parseDouble(String.valueOf(obj[5]))/(3600*24),Double.parseDouble(String.valueOf(obj[4]))/(3600*24));
			// [4395,153206344,\"MTE0LjQyOTE0NQ\\u003d\\u003d\",\"MzAuNDQzMzYy\",1420330718,1420331069,1,0,0,0,\"鄂A6QM99\",6],
			JSONObject jsons = new JSONObject();
			jsons.put("order", i);
			jsons.put("uid", obj[1]);
			
			jsons.put("time0", getTimes(Long.parseLong(obj[6].toString())));
			
			jsons.put("time1", getTimes(Long.parseLong(obj[7].toString())));
			//System.out.println("**"+getTimes(Integer.parseInt(obj[4].toString())));
			jsons.put("maxspeed", obj[9]);
			jsons.put("minspeed",  obj[10]);
			jsons.put("pt0", String.format("%.6f", point.getLongitude())+","+String.format("%.6f", point.getLatitude()));
			jsons.put("pt1", String.format("%.6f", point2.getLongitude())+","+String.format("%.6f", point2.getLatitude()));
			jsons.put("mileage",  String.format("%.2f", Long.parseLong(obj[11].toString())/1000.0));
			jsons.put("plate",  obj[13]);
			jsons.put("accountId",  obj[14]);
			// System.out.println(obj[13].toString()+i);
			if(obj[15] != null){
				//System.out.println(obj[13]+"");
			 Team t =	teamService.get(Integer.parseInt(obj[15].toString()));
			 if(t != null)
			jsons.put("team",  t.getTeamName());
			 map.put("team",  t.getTeamName());
			// System.out.println(t.getTeamName()+i);
			}
			long obd_m = Long.parseLong(obdmile);
			double tem = (obd_m/100000.0)/(m/1000.0);
			jsons.put("timespan",Long.parseLong(obj[7].toString())-Long.parseLong(obj[6].toString()));
			if(m!=0){
				//System.out.println(obj[11].toString()+"-"+obdmile+"-"+m+"-"+fule+"-"+getTimes(Long.parseLong(obj[7].toString())));
			jsons.put("oil", String.format("%.2f", (((Long.parseLong(obj[11].toString())/1000.0)*tem*fule)/100.0)));
			map.put("oil", String.format("%.2f", (((Long.parseLong(obj[11].toString())/1000.0)*tem*fule)/100.0)));
			}else{
				jsons.put("oil", 0);
				 map.put("oil", "0");
			}
			//289059-129069228-1312818-12.2-2015-01-24 15:47:22
			list_json.add(jsons);
			 map.put("uid", obj[1].toString());
			 map.put("time0", getTimes(Long.parseLong(obj[6].toString())));
			 map.put("time1", getTimes(Long.parseLong(obj[7].toString())));
			 map.put("maxspeed", obj[9].toString());
			 map.put("minspeed",  obj[10].toString());
			 map.put("mileage",   String.format("%.2f", Long.parseLong(obj[11].toString())/1000.0));
			 map.put("plate",  obj[13].toString());
			 map.put("timespan",Long.parseLong(obj[7].toString())-Long.parseLong(obj[6].toString())+"");
			i++;
			mileMap.add(map);
			
		}
	//	Gson go = new Gson(); //211
		json.put("result", list_json);
		//json.put("dayData", udi_list);
		return json.toString();
	}
	
	
	/**************************************
	 * 获取超速报表列表数据 单人
	 **********************/
    @Override
    public String getSpeedList(int uid, String beginTime, String endTime, int sp)
			throws Exception {
		// TODO Auto-generated method stub
		List list = getHistoryPath2(uid, beginTime, endTime, sp);
		// List res_list = getOverSpeed(list);
		 
		JSONObject json = new JSONObject();
		json.put("result", list);
		return json.toString();
	}

	/********************************************
	 * 获取轨迹数据 分时间端划分
	 **********************************/
    public List getHistoryPath(int uid, String firstDate, String lastDate,int sec) {
		// {"x":"MTE0LjQxMzIzNg==","y":"MzAuNDY3Mzg1","t":"2014-12-02
		// 01:20:16","tt":1417454416,"azimuth":35,"speed":39}
    	//String plate = getPlate(uid);
//		List dataList = new ArrayList();
		List<PointInfo> poiList = new ArrayList<PointInfo>();
		// String uid = "153206344";
		// String uid = "253039860";
		//http://www.chanceit.cn:82/GetBaiduPoints/getDayPointsInfo?uid=10753&getBeginDate=201301200000&getEndDate=20130120230000
		String url = pointUrl;
		String param = "uid=" + uid + "&getBeginDate=" + firstDate
				+ "&getEndDate=" + lastDate;
		String rec = crawlPost(url, param, "utf-8");

		// 解析json字符串
		JSONObject obj;
		try {
			obj = new JSONObject(rec);

			boolean isOK = (Boolean) obj.get("isok");

			if (isOK) {
				JSONArray pointArray = (JSONArray) obj.get("points");
				// gps = new Object[1][pointArray.length()];
				int times = 1;
				int length = pointArray.length();
				
//				if(length > 400){
//					times = length/400+1;
//				}
				int time_temp = 0;
				for (int i = 0; i < length;i++) {
					
					PointInfo poi = new PointInfo();
					JSONObject jo = pointArray.getJSONObject(i);
					//JSONObject jox = pointArray.getJSONObject(i + 1);
					String x = "";
					String y = "";
					boolean base64 = (Boolean) obj.get("base64");
					// 判断是否加密,如果加密就进行解密处理
					if (base64) {
						byte[] deX = Base64.decode(jo.getString("x"));
						byte[] deY = Base64.decode(jo.getString("y"));
						x = new String(deX);
						y = new String(deY);
					} else {
						x = jo.getString("x");
						y = jo.getString("y");
					}

					String time = jo.getString("t");
					int time_int = jo.getInt("tt");
					//int time_int_x = jox.getInt("tt");
					int speed = 0;
					int azimuth = 0;
					// 判断老数据是否有方位角
					if (jo.get("speed") != null) {
						speed = jo.getInt("speed");
					}
					if (jo.get("azimuth") != null) {
						// 方位角除以10 然后四舍五入取整
						Double azi = jo.getDouble("azimuth") / 10;
						BigDecimal bd = new BigDecimal(azi).setScale(0,
								BigDecimal.ROUND_HALF_UP);
						azimuth = bd.intValue();
					}
					poi.setX(x);
					poi.setY(y);
					poi.setTime(time);
					poi.setTimeInt(time_int);
					poi.setAzimuth(azimuth);
					poi.setSpeed(speed);
//					if (maxSp < speed) {
//						maxSp = speed;
//					}
//					if (minSp > speed) {
//						minSp = speed;
//					}
					if(i == 0){
						poiList.add(poi);
						time_temp = time_int;
					}else{
						//if(time_temp+180<time_int){
						if(time_temp+sec<time_int){//根据传过来的sec来处理多长时间一个点
							poiList.add(poi);	
							time_temp = time_int;
						}
					}
					//time_temp = time_int;
					//}
					//if ((time_int - time_temp) >= times) {
						//poiList.add(poi);
						//time_temp = time_int;
					//}
					//i= i+times;
				}
				System.out.println(poiList.size()+"/////////***");
				return poiList;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


    /**
     * 通过x,y坐标获取实际地址
     * @param x
     * @param y
     * @return
     * @throws IOException
     */
    public String testPost(String x, String y) throws IOException {
        URL url = new URL("http://api.map.baidu.com/geocoder/v2/?ak=OPiT6AzjvCc74QymTZr7CWP7&location=" + y
                        + "," + x + "&output=json");
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);//
		connection.setReadTimeout(40000);//
		connection.setUseCaches(false);
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection
                .getOutputStream(), "utf-8");
        out.flush();
        out.close();
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                l_urlStream,"UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        String str = sb.toString();
      //  System.out.println(str);
    if(StringUtils.isNotEmpty(str)) {
//      int addStart = str.indexOf("formatted_address\":");
//      int addEnd = str.indexOf("\",\"business");
//      if(addStart > 0 && addEnd > 0) {
//        address = str.substring(addStart+20, addEnd);
//        return address;		
//      }
    	String address;
    	JSONObject jo;
		try {
			jo = new JSONObject(str);
			address=jo.getJSONObject("result").getString("formatted_address");
			return address;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    return null;
    
    }
    
	/**********************************************
	 * 获取轨迹数据 按速度值划分
	 ***************************************/
	public List getHistoryPath2(int uid, String firstDate, String lastDate,
			int sp) {
		// {"x":"MTE0LjQxMzIzNg==","y":"MzAuNDY3Mzg1","t":"2014-12-02
		// 01:20:16","tt":1417454416,"azimuth":35,"speed":39}
		String plate = getPlate(uid);
		List data_list = new ArrayList();
		List<PointInfo> poiList = new ArrayList<PointInfo>();
		int maxSp = 0;
		int minSp = 0;
		// String uid = "153206344";
		// String uid = "253039860";
		String url = pointUrl;
		String param = "uid=" + uid + "&getBeginDate=" + firstDate
				+ "&getEndDate=" + lastDate;
		String rec = crawlPost(url, param, "utf-8");

		// 解析json字符串
		JSONObject obj;
		try {
			obj = new JSONObject(rec);

			boolean isOK = (Boolean) obj.get("isok");

			if (isOK) {
				JSONArray pointArray = (JSONArray) obj.get("points");
				// gps = new Object[1][pointArray.length()];
				for (int i = 0; i < pointArray.length(); i++) {
					PointInfo poi = new PointInfo();
					JSONObject jo = pointArray.getJSONObject(i);
					//JSONObject jox = pointArray.getJSONObject(i + 1);
					String x = "";
					String y = "";
					boolean base64 = (Boolean) obj.get("base64");
					// 判断是否加密,如果加密就进行解密处理
					if (base64) {
						byte[] deX = Base64.decode(jo.getString("x"));
						byte[] deY = Base64.decode(jo.getString("y"));
						x = new String(deX);
						y = new String(deY);
					} else {
						x = jo.getString("x");
						y = jo.getString("y");
					}

					String time = jo.getString("t");
					int time_int = jo.getInt("tt");
					//int time_int_x = jox.getInt("tt");
					int speed = 0;
					int azimuth = 0;
					// 判断老数据是否有方位角
					if (jo.get("speed") != null) {
						speed = jo.getInt("speed");
					}
					if (jo.get("azimuth") != null) {
						// 方位角除以10 然后四舍五入取整
						Double azi = jo.getDouble("azimuth") / 10;
						BigDecimal bd = new BigDecimal(azi).setScale(0,
								BigDecimal.ROUND_HALF_UP);
						azimuth = bd.intValue();
					}
					poi.setX(x);
					poi.setY(y);
					poi.setTime(time);
					poi.setTimeInt(time_int);
					poi.setAzimuth(azimuth);
					poi.setSpeed(speed);
					if (speed >= sp) {
						if (maxSp < speed) {
							maxSp = speed;
						}
						if (minSp > speed) {
							minSp = speed;
						}
						poiList.add(poi);
					} else {
						if (poiList.size() > 1) {
							String res = getOverSpeed(uid,poiList, maxSp, minSp, plate);
							data_list.add(res);
						}
						minSp = 0;
						maxSp = 0;
						poiList = new ArrayList<PointInfo>();
					}
					
					if(data_list.size() == 0 && poiList.size() == pointArray.length() && i== pointArray.length()-1){
						String res = getOverSpeed(uid,poiList, maxSp, minSp, plate);
						data_list.add(res);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data_list;
	}

	/*********************************************
	 * 批量获取位置点
	 **************************************/
	@Override
	public Map<String,String> getLocation(String uids) {
		// {"x":"MTE0LjQxMzIzNg==","y":"MzAuNDY3Mzg1","t":"2014-12-02
		// 01:20:16","tt":1417454416,"azimuth":35,"speed":39}
		//String sql = "select * from status_log as a,(select uid,max(time2) as m from status_log where uid = 153206344  and UNIX_TIMESTAMP(data)>'1417363200'  and UNIX_TIMESTAMP(data)<'1419868800'     group by data,uid ) as b where a.time2 = b.m and a.uid = b.uid";
		Map<String,String> map = new HashedMap();
		// String uid = "153206344";
		// String uid = "253039860";
		String url = locationUrl;
		String param = "uids=" + uids;
		String rec = crawlPost(url, param, "utf-8");
		// 解析json字符串
		JSONObject obj;
		try {
			obj = new JSONObject(rec);

			boolean isOK = (Boolean) obj.get("isok");
			
			if (isOK) {
				JSONArray pointArray = (JSONArray) obj.get("data");
				// {"uid":"","time":0,"x":0,"y":0,"datatype":1,"online":0,"speed":0,"angle":0}
				for (int i = 0; i < pointArray.length(); i++) {
					
					JSONObject jo = pointArray.getJSONObject(i);
					JSONObject json = new JSONObject();
//					json.put("uid", jo.getString("uid"));
//					json.put("x", jo.getDouble("x"));
//					json.put("y", jo.getDouble("y"));
//					json.put("online", jo.getInt("online"));
//					json.put("speed", jo.getInt("speed"));
//					json.put("azimuth", jo.getInt("angle"));
					map.put(jo.getString("uid"), jo.getDouble("x")+","+jo.getDouble("y")+","+jo.getInt("time"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	/*********************************************
	 * 获取位置点  个人 根据轨迹获取
	 **************************************/
	@Override
	public List getLocationOne(int uid,String firstDate, String lastDate,int sec) {
		// {"x":"MTE0LjQxMzIzNg==","y":"MzAuNDY3Mzg1","t":"2014-12-02
		// 01:20:16","tt":1417454416,"azimuth":35,"speed":39}
		
		List<PointInfo> poiList = getHistoryPath(uid,  firstDate,  lastDate,sec);
		return poiList;
	}
	
	
	/**************************************************
	 * 得到停止报表数据（多人）
	 *******************************/
	@Override
	public  List getStopList(String uids,int beginTime,int endTime,int second){
	 //  String sql = "select a.uid,a.x,a.y,b.t,b.tt from status_log as a,(select uid,sum(time2-time1) as t,max(time2) as tt from status_log group by uid ) as b where a.uid=b.uid and a.time2=b.tt";
	   String sql = "select uid,sum(time2-time1) as t from status_log where time1>="+beginTime+" and time2<="+endTime+" and time2-time1>"+second+" and flag = 0 and uid in ("+uids+") group by uid";
	  System.out.println(sql);
	   List list = pointsFormDao.findSumList(sql);
//	   for (Iterator iterator = list.iterator();iterator.hasNext();) {
	    return list;
   } 
   /**********************************************
	 * 得到停止列表数据（单人）
	 *****************************/
	@Override
	public  List getStopForm(int uid,int beginTime,int endTime,int second){
	 //  String sql = "select a.uid,a.x,a.y,b.t,b.tt from status_log as a,(select uid,sum(time2-time1) as t,max(time2) as tt from status_log group by uid ) as b where a.uid=b.uid and a.time2=b.tt";
	   String sql = "select * from status_log where time1>="+beginTime+" and time2<="+endTime+" and time2-time1>"+second+" and  flag = 0 and uid = "+uid+" ";
	   List list = pointsFormDao.findList(sql);
	   return list;
  } 
   /***************************************
	 * 得到点火报表数据  多人
	 **************************************/
	@Override
	public  List getFireList(String uids,int beginTime,int endTime,int second){
	  // String sql = "select a.uid,a.x,a.y,b.t,b.tt from status_log as a,(select uid,sum(time2-time1) as t,max(time2) as tt from status_log group by uid ) as b where a.uid=b.uid and a.time2=b.tt";
	  String sql = "select uid,sum(time2-time1) as t from status_log where time1>="+beginTime+" and time2<="+endTime+" and time2-time1>"+second+" and flag = 1 and uid in ("+uids+") group by uid";
	  List list = pointsFormDao.findSumList(sql); 
	  return list;
  } 
  /********************************************
	 * 得到点火报表列表数据  单人
	 ************************************/
	@Override
	public  List getFireFormList(int uid,int beginTime,int endTime,int second){
	  // String sql = "select a.uid,a.x,a.y,b.t,b.tt from status_log as a,(select uid,sum(time2-time1) as t,max(time2) as tt from status_log group by uid ) as b where a.uid=b.uid and a.time2=b.tt";
	  String sql = "select * from status_log where time1>="+beginTime+" and time2<="+endTime+" and time2-time1>"+second+" and  flag = 1 and uid = "+uid+" order by time2 desc";
	  List list = pointsFormDao.findList(sql);
	  return list;
    } 
	/****************************************
	 * 得到月上线率
	 *************************/
	@Override
	public List getOnlineByMonth(String uids,String beginTime,String endTime) throws Exception{
		//String sql = "select uid, count(*) as count from status_log where  data>data and data<data group by data,uid";
		String sql_x = "select a.uid,count(a.uid) as c from (select uid,count(id),data  from status_log where  UNIX_TIMESTAMP(data)>='"+getTime(beginTime)+"' and UNIX_TIMESTAMP(data)<='"+getTime(endTime)+"' and uid in("+uids+") group by data,uid) as a group by a.uid";
		List list = pointsFormDao.findSumList(sql_x);
		return list;
	}
	
	/****************************************
	 * 得到某一个用户的月上线率
	 *************************/
	@Override
	public List getBodyByMonth(String uids,String beginTime,String endTime) throws Exception{
		//String sql = "select uid, count(*) as count from status_log where  data>data and data<data group by data,uid";
		String sql_x = "select uid,data  from status_log where  UNIX_TIMESTAMP(data)>='"+getTime(beginTime)+"' and UNIX_TIMESTAMP(data)<='"+getTime(endTime)+"' and uid ="+uids+" group by data,uid";
		List list = pointsFormDao.findSumList(sql_x);
		return list;
	}
	
	/***************************************
	 * 得到日上线率
	 *************************/
	@Override
	public List getOnlineByDay(String uids,String beginTime,String endTime)throws Exception{
		 String sql = "select count(a.data) as count ,a.data from (select count(id),data  from status_log where  UNIX_TIMESTAMP(data)>='"+getTime(beginTime)+"' and UNIX_TIMESTAMP(data)<='"+getTime(endTime)+"' and uid in("+uids+") group by data,uid) as a group by a.data order by  UNIX_TIMESTAMP(a.data)";
		 System.out.println(sql);
		 List list = pointsFormDao.findSumList(sql);
			return list;
			
	}
	
	/***************************************
	 * 得到断电报警统计数据 柱状图
	 *************************/
	@Override
	public String getBreakDian(String uids,String beginTime,String endTime) throws Exception{
		//String sql = "select a.*,b.identifier from boxreminder a,user b where b.identifier in ("+uids+") and b.user_id = a.vehicle_id";
		 String wsdlURL = "http://localhost:8080/shopHttp/services/UserOBDInfoService?wsdl"; 
		Resource resource;
			resource = new UrlResource(breakURL);
			Client client = new Client(resource.getURL(), null);
			Object[] objs = new Object[3];
			//objs[0]="B0001";
			objs[0] = uids;
			objs[1] = beginTime;
			objs[2] = endTime;
			//'20130201,20130401'
			//getDuandianList,getDuandianCount
			String result = client.invoke("getDuandianCount", objs)[0].toString();;
			return result;
	}
	/***************************************
	 * 得到断电报警统计数据 个人
	 *************************/
	@Override
	public String getBreakDianOne(String uid,String beginTime,String endTime) throws Exception{
		//String sql = "select a.*,b.identifier from boxreminder a,user b where b.identifier in ("+uids+") and b.user_id = a.vehicle_id";
		Resource resource;
			resource = new UrlResource(breakURL);
			Client client = new Client(resource.getURL(), null);
			Object[] objs = new Object[3];
			//objs[0]="B0001";
			objs[0] = uid;
			objs[1] = beginTime;
			objs[2] = endTime;
			//'20130201,20130401'
			//getDuandianList,getDuandianCount
			String result = client.invoke("getDuandianList", objs)[0].toString();;
			return result;
	}
	/***************************************
	 * 得到碰撞，侧翻，超速报警统计数据 柱状图
	 *************************/
	@Override
	public List getPengZhuangForm(String uids,int type,String beginTime,String endTime){
		String sql = "select count(b.warn_id) as count,b.vehicle_id, u.plate,u.identifier as identifier from boxreminder as b,user as u where b.vehicle_id=u.user_id and b.type="+type+" and  b.vehicle_id in("+uids+") and b.create_time>='"+beginTime+"' and b.create_time<='"+endTime+"' group by b.vehicle_id";
		 List list = pointsFormDao.findSumList(sql);
		return list;
	}
	
	
	/***************************************
	 * 得到碰撞，侧翻，超速报警统计数据 个人
	 *************************/
	@Override
	public List getPengZhuangList(int uid,int type,String beginTime,String endTime){
		String sql = "select b.x,b.y,b.create_time,u.plate from boxreminder as b,user as u where b.vehicle_id=u.user_id and b.type="+type+" and vehicle_id="+uid+" and b.create_time>='"+beginTime+"' and b.create_time<='"+endTime+"'";
		 System.out.println(sql);
		 List list = pointsFormDao.findSumList(sql);
		return list;
	}
	
	@Override
	public List getOverSpeedForm(String uids,String beginTime,String endTime)throws Exception{
		String sql = "select count(a.uid) as count,a.uid as uid,u.plate as plate,u.identifier as identifier from speed_log as a,user as u where a.uid = u.identifier and a.time1>= "+getTime(beginTime)+" and a.time2<="+getTime(endTime)+" and a.uid in("+uids+") group by a.uid";
		 List list = pointsFormDao.findSumList(sql);
		return list;
	}
	
	@Override
	public List getOverSpeedList(int uid,String beginTime,String endTime) throws Exception{
		String sql = "select a.x,a.y,a.time1,u.plate, a.max_speed from speed_log as a,user as u where a.uid = u.identifier and a.time1>= "+getTime(beginTime)+" and a.time2<="+getTime(endTime)+" and a.uid = "+uid;
		System.out.println(sql);
		List list = pointsFormDao.findSumList(sql);
		return list;
	}
	// /********
	// * 解析轨迹数据得到行驶报表统计
	// * **/
	// public List getDataByPath(List<List<PointInfo>> list){
	// List dataList = new ArrayList();
	//		
	// for(int i = 0;i < list.size(); i++ ){
	// JSONObject json = new JSONObject();
	// try {
	// System.out.println(list.get(i).size());
	// json.put("bx", list.get(i).get(0).getX());
	// json.put("by", list.get(i).get(0).getY());
	// json.put("ex", list.get(i).get(list.get(i).size()-1).getX());
	// json.put("ey", list.get(i).get(list.get(i).size()-1).getY());
	// json.put("bTime", list.get(i).get(0).getTimeInt());
	// json.put("eTime", list.get(i).get(list.get(i).size()-1).getTimeInt());
	// json.put("highSpeed", getMaxSpeed(list.get(i)));
	// json.put("time_str",
	// list.get(i).get(list.get(i).size()-1).getTimeInt()-list.get(i).get(0).getTimeInt());
	// // json.put("bx", list.get(i).get(0).getX());
	// dataList.add(json);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// return dataList;
	// }

	/**
	 * 解析轨迹数据得到报表统计数据
	 * 
	 * @param list
	 * @return
	 */
	public String getOverSpeed(int uid,List<PointInfo> resList, int maxSp, int minSp,String plate) {
		JSONObject json = new JSONObject();
		if (resList.size() > 0) {
			try {
				json.put("uid", uid);
				json.put("plate", plate);
				json.put("bx", resList.get(0).getX());
				json.put("by", resList.get(0).getY());
				json.put("ex", resList.get(resList.size() - 1).getX());
				json.put("ey", resList.get(resList.size() - 1).getY());
				json.put("bTime", resList.get(0).getTimeInt());
				json.put("eTime", resList.get(resList.size() - 1).getTimeInt());
				json.put("maxSp", maxSp);
				json.put("minSp", minSp);
				json.put("time_str", resList.get(resList.size() - 1)
						.getTimeInt()
						- resList.get(0).getTimeInt());
				// json.put("bx", list.get(i).get(0).getX());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json.toString();
	}
	/**
	 * 把一定格式的时间装换成秒数
	 */
	private int getTime(String strtime) throws ParseException{
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	    Date date = format.parse(strtime);  
	    return (int) (date.getTime()/1000);
	}
	/**
	 * 把一定格式的时间装换成秒数
	 */
	private String getTimes(Long strtime) throws ParseException{
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(strtime*1000);
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    Date date = new Date(strtime*1000);
	    format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	    return format.format(date);
	}

	public String getPointUrl() {
		return pointUrl;
	}

	public void setPointUrl(String pointUrl) {
		this.pointUrl = pointUrl;
	}

	public String getObdUrl() {
		return obdUrl;
	}

	public void setObdUrl(String obdUrl) {
		this.obdUrl = obdUrl;
	}

	public String getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(String locationUrl) {
		this.locationUrl = locationUrl;
	}
	 /**
     * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
     * @param time
     * @return
     */
    public static Integer StringToTimestamp(String time){
    
        int times = 0;
        try {  
            times = (int) ((Timestamp.valueOf(time).getTime())/1000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        if(times==0){
            System.out.println("String转10位时间戳失败");
        }
        return times; 
         
    }

	public String getBreakURL() {
		return breakURL;
	}

	public void setBreakURL(String breakURL) {
		this.breakURL = breakURL;
	}
	
	public String crawlPost(String url,String para, String charset) {
		String content = "";
		BufferedReader reader= null;
		try{
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(connectionTimeOut);//
			connection.setReadTimeout(readTimeOut);//
			connection.setUseCaches(false);
				connection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),charset);
				out.write(para);
				out.flush();
				out.close();
			String sCurrentLine = "";
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
					charset == null ? this.charSet : charset));// 读取网页源代码，如果没有指定字符集，则使用默认的UTF-8
			while ((sCurrentLine = reader.readLine()) != null) {
				content += sCurrentLine;
			}
			System.out.println(content);
		} catch (IOException e) {
			close(reader);
		}finally {
			close(reader);
		}
		return content;
	}
	public static void close(Closeable c) {
		if(c!=null)
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				c = null;
			}
	}
}
