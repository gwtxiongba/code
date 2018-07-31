package com.chanceit.http.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.Converter;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Member;
import com.chanceit.http.pojo.Point;
import com.chanceit.http.pojo.PointInfo;
import com.chanceit.http.pojo.PointsForm;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.IFormsService;
import com.chanceit.http.service.IMemberService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;

/**
 * @ClassName ExcelFormsView
 * @author gwt
 * @date 2015-01-02
 * @Description 报表统计接口
 */
@Component("excelFormsView")
@SuppressWarnings("unchecked")
public class ExcelFormsView extends BaseAction {
	@Autowired
	@Qualifier("driverService")
	private IDriverService driverService;
	
	@Autowired
	@Qualifier("memberService")
	private IMemberService memberService;
	
	@Autowired
	@Qualifier("formService")
	private IFormsService formService;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	private List<Map<String, String>> excelMap1 = null;
	private List<Map<String, String>> excelMap2 = null;

	/**
	 * 获取报表柱状图数据（总里程，油耗等）
	 */
	public String getDrivingForm() throws Exception {
		// http://192.168.0.153:8080/carmonitor/api.action?cmd=formdDriving&date0=2015-01-09&date1=2015-01-09&vehicleId=3,4&plate=&mileage=&fuel=&_dc=1420798038697&page=1&start=0&limit=20
		Map<String, String> map = new HashedMap();
		excelMap1 = new ArrayList<Map<String, String>>();
//		String uidsAfter = "";
//		String uuid ="";
//		List list_json = new ArrayList();
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String plate = getParameter("plate");
		String fule = getParameter("fuel");
		String mile = getParameter("mileage");
		String userIds = getParameter("vehicleIds");
		float f = 0;
		int m = 0;
		String user_ids = "";
		// if(!StringUtils.isEmpty(userIds)){
		if (!StringUtils.isEmpty(plate)) {
			List<User> users = userService.getUidByPlate(plate);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
					map.put(user_ids, plate);
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}else if(!StringUtils.isEmpty(userIds)){
		List<User> usersList = userService.getUidById(userIds);
		
		for (int i = 0; i < usersList.size(); i++) {
			map.put(usersList.get(i).getIdentifier(), usersList.get(i)
					.getPlate());
			if (i == 0) {
				user_ids = usersList.get(0).getIdentifier();
			} else {
				user_ids += "," + usersList.get(i).getIdentifier();
			}
		}
		 }else{
			return ResultManager.getFaildResult("请选择正确的车辆"); 
		 }
		
		if (!StringUtils.isEmpty(fule)) {
			f = Float.parseFloat(fule);
		}
		if (!StringUtils.isEmpty(mile)) {
			m = Integer.parseInt(mile);
		}
		// [{"f":0,"time":107022,"mileage":930608,"vehicleId":253006938,"obdmile":87180116,"fuel":"0.00"}]
		if(StringUtils.isEmpty(user_ids)){
			return	ResultManager.getFaildResult("请选择正确的车辆");	
		}
		String result = formService.getObdMile(user_ids, beginTime, endTime, m,
				f);
		JSONArray resultArray = new JSONArray();
		JSONObject json = new JSONObject(result);
		
//		}
		if(!json.has("result")){
			return	ResultManager.getFaildResult("数据解析错误");
		}
		String s = json.getString("result");
		JSONArray array = new JSONArray(s);

		for (int i = 0; i < array.length(); i++) {
			Map<String, String> o_map = new HashedMap();
			JSONObject item = array.getJSONObject(i);
			o_map.put("plate", map.get(item.getInt("vehicleId") + ""));
			o_map.put("mile", item.getString("mileage"));
			o_map.put("fuel", item.getString("fuel"));
			o_map.put("uid", item.getInt("vehicleId") + "");
			item.put("plate", map.get(item.getInt("vehicleId") + ""));
			excelMap1.add(o_map);
			resultArray.put(item);
		}
		String uid_str = json.getString("uid_str");
		String[] uidArray = uid_str.split(",");
		String uidss = "";
		for (int i = 0; i < uidArray.length; i++) {
			if (!uidArray[i].equals("0")) {
				if (uidss.length() > 0) {
					Map<String, String> o_map = new HashedMap();
					o_map.put("plate", map.get(uidArray[i]));
					o_map.put("uid", map.get(uidArray[i]));
					o_map.put("mile", "0");
					o_map.put("fuel", "0");
					excelMap1.add(o_map);
				}
			}
		}
		exportCycleMonth(excelMap1,1);
		return resultArray.toString();
	}

	/**
	 * 获取超速报表列表数据
	 */
	public String getOverSpeed() throws Exception {
		String uid = getParameter("uid");
		String beginTime = getParameter("beginTime");
		String endTime = getParameter("endTime");
		String speed = getParameter("speed");
		int sp = 0;
		if (!StringUtils.isEmpty(speed)) {
			sp = Integer.parseInt(speed);
		}
		String result = formService.getSpeedList(Integer.parseInt(uid),
				beginTime, endTime, sp);
		System.out.println(result);
		return result;
	}

	/**
	 * 获取位置报表数据
	 */
	@SuppressWarnings("unchecked")
	public String getLocation() throws Exception {
		excelMap1 = new ArrayList<Map<String,String>>();
		String vehicleId = getParameter("vehicleId");
		String beginTime = getParameter("date");
		beginTime = beginTime.replaceAll("-", "");
		String plate_ = getParameter("plate");
		String endTime = beginTime+"2359";
		String plate = "";
		String team = "";
		String uid = "";
//		if(StringUtils.isEmpty(vehicleId)){
//			return ResultManager.getFaildResult("参数错误");
//		}
	
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					uid = users.get(0).getIdentifier();
					plate = plate_;
					team = formService.getTeam(Integer.parseInt(uid));
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}
		else if(!StringUtils.isEmpty(vehicleId)){
		List list_p = formService.getTeamAndPlate(vehicleId);
		if(list_p != null && list_p.size() > 0 ){
			Object[] obj = (Object[]) list_p.get(0);
			if(obj[1] != null){
			plate = obj[1].toString();
			}
			if(obj[2] != null){
			team = obj[2].toString();
			}
			if(obj[0] != null){
				uid = obj[0].toString();
			}
		}
		}else{
			return	ResultManager.getFaildResult("请选择正确的车辆");
		}
		
		List<PointInfo> list = formService.getLocationOne(Integer.parseInt(uid), beginTime+"0000", endTime,25);//报表导出 (25+5)秒一个点
		JSONArray array = new JSONArray();
		//{"vehicleId":10,"plate":"\u9102A6QM10","team":"\u6e56\u5317\u8f93\u6cb9\u6c14\u5206\u516c\u53f8","gps":[116.307852,40.057031,"2015-01-12 9:15:24",56,89]}
		if(list == null){
			return ResultManager.getFaildResult("暂无数据");
			}
		//http://api.map.baidu.com/geocoder/v2/?ak=OPiT6AzjvCc74QymTZr7CWP7&callback=renderReverse&location=30.459967,113.412966&output=json
		String address;
		for(int i=0;i<list.size();i++){
			Map map = new HashedMap();
			PointInfo info = list.get(i);
			address=formService.testPost(info.getX(), info.getY());
			map.put("plate", plate);
			map.put("uid", uid);
			map.put("team", team);
			map.put("x", info.getX());
			map.put("y", info.getY());
			map.put("time", info.getTime());
			map.put("speed", info.getSpeed());
			if(address == null){
				address = "";
			}
			map.put("address", address);
			excelMap1.add(map);
		}
		exportCycleMonth(excelMap1,2);
		return null;
	}

	/**
	 * 获取行驶报表列表数据
	 */
	public String getDrivingList() throws Exception {
		// http://192.168.0.153:8080/carmonitor/api.action?cmd=formdPath&date0=2015-01-01&date1=2015-01-10&vehicleId=253006938&obdmile=1228207&fuel=0.00&_dc=1420856865953&page=1&start=0&limit=20
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formdPath&date0=2014-12-01&date1=2015-01-11&vehicleId=153206344&obdmile=122363458&fuel=7.54&_dc=1421033572914&page=1&start=0&limit=20
		excelMap1 = new ArrayList<Map<String,String>>();
		String uid = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String fule = getParameter("fuel");
		String mile = getParameter("obdmile");
		float m = 0;
		float f = 0;
		if (!StringUtils.isEmpty(mile)) {
			m = Float.parseFloat(mile);
		}
		if (!StringUtils.isEmpty(fule)) {
			f = Float.parseFloat(fule);
		}
		if(StringUtils.isEmpty(uid)){
			return ResultManager.getFaildResult("参数错误");
		}
		String result = formService.getDrivingList(Integer.parseInt(uid),
 			beginTime, endTime, mile, f);
		JSONObject json = new JSONObject(result);
		String s = json.getString("result");
		JSONArray arra  =  new JSONArray(s);
	  //{"uid":194340920,"mileage":"0.02","accountId":6,"minspeed":0,"plate":"鄂A40920","oil":"0.00","time1":"2015-01-01 06:13:14","order":1,"time0":"2015-01-01 06:11:29","timespan":105,"pt1":"35.59253679349611,103.1946737729536","pt0":"35.592600385117166,103.19464190216023","maxspeed":22}
		for(int i = 0;i<arra.length();i++){
			JSONObject json_r = arra.getJSONObject(i);
			Map map_m = new HashedMap();
			map_m.put("uid", json_r.getInt("uid"));
			map_m.put("plate", json_r.getString("plate"));
			map_m.put("mile", json_r.getString("mileage"));
			map_m.put("time0", json_r.getString("time0"));
			map_m.put("time1", json_r.getString("time1"));
			map_m.put("times", json_r.getInt("timespan"));
			map_m.put("speed", json_r.getInt("maxspeed"));
			map_m.put("pt1", json_r.getString("pt0"));
			map_m.put("pt2", json_r.getString("pt1"));
			excelMap1.add(map_m);
		}
		exportCycleMonth(excelMap1, 10);
		return s;
	}

	/**
	 * 获取里程报表列表数据
	 */
	public String getObdMileList() throws Exception {
		excelMap1 = new ArrayList<Map<String,String>>();
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formObdList&date0=2014-12-01&date1=2015-01-11&vehicleIds=9824,10418,10429,10189,10428,4627,9777,9779,9788,9780,9792,9793&plate=&_dc=1421029309493&page=1&start=0&limit=20
		String userIds = getParameter("vehicleIds");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		Map<String, String> map_l =  new HashedMap();
		Map<String, String> map = new HashedMap();
		String plate = getParameter("plate");
		float f = 0;
		int m = 0;
		String user_ids = "";
		// if(!StringUtils.isEmpty(userIds)){
		if (!StringUtils.isEmpty(plate)) {
			List<User> users = userService.getUidByPlate(plate);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
					map.put(user_ids, plate);
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}else if(!StringUtils.isEmpty(userIds)){
		List<User> usersList = userService.getUidById(userIds);
		
		for (int i = 0; i < usersList.size(); i++) {
			map.put(usersList.get(i).getIdentifier(), usersList.get(i)
					.getPlate());
			if (i == 0) {
				user_ids = usersList.get(0).getIdentifier();
			} else {
				user_ids += "," + usersList.get(i).getIdentifier();
			}
		}
		 }else{
			return ResultManager.getFaildResult("请选择正确的车辆"); 
		 }
		
		// [{"f":0,"time":107022,"mileage":930608,"vehicleId":253006938,"obdmile":87180116,"fuel":"0.00"}]
		if(StringUtils.isEmpty(user_ids)){
			return	ResultManager.getFaildResult("请选择正确的车辆");	
		}
		map_l = formService.getLocation(user_ids);
		String result = formService.getObdMile(user_ids, beginTime, endTime, m,
				f);
		JSONArray resultArray = new JSONArray();
		JSONObject json = new JSONObject(result);
		if(!json.has("result")){
			return	ResultManager.getFaildResult("数据解析错误");
		}
		String s = json.getString("result");
		JSONArray array = new JSONArray(s);
		String uiss = user_ids;
		for (int i = 0; i < array.length(); i++) {
			Map<String, String> o_map = new HashedMap();
			JSONObject item = array.getJSONObject(i);
			o_map.put("plate", map.get(item.getInt("vehicleId") + ""));
			o_map.put("mile", item.getString("mileage"));
			o_map.put("fuel", item.getString("fuel"));
			o_map.put("uid", item.getInt("vehicleId") + "");
			String  ss = map_l.get(item.getInt("vehicleId") + "");
			String[] strs = ss.split(",");
			o_map.put("x",  strs[0]);
			o_map.put("y",  strs[1]);
			o_map.put("date",  beginTime+"至"+endTime);
			uiss = uiss.replaceAll(item.getInt("vehicleId")+"", "0");
			excelMap1.add(o_map);
		}
		String[] uuss = uiss.split(",");
		for (int i = 0; i < uuss.length; i++) {
			if(!uuss[i].equals("0")){
			Map<String, String> o_map = new HashedMap();
			o_map.put("plate", map.get(uuss[i]));
			o_map.put("uid", uuss[i]);
			o_map.put("mile", "0");
			o_map.put("fuel", "0");
			o_map.put("x",  "0");
			o_map.put("y", "0");
			o_map.put("date",  beginTime+"至"+endTime);
			excelMap1.add(o_map);
			}
		}
		exportCycleMonth(excelMap1,3);
		return resultArray.toString();
	}


	/**
	 * 获取停车报表柱状图数据
	 */
	public String getStopForm() throws Exception {
		// http://192.168.0.153:8080/carmonitor/api.action?cmd=formStop&date0=2015-01-11&date1=2015-01-11&vehicleId=3&plate=&timeout=&_dc=1420954388891&page=1&start=0&limit=20
	   excelMap1 = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashedMap();
		
		String uids = getParameter("vehicleIds");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String second = getParameter("timeout");
		String plate_ = getParameter("plate");
		int sec = 0;
		String user_ids = "";
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
					map.put(user_ids, plate_);
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}else if(!StringUtils.isEmpty(uids)){
			List<User> usersList = userService.getUidById(uids);
			
			for (int i = 0; i < usersList.size(); i++) {
				map.put(usersList.get(i).getIdentifier(), usersList.get(i)
						.getPlate());
				if (i == 0) {
					user_ids = usersList.get(0).getIdentifier();
				} else {
					user_ids += "," + usersList.get(i).getIdentifier();
				}
			}
		}else{
			return ResultManager.getFaildResult("参数错误");	
		}
		if (!StringUtils.isEmpty(second)) {
			sec = Integer.parseInt(second);
		}
		
		List result = formService.getStopList(user_ids, getTime(beginTime),
				getTime(endTime), sec);
		JSONArray  array = new JSONArray();
		String uidafter = user_ids;
		for (Iterator it = result.iterator(); it.hasNext();) {
			//Map<String, String> o_map = new HashedMap();
			Map map_ = new HashedMap();
			Object[] obj = (Object[]) it.next();
			map_.put("uid", obj[0]);
			map_.put("plate",map.get(obj[0].toString()));
			map_.put("times",obj[1]);
			excelMap1.add(map_);
			System.out.println(obj[0]+"****");
			uidafter = uidafter.replaceAll(obj[0].toString(), "0");
			
		}
		String[] uus = uidafter.split(",");
		for (int j = 0; j < uus.length; j++) {
			
			Map map_m = new HashedMap();
			if(!uus[j].equals("0")){
			map_m.put("order", j);
			map_m.put("plate", map.get(uus[j]));
			map_m.put("uid", uus[j]);
			map_m.put("times", 0);
			excelMap1.add(map_m);
			}
		}
		exportCycleMonth(excelMap1,4);
		return array.toString();
	}

	/**
	 * 获取停车报表列表数据
	 */
	public String getStopList() throws Exception {
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formdPath&date0=2014-12-01&date1=2015-01-11&vehicleId=153206344&_dc=1420955593216&page=1&start=0&limit=20
		excelMap1 = new ArrayList<Map<String,String>>();
		String uid = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String second = getParameter("timeout");
		String teamname =	formService.getTeam(Integer.parseInt(uid));
		String plate = formService.getPlate(Integer.parseInt(uid));
		int sec = 0;
		if (!StringUtils.isEmpty(second)) {
			sec = Integer.parseInt(second);
		}
		if(StringUtils.isEmpty(uid)){
			return ResultManager.getFaildResult("参数错误");
		}
		List<PointsForm> result = formService.getStopForm(Integer.parseInt(uid),
				getTime(beginTime), getTime(endTime), sec);
		JSONArray array = new JSONArray();
		for(int i=0;i<result.size();i++){
			Map map = new HashedMap();
			PointsForm pf = result.get(i);
			Point point = converter.wgs2bd(Double.parseDouble(String.valueOf(pf.getY()))/(3600*24),Double.parseDouble(String.valueOf(pf.getX()))/(3600*24));
			map.put("order",i+1);
			map.put("plate",plate);
			map.put("uid",uid);
			map.put("team",teamname);
			map.put("time0",getTimes(Long.parseLong(pf.getTime1().toString())));
			map.put("time1",getTimes(Long.parseLong(pf.getTime2().toString())));
			map.put("times",pf.getTime2()-pf.getTime1());
			map.put("x",point.getLatitude());
			map.put("y",point.getLongitude());
			excelMap1.add(map);
		}
		exportCycleMonth(excelMap1, 6);
		return array.toString();
	}

	/**
	 * 获取点火报表列表数据
	 */
	public String getFireList() throws Exception {
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formFireList&date0=2014-12-01&date1=2015-01-11&vehicleId=194515836&_dc=1420960108570&page=1&start=0&limit=20
		String uid = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String second = getParameter("timeout");
		excelMap1 = new ArrayList<Map<String,String>>();
		int sec = 0;
		if (!StringUtils.isEmpty(second)) {
			sec = Integer.parseInt(second);
		}
		if(StringUtils.isEmpty(uid)){
			return ResultManager.getFaildResult("参数错误");
		}
		String teamname =	formService.getTeam(Integer.parseInt(uid));
		String plate = formService.getPlate(Integer.parseInt(uid));
		List<PointsForm> result = formService.getFireFormList(Integer.parseInt(uid),
				getTime(beginTime), getTime(endTime), sec);
		JSONArray array = new JSONArray();
		for(int i=0;i<result.size();i++){
			Map map = new HashedMap();
			PointsForm pf = result.get(i);
			Point point = converter.wgs2bd(Double.parseDouble(String.valueOf(pf.getY()))/(3600*24),Double.parseDouble(String.valueOf(pf.getX()))/(3600*24));
			map.put("order", i+1);
			map.put("plate", plate);
			map.put("uid",uid);
			map.put("team", teamname);
			map.put("time0",getTimes(Long.parseLong(pf.getTime1().toString())));
			map.put("time1",  getTimes(Long.parseLong(pf.getTime2().toString())));
			map.put("times", pf.getTime2()-pf.getTime1());
			map.put("x", point.getLatitude());
			map.put("y", point.getLongitude());
			map.put("mile", pf.getMiles());
			excelMap1.add(map);
		}
		exportCycleMonth(excelMap1, 6);
		return array.toString();
	}

	/**
	 * 获取点火报表柱状图数据
	 */
	public String getFireForm() throws Exception {
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formFire&date0=2014-12-01&date1=2015-01-11&vehicleId=9824,10418,10429,10189,10428,4627,9777,9779,9788,9780,9792,9793&plate=&_dc=1420959819645&page=1&start=0&limit=20
		String uids = getParameter("vehicleIds");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String second = getParameter("timeout");
		String plate_ = getParameter("plate");
		excelMap1 = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashedMap();
		int sec = 0;
		if (!StringUtils.isEmpty(second)) {
			sec = Integer.parseInt(second);
		}
		String user_ids = "";
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
					map.put(user_ids, plate_);
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}else if(!StringUtils.isEmpty(uids)){
			List<User> usersList = userService.getUidById(uids);
			
			for (int i = 0; i < usersList.size(); i++) {
				map.put(usersList.get(i).getIdentifier(), usersList.get(i)
						.getPlate());
				if (i == 0) {
					user_ids = usersList.get(0).getIdentifier();
				} else {
					user_ids += "," + usersList.get(i).getIdentifier();
				}
			}
		}else{
			return ResultManager.getFaildResult("参数错误");	
		}
		List result = formService.getFireList(user_ids, getTime(beginTime),
				getTime(endTime), sec);
		JSONArray  array = new JSONArray();
		String uidafter = user_ids;
		for (Iterator it = result.iterator(); it.hasNext();) {
			Object[] obj = (Object[]) it.next();
			Map map_ = new HashedMap();
			map_.put("uid", obj[0]);
			map_.put("plate",  map.get(obj[0].toString()));
			map_.put("times", obj[1]);
			uidafter = uidafter.replaceAll(obj[0].toString(), "0");
			excelMap1.add(map_);
		}
		String[] uus = uidafter.split(",");
		for (int i = 0; i < uus.length; i++) {
			Map map_ = new HashedMap();
			if(!uus[i].equals("0")){
				map_.put("uid", uus[i]);
				map_.put("plate",  map.get(uus[i]));
				map_.put("times", 0);
				excelMap1.add(map_);
			}
		}
		exportCycleMonth(excelMap1,4);
		return array.toString();
	}

	/***************************************************************************
	 * 月上线率统计
	 */
	public String getOnlineByMonth() throws Exception {
		// formService.get
		//cmd=monthOnlines&year=2015&month=1&vehicleIds=3,4&plate=&_dc=1421896718445&page=1&start=0&limit=20
		excelMap1 = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashedMap();
		Map<String, String> map_ = new HashedMap();
		String uids = getParameter("vehicleIds");
		String time = getParameter("date");
		String plate_ = getParameter("plate");
		String beginTime = DateUtil.getFirstDayOfMonth(time);
		String endTime = DateUtil.getLastDay(time);
		String user_ids = "";
		String plate = "";
		String team = "";
		String uid = "";
		
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
					map.put(user_ids, plate_);
					team = formService.getTeam(Integer.parseInt(user_ids));
					map_.put(user_ids, team);
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}else if(!StringUtils.isEmpty(uids)){
			List<User> usersList = userService.getUidById(uids);
			
			for (int i = 0; i < usersList.size(); i++) {
				map.put(usersList.get(i).getIdentifier(), usersList.get(i)
						.getPlate());
				if (i == 0) {
					user_ids = usersList.get(0).getIdentifier();
				} else {
					user_ids += "," + usersList.get(i).getIdentifier();
				}
			}
			
			List list_p = formService.getTeamAndPlate(uids);
			
			for(int i = 0;i<list_p.size();i++){
				Object[] obj = (Object[]) list_p.get(i);
				if(obj[1] != null){
				plate = obj[1].toString();
				}
				if(obj[2] != null){
				team = obj[2].toString();
				}
				if(obj[0] != null){
					uid = obj[0].toString();
				}
				map_.put(uid, team);
			}
		}else{
			return ResultManager.getFaildResult("参数错误");	
		}
		List result = formService.getOnlineByMonth(user_ids, beginTime, endTime);
		int i=1;
		JSONArray array = new JSONArray(); 
		String uidafter = user_ids;
		for(Iterator it = result.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			Map map_m = new HashedMap();
			map_m.put("order", i);
			map_m.put("plate", map.get(obj[0].toString()));
			if(map_.get(obj[0].toString())!=null){
				System.out.println(map_.get(obj[0].toString()));
			map_m.put("team", map_.get(obj[0].toString()));
			}
			else{
				map_m.put("team", "未分组车辆");
			}
			map_m.put("uid", obj[0].toString());
			map_m.put("days", obj[1]);
			excelMap1.add(map_m);
			uidafter = uidafter.replaceAll(obj[0].toString(), "0");
		}
		String[] uus = uidafter.split(",");
		for (int j = 0; j < uus.length; j++) {
			
			Map map_m = new HashedMap();
			if(!uus[j].equals("0")){
			map_m.put("order", j);
			map_m.put("plate", map.get(uus[j]));
			if(map_.get(uus[j]) != null){
				System.out.println(map_.get(uus[j]));
			map_m.put("team", map_.get(uus[j]));
			}else{
			map_m.put("team", "未分组车辆");
			}
			map_m.put("uid", uus[j]);
			map_m.put("days", 0);
			excelMap1.add(map_m);
			}
		}
		exportCycleMonth(excelMap1, 8);
		return array.toString();
	}

	/***************************************************************************
	 * 日行油耗统计
	 */
	public String getFuleByDay() throws Exception{
		//api.action?cmd=dateFuel&date0=2015-01-01&date1=2015-01-21&vehicleId=9824&_dc=1421916979185&page=1&start=0&limit=20
		excelMap2 = new ArrayList<Map<String,String>>();
		String uids = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String plate_ = getParameter("plate");
		String uid_str = "";
		String plate = "";
		String team = "";
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					uid_str = users.get(0).getIdentifier();
					plate = plate_;
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}
		else if(!StringUtils.isEmpty(uids)){
		List list_p = formService.getTeamAndPlate(uids);
		if(list_p != null && list_p.size() > 0 ){
			Object[] obj = (Object[]) list_p.get(0);
			if(obj[1] != null){
			plate = obj[1].toString();
			}
			if(obj[0] != null){
				uid_str = obj[0].toString();
			}
			if(obj[2] != null){
				team = obj[2].toString();
			}
		}
		}else{
			return	ResultManager.getFaildResult("请选择正确的车辆");
		}
		
//		List<User> usersList = userService.getUidById(uids);
		if(StringUtils.isEmpty(uid_str)){
			return	ResultManager.getFaildResult("参数错误");
		}
		int uid = Integer.parseInt(uid_str);
//		String plate = formService.getPlate(uid);
		//{"recorderID":462240,"identifier":209828412,"recorderDate":"2015-01-15 00:00:00","mile":12248,"mileDay":10748,"driveTimeDay":2278,"driveTime":2789,"highestSpeed":51,"speedL20":2124,"speed20T50":659,"speed50T80":6,"speed80T120":0,"speedU120":0,"obdMile":0,"obdFuel":0,"acceleration":0,"brakes":0,"fatigueDriving":0,"lastDate":"2015-01-15 21:41:40"},{"recorderID":462842,"identifier":209828412,"recorderDate":"2015-01-16 00:00:00","mile":28587,"mileDay":19756,"driveTimeDay":4682,"driveTime":6318,"highestSpeed":71,"speedL20":4096,"speed20T50":2007,"speed50T80":215,"speed80T120":0,"speedU120":0,"obdMile":0,"obdFuel":0,"acceleration":0,"brakes":0,"fatigueDriving":0,"lastDate":"2015-01-16 22:42:52"},{"recorderID":464033,"identifier":209828412,"recorderDate":"2015-01-17 00:00:00","mile":13939,"mileDay":10667,"driveTimeDay":1801,"driveTime":2314,"highestSpeed":68,"speedL20":1292,"speed20T50":793,"speed50T80":229,"speed80T120":0,"speedU120":0,"obdMile":0,"obdFuel":0,"acceleration":0,"brakes":0,"fatigueDriving":0,"lastDate":"2015-01-17 08:50:43"}
		String result = formService.getObdMileList(uid,beginTime,endTime);
		//{"order":1,"plate":"\u9102A6QM10","date":"20150118","mileage":838,"fuel":68,"overspeedtimes":2,"stoptimes":7,"avgfuel":10},
		JSONObject json = new JSONObject(result); //513569269,209988130
		System.out.println(result);
		if(!json.has("result")){
			return	ResultManager.getFaildResult("数据解析错误");
		}
		//System.out.println(json.toString());
		JSONArray array = new JSONArray(json.getString("result"));
		JSONArray arr = new JSONArray();
		for(int i=0;i<array.length();i++){
			Map map = new HashedMap();
			JSONObject jj = array.getJSONObject(i);
			String[] time =  jj.getString("recorderDate").split(" ");
			if(jj.getInt("obdMile")!=0){
				map.put("fuel", jj.getInt("obdFuel")*100/jj.getInt("obdMile")+"");
			}else{
				map.put("fuel", "0");
			}
			map.put("order", i+1);
			map.put("plate", plate);
			map.put("uid", uid_str);
			map.put("team", team);
			map.put("date", time[0]);
			map.put("mile", jj.getInt("mile")/1000.0);
			map.put("speedU120",jj.getInt("speedL20") + "");
			map.put("accel", jj.getInt("acceleration") + "");
			map.put("brakes",jj.getInt("brakes") + "0");
			excelMap2.add(map);
		}
		//	Map map_ = (Map)excelMap2.get(i);
		exportCycleMonth(excelMap2,9);
		return arr.toString();
	}

	/***************************************************************************
	 * 断电统计柱状图
	 */
	public String getBreakDian() {
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=totalAlarms&date0=2015-01-20&date1=2015-01-20&vehicleIds=9824,10418,10429&plate=&type=poweroff&_dc=1421828836506&page=1&start=0&limit=20
		Map<String, String> map = new HashedMap();
		Map<String, String> map1 = new HashedMap();
		String uids = getParameter("vehicleIds");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String plate_ = getParameter("plate");
		String type = getParameter("type");
		String user_ids = "";
		int size = 1;
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
					uids = users.get(0).getVehicleId()+"";
					map.put(user_ids, plate_);
					map1.put(user_ids, users.get(0)
							.getVehicleId()+"");
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}else if(!StringUtils.isEmpty(uids)){
			List<User> usersList = userService.getUidById(uids);
			 size = usersList.size();
			for (int i = 0; i < usersList.size(); i++) {
				map.put(usersList.get(i).getIdentifier(), usersList.get(i)
						.getPlate());
				map1.put(usersList.get(i).getIdentifier(), usersList.get(i)
						.getVehicleId()+"");
				if (i == 0) {
					user_ids = usersList.get(0).getIdentifier();
				} else {
					user_ids += "," + usersList.get(i).getIdentifier();
				}
			}
		}else{
			return	ResultManager.getFaildResult("请选择正确的车辆");
		}
		if(type.equals("poweroff")){
			excelMap1 = new ArrayList<Map<String,String>>();
			try {
			String json = 	formService.getBreakDian(user_ids,beginTime,endTime);
			JSONObject obj = new JSONObject(json);
			if(!obj.has("result")){
				return	ResultManager.getFaildResult("数据解析错误");
			}
			  JSONArray array = (JSONArray) obj.get("result");
			  JSONArray arr = new JSONArray();
			  String uidafter = user_ids;
			  for(int i=0;i<array.length();i++){
				  Map map_ = new HashedMap();
				  JSONObject list_json = new JSONObject(array.get(i).toString());
				  map_.put("plate",  map.get(list_json.getString("identifier")));
				  map_.put("uid",list_json.getString("identifier"));
				  map_.put("times",list_json.getString("count"));
					excelMap1.add(map_);
			  uidafter = uidafter.replaceAll(list_json.getString("identifier"), "0");
			}
			String[] uus = uidafter.split(",");
			for (int j = 0; j < uus.length; j++) {
				
				Map map_m = new HashedMap();
				if(!uus[j].equals("0")){
				map_m.put("order", j);
				map_m.put("plate", map.get(uus[j]));
				map_m.put("uid", uus[j]);
				map_m.put("times", 0);
				excelMap1.add(map_m);
				}
			}
			  exportCycleMonth(excelMap1, 5);
			  return arr.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return	ResultManager.getFaildResult("异常："+e.getMessage());
			}
		}else if(type.equals("crash")){
			List list = formService.getPengZhuangForm(uids, 1, beginTime, endTime);
			return parsList(list,user_ids,map);
		}else if(type.equals("rollover")){
			List list = formService.getPengZhuangForm(uids, 2, beginTime, endTime);
			return parsList(list,user_ids,map);
		}else if(type.equals("quake")){
			List list = formService.getPengZhuangForm(uids, 6, beginTime, endTime);
			return parsList(list,user_ids,map);
		}else if(type.equals("overspeed")){
//			List<User> usersList = userService.getUidById(uids);
//			for (int i = 0; i < usersList.size(); i++) {
//				map.put(usersList.get(i).getIdentifier(), usersList.get(i)
//						.getPlate());
//				map1.put(usersList.get(i).getIdentifier(), usersList.get(i)
//						.getVehicleId()+"");
//				if (i == 0) {
//					user_ids = usersList.get(0).getIdentifier();
//				} else {
//					user_ids += "," + usersList.get(i).getIdentifier();
//				}
//			}
			try {
			List list =	formService.getOverSpeedForm(user_ids, beginTime, endTime);
			return parsList(list,user_ids,map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return ResultManager.getFaildResult("暂无数据");
	}
	
	public String parsList(List list,String uid_ids,Map map_){
		excelMap1 = new ArrayList<Map<String,String>>();
		 JSONArray arr = new JSONArray();
		 String uidafter = uid_ids;
		  for(Iterator it = list.iterator();it.hasNext();){
			  Map map = new HashedMap();
			  Object[] obj = (Object[]) it.next();
				map.put("plate", obj[2]);
				map.put("uid",obj[3]);
				map.put("times",obj[0]);
				excelMap1.add(map);
		  uidafter = uidafter.replaceAll(obj[3].toString(), "0");
	}
	String[] uus = uidafter.split(",");
	for (int j = 0; j < uus.length; j++) {
		
		Map map_m = new HashedMap();
		if(!uus[j].equals("0")){
		map_m.put("order", j);
		map_m.put("plate", map_.get(uus[j]));
		map_m.put("uid", uus[j]);
		map_m.put("times", 0);
		excelMap1.add(map_m);
		}
	}
		  try {
			exportCycleMonth(excelMap1, 5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return arr.toString();
	}
	/***
	**报警报表列表数据***/
	public String getBreakDianOne(){
		//cmd=detailAlarms&date0=2015-01-01&date1=2015-01-21&vehicleId=10428&type=poweroff&_dc=1421893530761&page=1&start=0&limit=20
		String uid = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String second = getParameter("plate");
		String type = getParameter("type");
		String result="";
		String teamname =	formService.getTeam(Integer.parseInt(uid));
		if(type.endsWith("poweroff")){
			excelMap1 = new ArrayList<Map<String,String>>();
			List<User> usersList = userService.getUidById(uid);
			if(usersList != null && usersList.size()>0){
				uid = usersList.get(0).getIdentifier();
			}else{
				ResultManager.getFaildResult("参数错误");
			}
			String plate = formService.getPlate(Integer.parseInt(uid));
			try {
			//{"jd":"112.32540509259259","create_time":"2015-01-01","bkint":0,"code":6,"wd":"29.928703703703704","reminder_id":342034,"identifier":"153206344","y":"2585840","x":"9704915"},{"jd":"112.32540509259259","create_time":"2015-01-01","bkint":0,"code":4,"wd":"29.928703703703704","reminder_id":342055,"identifier":"153206344","y":"2585840","x":"9704915"}	
			String ss =	formService.getBreakDianOne(uid,beginTime,endTime);
			JSONObject obj = new JSONObject(ss);
			if(!obj.has("result")){
				return	ResultManager.getFaildResult("数据解析错误");
			}
			  JSONArray array = (JSONArray) obj.get("result");
			  JSONArray arr = new JSONArray();
			  for(int i=0;i<array.length();i++){
				  Map map = new HashedMap();
				  JSONObject list_json = new JSONObject(array.get(i).toString());
				   map.put("order", i+1);
					map.put("plate", plate);
					map.put("team", teamname);
					map.put("uid", uid);
					map.put("x", list_json.getString("jd"));
					map.put("y", list_json.getString("wd"));
					map.put("time", list_json.getString("create_time"));
					excelMap1.add(map);
					exportCycleMonth(excelMap1, 7);
			  }
			  return arr.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result=ResultManager.getFaildResult("数据解析错误");
			}
			
			return result;
		}else if(type.equals("crash")){
			List list = formService.getPengZhuangList(Integer.parseInt(uid), 1, beginTime, endTime);
			return parsListOne(list,teamname,1,uid);
		}else if(type.equals("rollover")){
			List list = formService.getPengZhuangList(Integer.parseInt(uid), 2, beginTime, endTime);
			return parsListOne(list,teamname,1,uid);
		}else if(type.equals("quake")){
			List list = formService.getPengZhuangList(Integer.parseInt(uid), 6, beginTime, endTime);
			return parsListOne(list,teamname,1,uid);
		}else if(type.equals("overspeed")){
			List<User> usersList = userService.getUidById(uid);
			if(usersList != null && usersList.size()>0){
				uid = usersList.get(0).getIdentifier();
			}else{
				ResultManager.getFaildResult("参数错误");
			}
			try {
			List list = formService.getOverSpeedList(Integer.parseInt(uid), beginTime, endTime);
			return parsListOne(list,teamname,0,uid);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ResultManager.getFaildResult("暂无数据");
	}
	
	public String parsListOne(List list,String name,int type,String uid){
		excelMap1 = new ArrayList<Map<String,String>>();
		 JSONArray arr = new JSONArray();
		 int i = 1;
		 try {
		  for(Iterator it = list.iterator();it.hasNext();){
			  Map map = new HashedMap();
			  Object[] obj = (Object[]) it.next();
			 // res.put("time",obj[2]);
			  String x = "";
			  String y = "";
			  if(obj[0].toString().contains(".")){
				  x = obj[0].toString();
				  y = obj[1].toString();
			  }else{
				  x = Double.parseDouble(obj[0].toString())/(3600.0*24.0)+"";
				  y = Double.parseDouble(obj[1].toString())/(3600.0*24.0)+"";
			  }
			  
				i++;
				if(type == 0){
					JSONArray arry = new JSONArray();
					arry.put(x);
					arry.put(y);
					arry.put(getTimes(Long.parseLong(obj[2].toString())));
					arry.put(obj[4]);
					arry.put((int)Math.random()*360);
					System.out.println((int)(Math.random()*360));
		      }else{
		      }
				map.put("order", i);
				map.put("plate", obj[3]);
				map.put("team", name);
				map.put("x", x);
				map.put("y", y);
				map.put("uid", uid);
				map.put("time", getTimes(Long.parseLong(obj[2].toString())));
				excelMap1.add(map);
				
		  }
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			try {
				exportCycleMonth(excelMap1, 7);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return arr.toString();
	}
	
	/**日,月   周期报表***/
	public String dayForm() throws Exception{
		//carmonitor/api.action?cmd=statsTotalMF&date0=2015-01-22&date1=2015-01-22&vehicleIds=9824,1041,9779,9788,9780,9792,9793&plate=&fuel=&_dc=1421983151530&page=1&start=0&limit=20
		Map<String, String> map = new HashedMap();
		excelMap2 = new ArrayList<Map<String,String>>();
		Map<String,String> map_t = new HashedMap();
		String userIds = getParameter("vehicleIds");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String plate_ = getParameter("plate");
		String fuel = getParameter("fuel");
		String user_ids = "";
		int size = 1;
		String plate = "";
		String team = "";
		String uid = "";
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
					team = formService.getTeam(Integer.parseInt(user_ids));
					map_t.put(user_ids, team);
					map.put(user_ids, plate_);
				} else {
					return	ResultManager.getFaildResult("请选择正确的车辆");
				}
			} else {
				return	ResultManager.getFaildResult("请选择正确的车辆");
			}
		}else if(!StringUtils.isEmpty(userIds)){
			List<User> usersList = userService.getUidById(userIds);
			 size = usersList.size();
			for (int i = 0; i < usersList.size(); i++) {
				map.put(usersList.get(i).getIdentifier(), usersList.get(i)
						.getPlate());
				if (i == 0) {
					user_ids = usersList.get(0).getIdentifier();
				} else {
					user_ids += "," + usersList.get(i).getIdentifier();
				}
			}
			List list_p = formService.getTeamAndPlate(userIds);
			for(int i = 0;i<list_p.size();i++){
				Object[] obj = (Object[]) list_p.get(i);
				if(obj[1] != null){
				plate = obj[1].toString();
				}
				if(obj[2] != null){
				team = obj[2].toString();
				}
				if(obj[0] != null){
					uid = obj[0].toString();
				}
				map_t.put(uid, team);
			}
		}else{
			return	ResultManager.getFaildResult("请选择正确的车辆");
		}
		
		
		float f = 0;
		if(!StringUtils.isEmpty(fuel)){
			f=Float.parseFloat(fuel);
		}
		String result = formService.getObdMile(user_ids, beginTime, endTime,0,f);
//		List list =	formService.getOverSpeedForm(user_ids, beginTime, endTime);
//		  for(Iterator it = list.iterator();it.hasNext();){
//			  Object[] obj = (Object[]) it.next();
//			  if(obj[1] != null && obj[0] != null){
//				  map_over.put(obj[1].toString(), obj[0].toString());
//			  }
//			} 
		  JSONObject json = new JSONObject(result);
		  if(!json.has("result")){
				return	ResultManager.getFaildResult("数据解析错误");
			}
		  String s = json.getString("result");
			JSONArray array = new JSONArray(s);
			JSONArray resultArray = new JSONArray();
			String uidafter = user_ids;
			for (int i = 0; i < array.length(); i++) {
				Map map_ = new HashedMap();
				JSONObject item = array.getJSONObject(i);
				map_.put("plate", map.get(item.getInt("vehicleId") + ""));
				map_.put("uid", item.getInt("vehicleId"));
				if( map_t.get(item.getInt("vehicleId") + "")!=null){
					map_.put("team", map_t.get(item.getInt("vehicleId") + ""));
					}else{
						map_.put("team","未分组车辆");	
					}
				map_.put("mile", item.getString("mileage"));
				map_.put("fuel", item.getString("fuel"));
				map_.put("speedU120", item.getInt("speedtime"));
				map_.put("accel", item.getInt("acceleration"));
				map_.put("brakes",  item.getInt("brakes"));
				map_.put("date",  beginTime+"至"+endTime);
				uidafter = uidafter.replaceAll(item.getInt("vehicleId")+"", "0");
				excelMap2.add(map_);
				// list_json.add(item);
			}
			String[] uidas = uidafter.split(",");
			for (int i = 0; i < uidas.length; i++) {
				Map map_ = new HashedMap();
				if(!uidas[i].equals("0")){
					map_.put("plate", map.get(uidas[i]));
					map_.put("uid", uidas[i]);
					if(map_t.get(uidas[i])!=null){
					map_.put("team", map_t.get(uidas[i]));
					}else{
						map_.put("team","未分组车辆");	
					}
					map_.put("mile", 0);
					map_.put("fuel", 0);
					map_.put("speedU120", 0);
					map_.put("accel", 0);
					map_.put("brakes",  0);
					map_.put("date",  beginTime+"至"+endTime);
					excelMap2.add(map_);
				}
			}
			exportCycleMonth(excelMap2, 9);
		return resultArray.toString();
	}
	public String dayFormList() throws Exception{
		//api.action?cmd=statsDaysMF&vehicleId=&date0=2014-10-01&date1=2015-01-22&plate=&fuel=&_dc=1421994228068&page=1&sta
		excelMap2 = new ArrayList<Map<String,String>>();
		String uid = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		if(StringUtils.isEmpty(uid)){
			return ResultManager.getFaildResult("参数错误");
		}
		String team = formService.getTeam(Integer.parseInt(uid));
		String plate = formService.getPlate(Integer.parseInt(uid));
		String result = formService.getObdMileList(Integer.parseInt(uid), beginTime, endTime);
		//{"result":"[{\"recorderID\":449609,\"identifier\":153060472,\"recorderDate\":\"2015-01-01 00:00:00\",\"mile\":25846,\"mileDay\":21704,\"driveTimeDay\":3205,\"driveTime\":3838,\"highestSpeed\":63,\"speedL20\":1691,\"speed20T50\":2055,\"speed50T80\":92,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":2334083,\"obdFuel\":207152,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-01 21:22:52\"},{\"recorderID\":450446,\"identifier\":153060472,\"recorderDate\":\"2015-01-02 00:00:00\",\"mile\":25924,\"mileDay\":21822,\"driveTimeDay\":3322,\"driveTime\":3980,\"highestSpeed\":71,\"speedL20\":1904,\"speed20T50\":1949,\"speed50T80\":127,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":2386488,\"obdFuel\":226557,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-02 20:09:01\"},{\"recorderID\":451311,\"identifier\":153060472,\"recorderDate\":\"2015-01-03 00:00:00\",\"mile\":14144,\"mileDay\":10290,\"driveTimeDay\":1988,\"driveTime\":2484,\"highestSpeed\":53,\"speedL20\":1220,\"speed20T50\":1258,\"speed50T80\":6,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":1294300,\"obdFuel\":137060,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-03 19:09:16\"},{\"recorderID\":452409,\"identifier\":153060472,\"recorderDate\":\"2015-01-04 00:00:00\",\"mile\":37272,\"mileDay\":33114,\"driveTimeDay\":4562,\"driveTime\":5139,\"highestSpeed\":72,\"speedL20\":1975,\"speed20T50\":2864,\"speed50T80\":300,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":3418036,\"obdFuel\":296035,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-04 19:46:31\"},{\"recorderID\":453330,\"identifier\":153060472,\"recorderDate\":\"2015-01-05 00:00:00\",\"mile\":33990,\"mileDay\":24852,\"driveTimeDay\":3617,\"driveTime\":5387,\"highestSpeed\":71,\"speedL20\":2595,\"speed20T50\":2510,\"speed50T80\":282,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":3139322,\"obdFuel\":287626,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-05 20:50:30\"},{\"recorderID\":454235,\"identifier\":153060472,\"recorderDate\":\"2015-01-06 00:00:00\",\"mile\":34800,\"mileDay\":34800,\"driveTimeDay\":4624,\"driveTime\":4624,\"highestSpeed\":68,\"speedL20\":1852,\"speed20T50\":2397,\"speed50T80\":375,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":3190194,\"obdFuel\":266421,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-06 18:56:04\"},{\"recorderID\":455196,\"identifier\":153060472,\"recorderDate\":\"2015-01-07 00:00:00\",\"mile\":44904,\"mileDay\":34848,\"driveTimeDay\":4153,\"driveTime\":5321,\"highestSpeed\":93,\"speedL20\":1836,\"speed20T50\":3009,\"speed50T80\":449,\"speed80T120\":27,\"speedU120\":0,\"obdMile\":4159073,\"obdFuel\":337712,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-07 20:04:25\"},{\"recorderID\":456138,\"identifier\":153060472,\"recorderDate\":\"2015-01-08 00:00:00\",\"mile\":22616,\"mileDay\":18662,\"driveTimeDay\":2361,\"driveTime\":2857,\"highestSpeed\":60,\"speedL20\":963,\"speed20T50\":1748,\"speed50T80\":146,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":2068336,\"obdFuel\":181188,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-08 20:09:46\"},{\"recorderID\":457073,\"identifier\":153060472,\"recorderDate\":\"2015-01-09 00:00:00\",\"mile\":24762,\"mileDay\":20508,\"driveTimeDay\":3132,\"driveTime\":3798,\"highestSpeed\":61,\"speedL20\":1785,\"speed20T50\":1961,\"speed50T80\":52,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":2227408,\"obdFuel\":201948,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-09 19:50:49\"},{\"recorderID\":457851,\"identifier\":153060472,\"recorderDate\":\"2015-01-10 00:00:00\",\"mile\":15820,\"mileDay\":10216,\"driveTimeDay\":1482,\"driveTime\":2273,\"highestSpeed\":55,\"speedL20\":946,\"speed20T50\":1291,\"speed50T80\":36,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":1449905,\"obdFuel\":135792,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-10 20:56:12\"},{\"recorderID\":458663,\"identifier\":153060472,\"recorderDate\":\"2015-01-11 00:00:00\",\"mile\":20244,\"mileDay\":20244,\"driveTimeDay\":2888,\"driveTime\":2891,\"highestSpeed\":69,\"speedL20\":1371,\"speed20T50\":1416,\"speed50T80\":104,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":1808654,\"obdFuel\":156124,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-11 19:01:30\"},{\"recorderID\":459852,\"identifier\":153060472,\"recorderDate\":\"2015-01-12 00:00:00\",\"mile\":26822,\"mileDay\":26810,\"driveTimeDay\":3097,\"driveTime\":3106,\"highestSpeed\":73,\"speedL20\":1032,\"speed20T50\":1648,\"speed50T80\":426,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":2455484,\"obdFuel\":197807,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-12 20:20:41\"},{\"recorderID\":460563,\"identifier\":153060472,\"recorderDate\":\"2015-01-13 00:00:00\",\"mile\":41368,\"mileDay\":35380,\"driveTimeDay\":3928,\"driveTime\":4778,\"highestSpeed\":58,\"speedL20\":1273,\"speed20T50\":3156,\"speed50T80\":349,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":3791297,\"obdFuel\":282774,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-13 19:33:28\"},{\"recorderID\":461526,\"identifier\":153060472,\"recorderDate\":\"2015-01-14 00:00:00\",\"mile\":13292,\"mileDay\":9736,\"driveTimeDay\":2098,\"driveTime\":2553,\"highestSpeed\":71,\"speedL20\":1509,\"speed20T50\":975,\"speed50T80\":69,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":1202594,\"obdFuel\":129622,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-14 20:11:28\"},{\"recorderID\":462408,\"identifier\":153060472,\"recorderDate\":\"2015-01-15 00:00:00\",\"mile\":53062,\"mileDay\":26894,\"driveTimeDay\":3555,\"driveTime\":6390,\"highestSpeed\":68,\"speedL20\":2046,\"speed20T50\":3795,\"speed50T80\":549,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":4968332,\"obdFuel\":383513,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-15 22:03:40\"},{\"recorderID\":463450,\"identifier\":153060472,\"recorderDate\":\"2015-01-16 00:00:00\",\"mile\":46827,\"mileDay\":34309,\"driveTimeDay\":4277,\"driveTime\":6519,\"highestSpeed\":75,\"speedL20\":2680,\"speed20T50\":3372,\"speed50T80\":467,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":4221913,\"obdFuel\":356096,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-16 21:14:41\"},{\"recorderID\":464245,\"identifier\":153060472,\"recorderDate\":\"2015-01-17 00:00:00\",\"mile\":23776,\"mileDay\":16175,\"driveTimeDay\":2636,\"driveTime\":3763,\"highestSpeed\":74,\"speedL20\":1940,\"speed20T50\":1589,\"speed50T80\":234,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":2139557,\"obdFuel\":201521,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-17 21:37:46\"},{\"recorderID\":465186,\"identifier\":153060472,\"recorderDate\":\"2015-01-18 00:00:00\",\"mile\":18832,\"mileDay\":14902,\"driveTimeDay\":2095,\"driveTime\":2675,\"highestSpeed\":56,\"speedL20\":1027,\"speed20T50\":1620,\"speed50T80\":28,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":1712874,\"obdFuel\":155997,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-18 19:45:35\"},{\"recorderID\":466430,\"identifier\":153060472,\"recorderDate\":\"2015-01-19 00:00:00\",\"mile\":221948,\"mileDay\":217974,\"driveTimeDay\":12236,\"driveTime\":12794,\"highestSpeed\":105,\"speedL20\":1485,\"speed20T50\":1946,\"speed50T80\":5967,\"speed80T120\":3396,\"speedU120\":0,\"obdMile\":21472964,\"obdFuel\":1402538,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-19 19:36:38\"},{\"recorderID\":467140,\"identifier\":153060472,\"recorderDate\":\"2015-01-20 00:00:00\",\"mile\":36642,\"mileDay\":31888,\"driveTimeDay\":4650,\"driveTime\":5534,\"highestSpeed\":62,\"speedL20\":2555,\"speed20T50\":2749,\"speed50T80\":230,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":3325049,\"obdFuel\":277965,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-20 21:33:53\"},{\"recorderID\":468009,\"identifier\":153060472,\"recorderDate\":\"2015-01-21 00:00:00\",\"mile\":38267,\"mileDay\":28618,\"driveTimeDay\":6122,\"driveTime\":8228,\"highestSpeed\":56,\"speedL20\":5207,\"speed20T50\":2987,\"speed50T80\":34,\"speed80T120\":0,\"speedU120\":0,\"obdMile\":3436078,\"obdFuel\":344657,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-21 22:22:41\"},{\"recorderID\":469063,\"identifier\":153060472,\"recorderDate\":\"2015-01-22 00:00:00\",\"mile\":55704,\"mileDay\":45243,\"driveTimeDay\":7975,\"driveTime\":9430,\"highestSpeed\":82,\"speedL20\":5216,\"speed20T50\":3860,\"speed50T80\":351,\"speed80T120\":3,\"speedU120\":0,\"obdMile\":4985439,\"obdFuel\":459602,\"acceleration\":0,\"brakes\":0,\"fatigueDriving\":0,\"lastDate\":\"2015-01-22 21:40:07\"}]","code":1}
		 JSONObject json = new JSONObject(result);
		 if(!json.has("result")){
				return	ResultManager.getFaildResult("数据解析错误");
			}
		  String s = json.getString("result");
			JSONArray array = new JSONArray(s);
			JSONArray resultArray = new JSONArray();
			for (int i = 0; i < array.length(); i++) {
				Map map = new HashedMap();
				JSONObject item = array.getJSONObject(i);
				String[] t = item.getString("recorderDate").split(" ");
				map.put("plate", plate);
				map.put("uid", item.getInt("identifier"));
				map.put("team", team);
				map.put("mile", String.format("%.2f",item.getInt("mile")/1000.0));
				map.put("fuel", String.format("%.2f", item.getInt("obdFuel")/100000.0));
				map.put("speedU120", item.getInt("speedL20"));
				map.put("accel", item.getInt("acceleration"));
				map.put("brakes", item.getInt("brakes"));
				map.put("date", t[0]);
				excelMap2.add(map);
				
			}
			exportCycleMonth(excelMap2, 9);
		return resultArray.toString();
	}
	/**
	 * 把一定格式的时间装换成秒数
	 */
	private int getTime(String strtime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(strtime);
		return (int) (date.getTime() / 1000);
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

	 /**
	 * 明天做按月统计
	 */
	public List getListByDriving() {

		return null;
	}
	/*****
	 * 导出 excel 表  flag不同导出不同的统计表
	 * *****/
	public void exportCycleMonth(List list,int flag) throws Exception{
		HSSFWorkbook workBook = new HSSFWorkbook();
		String title = String.valueOf(System.currentTimeMillis());
		HSSFSheet sheet = workBook.createSheet(title);
		HttpServletResponse response = getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename="+title+".xls");
		sheet.setDefaultColumnWidth(16);
		String uid = "";
		String plate = "";
		String team = "";
		String mileage = "0";
		String oil = "0";
		String x = "";
		String y = "";
		String times = "0";
		String acclation = "0";
		String breaks = "0";
		String date = "";
		String time = "";
		String speedU120 = "0";
		String speed = "0";
		String time1 = "";
		String time2 = "";
		String days = "0";
		String pt1 = "";
		String pt2 = "";
		
		String address="";
		HSSFCellStyle cellHeadStyle = workBook.createCellStyle();
		HSSFCellStyle contentCellStyle = workBook.createCellStyle();
		contentCellStyle.setBorderBottom((short)1);
		contentCellStyle.setBorderTop((short)1);
		contentCellStyle.setBorderLeft((short)1);
		contentCellStyle.setBorderRight((short)1);
		cellHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setBorderBottom((short)1);
		cellHeadStyle.setBorderTop((short)1);
		cellHeadStyle.setBorderLeft((short)1);
		cellHeadStyle.setBorderRight((short)1);
		HSSFFont font = workBook.createFont();
		font.setBoldweight((short)2);
		cellHeadStyle.setFont(font);
		//System.out.println(list);
		for(int i = 0;i<list.size();i++){
			Map map = (Map)list.get(i);
			uid = map.get("uid").toString();
			plate = map.get("plate").toString();
			
			if(map.containsKey("address")){
				
				address=map.get("address").toString();
			}
			if(map.containsKey("mile")){
			mileage = map.get("mile").toString();
			}
			if(map.containsKey("fuel")){
			oil = map.get("fuel").toString();
			}
			if(map.containsKey("x")){
			x = map.get("x").toString();
			}
			if(map.containsKey("y")){
			y = map.get("y").toString();
			}
			if(map.containsKey("time")){
			time = map.get("time").toString();
			}
			if(map.containsKey("team")){
				team = map.get("team").toString();
				}
			if(map.containsKey("accel")){
			 acclation = map.get("accel").toString();
			}
			 if(map.containsKey("brakes")){
			 breaks =  map.get("brakes").toString();
			 System.out.println(breaks+"**");
			 }
			 if(map.containsKey("date")){
			 date =  map.get("date").toString();
			 }
			 if(map.containsKey("times")){
			 times =  map.get("times").toString();
			 }
			 if(map.containsKey("speedU120")){
			 speedU120 =  map.get("speedU120").toString();
			 }
			 if(map.containsKey("speed")){
			 speed =  map.get("speed").toString();
			 }
			 if(map.containsKey("time0")){
				 time1 =  map.get("time0").toString();
				 }
			 if(map.containsKey("time1")){
				 time2 =  map.get("time1").toString();
				 }
			 if(map.containsKey("days")){
				 days =  map.get("days").toString();
				 }
			 if(map.containsKey("pt1")){
				 pt1 =  map.get("pt1").toString();
				 }
			 if(map.containsKey("pt2")){
				 pt2 =  map.get("pt2").toString();
				 }
			 if(team == null || team == ""){
				 team = "未分组车辆";
			 }
			if(i == 0){
				HSSFRow row = sheet.createRow(0);
//				row.setRowStyle(cellHeadStyle);
				HSSFCell headPlateCell = row.createCell(0);
				headPlateCell.setCellValue("车牌号码");
				headPlateCell.setCellStyle(cellHeadStyle);
				HSSFCell headGroupCell = row.createCell(1);
				headGroupCell.setCellValue("设备id");
				headGroupCell.setCellStyle(cellHeadStyle);
		switch(flag){
		case 1:  //行驶报表 统计
			
			HSSFCell headMileageCell = row.createCell(2);
			headMileageCell.setCellValue("行驶里程(公里)");
			headMileageCell.setCellStyle(cellHeadStyle);
			HSSFCell headOilCell = row.createCell(3);
			headOilCell.setCellValue("油耗(升)");
			headOilCell.setCellStyle(cellHeadStyle);
			break;
		case 2:  //位置报表
			HSSFCell cell_11 = row.createCell(2);
			cell_11.setCellValue("所属分组");
	     	cell_11.setCellStyle(cellHeadStyle);
			HSSFCell cell_12 = row.createCell(3);
			cell_12.setCellValue("时间");
			cell_12.setCellStyle(cellHeadStyle);
			HSSFCell cell_13 = row.createCell(4);
			cell_13.setCellValue("经度");
			cell_13.setCellStyle(cellHeadStyle);
			HSSFCell cell_14 = row.createCell(5);
			cell_14.setCellValue("纬度");
			cell_14.setCellStyle(cellHeadStyle);
			HSSFCell headtCell = row.createCell(6);
			headtCell.setCellValue("速度(公里/小时)");
			headtCell.setCellStyle(cellHeadStyle);
			
			HSSFCell cell_16 = row.createCell(7);
			cell_16.setCellValue("位置");
			cell_16.setCellStyle(cellHeadStyle);
			break;
		case 3: //里程报表
			HSSFCell cell_1 = row.createCell(2);
			cell_1.setCellValue("行驶里程(公里)");
			cell_1.setCellStyle(cellHeadStyle);
			HSSFCell cell_2 = row.createCell(3);
			cell_2.setCellValue("油耗(升)");
			cell_2.setCellStyle(cellHeadStyle);
			HSSFCell cell_3 = row.createCell(4);
			cell_3.setCellValue("经度");
			cell_3.setCellStyle(cellHeadStyle);
			HSSFCell cell_4 = row.createCell(5);
			cell_4.setCellValue("纬度");
			cell_4.setCellStyle(cellHeadStyle);
			HSSFCell cell_5 = row.createCell(6);
			cell_5.setCellValue("时间段");
			cell_5.setCellStyle(cellHeadStyle);
			break;
		case 4: //停车报表,点火报表
			HSSFCell cell_6 = row.createCell(2);
			cell_6.setCellValue("时长(分钟)");
			cell_6.setCellStyle(cellHeadStyle);
			break;
		case 5: //报警
			HSSFCell cell_65 = row.createCell(2);
			cell_65.setCellValue("次数");
			cell_65.setCellStyle(cellHeadStyle);
			break;
		case 6:  //停车 ，点火列表
			HSSFCell cell_61 = row.createCell(2);
			cell_61.setCellValue("所属分组");
			cell_61.setCellStyle(cellHeadStyle);
			HSSFCell cell_62 = row.createCell(3);
			cell_62.setCellValue("开始时间");
			cell_62.setCellStyle(cellHeadStyle);
			HSSFCell cell_63 = row.createCell(4);
			cell_63.setCellValue("结束时间");
			cell_63.setCellStyle(cellHeadStyle);
			HSSFCell cell_64 = row.createCell(5);
			cell_64.setCellValue("时长(分钟)");
			cell_64.setCellStyle(cellHeadStyle);
			HSSFCell cell_65s = row.createCell(6);
			cell_65s.setCellValue("经度");
			cell_65s.setCellStyle(cellHeadStyle);
			HSSFCell cell_66 = row.createCell(7);
			cell_66.setCellValue("纬度");
			cell_66.setCellStyle(cellHeadStyle);
			HSSFCell cell_67 = row.createCell(8);
			cell_67.setCellValue("里程(公里)");
			cell_67.setCellStyle(cellHeadStyle);
			break;
		case 7: //报警列表
			HSSFCell cell_111 = row.createCell(2);
			cell_111.setCellValue("所属分组");
			cell_111.setCellStyle(cellHeadStyle);
			HSSFCell cell_121 = row.createCell(3);
			cell_121.setCellValue("时间");
			cell_121.setCellStyle(cellHeadStyle);
			HSSFCell cell_131 = row.createCell(4);
			cell_131.setCellValue("经度");
			cell_131.setCellStyle(cellHeadStyle);
			HSSFCell cell_141 = row.createCell(5);
			cell_141.setCellValue("纬度");
			cell_141.setCellStyle(cellHeadStyle);
			
			break;
		case 8:  //月上线统计
			HSSFCell cell_7 = row.createCell(2);
			cell_7.setCellValue("所属分组)");
			cell_7.setCellStyle(cellHeadStyle);
			HSSFCell cell_8 = row.createCell(3);
			cell_8.setCellValue("天数");
			cell_8.setCellStyle(cellHeadStyle);
			break;
		
		case 9: //周期报表列表  ，日行油耗
			HSSFCell cell3 = row.createCell(2);
			cell3.setCellValue("所属分组");
			cell3.setCellStyle(cellHeadStyle);
			HSSFCell cell4 = row.createCell(3);
			cell4.setCellValue("行驶里程(公里)");
			cell4.setCellStyle(cellHeadStyle);
			HSSFCell cell5 = row.createCell(4);
			cell5.setCellValue("油耗(升)");
			cell5.setCellStyle(cellHeadStyle);
			HSSFCell cell6 = row.createCell(5);
			cell6.setCellValue("超速行驶时间");
			cell6.setCellStyle(cellHeadStyle);
			HSSFCell cell7 = row.createCell(6);
			cell7.setCellValue("急加速");
			cell7.setCellStyle(cellHeadStyle);
			HSSFCell cell8 = row.createCell(7);
			cell8.setCellValue("急减速");
			cell8.setCellStyle(cellHeadStyle);
			HSSFCell cell9 = row.createCell(8);
			cell9.setCellValue("时间");
			cell9.setCellStyle(cellHeadStyle);
			break;
		case 10: //周期报表列表  ，日行油耗
			HSSFCell cell43 = row.createCell(2);
			cell43.setCellValue("行驶里程(公里)");
			cell43.setCellStyle(cellHeadStyle);
			HSSFCell cell53 = row.createCell(3);
			cell53.setCellValue("起始时间");
			cell53.setCellStyle(cellHeadStyle);
			HSSFCell cell63 = row.createCell(4);
			cell63.setCellValue("结束时间");
			cell63.setCellStyle(cellHeadStyle);
			HSSFCell cell73 = row.createCell(5);
			cell73.setCellValue("时长(分钟)");
			cell73.setCellStyle(cellHeadStyle);
			HSSFCell cell83 = row.createCell(6);
			cell83.setCellValue("最大速度");
			cell83.setCellStyle(cellHeadStyle);
			HSSFCell cell93 = row.createCell(7);
			cell93.setCellValue("起始经纬度");
			cell93.setCellStyle(cellHeadStyle);
			HSSFCell cell103 = row.createCell(8);
			cell103.setCellValue("结束经纬度");
			cell103.setCellStyle(cellHeadStyle);
			break;
			}
			}
			HSSFRow contentRow = sheet.createRow(i+1);
			HSSFCell plateCell = contentRow.createCell(0);
			plateCell.setCellValue(plate);
			plateCell.setCellStyle(contentCellStyle);
			HSSFCell teamCell = contentRow.createCell(1);
			teamCell.setCellValue(uid);
			teamCell.setCellStyle(contentCellStyle);
			if(flag == 1){
				HSSFCell mileageCell = contentRow.createCell(2);
				mileageCell.setCellValue(mileage);
				mileageCell.setCellStyle(contentCellStyle);
				HSSFCell oilCell = contentRow.createCell(3);
				oilCell.setCellValue(oil);
				oilCell.setCellStyle(contentCellStyle);
			}else if(flag == 2){
				HSSFCell teamcell = contentRow.createCell(2);
				teamcell.setCellValue(team);
				teamcell.setCellStyle(contentCellStyle);
				HSSFCell timeCell = contentRow.createCell(3);
				timeCell.setCellValue(time);
				timeCell.setCellStyle(contentCellStyle);
				HSSFCell xCell = contentRow.createCell(4);
				xCell.setCellValue(x);
				xCell.setCellStyle(contentCellStyle);
				HSSFCell yCell = contentRow.createCell(5);
				yCell.setCellValue(y);
				yCell.setCellStyle(contentCellStyle);
				HSSFCell tCell = contentRow.createCell(6);
				tCell.setCellValue(speed);
				tCell.setCellStyle(contentCellStyle);
				
				HSSFCell addCell = contentRow.createCell(7);
				addCell.setCellValue(address);
				addCell.setCellStyle(contentCellStyle);
			}else if(flag == 3){
				HSSFCell mileageCell = contentRow.createCell(2);
				mileageCell.setCellValue(mileage);
				mileageCell.setCellStyle(contentCellStyle);
				HSSFCell oilCell = contentRow.createCell(3);
				oilCell.setCellValue(oil);
				oilCell.setCellStyle(contentCellStyle);
				HSSFCell xCell = contentRow.createCell(4);
				xCell.setCellValue(x);
				xCell.setCellStyle(contentCellStyle);
				HSSFCell yCell = contentRow.createCell(5);
				yCell.setCellValue(y);
				yCell.setCellStyle(contentCellStyle);
				HSSFCell tCell = contentRow.createCell(6);
				tCell.setCellValue(date);
				tCell.setCellStyle(contentCellStyle);
			}else if(flag == 4){
				HSSFCell tCell = contentRow.createCell(2);
				tCell.setCellValue(String.format("%.2f", Integer.parseInt(times)/60.0));
				tCell.setCellStyle(contentCellStyle);
			}else if(flag == 5){
				HSSFCell tCell = contentRow.createCell(2);
				tCell.setCellValue(String.format("%.2f", Integer.parseInt(times)/60.0));
				tCell.setCellStyle(contentCellStyle);
			}else if(flag == 6){
				HSSFCell mileageCell = contentRow.createCell(2);
				mileageCell.setCellValue(team);
				mileageCell.setCellStyle(contentCellStyle);
				HSSFCell oilCell = contentRow.createCell(3);
				oilCell.setCellValue(time1);
				oilCell.setCellStyle(contentCellStyle);
				HSSFCell oilCells = contentRow.createCell(4);
				oilCells.setCellValue(time2);
				oilCells.setCellStyle(contentCellStyle);
				HSSFCell oilCelld = contentRow.createCell(5);
				oilCelld.setCellValue(String.format("%.2f", Integer.parseInt(times)/60.0));
				oilCelld.setCellStyle(contentCellStyle);
				HSSFCell xCell = contentRow.createCell(6);
				xCell.setCellValue(x);
				xCell.setCellStyle(contentCellStyle);
				HSSFCell yCell = contentRow.createCell(7);
				yCell.setCellValue(y);
				yCell.setCellStyle(contentCellStyle);
				HSSFCell tCell = contentRow.createCell(8);
				tCell.setCellValue(mileage);
				tCell.setCellStyle(contentCellStyle);
			}else if(flag == 7){
				HSSFCell oilCells = contentRow.createCell(2);
				oilCells.setCellValue(team);
				oilCells.setCellStyle(contentCellStyle);
				HSSFCell oilCelld = contentRow.createCell(3);
				oilCelld.setCellValue(time);
				oilCelld.setCellStyle(contentCellStyle);
				HSSFCell xCell = contentRow.createCell(4);
				xCell.setCellValue(x);
				xCell.setCellStyle(contentCellStyle);
				HSSFCell yCell = contentRow.createCell(5);
				yCell.setCellValue(y);
				yCell.setCellStyle(contentCellStyle);
				
			}else if(flag == 8){
				HSSFCell tCell = contentRow.createCell(2);
				tCell.setCellValue(team);
				tCell.setCellStyle(contentCellStyle);
				HSSFCell tCellx = contentRow.createCell(3);
				tCellx.setCellValue(days);
				tCellx.setCellStyle(contentCellStyle);
			}else if(flag == 9){
			
				HSSFCell teamcell = contentRow.createCell(2);
				teamcell.setCellValue(team);
				teamcell.setCellStyle(contentCellStyle);
				HSSFCell mileageCell = contentRow.createCell(3);
				mileageCell.setCellValue(mileage);
				mileageCell.setCellStyle(contentCellStyle);
				HSSFCell oilCell = contentRow.createCell(4);
				oilCell.setCellValue(oil);
				oilCell.setCellStyle(contentCellStyle);
				HSSFCell tCell_o = contentRow.createCell(5);
				tCell_o.setCellValue(speedU120);
				tCell_o.setCellStyle(contentCellStyle);
				HSSFCell xCell = contentRow.createCell(6);
				xCell.setCellValue(acclation);
				xCell.setCellStyle(contentCellStyle);
				HSSFCell yCell = contentRow.createCell(7);
				yCell.setCellValue(breaks);
				yCell.setCellStyle(contentCellStyle);
				HSSFCell tCell = contentRow.createCell(8);
				tCell.setCellValue(date);
				tCell.setCellStyle(contentCellStyle);
			}else if(flag == 10){
				HSSFCell mileageCell = contentRow.createCell(2);
				mileageCell.setCellValue(mileage);
				mileageCell.setCellStyle(contentCellStyle);
				HSSFCell oilCell = contentRow.createCell(3);
				oilCell.setCellValue(time1);
				oilCell.setCellStyle(contentCellStyle);
				HSSFCell tCell_o = contentRow.createCell(4);
				tCell_o.setCellValue(time2);
				tCell_o.setCellStyle(contentCellStyle);
				HSSFCell xCell = contentRow.createCell(5);
				xCell.setCellValue(String.format("%.2f", Integer.parseInt(times)/60.0));
				xCell.setCellStyle(contentCellStyle);
				HSSFCell xCells = contentRow.createCell(6);
				xCells.setCellValue(speed);
				xCells.setCellStyle(contentCellStyle);
				HSSFCell yCell = contentRow.createCell(7);
				yCell.setCellValue(pt1);
				yCell.setCellStyle(contentCellStyle);
				HSSFCell tCell = contentRow.createCell(8);
				tCell.setCellValue(pt2);
				tCell.setCellStyle(contentCellStyle);
			}
			
		}
		
		OutputStream out;
		try {
			out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CommonException("导出Excel出错");
		}
	}
	
	
	public String memberExcel() throws Exception{
		excelMap1 = new ArrayList<Map<String,String>>();
		
//		String uid = getParameter("uid");
//		String type = getParameter("type");
//		String status = getParameter("status");
		int dtid = 0;
		Account	account = getSessionAccount();
			if (account.getLevel().getLevelId() == 4) {
				dtid = account.getDept().getDeptId();
			} else {
				dtid = account.getTeam().getTeamId();
			}
		
		Object[] keywords = new Object[] { dtid,"1",null};
		List list = memberService.getListSql(account.getLevel().getLevelId(),
				keywords);
		
		for (int i = 0; i < list.size(); i++) {
			Map map=new HashMap();
			String result=ResultManager.getBodyResult(list.get(i));
			JSONObject jo=new JSONObject(result);
			map.put("userName",jo.getString("user_name"));
			map.put("name",jo.getString("name"));
			map.put("tel",jo.getString("tel")=="null"?"--":jo.getString("tel"));
			map.put("team",jo.getString("teamName"));
			map.put("dept",jo.getString("deptName")=="null"?"--":jo.getString("deptName"));
			excelMap1.add(map);
		}
		export(excelMap1, 1);
		return null;
	}
	
	
	public String driverExcel() throws Exception{
		excelMap1 = new ArrayList<Map<String,String>>();
		
		String driverName = getParameter("driverName");//搜索：司机姓名
		String ss_tel=getParameter("ss_tel"); //搜索：联系电话
		String status = getParameter("status");
		Account account = getSessionAccount();
		String teamIds =account.getTeam().getTeamId()+"";
//		if(account.getLevel().getLevelId()!=1){
//			teamIds = account.getTeam().getTeamId()+"";
//		}
		Object[] keywords = new Object[]{teamIds,driverName,1,ss_tel};
			List list = driverService.getList(keywords);
		
		for (int i = 0; i < list.size(); i++) {
			Map map=new HashMap();
			String result=ResultManager.getBodyResult(list.get(i));
			JSONObject jo=new JSONObject(result);
			map.put("userName",jo.getString("userName"));
			map.put("name",jo.getString("driverName"));
			map.put("tel",jo.getString("driverTel")=="null"?"--":jo.getString("driverTel"));
			map.put("license",jo.getString("license")=="null"?"--":jo.getString("license"));
			map.put("zjcx",jo.getString("zjcx")=="null"?"--":jo.getString("zjcx"));
			map.put("card_no",jo.getString("cardNo")=="null"?"--":jo.getString("cardNo"));
			excelMap1.add(map);
		}
		export(excelMap1, 2);
		return null;
	}
	
	public String carExcel() throws Exception{
		excelMap1 = new ArrayList<Map<String,String>>();
		String plate = getParameterNoCheck("plate");
		String identifier = getParameterNoCheck("identifier");
		Account account = getSessionAccount();
		Object[] keywords ;
		String teamIds="";
		teamIds = teamService.getTeamIdStr(account);
		keywords = new Object[]{plate,identifier,teamIds};
		List list = userService.getListForExcel(keywords);
		
		for (int i = 0; i < list.size(); i++) {
			Map map=new HashMap();
			String result=ResultManager.getBodyResult(list.get(i));
			JSONObject jo=new JSONObject(result);
			map.put("identifier",jo.getString("identifier"));
			map.put("plate",jo.getString("plate"));
			map.put("team",jo.getString("teamName")=="null"?"--":jo.getString("teamName"));
			map.put("type",jo.getString("carStyle")=="null"?"--":jo.getString("carStyle"));
			map.put("brand",jo.getString("brand")=="null"?"--":jo.getString("brand"));
			map.put("regTime",jo.getString("reg_time")=="null"?"--":jo.getString("reg_time"));
			map.put("buyTime",jo.getString("buy_time")=="null"?"--":jo.getString("buy_time"));
			map.put("tel",jo.getString("tel")=="null"?"--":jo.getString("tel"));
			map.put("iccid",jo.getString("iccid")=="null"?"--":jo.getString("iccid"));
			
			map.put("driverName",jo.getString("driverName")=="null"?"--":jo.getString("driverName"));
			map.put("driverTel",jo.getString("driverTel")=="null"?"--":jo.getString("driverTel"));
			
			if(jo.getString("online")=="null"){
				map.put("online","空闲");
			}
			else if("0".equals(jo.getString("online"))){
				map.put("online","空闲");
			}
			else if("1".equals(jo.getString("online"))){
				map.put("online","维修中");
			}
			else if("2".equals(jo.getString("online"))){
				map.put("online","已派出");
			} 
			excelMap1.add(map);
		}
		export(excelMap1, 3);
		return null;
	}
	
	
	/*****
	 * 导出 excel 表  flag不同导出不同的统计表
	 * add   2016.12.01
	 * *****/
	public void export(List list,int flag) throws Exception{
		HSSFWorkbook workBook = new HSSFWorkbook();
		String title = String.valueOf(System.currentTimeMillis());
		HSSFSheet sheet = workBook.createSheet(title);
		HttpServletResponse response = getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename="+title+".xls");
		sheet.setDefaultColumnWidth(16);
		String userName="";//登录名
		String name="";//真实姓名
		String tel="";//电话
		String team = "";//公司
		String dept="";//部门
		String license="";//驾照
		String zjcx="";//准驾车型
		String card_no="";//身份证号码
		String identifier = "";//设备号
		String plate = "";//车牌
//		String Team="";			//公司pojo
		String type="";//车型
		String brand="";//品牌
		String regTime="";//录入时间
		String buyTime="";//购买时间
		
		String driverName="";//车辆管理 司机姓名
		String driverTel="";//车辆管理 司机电话
		
		String iccid="";
		String online="";//车辆状态
//		String mileage = "0";
//		String oil = "0";
//		String x = "";
//		String y = "";
//		String speedU120 = "0";
//		String speed = "0";
//		String time1 = "";
//		String time2 = "";
		
		HSSFCellStyle cellHeadStyle = workBook.createCellStyle();
		HSSFCellStyle contentCellStyle = workBook.createCellStyle();
		contentCellStyle.setBorderBottom((short)1);
		contentCellStyle.setBorderTop((short)1);
		contentCellStyle.setBorderLeft((short)1);
		contentCellStyle.setBorderRight((short)1);
		cellHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		cellHeadStyle.setBorderBottom((short)1);
		cellHeadStyle.setBorderTop((short)1);
		cellHeadStyle.setBorderLeft((short)1);
		cellHeadStyle.setBorderRight((short)1);
		HSSFFont font = workBook.createFont();
		font.setBoldweight((short)2);
		cellHeadStyle.setFont(font);
		//System.out.println(list);
		for(int i = 0;i<list.size();i++){
			Map map = (Map)list.get(i);
//			if(map.containsKey("uid")){
//				uid = map.get("uid").toString();
//			}
//			if(map.containsKey("plate")){
//				plate = map.get("plate").toString();
//			}
			if(map.containsKey("userName")){
				userName = map.get("userName").toString();
			}
			if(map.containsKey("name")){
				name = map.get("name").toString();
			}
			if(map.containsKey("tel")){
				tel = map.get("tel").toString();
			}
			if(map.containsKey("team")){
				team = map.get("team").toString();
				}
			if(map.containsKey("dept")){
				dept = map.get("dept").toString();
			}
			if(map.containsKey("license")){
				license = map.get("license").toString();
			}
			if(map.containsKey("zjcx")){
				zjcx = map.get("zjcx").toString();
			}
			if(map.containsKey("card_no")){
				card_no = map.get("card_no").toString();
			}
			
			if(map.containsKey("identifier")){
				identifier = map.get("identifier").toString();
			}
			if(map.containsKey("plate")){
				plate = map.get("plate").toString();
			}
//			if(map.containsKey("team")){
//				team = map.get("team").toString();
//			}
			if(map.containsKey("type")){
				type = map.get("type").toString();
			}
			if(map.containsKey("brand")){
				brand = map.get("brand").toString();
			}
			if(map.containsKey("regTime")){
				regTime = map.get("regTime").toString();
			}
			if(map.containsKey("buyTime")){
				buyTime = map.get("buyTime").toString();
			}
			
			if(map.containsKey("driverName")){
				driverName = map.get("driverName").toString();
			}
			if(map.containsKey("driverTel")){
				driverTel = map.get("driverTel").toString();
			}
			
			if(map.containsKey("iccid")){
				iccid = map.get("iccid").toString();
			}
			if(map.containsKey("online")){
				online = map.get("online").toString();
			}
//			 if(team == null || team == ""){
//				 team = "未分组车辆";
//			 }
			if(i == 0){
				HSSFRow row = sheet.createRow(0);
				
		switch(flag){
		case 1:  //乘客
			HSSFCell cell10 = row.createCell(0);
			cell10.setCellValue("登录名");
			cell10.setCellStyle(cellHeadStyle);
			HSSFCell cell11 = row.createCell(1);
			cell11.setCellValue("真实姓名");
			cell11.setCellStyle(cellHeadStyle);
			HSSFCell cell12 = row.createCell(2);
			cell12.setCellValue("联系电话");
			cell12.setCellStyle(cellHeadStyle);
			HSSFCell cell13 = row.createCell(3);
			cell13.setCellValue("公司");
			cell13.setCellStyle(cellHeadStyle);
			HSSFCell cell14=row.createCell(4);
			cell14.setCellValue("部门");
			cell14.setCellStyle(cellHeadStyle);
			break;
		case 2:  //司机
			HSSFCell cell20 = row.createCell(0);
			cell20.setCellValue("司机");
			cell20.setCellStyle(cellHeadStyle);
			HSSFCell cell21 = row.createCell(1);
			cell21.setCellValue("账号");
			cell21.setCellStyle(cellHeadStyle);
			HSSFCell cell22 = row.createCell(2);
			cell22.setCellValue("联系电话");
			cell22.setCellStyle(cellHeadStyle);
			HSSFCell cell23 = row.createCell(3);
			cell23.setCellValue("驾照");
			cell23.setCellStyle(cellHeadStyle);
			HSSFCell cell24 = row.createCell(4);
			cell24.setCellValue("准驾车型");
			cell24.setCellStyle(cellHeadStyle);
			HSSFCell cell25 = row.createCell(5);
			cell25.setCellValue("身份证号码");
			cell25.setCellStyle(cellHeadStyle);
			break;
		case 3: //车辆
			HSSFCell cell30 = row.createCell(0);
			cell30.setCellValue("设备码");
			cell30.setCellStyle(cellHeadStyle);
			HSSFCell cell31 = row.createCell(1);
			cell31.setCellValue("车牌");
			cell31.setCellStyle(cellHeadStyle);
			HSSFCell cell32 = row.createCell(2);
			cell32.setCellValue("所属公司");
			cell32.setCellStyle(cellHeadStyle);
			HSSFCell cell33 = row.createCell(3);
			cell33.setCellValue("车型");
			cell33.setCellStyle(cellHeadStyle);
			HSSFCell cell34 = row.createCell(4);
			cell34.setCellValue("品牌");
			cell34.setCellStyle(cellHeadStyle);
			HSSFCell cell35 = row.createCell(5);
			cell35.setCellValue("录入时间");
			cell35.setCellStyle(cellHeadStyle);
			HSSFCell cell36 = row.createCell(6);
			cell36.setCellValue("购买时间");
			cell36.setCellStyle(cellHeadStyle);
			
//			HSSFCell cell37 = row.createCell(7);
//			cell37.setCellValue("电话号码");
//			cell37.setCellStyle(cellHeadStyle);
//			HSSFCell cell38 = row.createCell(8);
//			cell38.setCellValue("iccid");
//			cell38.setCellStyle(cellHeadStyle);
			HSSFCell cell37 = row.createCell(7);
			cell37.setCellValue("司机");
			cell37.setCellStyle(cellHeadStyle);
			HSSFCell cell38 = row.createCell(8);
			cell38.setCellValue("司机电话");
			cell38.setCellStyle(cellHeadStyle);
			
			HSSFCell cell39 = row.createCell(9);
			cell39.setCellValue("车辆状态");
			cell39.setCellStyle(cellHeadStyle);
			break;
			}
			}
			HSSFRow contentRow = sheet.createRow(i+1);
			if(flag == 1){
				HSSFCell cell_a0 = contentRow.createCell(0);
				cell_a0.setCellValue(userName);
				cell_a0.setCellStyle(contentCellStyle);
				HSSFCell cell_a1 = contentRow.createCell(1);
				cell_a1.setCellValue(name);
				cell_a1.setCellStyle(contentCellStyle);
				HSSFCell cell_a2 = contentRow.createCell(2);
				cell_a2.setCellValue(tel);
				cell_a2.setCellStyle(contentCellStyle);
				HSSFCell cell_a3 = contentRow.createCell(3);
				cell_a3.setCellValue(team);
				cell_a3.setCellStyle(contentCellStyle);
				HSSFCell cell_a4 = contentRow.createCell(4);
				cell_a4.setCellValue(dept);
				cell_a4.setCellStyle(contentCellStyle);
			}
			else if(flag == 2){
				HSSFCell cell_b0 = contentRow.createCell(0);
				cell_b0.setCellValue(name);
				cell_b0.setCellStyle(contentCellStyle);
				HSSFCell cell_b1 = contentRow.createCell(1);
				cell_b1.setCellValue(userName);
				cell_b1.setCellStyle(contentCellStyle);
				HSSFCell cell_b2 = contentRow.createCell(2);
				cell_b2.setCellValue(tel);
				cell_b2.setCellStyle(contentCellStyle);
				HSSFCell cell_b3 = contentRow.createCell(3);
				cell_b3.setCellValue(license);
				cell_b3.setCellStyle(contentCellStyle);
				HSSFCell cell_b4 = contentRow.createCell(4);
				cell_b4.setCellValue(zjcx);
				cell_b4.setCellStyle(contentCellStyle);
				HSSFCell cell_b5 = contentRow.createCell(5);
				cell_b5.setCellValue(card_no);
				cell_b5.setCellStyle(contentCellStyle);
			}
			else if(flag == 3){
				HSSFCell cell_c0 = contentRow.createCell(0);
				cell_c0.setCellValue(identifier);
				cell_c0.setCellStyle(contentCellStyle);
				HSSFCell cell_c1 = contentRow.createCell(1);
				cell_c1.setCellValue(plate);
				cell_c1.setCellStyle(contentCellStyle);
				HSSFCell cell_c2 = contentRow.createCell(2);
				cell_c2.setCellValue(team);
				cell_c2.setCellStyle(contentCellStyle);
				HSSFCell cell_c3 = contentRow.createCell(3);
				cell_c3.setCellValue(type);
				cell_c3.setCellStyle(contentCellStyle);
				HSSFCell cell_c4 = contentRow.createCell(4);
				cell_c4.setCellValue(brand);
				cell_c4.setCellStyle(contentCellStyle);
				HSSFCell cell_c5 = contentRow.createCell(5);
				cell_c5.setCellValue(regTime);
				cell_c5.setCellStyle(contentCellStyle);
				HSSFCell cell_c6 = contentRow.createCell(6);
				cell_c6.setCellValue(buyTime);
				cell_c6.setCellStyle(contentCellStyle);
				
//				HSSFCell cell_c7 = contentRow.createCell(7);
//				cell_c7.setCellValue(tel);
//				cell_c7.setCellStyle(contentCellStyle);
//				HSSFCell cell_c8 = contentRow.createCell(8);
//				cell_c8.setCellValue(iccid);
//				cell_c8.setCellStyle(contentCellStyle);
				HSSFCell cell_c7 = contentRow.createCell(7);
				cell_c7.setCellValue(driverName);
				cell_c7.setCellStyle(contentCellStyle);
				HSSFCell cell_c8 = contentRow.createCell(8);
				cell_c8.setCellValue(driverTel);
				cell_c8.setCellStyle(contentCellStyle);
				
				HSSFCell cell_c9 = contentRow.createCell(9);
				cell_c9.setCellValue(online);
				cell_c9.setCellStyle(contentCellStyle);
			}
			
		}
		
		OutputStream out;
		try {
			out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CommonException("导出Excel出错");
		}
	}
	
}
