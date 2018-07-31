package com.chanceit.http.service;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.client.Client;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.chanceit.framework.utils.ClientFactory;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.http.dao.IDriveReportDao;
import com.chanceit.http.pojo.CycleReport;
import com.chanceit.http.pojo.DriveReport;
import com.chanceit.http.pojo.Report;
import com.chanceit.http.pojo.SignInLog;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Transactional
@Component("driveReportService")
public class DriveReportService implements IDriveReportService{
	private String charSet="UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	private String driveReportUrl;
	private String cycleReportUrl;
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	@Autowired
	@Qualifier("signInLogService")
	private ISignInLogService signInLogService;
	
	@Autowired
	@Qualifier("driveReportDao")
	private IDriveReportDao driverReportDao;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	public String getDriveReportUrl() {
		return driveReportUrl;
	}
	public void setDriveReportUrl(String driveReportUrl) {
		this.driveReportUrl = driveReportUrl;
	}
	
	public void saveCycleReport(User user,Date date) throws Exception{
			Client client = ClientFactory.getClient(cycleReportUrl);
			Object[] objs = new Object[3];
			objs[0] = user.getIdentifier();
			date = DateUtil.getAfterDate(date,-1);
			String start = DateUtil.getDateFirstString(date);
			String end = DateUtil.getDateLastString(date);
			objs[1] = start;
			objs[2] = end;
			String result = client.invoke("getRecorder_and", objs)[0].toString();
			JSONObject obj = new JSONObject(result);
			Team group = user.getTeam();
			String groupName = group == null?"":group.getTeamName();
			if(!"{}".equals(result) && (((obj.has("code"))&&1==obj.getInt("code"))||((obj.has("isok"))&&obj.getBoolean("isok")))){
				JSONArray jsonArray = null;
				if(obj.has("daydata")){
					jsonArray = obj.getJSONArray("daydata");
				}else{
					jsonArray = obj.getJSONArray(DateUtil.getFirstDayOfMonth(start));
				}
				double driveTime = 0;// 行驶总时间
				float mile = 0; // 行驶总里程
//				int mileDay = 0; // 白天行驶里程数(6:00~~19:00)
//				int mileNight = 0; // 晚行驶上里程数()
//				int driveTimeDay = 0; // 白天行驶时间
//				int driveTimeNight = 0; // 晚上行驶时间
				float highestSpeed = 0; // 最高速度值（km/h）
				float speedU120 = 0; // 速度超多120的行驶时间（秒）
				float per_oil_count =0;//百公里油耗
				float oil_count = 0;//总油耗
				int brakes = 0;  //刹车次数
				int acceleration = 0; // 急加速次数
				int fatigueDriving = 0; //疲劳驾驶次数
				String dateTime = "";
				
				for(int i = 0;i < jsonArray.length();i++){
					JSONObject object = jsonArray.getJSONObject(i);
					driveTime = object.getDouble("driveTime");
					mile = (float)object.getDouble("mile");
					per_oil_count = (float)object.getDouble("fule");
					oil_count = (float)(per_oil_count * mile/100.0);
					highestSpeed = (float)object.getDouble("highestSpeed");
					speedU120 = (float)object.getDouble("speedU120");
					dateTime = object.getString("dateTime");
					brakes = object.getInt("brakes");
					acceleration = object.getInt("acceleration");
					fatigueDriving = object.getInt("fatigueDriving");
					if(driverReportDao.getCycleReportCount(user.getVehicleId(),dateTime) == 0){
						CycleReport report = new CycleReport();
						report.setBeyondParkCount(0);
						report.setBeyondSpeedCount(0);
						report.setMileage(mile);
						report.setMaxSpeed(highestSpeed);
						report.setOilCount(oil_count);
						report.setPerOilCount(per_oil_count);
						report.setIdentifier(user.getIdentifier());
						report.setGroup(groupName);
						report.setPlate(user.getPlate());
						report.setBeyondSpeedTime(speedU120);
						report.setCreateDate(DateUtil.parseDate(dateTime, "yyyy-MM-dd"));
						report.setCountTime((float)driveTime);
						report.setAccTimes(acceleration);
						report.setFatigueTimes(fatigueDriving);
						report.setBrakes(brakes);
						report.setVehicleId(user.getVehicleId());
						driverReportDao.saveCycleReport(report);
					}
				}
			}else{
				//logger.info("保存周期报表失败，原因："+obj.getString("reason"));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = format.format(date);
				if(driverReportDao.getCycleReportCount(user.getVehicleId(),dateStr) == 0){
					CycleReport report = new CycleReport();
					report.setBeyondParkCount(0);
					report.setBeyondSpeedCount(0);
					report.setMileage(0f);
					report.setMaxSpeed(0f);
					report.setOilCount(0f);
					report.setPerOilCount(0f);
					report.setIdentifier(user.getIdentifier());
					report.setGroup(groupName);
					report.setPlate(user.getPlate());
					report.setBeyondSpeedTime(0f);
					report.setCreateDate(date);
					report.setCountTime(0f);
					report.setAccTimes(0);
					report.setFatigueTimes(0);
					report.setBrakes(0);
					report.setVehicleId(user.getVehicleId());
					driverReportDao.saveCycleReport(report);
				}
				
			}
	}
	
	//获取报表数据
	/* (non-Javadoc)
	 * @see com.chanceit.http.service.IDriveReportService#report()
	 */
	@Override
	public void report() {
		// TODO Auto-generated method stub
		try{
			Date currentDate = DateUtil.getAfterDate(new Date(),0);
			Date date = DateUtil.getAfterDate(getMaxDate(), 0);
			int days = (int)((currentDate.getTime() - date.getTime())/1000/3600/24);
			System.out.println(days);
			Calendar cl = Calendar.getInstance();
			List list = userService.getList();
			Iterator<User> it = list.iterator();
			if(days == 1){
				User user = null;
				while(it.hasNext()){
					user = it.next();
					saveCycleReport(user,currentDate);
					//saveDriveReport(user,date);
				}
			}else{
				int temp = cl.get(Calendar.DAY_OF_YEAR);
				for(int i = days -2;i > -1;i--){
					cl.set(Calendar.DAY_OF_YEAR, temp-i);
					User user = null;
					while(it.hasNext()){
						user = it.next();
						saveCycleReport(user,cl.getTime());
						//saveDriveReport(user,date);
					}
				}
			}
		}catch(Exception e){
			logger.info("更新报表失败，原因："+e.getMessage());
		}
	}
	
//	//获取行驶报表数据
//	public void driveReport(){
//		try{
//			Date date = new Date();
//			Object[] values = new Object[3];
//			List list = userService.getList(values);
//			Iterator it = list.iterator();
//			User user = new User();
//			while(it.hasNext()){
//				 Map map = (Map)it.next();
//				 user = userService.get(Integer.valueOf(map.get("vehicleId").toString()));
//				 
//			}
//		}catch(Exception e){
//			logger.info("更新周期报表失败，原因："+e.getMessage());
//		}
//	}
	//保存行驶报表数据
	public void saveDriveReport(User user,Date date) throws Exception{
		String url = driveReportUrl;
		
		String beginDate = DateUtil.getDateFirstString(date);
		String endDate = DateUtil.getDateLastString(date);
		String param = "uid=" + user.getIdentifier() + 
				   	   "&getBeginDate=" + beginDate + 
				       "&getEndDate=" + endDate;
		String rec = crawlPost(url, param, "utf-8");
		
		//解析json字符串
		JSONObject obj = new JSONObject(rec);
		boolean isOK = (Boolean)obj.get("isok");
		
		float max_speed = 0.0f;
		float mileage = 0.0f;
		Location start = new Location();
		Location end = new Location();
		Team group = user.getTeam();
		String groupName = group == null?"":group.getTeamName();
		if(isOK){
			JSONArray pointArray = (JSONArray)obj.get("points");
		    for(int i = 0;i < pointArray.length();i++){  
		    	JSONObject jo = pointArray.getJSONObject(i);
		    	String x = "";
		    	String y = "";
		    	boolean base64 = (Boolean)obj.get("base64");
		    	//判断是否加密,如果加密就进行解密处理
		    	if(base64){
		    		byte[] deX = Base64.decode(jo.getString("x")); 
		    		byte[] deY = Base64.decode(jo.getString("y")); 
		    		x = new String(deX);
		    		y = new String(deY);
		    	}else{
		    		x = jo.getString("x");
		    		y = jo.getString("y");
		    	}
		    	
		    	String time = jo.getString("t");
		    	int speed = 0;
		    	int azimuth = 0;
		    	
		    	if(jo.get("speed") != null){
		    		speed = jo.getInt("speed");
		    	}
		    	//判断老数据是否有方位角
		    	if(jo.get("azimuth") != null){
		    		//方位角除以10  然后四舍五入取整
		    		Double azi = jo.getDouble("azimuth")/10;
		    		BigDecimal bd = new BigDecimal(azi).setScale(0, BigDecimal.ROUND_HALF_UP);
		    		azimuth = bd.intValue();
		    	}
		    	if(i ==0){
		    		start.setT(time);
		    		start.setX(x);
		    		start.setY(y);
		    		start.gps = x+","+y;
		    		max_speed =speed;
		    	}else if(i == pointArray.length() - 1){
		    		if(max_speed < speed){
		    			max_speed = speed;
		    		}
		    		if(driverReportDao.getDriveReportCount(user.getIdentifier(), DateUtil.getDateByMills(start.getT().getTime()), time) == 0){
//		    			List<SignInLog> list = signInLogService.getSignInLogList(user.getVehicleId(),start.getT(),DateUtil.getDate(time));
//		    			String driver = "";
//		    			for(int j=0;j<list.size();j++){
//		    				String  name = list.get(j).getDriver().getDriverName();
//		    				driver += name == null?"":name+",";
//		    			}
//		    			if(driver.indexOf(",") != -1){
//		    				driver = driver.substring(0, driver.length()-1);
//		    			}
		    			DriveReport driveReport = new DriveReport();
		    			driveReport.setIdentifier(user.getIdentifier());
		    			driveReport.setBeginAddress(start.getGps());
		    			driveReport.setEndTime(DateUtil.getDate(time));
		    			driveReport.setOverAddress(x+","+y);
//		    			driveReport.setDriver(driver);
		    			driveReport.setStartTime(start.getT());
		    			driveReport.setMaxSpeed(max_speed);
		    			driveReport.setTime_length((DateUtil.getDate(time).getTime() - start.getT().getTime())/60000.0f);
		    			driveReport.setPlate(user.getPlate());
		    			driveReport.setGroupName(groupName);
		    			//mileage = (float)BaiduMap.GetShortDistance(start.x, start.y, Double.valueOf(x),Double.valueOf(y));
		    			driveReport.setMileage(mileage/1000.0f);
		    			
		    			driverReportDao.saveDriveReport(driveReport);
		    		}
		    	}else{
		    		JSONObject jo1 = pointArray.getJSONObject(i-1);
		    		Date t = DateUtil.getDate(jo1.getString("t"));
		    		Date currT = DateUtil.getDate(jo.getString("t"));
		    		if(currT.getTime() - t.getTime() >= 300000){//超过五分钟分段
		    			if(driverReportDao.getDriveReportCount(user.getIdentifier(), DateUtil.getDateByMills(start.getT().getTime()), jo1.getString("t")) == 0){
		    				base64 = (Boolean)obj.get("base64");
					    	//判断是否加密,如果加密就进行解密处理
			    			String endX = "", endY= "";
					    	if(base64){
					    		byte[] deX = Base64.decode(jo1.getString("x")); 
					    		byte[] deY = Base64.decode(jo1.getString("y")); 
					    		endX = new String(deX);
					    		endY = new String(deY);
					    	}else{
					    		endX = jo1.getString("x");
					    		endY = jo1.getString("y");
					    	}
			    			end.setGps(endX+","+endY);
			    			end.setT(t);
			    			end.setX(endX);
			    			end.setY(endY);
			    			DriveReport driveReport = new DriveReport();
			    			List<SignInLog> list = signInLogService.getSignInLogList(user.getVehicleId(),start.getT(),DateUtil.getDate(time));
			    			String driver = "";
			    			for(int j=0;j<list.size();j++){
			    				String  name = list.get(j).getDriver().getDriverName();
			    				driver += name == null?"":name+",";
			    			}
			    			if(driver.indexOf(",") != -1){
			    				driver = driver.substring(0, driver.length()-1);
			    			}
			    			driveReport.setIdentifier(user.getIdentifier());
			    			driveReport.setBeginAddress(start.getGps());
			    			driveReport.setEndTime(t);
			    			driveReport.setOverAddress(endX+","+endY);
			    			driveReport.setStartTime(start.getT());
			    			driveReport.setMaxSpeed(max_speed);
			    			driveReport.setDriver(driver);
			    			driveReport.setTime_length((t.getTime() - start.getT().getTime())/60000.0f);
			    			driveReport.setPlate(user.getPlate());
			    			driveReport.setGroupName(groupName);
			    			//mileage = (float)BaiduMap.GetShortDistance(start.x, start.y, end.x, end.y);
			    			driveReport.setMileage(mileage/1000.0f);
			    			
			    			driverReportDao.saveDriveReport(driveReport);
		    			}
		    			start.setT(time);
			    		start.setX(x);
			    		start.setY(y);
			    		start.gps = x+","+y;
			    		max_speed =speed;
		    		}else{
		    			if(max_speed < speed){
		    				max_speed = speed;
		    			}
		    		}
		    	}
		    	Report report = new Report();
		    	report.setAzimuth(azimuth);
		    	report.setDate(DateUtil.getDate(time));
		    	report.setX(x);
		    	report.setY(y);
		    	report.setGroup(groupName);
		    	report.setIdentifier(user.getIdentifier());
		    	report.setPlate(user.getPlate());
		    	report.setSpeed((float)speed);
		    	SignInLog inLog = signInLogService.getSignInLog(user.getVehicleId(),DateUtil.getDate(time));
		    	if(inLog != null){
		    		report.setDriver(inLog.getDriver().getDriverName());
		    	}
		    	driverReportDao.saveReport(report);
		    	
		    }
		}
	}
//	/*
//	 * flag  -7代表近七天 -3代表近3天 -1代表昨天 0代表今天 
//	 */
//	public List getCycleReport(String vehicleIds,String flag){
//		Date currentDate = new Date();
//		String startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag), "yyyy-MM-dd");
//		String endTime = DateUtil.getAfterDate(currentDate,0,"yyyy-MM-dd");
//		String sql = "select c.vehicle_id as vehicle_id,c.plate as plate,c.group_name as team,c.driver as driver,sum(c.mileage) as mileage,sum(c.oil_count) as fuel" +
//				",sum(c.beyond_speed_time) as overspeedhours,concat(sum(c.acc_times),'/',sum(c.brakes)) as acctimes,c.fatigue_times as sleepytimes from cycle_report c where c.vehicle_id in ("+vehicleIds+") and (c.create_date >='+"+startTime+"' and c.create_date <='"+endTime+"') group by c.vehicle_id";
//		return driverReportDao.getCycleReport(sql);
//	}
	
	/*
	 *  startTime 起始时间
	 *  endTime 结束时间
	 */
	@Override
	public List getCycleReport(String vehicleIds,String startTime,String endTime,String oilper,String plate){
		String sql = "select c.vehicle_id as vehicleId,c.plate as plate,c.group_name as team,c.driver as driver,sum(c.mileage) as mileage,sum(c.oil_count) as fuel" +
				",sum(c.beyond_speed_time) as overspeedhours,concat(sum(c.acc_times),'/',sum(c.brakes)) as acctimes,sum(c.fatigue_times) as sleepytimes from cycle_report c where (c.create_date >='"+startTime+"' and c.create_date <='"+endTime+"') ";
		if(StringUtils.isNotBlank(vehicleIds)){
			sql += " and c.vehicle_id in ("+vehicleIds+") ";
		}
		if(StringUtils.isNotBlank(plate)){
			sql += " and c.plate = '"+plate+"'";
		}
		sql +=" group by c.vehicle_id";
		if(StringUtils.isNotBlank(oilper)){
			sql +=" HAVING sum(c.oil_count)/sum(c.mileage)*100>="+oilper;
		}
		return driverReportDao.getCycleReport(sql);
	}
	
	/*
	 *  startTime 起始时间
	 *  endTime 结束时间
	 */
	@Override
	public List getCycleReportMileageOil(String vehicleIds,String startTime,String endTime,String oilper,String plate){
		String sql = "select date_format(c.create_date,'%Y-%m-%d') as date,c.identifier as identifier,c.group_name as team,c.plate as plate,c.vehicle_id as vehicleId, c.mileage as mileage,c.oil_count as fuel" +
				" ,c.beyond_speed_time as overspeedhours,concat(c.acc_times,'/',c.brakes) as acctimes,c.fatigue_times as sleepytimes from cycle_report c where (c.create_date >='"+startTime+"' and c.create_date <='"+endTime+"')";
		if(StringUtils.isNotBlank(vehicleIds)){
			sql += " and c.vehicle_id in ("+vehicleIds+") ";
		}
		if(StringUtils.isNotBlank(plate)){
			sql += " and c.plate = '"+plate+"'";
		}
		sql+=" group by c.vehicle_id,c.create_date";
		if(StringUtils.isNotBlank(oilper)){
			sql +=" HAVING sum(c.oil_count)/sum(c.mileage)*100>="+oilper;
		}		
		return driverReportDao.getCycleReportMileageOil(sql);
	}
	
	/*
	 * 按月统计里程和油耗 
	 */
	@Override
	public List getCycleReportMileageOilByMonth(String vehicleIds,String year,String month,String oilper,String plate){
		String startTime = DateUtil.getFirstDayOfMonth(year+"-"+month);
		String endTime = DateUtil.getLastDay(year+"-"+month);
		String sql = "select c.group_name as team,c.driver as driver,c.plate as plate,c.vehicle_id as vehicleId,sum(c.mileage) as mileage,sum(c.oil_count) as fuel," +
				" concat(sum(c.acc_times),'/',sum(c.brakes)) as acctimes,sum(c.beyond_speed_time) as overspeedhours,sum(c.fatigue_times) as sleepytimes from cycle_report c where (c.create_date >='"+startTime+"' and c.create_date <='"+endTime+"') ";
		if(StringUtils.isNotBlank(vehicleIds)){
			sql += " AND c.vehicle_id in ("+vehicleIds+") ";
		}
		if(StringUtils.isNotBlank(plate)){
			sql += " and c.plate = '"+plate+"'";
		}
		sql +=" group by c.vehicle_id";		
		if(StringUtils.isNotBlank(oilper)){
			sql +=" HAVING sum(c.oil_count)/sum(c.mileage)*100>="+oilper;
		}		
				
		return driverReportDao.getCycleReportMileageOil(sql);
	}
	
//	/*
//	 * flag  -7代表近七天 -3代表近3天 -1代表昨天 0代表今天 
//	 */
//	public List getCycleReportMileageOil1(String vehicleId,String flag){
//		Date currentDate = new Date();
//		String startTime = DateUtil.getAfterDate(currentDate, Integer.valueOf(flag), "yyyy-MM-dd");
//		String endTime = DateUtil.getAfterDate(currentDate,0,"yyyy-MM-dd");
//		String sql = "select date_format(c.create_date,'%Y-%m-%d') as create_date,c.vehicle_id as vehicle_id,c.group_name as team,c.plate as plate,c.identifier as identifier, c.mileage as mileage,c.oil_count as fuel" +
//				" ,c.beyond_speed_time as overspeedhours,concat(c.acc_times,'/',c.brakes) as acctimes,c.fatigue_times as sleepytimes from cycle_report c where c.vehicle_id in ("+vehicleId+") and (c.create_date >='"+startTime+"' and c.create_date <='"+endTime+"') group by c.vehicle_id";
//		return driverReportDao.getCycleReportMileageOil(sql);
//	}
	
	private Date getMaxDate(){
		return driverReportDao.getMaxDate();
	}
	public String getCycleReportUrl() {
		return cycleReportUrl;
	}
	public void setCycleReportUrl(String cycleReportUrl) {
		this.cycleReportUrl = cycleReportUrl;
	}
	
	public class Location{
		public Date t;
		public String gps;
		public double x;
		public double y;
		public Date getT() {
			return t;
		}
		public void setT(String dateSource) throws ParseException {
			this.t = DateUtil.getDate(dateSource);
		}
		public String getGps() {
			return gps;
		}
		public void setGps(String gps) {
			this.gps = gps;
		}
		public double getX() {
			return x;
		}
		public void setX(String x) {
			this.x = Double.valueOf(x);
		}
		
		public void setT(Date d){
			this.t = d;
		}
		public double getY() {
			return y;
		}
		public void setY(String y) {
			this.y = Double.valueOf(y);
		}
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
			logger.error("访问"+url + "失败！原因:"+e.getMessage());
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
