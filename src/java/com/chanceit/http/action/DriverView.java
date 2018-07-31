/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.client.Client;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.ClientFactory;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.Member;
import com.chanceit.http.pojo.SignInLog;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ISignInLogService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @ClassName AccountView
 * @author zhangxin 2014-08-13
 * @Description 账号管理相关接口
 */
@Component("driverView")
public class DriverView extends BaseAction {
	private String charSet="UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	@Autowired
	@Qualifier("driverService")
	private IDriverService driverService;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	
	@Autowired
	@Qualifier("signInLogService")
	private ISignInLogService signInLogService;
	
	@Autowired
	@Qualifier("opTeamService")
	private IOperatorTeamService opTeamService;
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	//轨迹回放url
	private String pointUrl;
	
	private String phoneUrl;
	
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	public String saveDriver() throws Exception{
		String driverName = getParameter("driverName");
		String driverTel = getParameter("driverTel"); //12,12
		String license = getParameter("license");//33,4
		String remark = getParameter("remark");
		String DriverId = getParameter("driverId");
		String zjcx = getParameter("zjcx");
		String loginName = getParameter("userName");
		String password = getParameter("pwd");
		String cardNo = getParameter("cardNo");
		if(StringUtils.isBlank(driverName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入司机名称");
		//Account account = getSessionAccount();
		//String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
		if(StringUtils.isBlank(DriverId)){//add
			//查询是否有重名,有重名添加序号后缀,序号为尾数依次加1
			List driverList = driverService.getExistNameList(loginName);
			if(driverList.size()>0) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"司机登录名已存在");
			Driver driver = new Driver();
			driver.setTeamId(account.getTeam().getTeamId());//添加司机的teamId为添加人对应的teamId
			driver.setDriverName(driverName);
			driver.setDriverTel(driverTel);
			driver.setLicense(license);
			driver.setRemark(remark);
			driver.setCreateTime(new Date());
			driver.setZjcx(zjcx);
			driver.setUserName(loginName);
			driver.setPwd(password);
			driver.setStatus(1);
			driver.setCardNo(cardNo);
			DriverId = driverService.save(driver);
			return ResultManager.getSuccResult();
			 
		}else{//edit
			Driver driver =	driverService.get(Integer.parseInt(DriverId));
			if(driver==null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在此id！");
			}
			driver.setDriverName(driverName);
			driver.setDriverTel(driverTel);
			driver.setLicense(license);
			driver.setRemark(remark);
			driver.setZjcx(zjcx);
			if(!StringUtils.isBlank(cardNo)){
				driver.setCardNo(cardNo);
				}
			
			if(!StringUtils.isBlank(loginName)){
				driver.setUserName(loginName);
			}
			
          if(!StringUtils.isBlank(password)){
	          driver.setPwd(password);
			}
			driverService.saveDriver(driver);
			return ResultManager.getSuccResult();
		}
	}
	
	
	public String delete() throws Exception{
		String DriverIds = getParameter("driverIds");
		if(StringUtils.isBlank(DriverIds)){
			return ResultManager.getFaildResult("请输入需要删除的账号ID，以英文的逗号区分");
		}
//		if(driverService.ifBind(DriverIds)){
//			return ResultManager.getFaildResult("请先解除该司机与车辆的绑定！");
//		}
		Account account = getSessionAccount();
		driverService.deleteDriver(DriverIds);
		return ResultManager.getSuccResult();
	}
	
	public String list(Page page){
		String driverName = getParameter("driverName");//搜索：司机姓名
		String ss_tel=getParameter("ss_tel"); //搜索：联系电话
		String type = getParameter("type");
		String status = getParameter("status");
		Account account = getSessionAccount();
		String teamIds =account.getTeam().getTeamId()+"";
//		if(account.getLevel().getLevelId()!=1){
//			teamIds = account.getTeam().getTeamId()+"";
//		}
		Object[] keywords = new Object[]{teamIds,driverName,1,ss_tel};
		if(!StringUtils.isBlank(status)){
			keywords = new Object[]{teamIds,driverName,0,ss_tel};
		}
		if(StringUtils.isBlank(type)){
			List list = driverService.getList(keywords);
			return ResultManager.getBodyResult(list);
		}else{
			page = driverService.getPageList(page, keywords);
			return ResultManager.getBodyResult(page);
		}
		
	}


	public String unbindDrivers() {
		String plate = getParameter("driverName");
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
		Object[] keywords = new Object[]{account.getTeam().getTeamId(),plate};
		List list = driverService.getUnbindList(keywords);
		return ResultManager.getBodyResult(list);
	}
	/**
	 * 获取司机考勤日志
	 * @param page
	 * @return
	 */
	public String getDriverLogPage(Page page){
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		String driverId = getParameter("driverId");
		//操作员权限只获取自身有权限的车队ID
		Object[] keywords ;
		if(account.getRole().getRoleId() == 3){
			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{driverId,accountId,teamIds};
		}else{
			keywords = new Object[]{driverId,accountId,null};
		}
		page = driverService.getDriverLogPage(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	public String getCarList(){
		String driverId = getParameter("driverId");
		Object[] keywords = new Object[]{driverId};
		List list = driverService.getCar(keywords);
		if(list.size()>0){
			return ResultManager.getBodyResult(list);
		}
		return ResultManager.getSuccResult();
	}
	
	/**
	 * 获取位置点信息
	 */
	public String getPoint() throws Exception{
		String driverId = getParameter("driverId");
		String strDate = getParameter("date")+" 00:00:00"; 
		String endDate = addDate(strDate, 1)+" 00:00:00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Object[] keywords = new Object[]{Integer.parseInt(driverId),dateFormat.parse(strDate),dateFormat.parse(endDate)};
		List<SignInLog> list = signInLogService.getSignInLog(keywords);
		
		List gps = new ArrayList();
		for(SignInLog log:list){
			String plate = log.getCar().getPlate();
			int vehicleId = log.getCar().getVehicleId();
			String signintime = "";
			String unbindtime = "";
			if(log.getUnbindTime() == null){
				signintime = strDate;
				unbindtime =  endDate;
			}else if(log.getUnbindTime().before(dateFormat.parse(strDate))){
				signintime = strDate;
				unbindtime =  dateFormat.format(log.getUnbindTime());
			}else{
				signintime = dateFormat.format(log.getSignInTime());
				unbindtime = dateFormat.format(log.getUnbindTime());
			}
			String firstDate = signintime.replace("-", "").replace(":", "").replace(" ", "");
			
			String lastDate = unbindtime.replace("-", "").replace(":", "").replace(" ", "");
			
			User user = userService.get(vehicleId);
			String identifier = user.getIdentifier();
			
			//String uid = "153206344";
			//String uid = "253039860";
			String url = pointUrl;
			String param = "uid=" + identifier + 
					   	   "&getBeginDate=" + firstDate + 
					       "&getEndDate=" + lastDate;
			String rec = crawlPost(url, param, "utf-8");
			
			//解析json字符串
			JSONObject obj = new JSONObject(rec);
			boolean isOK = (Boolean)obj.get("isok");
			
			if(isOK){
				JSONArray pointArray = (JSONArray)obj.get("points");
				System.out.println(pointArray);
				//gps = new Object[1][pointArray.length()];
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
			    	//判断老数据是否有方位角
			    	if(jo.get("speed") != null){
			    		speed = jo.getInt("speed");
			    	}
			    	if(jo.get("azimuth") != null){
			    		//方位角除以10  然后四舍五入取整
			    		Double azi = jo.getDouble("azimuth")/10;
			    		BigDecimal bd = new BigDecimal(azi).setScale(0, BigDecimal.ROUND_HALF_UP);
			    		azimuth = bd.intValue();
			    	}
			    	
			    	Object[] pointObj = new Object[]{x,y,time,speed,azimuth,plate};
			    	gps.add(pointObj);
			    }
			}
		}
		return ResultManager.getBodyResult(gps);
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
	/**
	 * 日期 加 天数
	 * add by zhangxin 2014-07-16
	 * @param strDate
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static String addDate(String strDate, int days) throws ParseException{
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dft.parse(strDate);
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		// 日期加一天为结束日期
		ca.add(Calendar.DATE, days);
		
		Date eDate = ca.getTime();
		String addDate = dft.format(eDate);
		return addDate;
	}
	
	public String editDriver() throws Exception {
		String uid = getParameter("id");
		String type = getParameter("type");
		String status = getParameter("status");
			Driver d = driverService.get(Integer.parseInt(uid));
			if("-1".equals(status)){
				d.setStatus(-1);
			}else if("1".equals(status)){
				d.setStatus(1);
			}
			driverService.update(d);
		

			return ResultManager.getSuccResult();

	}
	
	/**
	 * 获取异常考勤列表
	 */
	public String getAbnormaDriverLogPage(Page page) throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(); 
		String strDate = dateFormat.format(date);
		String endDate = addDate(strDate, 1);
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		
		Object[] keywords;
		//操作员权限只获取自身有权限的车队ID
		if(account.getRole().getRoleId() == 3){
			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{strDate,endDate,accountId,teamIds};
		}else{
			keywords = new Object[]{strDate,endDate,accountId,null};
		}
		page = driverService.getAbnormaDriverLogPage(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	/**
	 * 获取异常考勤总数
	 * @return
	 * @throws Exception
	 */
	public String getAbnormalDriverLogCount() throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(); 
		String strDate = dateFormat.format(date);
		String endDate = addDate(strDate, 1);
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		String teamIds = teamService.getTeamIdStr(account);
		Object[] keywords;
		//操作员权限只获取自身有权限的车队ID
//		if(account.getRole().getRoleId() == 3){
//			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{strDate,endDate,accountId,teamIds};
//		}else{
//			keywords = new Object[]{strDate,endDate,accountId,null};
//		}
		return driverService.getAbnormalDriverLogCount(keywords);
	}
	
	public String phoneOrder() throws Exception{
		String identifier = getParameter("identifier");
		Client client = ClientFactory.getClient(phoneUrl);
		Object[] objs = new Object[1];
		objs[0] = identifier;
		String result = "["+ client.invoke("getPhone", objs)[0].toString()+"]";
		return result;
	}
	
	public String resetDriver() throws Exception{
		String ids=getParameter("driverIds");
		driverService.resetDriver(ids);
		return ResultManager.getSuccResult();
	}
	
	
	public String getPointUrl() {
		return pointUrl;
	}


	public void setPointUrl(String pointUrl) {
		this.pointUrl = pointUrl;
	}


	public String getPhoneUrl() {
		return phoneUrl;
	}


	public void setPhoneUrl(String phoneUrl) {
		this.phoneUrl = phoneUrl;
	}
}
