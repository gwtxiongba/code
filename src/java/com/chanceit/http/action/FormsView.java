package com.chanceit.http.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.json.JSONException;
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
import com.chanceit.http.pojo.Point;
import com.chanceit.http.pojo.PointInfo;
import com.chanceit.http.pojo.PointsForm;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IFormsService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.google.gson.Gson;

/**
 * @ClassName FormsView
 * @author gwt
 * @date 2015-01-02
 * @Description 报表统计接口
 */
@Component("formsView")
public class FormsView extends BaseAction {
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
	private List<Map<String, String>> excelMap1 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> excelMap2 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> mileMap = new ArrayList<Map<String, String>>();
	private Map<String,String> map_over = new HashedMap();
	public String getTeamCar() throws Exception {
		String account_id = getParameter("account_id");
		Account account = null;
		account = getSessionAccount();
		if (account == null) {
			return ResultManager.getFaildResult(102, "您未登录系统或长时间未操作");
		}
		String accountId;
		// json2 = userService.getJson(accountId);
		// json.put("teams", teamService.getJson(Integer.parseInt(accountId)));
		// json.put("teams", teamService.getJson(Integer.parseInt(accountId)));
		if (account.getRole().getRoleId() != 2) {
			accountId = getCompanyId();
		} else {
			accountId = account.getAccountId().toString();
		}
		String res = formService.getTeamCars(account, Integer
				.parseInt(accountId));
		return res;
	}

	/**
	 * 获取报表柱状图数据（总里程，油耗等）
	 */
	public String getDrivingForm() throws Exception {
		// http://192.168.0.153:8080/carmonitor/api.action?cmd=formdDriving&date0=2015-01-09&date1=2015-01-09&vehicleId=3,4&plate=&mileage=&fuel=&_dc=1420798038697&page=1&start=0&limit=20
		Map<String, String> map = new HashedMap();
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
		
		if(!json.has("result")){
			return	ResultManager.getFaildResult("数据解析错误");
		}
		String s = json.getString("result");
		JSONArray array = new JSONArray(s);

		for (int i = 0; i < array.length(); i++) {
//			Map<String, String> o_map = new HashedMap();
			JSONObject item = array.getJSONObject(i);
//			o_map.put("plate", map.get(item.getInt("vehicleId") + ""));
//			o_map.put("mile", item.getString("mileage"));
//			o_map.put("fuel", item.getString("fuel"));
//			o_map.put("uid", item.getInt("vehicleId") + "");
			item.put("plate", map.get(item.getInt("vehicleId") + ""));
			resultArray.put(item);
//			uidsAfter = uidsAfter.replace(item.getInt("vehicleId") + "", "0");
			// list_json.add(item);
		}
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
	public String getLocation() throws Exception {
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
		
		List<PointInfo> list = formService.getLocationOne(Integer.parseInt(uid), beginTime+"0000", endTime,180);//报表展示三分钟一个位置点
		JSONArray array = new JSONArray();
		//{"vehicleId":10,"plate":"\u9102A6QM10","team":"\u6e56\u5317\u8f93\u6cb9\u6c14\u5206\u516c\u53f8","gps":[116.307852,40.057031,"2015-01-12 9:15:24",56,89]}
		if(list == null){
			return ResultManager.getFaildResult("暂无数据");
			}
		for(int i=0;i<list.size();i++){
			PointInfo info = list.get(i);
			JSONObject json = new JSONObject();
			JSONArray arr = new JSONArray();
			arr.put(info.getX());
			arr.put(info.getY());
			arr.put(info.getTime());
			arr.put(info.getSpeed());
			arr.put(info.getAzimuth());
			json.put("vehicleId", vehicleId);
			json.put("plate", plate);
			json.put("team", team);
			json.put("gps", arr);
			
			array.put(json);
			
		}
		return array.toString();
	}

	/**
	 * 获取行驶报表列表数据
	 */
	public String getDrivingList() throws Exception {
		// http://192.168.0.153:8080/carmonitor/api.action?cmd=formdPath&date0=2015-01-01&date1=2015-01-10&vehicleId=253006938&obdmile=1228207&fuel=0.00&_dc=1420856865953&page=1&start=0&limit=20
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formdPath&date0=2014-12-01&date1=2015-01-11&vehicleId=153206344&obdmile=122363458&fuel=7.54&_dc=1421033572914&page=1&start=0&limit=20
		String uid = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String fule = getParameter("fuel");
		String mile = getParameter("obdmile");
		//String m = 0;
		double f = 0;
//		if (!StringUtils.isEmpty(mile)) {
//			m = Long.parseLong(mile);
//			System.out.println(m+"***");
//		}
		if (!StringUtils.isEmpty(fule)) {
			f = Double.parseDouble(fule);
		}
		if(StringUtils.isEmpty(uid)){
			return ResultManager.getFaildResult("参数错误");
		}
		String result = formService.getDrivingList(Integer.parseInt(uid),
				beginTime+" 00:00:00", endTime+" 23:59:59", mile, f);
		JSONObject json = new JSONObject(result);
		String s = json.getString("result");
		return s;
	}

	/**
	 * 获取里程报表列表数据
	 */
	public String getObdMileList() throws Exception {
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formObdList&date0=2014-12-01&date1=2015-01-11&vehicleIds=9824,10418,10429,10189,10428,4627,9777,9779,9788,9780,9792,9793&plate=&_dc=1421029309493&page=1&start=0&limit=20
		String userIds = getParameter("vehicleIds");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
//		String result = formService.getObdMile(userIds,
//				beginTime, endTime,0,0);
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
		for (int i = 0; i < array.length(); i++) {
			Map<String, String> o_map = new HashedMap();
			JSONObject item = array.getJSONObject(i);
			o_map.put("plate", map.get(item.getInt("vehicleId") + ""));
			o_map.put("mile", item.getString("mileage"));
			o_map.put("fuel", item.getString("fuel"));
			o_map.put("uid", item.getInt("vehicleId") + "");
			item.put("plate", map.get(item.getInt("vehicleId") + ""));
			String  ss = map_l.get(item.getInt("vehicleId") + "");
			String[] strs = ss.split(",");
			item.put("pt1", String.format("%.6f", Double.parseDouble(strs[0]))+ ","+String.format("%.6f", Double.parseDouble(strs[1])));
			item.put("lasttime", getTimes(Long.parseLong(strs[2])));
			resultArray.put(item);
		}
		return resultArray.toString();
	}

	/**
	 * 获取超速报警列表数据
	 */
	public String getOverSpWarn() throws Exception {
		String uid = getParameter("uid");
		String beginTime = getParameter("beginTime");
		String endTime = getParameter("endTime");
		String speed = getParameter("speed");
		String plate_ = getParameter("plate");
		int sp = 0;
		if (!StringUtils.isEmpty(speed)) {
			sp = Integer.parseInt(speed);
		}
		// String result =
		// formService.getSpeedList(Integer.parseInt(uid),beginTime,endTime,sp);
		return null;
	}

	/**
	 * 获取停车报表柱状图数据
	 */
	public String getStopForm() throws Exception {
		// http://192.168.0.153:8080/carmonitor/api.action?cmd=formStop&date0=2015-01-11&date1=2015-01-11&vehicleId=3&plate=&timeout=&_dc=1420954388891&page=1&start=0&limit=20
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
		
		System.out.println(user_ids+"////");
		List result = formService.getStopList(user_ids, getTime(beginTime+" 00:00:00"),
				getTime(endTime+" 23:59:59"), sec);
		JSONArray  array = new JSONArray();
		for (Iterator it = result.iterator(); it.hasNext();) {
			//Map<String, String> o_map = new HashedMap();
			Object[] obj = (Object[]) it.next();
			JSONObject json = new JSONObject();
			json.put("vehicleId", obj[0]);
			json.put("plate", map.get(obj[0].toString()));
			json.put("stopseconds", obj[1]);
			array.put(json);
			
		}
		Gson go = new Gson();
		return array.toString();
	}

	/**
	 * 获取停车报表列表数据
	 */
	public String getStopList() throws Exception {
		//http://192.168.0.153:8080/carmonitor/api.action?cmd=formdPath&date0=2014-12-01&date1=2015-01-11&vehicleId=153206344&_dc=1420955593216&page=1&start=0&limit=20
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
				getTime(beginTime+" 00:00:00"), getTime(endTime+" 23:59:59"), sec);
		JSONArray array = new JSONArray();
		for(int i=0;i<result.size();i++){
			JSONObject json = new JSONObject();
			PointsForm pf = result.get(i);
			Point point = converter.wgs2bd(Double.parseDouble(String.valueOf(pf.getY()))/(3600*24),Double.parseDouble(String.valueOf(pf.getX()))/(3600*24));
			json.put("plate", plate);
			json.put("team", teamname);
			json.put("time0", getTimes(Long.parseLong(pf.getTime1().toString())));
			json.put("time1",  getTimes(Long.parseLong(pf.getTime2().toString())));
			json.put("timespan", pf.getTime2()-pf.getTime1());
			json.put("pt1", String.format("%.6f", point.getLongitude())+","+String.format("%.6f",point.getLatitude()));
			json.put("pt", String.format("%.6f", point.getLongitude())+","+String.format("%.6f",point.getLatitude()));
			json.put("order", i+1);
			array.put(json);
		}
		Gson go = new Gson();
		go.toJson(result);
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
				getTime(beginTime+" 00:00:00"), getTime(endTime+" 23:59:59"), sec);
		JSONArray array = new JSONArray();
		for(int i=0;i<result.size();i++){
			JSONObject json = new JSONObject();
			PointsForm pf = result.get(i);
			Point point = converter.wgs2bd(Double.parseDouble(String.valueOf(pf.getY()))/(3600*24),Double.parseDouble(String.valueOf(pf.getX()))/(3600*24));
			json.put("plate", plate);
			json.put("team", teamname);
			json.put("time0", getTimes(Long.parseLong(pf.getTime1().toString())));
			json.put("time1",  getTimes(Long.parseLong(pf.getTime2().toString())));
			json.put("timespan", pf.getTime2()-pf.getTime1());
			json.put("pt0",String.format("%.6f", point.getLongitude())+","+String.format("%.6f",point.getLatitude()));
			json.put("pt1", "");
			json.put("mileage", pf.getMiles());
			json.put("order", i+1);
			array.put(json);
		}
		Gson go = new Gson();
		go.toJson(result);
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
		List result = formService.getFireList(user_ids, getTime(beginTime+" 00:00:00"),
				getTime(endTime+" 23:59:59"), sec);
		JSONArray  array = new JSONArray();
		for (Iterator it = result.iterator(); it.hasNext();) {
			//Map<String, String> o_map = new HashedMap();
			Object[] obj = (Object[]) it.next();
			JSONObject json = new JSONObject();
			json.put("vehicleId", obj[0]);
			json.put("plate", map.get(obj[0].toString()));
			json.put("timespan", obj[1]);
			array.put(json);
			
		}
		Gson go = new Gson();
		return array.toString();
	}

	/***************************************************************************
	 * 月上线率统计
	 */
	public String getOnlineByMonth() throws Exception {
		// formService.get
		//cmd=monthOnlines&year=2015&month=1&vehicleIds=3,4&plate=&_dc=1421896718445&page=1&start=0&limit=20
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
		List result = formService.getOnlineByMonth(user_ids, beginTime+" 00:00:00", endTime+" 23:59:59");
		int i=1;
		JSONArray array = new JSONArray(); 
		for(Iterator it = result.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			JSONObject json = new JSONObject();
			json.put("order", i);
			json.put("plate", map.get(obj[0].toString()));
			json.put("team", map_.get(obj[0].toString()));
			json.put("identifier", obj[0].toString());
			json.put("days", obj[1]);
			array.put(json);
		}
		
		return array.toString();
	}

	/***************************************************************************
	 * 日上线率统计
	 */
	public String getOnlineByDay() throws Exception {
		//api.action?cmd=dateOnlines&date=2014-12-1&vehicleIds=9824,10418,10429,10189,10428,10468,4627,9777,9779,9788,9780,9792,9793&plate=&_dc=1421907389285&page=1&start=0&limit=20
		String uids = getParameter("vehicleIds");
		String time = getParameter("date");
		String start = DateUtil.getFirstDayOfMonth(time);
		String end = DateUtil.getLastDay(time);
		String plate_ = getParameter("plate");
//		if(StringUtils.isEmpty(uids)){
//			return ResultManager.getFaildResult("参数错误");
//		}
		//List<User> usersList = userService.getUidById(uids);
		String user_ids = "";
		int size = 1;
		if (!StringUtils.isEmpty(plate_)) {
			List<User> users = userService.getUidByPlate(plate_);
			if (users != null) {
				if (users.size() > 0) {
					user_ids = users.get(0).getIdentifier();
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
//				map.put(usersList.get(i).getIdentifier(), usersList.get(i)
//						.getPlate());
				if (i == 0) {
					user_ids = usersList.get(0).getIdentifier();
				} else {
					user_ids += "," + usersList.get(i).getIdentifier();
				}
			}
		}else{
			return	ResultManager.getFaildResult("请选择正确的车辆");
		}
		
		List result = formService.getOnlineByDay(user_ids, start+" 00:00:00",end+" 23:59:59");
		JSONArray array = new JSONArray(); 
		JSONObject json = new JSONObject();
		json.put("name", "畅派终端设备");
		int i=1;
		double num = 0;
		double avg = 0;
		for(Iterator it = result.iterator();it.hasNext();){
			Object[] obj = (Object[]) it.next();
			json.put("d"+i, String.format("%.2f", (Double.parseDouble(obj[0].toString())/size)*100));
			i++;
			avg += (Double.parseDouble(obj[0].toString())/size)*100;
			num += Integer.parseInt(obj[0].toString());
		}
		json.put("avg",String.format("%.2f", avg/i));
		array.put(json);
		
		return array.toString();
	}

	/***************************************************************************
	 * 日行油耗统计
	 */
	public String getFuleByDay() throws Exception{
		//api.action?cmd=dateFuel&date0=2015-01-01&date1=2015-01-21&vehicleId=9824&_dc=1421916979185&page=1&start=0&limit=20
		String uids = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String plate_ = getParameter("plate");
		String uid_str = "";
		String plate = "";
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
			JSONObject jj = array.getJSONObject(i);
			String[] time =  jj.getString("recorderDate").split(" ");
			JSONObject res = new JSONObject();
			res.put("order", i+1);
			res.put("plate", plate);
			res.put("date", time[0].replaceAll("-", ""));
			res.put("mileage", jj.getInt("mile")/1000.0);
			res.put("fuel", jj.getInt("obdFuel")/100000);
			res.put("overspeedtime",  jj.getInt("speedL20") + "");
			res.put("speeduptimes",  jj.getInt("acceleration") + "");
			res.put("breaktimes",  jj.getInt("brakes") + "");
			if(jj.getInt("obdMile")!=0){
			res.put("avgfuel", jj.getInt("obdFuel")*100/jj.getInt("obdMile"));
			}else{
				res.put("avgfuel", 0);	
			}
			arr.put(res);
		}
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
		beginTime = beginTime+" 00:00:00";
		endTime = endTime+" 23:59:59";
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
			List list = formService.getPengZhuangForm(uids,5, beginTime, endTime);
			return parsList(list);
		}else if(type.equals("crash")){
			List list = formService.getPengZhuangForm(uids, 1, beginTime, endTime);
			return parsList(list);
		}else if(type.equals("rollover")){
			List list = formService.getPengZhuangForm(uids, 2, beginTime, endTime);
			return parsList(list);
		}else if(type.equals("quake")){
			List list = formService.getPengZhuangForm(uids, 6, beginTime, endTime);
			return parsList(list);
		}else if(type.equals("overspeed")){
			try {
			List list =	formService.getOverSpeedForm(user_ids, beginTime, endTime);
			return parsList(list);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return ResultManager.getFaildResult("暂无数据");
	}
	
	public String parsList(List list){
		 JSONArray arr = new JSONArray();
		 try {
		  for(Iterator it = list.iterator();it.hasNext();){
			  Object[] obj = (Object[]) it.next();
			  JSONObject res = new JSONObject();
				res.put("plate", obj[2]);
				res.put("vehicleId", obj[1]);
				res.put("times",obj[0]);
				arr.put(res);
		      }
			} catch (JSONException e) {
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
		beginTime = beginTime+" 00:00:00";
		endTime = endTime+" 23:59:59";
		String result="";
		String teamname =	formService.getTeam(Integer.parseInt(uid));
		if(type.endsWith("poweroff")){
			List list = formService.getPengZhuangList(Integer.parseInt(uid), 5, beginTime, endTime);
			return parsListOne(list,teamname,1);
		}else if(type.equals("crash")){
			List list = formService.getPengZhuangList(Integer.parseInt(uid), 1, beginTime, endTime);
			return parsListOne(list,teamname,1);
		}else if(type.equals("rollover")){
			List list = formService.getPengZhuangList(Integer.parseInt(uid), 2, beginTime, endTime);
			return parsListOne(list,teamname,1);
		}else if(type.equals("quake")){
			List list = formService.getPengZhuangList(Integer.parseInt(uid), 6, beginTime, endTime);
			return parsListOne(list,teamname,1);
		}else if(type.equals("overspeed")){
			List<User> usersList = userService.getUidById(uid);
			if(usersList != null && usersList.size()>0){
				uid = usersList.get(0).getIdentifier();
			}else{
				ResultManager.getFaildResult("参数错误");
			}
			try {
			List list = formService.getOverSpeedList(Integer.parseInt(uid), beginTime, endTime);
			return parsListOne(list,teamname,0);
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
	
	public String parsListOne(List list,String name,int type){
		 JSONArray arr = new JSONArray();
		 int i = 1;
		 try {
		  for(Iterator it = list.iterator();it.hasNext();){
			  Object[] obj = (Object[]) it.next();
			  JSONObject res = new JSONObject();
			  res.put("order", i);
			  res.put("plate", obj[3]);
			  res.put("team", name);
			 // res.put("time",obj[2]);
			  String x = "";
			  String y = "";
			  if(obj[0].toString().contains(".")){
				  x = obj[0].toString();
				  y = obj[1].toString();
			  }else{
				  x = Double.parseDouble(obj[0].toString())/(3600*24.0)+"";
				  y = Double.parseDouble(obj[1].toString())/(3600*24.0)+"";
			  }
			  
				i++;
				if(type == 0){
					JSONArray arry = new JSONArray();
					arry.put(String.format("%.6f", Double.parseDouble(x)));
					arry.put(String.format("%.6f", Double.parseDouble(y)));
					arry.put(getTimes(Long.parseLong(obj[2].toString())));
					arry.put(obj[4]);
					arry.put((int)Math.random()*360);
					res.put("pt", String.format("%.6f", Double.parseDouble(x))+","+String.format("%.6f", Double.parseDouble(y)));
					res.put("gps",arry);
		      }else{
		    	  res.put("address", String.format("%.6f", Double.parseDouble(x))+","+String.format("%.6f", Double.parseDouble(y)));
		    	  res.put("pt", String.format("%.6f", Double.parseDouble(x))+","+String.format("%.6f", Double.parseDouble(y)));
		      }
				res.put("time", obj[2]);
				arr.put(res);
		  }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return arr.toString();
	}
	
	public String dayForm() throws Exception{
		//carmonitor/api.action?cmd=statsTotalMF&date0=2015-01-22&date1=2015-01-22&vehicleIds=9824,1041,9779,9788,9780,9792,9793&plate=&fuel=&_dc=1421983151530&page=1&start=0&limit=20
		Map<String, String> map = new HashedMap();
		
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
				String teama = "";
				Object[] obj = (Object[]) list_p.get(i);
				if(obj[1] != null){
				plate = obj[1].toString();
				}
				if(obj[2] != null){
				teama = obj[2].toString();
				}
				if(obj[0] != null){
					uid = obj[0].toString();
				}
				map_t.put(uid, teama);
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
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.getJSONObject(i);
				JSONObject res_jspn = new JSONObject();
				res_jspn.put("plate", map.get(item.getInt("vehicleId") + ""));
				res_jspn.put("mileage", item.getString("mileage"));
				res_jspn.put("avgfuel", item.getString("fuel"));
				res_jspn.put("fuel", String.format("%.2f", item.getInt("f")/100000.0));
				res_jspn.put("date", item.getString("time"));
				res_jspn.put("team",  map_t.get(item.getInt("vehicleId") + ""));
				res_jspn.put("vehicleId", item.getInt("vehicleId"));
				res_jspn.put("overspeedtime",  item.getInt("speedtime"));
				res_jspn.put("speeduptimes",  item.getInt("acceleration"));
				res_jspn.put("breaktimes",  item.getInt("brakes"));
				resultArray.put(res_jspn);
				// list_json.add(item);
			}
		return resultArray.toString();
	}
	public String dayFormList() throws Exception{
		//api.action?cmd=statsDaysMF&vehicleId=&date0=2014-10-01&date1=2015-01-22&plate=&fuel=&_dc=1421994228068&page=1&sta
		String uid = getParameter("vehicleId");
		String beginTime = getParameter("date0");
		String endTime = getParameter("date1");
		String second = getParameter("plate");
		String fuel = getParameter("fuel");
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
				JSONObject item = array.getJSONObject(i);
				JSONObject res_jspn = new JSONObject();
				res_jspn.put("plate", plate);
				res_jspn.put("mileage", String.format("%.2f",item.getInt("mile")/1000.0));
				res_jspn.put("fuel", String.format("%.2f", item.getInt("obdFuel")/100000.0));
				String[] t = item.getString("recorderDate").split(" ");
				res_jspn.put("date", t[0]);
				res_jspn.put("team",  team);
				res_jspn.put("vehicleId", item.getInt("identifier"));
				//if(map_over.containsKey(item.getInt("identifier")+"")){
				res_jspn.put("overspeedtime",  item.getInt("speedL20"));
				res_jspn.put("speeduptimes",  item.getInt("acceleration"));
				res_jspn.put("breaktimes",  item.getInt("brakes"));
//				}else{
//					res_jspn.put("overspeedtimes",  0);
//				}
				resultArray.put(res_jspn);
				// list_json.add(item);
			}
		return resultArray.toString();
	}
	/**
	 * 把一定格式的时间装换成秒数
	 */
	private int getTime(String strtime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

	 /*
	 * 明天做按月统计
	 */
	public List getListByDriving() {

		return null;
	}
	public void exportCycleMonth() throws Exception{
//		String vehicleIds = this.getParameterNoCheck("vehicleId");
////		String year = this.getParameterNoCheck("year");
////		String month = this.getParameterNoCheck("month");
//		String yM = this.getParameterNoCheck("date");
//		String arr[] = yM.split("-");
//		String year = arr[0];
//		String month = arr[1];
//		String oilper = this.getParameterNoCheck("fuel");
//		String platePram = this.getParameterNoCheck("plate");
//		String accountId;
//		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
//		if(StringUtils.isNotBlank(platePram)){
//			boolean ifExistPlate = userService.ifExistPlate(platePram, Integer.parseInt(accountId), null);
//			if(!ifExistPlate){
//				throw new CommonException("车牌对应的车辆不存在");
//			}
//		}else{
//			Object[]values = {accountId,null,null};
//			List userList = userService.getList(values);
//			for(int i=0;i<userList.size();i++){
//				User user = (User)userList.get(i);
//				if(StringUtils.isBlank(vehicleIds)){
//					vehicleIds = user.getVehicleId().toString();
//				}else{
//					vehicleIds +=","+user.getVehicleId().toString();
//				}
//			}
//		}
		List list = excelMap1;
		HSSFWorkbook workBook = new HSSFWorkbook();
		String title = String.valueOf(System.currentTimeMillis());
		HSSFSheet sheet = workBook.createSheet(title);
		HttpServletResponse response = this.getResponse();
		response.setContentType("octets/stream");
		response.addHeader("Content-Disposition", "attachment;filename="+title+".xls");
		sheet.setDefaultColumnWidth(15);
		String identifer = "";
		String id = "";
		String plate = "";
		String date = "";
		String team = "";
		String mileage = "";
		String oil = "";
		String accTimes = "";
		String brakes = "";
		String speedTime = "";
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
		for(int i = 0;i<list.size();i++){
			Map map = (Map)list.get(i);
			id = (String)map.get("uid");
			plate = (String)map.get("plate");
			mileage = map.get("mile").toString();
			oil = map.get("fuel").toString();
			if(i == 0){
				HSSFRow row = sheet.createRow(0);
//				row.setRowStyle(cellHeadStyle);
				HSSFCell headPlateCell = row.createCell(0);
				headPlateCell.setCellValue("车牌号码");
				headPlateCell.setCellStyle(cellHeadStyle);
				HSSFCell headGroupCell = row.createCell(1);
				headGroupCell.setCellValue("设备id");
				headGroupCell.setCellStyle(cellHeadStyle);
				HSSFCell headMileageCell = row.createCell(2);
				headMileageCell.setCellValue("行驶里程(公里)");
				headMileageCell.setCellStyle(cellHeadStyle);
				HSSFCell headOilCell = row.createCell(3);
				headOilCell.setCellValue("油耗(升)");
				headOilCell.setCellStyle(cellHeadStyle);
			}
			HSSFRow contentRow = sheet.createRow(i+1);
			HSSFCell plateCell = contentRow.createCell(0);
			plateCell.setCellValue(plate);
			plateCell.setCellStyle(contentCellStyle);
			HSSFCell teamCell = contentRow.createCell(1);
			teamCell.setCellValue(id);
			teamCell.setCellStyle(contentCellStyle);
			HSSFCell mileageCell = contentRow.createCell(2);
			mileageCell.setCellValue(mileage);
			mileageCell.setCellStyle(contentCellStyle);
			HSSFCell oilCell = contentRow.createCell(3);
			oilCell.setCellValue(oil);
			oilCell.setCellStyle(contentCellStyle);
			
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
