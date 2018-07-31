package com.chanceit.http.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Coder;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.JedisService;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.SignInLog;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IBoxreminderService;
import com.chanceit.http.service.ICarDriverService;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.IGuanHuiService;
import com.chanceit.http.service.ILevelService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ISignInLogService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.chanceit.http.service.IWarnService;
import com.chanceit.http.service.IpcodeCensusService;
import com.google.gson.Gson;

/**
 * @ClassName AccountView
 * @author Administrator
 * @date Jun 26, 2013 2:28:41 PM
 * @Description 账号管理相关接口
 */
@Component("appView")
public class AppView extends BaseAction {
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;

	@Autowired
	@Qualifier("levelService")
	private ILevelService levelService;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("opTeamService")
	private IOperatorTeamService opTeamService;

	@Autowired
	@Qualifier("pcodeCensusService")
	private IpcodeCensusService pcodeCensusService;
	@Autowired
	@Qualifier("boxreminderService")
	private IBoxreminderService boxreminderService;

	private final int verificationCount = 4;


	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;

	@Autowired
	@Qualifier("jedisService")
	private JedisService jedisService;
	@Autowired
	@Qualifier("driverService")
	private IDriverService driverService;

	@Autowired
	@Qualifier("signInLogService")
	private ISignInLogService signInLogService;

	@Autowired
	@Qualifier("warnService")
	private IWarnService warnService;
	@Autowired
	@Qualifier("carDriverService")
	private ICarDriverService carDriverService;
	@Autowired
	@Qualifier("guanhuiService")
	private IGuanHuiService guanhuiService;
	/**
	 * add by gwt 2014-12-26
	 * 
	 * @Description 账号登陆
	 */
	public String loginForApp() throws Exception {

		String accountName = getParameterNoCheck("account");
		String password = getParameterNoCheck("pwd");
		Account account = accountService.getByName(accountName);
		if (account != null
				&& account.getAccountPwd().equals(Coder.encryptMD5(password))) {
			List<User> listUser = userService.getList1(account.getAccountId()
					+ "");
			String uidss = "";
			for (int i = 0; i < listUser.size(); i++) {
				User u = listUser.get(i);
				if (i == 0) {
					uidss = u.getIdentifier();
				} else {
					uidss += "," + u.getIdentifier();
				}
			}
			account.setVisitIp(getRequest().getRemoteAddr());
			account.setVisitTime(new Date());
			account.setLogintimes(account.getLogintimes() + 1);
			accountService.save(account);
			JSONObject json = new JSONObject();
			json.put("accountId", account.getAccountId());
			json.put("roleId", account.getRole().getRoleId());
			json.put("parentId", account.getTeam().getPid());
			json.put("companyName", account.getTeam().getTeamName());
			json.put("address", account.getAddress());
			json.put("phone", account.getAccountTel());
			json.put("uids", uidss);
			//增加teamId
			json.put("teamId",account.getTeam().getTeamId());
			String data = json.toString();
			return ResultManager.getResultForApp(true, data);
		}
		return ResultManager.getResultForApp(false, "账号名或者密码错误");
	}

	public String getTeams() throws Exception {
//		String teamId = getParameter("teamId");
		String accountId = getParameter("accountId");
		Account account = accountService.get(Integer.parseInt(accountId));
//		account.setAccountId(6);
//		Team t = new Team();
//		t.setTeamId(36);
//		account.setTeam(t);
		return teamService.getJson(account,
				Integer.parseInt(accountId), 1)
				.toString();
	}

	
	/**
	 * @des 车队列表
	 * @return
	 * @throws Exception
	 */
	public String listForApp() throws Exception {
		String teamName = getParameter("teamName");
		String accountId = getParameter("accountId");
		Object[] keywords = new Object[] { accountId, teamName };
		// List list = teamService.getList(keywords);
		List list = teamService.getTeamListForApp(Integer.parseInt(accountId));
		JSONObject obj = new JSONObject();
		obj.put("isok", true);
		obj.put("result", ResultManager.getBodyResult(list));
		return obj.toString();
	}

	/**
	 * @des 监控的车辆列表
	 * @return
	 * @throws Exception
	 */
	public String monitorListForApp() throws Exception {
		String accountId = getParameter("accountId");
//		String teamId = getParameterNoCheck("teamId");
		String plate = getParameterNoCheck("plate");
		String roleId = getParameter("roleId");
		Account account = accountService.get(Integer.parseInt(accountId));
			//String teamids=teamService.getTeamIdStr(account);
			String teamids=account.getTeam().getTeamId()+"";
			Object[] keywords = new Object[]{null,plate,teamids};
//			String data = ResultManager.getBodyResult(userService
//					.getList(keywords));
//			return ResultManager.getResultForApp(true, data);
			Map<String,Short> map = new HashMap<String,Short>();
			;
			List list=userService.getList(keywords);
			String uids = "";
			for (int i = 0; i < list.size(); i++) {
				User u = (User) list.get(i);
				if(i == 0){
					uids += u.getIdentifier();
				}else{
					uids += ","+u.getIdentifier();
				}
			}
			
			try {
				String res = jedisService.getLocationByUidsMe(uids);
				//System.out.println("*********"+res);
				if(res != null && res != ""){
				
					JSONObject json = new JSONObject(res);
					String uidsc = json.getString("uids");
					JSONArray array = (JSONArray) json.get("res");
					
					for (int i = 0; i < array.length(); i++) {
						JSONObject json_tem = array.getJSONObject(i);
						String uid = json_tem.getString("uid");
					    int online = json_tem.getInt("online");
					    map.put(uid, (short)online);
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			for(int i = 0; i < list.size(); i++){
				User u = (User) list.get(i);
				if(map.containsKey(u.getIdentifier())){
					u.setIfOff(map.get(u.getIdentifier()));
				}else{
					u.setIfOff((short)-1);	
				}
			}
			
			JSONObject obj = new JSONObject();
			obj.put("isok", true);
			obj.put("result", ResultManager.getBodyResult(list));
			return obj.toString();
	}

	/**
	 * @des 当前车队分组车辆
	 * @return
	 * @throws Exception
	 *             add by gwt 2017-12-18
	 */
	public String getListByTeamId() throws Exception {
		String teamId = getParameterNoCheck("teamId");
		//String accountId = getParameter("accountId");
		List list = new ArrayList();
		Map<String,Short> map = new HashMap<String,Short>();
		;
//		if (StringUtils.isBlank(teamId)) {
//			list = userService.getList(accountId, 0);
//		} else {
//			list = userService.getListById(accountId, Integer.parseInt(teamId));
//		}
		list=userService.getList2(Integer.parseInt(teamId), teamId);
		String uids = "";
		for (int i = 0; i < list.size(); i++) {
			User u = (User) list.get(i);
			if(i == 0){
				uids += u.getIdentifier();
			}else{
				uids += ","+u.getIdentifier();
			}
		}
		
		try {
			String res = jedisService.getLocationByUidsMe(uids);
			//System.out.println("*********"+res);
			if(res != null && res != ""){
			
				JSONObject json = new JSONObject(res);
				String uidsc = json.getString("uids");
//				List<User> list = userService.getUserListToMap(uids);
//				for(int i=0;i<list.size();i++){
//					User u = list.get(i);
//					if(u != null){
//					 map.put(u.getIdentifier(), u);	
//					}
//				}
				JSONArray array = (JSONArray) json.get("res");
				//toatal =listUser.size();
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject json_tem = array.getJSONObject(i);
					String uid = json_tem.getString("uid");
				    int online = json_tem.getInt("online");
				    map.put(uid, (short)online);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		for(int i = 0; i < list.size(); i++){
			User u = (User) list.get(i);
			if(map.containsKey(u.getIdentifier())){
				u.setIfOff(map.get(u.getIdentifier()));
			}else{
				u.setIfOff((short)-1);	
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("isok", true);
		obj.put("result", ResultManager.getBodyResult(list));
		return obj.toString();
	}

	//
	/**
	 * @return
	 * @throws Exception
	 *             未分组车辆 add by gwt
	 */
	public String getListNo() throws Exception {
		String accountId = getParameter("accountId");
		// Account account = getSessionAccount();
		// if(account.getRole().getRoleId() != 2){
		// accountId = getCompanyId();
		// }else{
		// accountId = account.getAccountId().toString();
		// }
		List list = new ArrayList();
		;
		list = userService.getList(accountId, 0);
		JSONObject obj = new JSONObject();
		obj.put("isok", true);
		obj.put("result", ResultManager.getBodyResult(list));
		return obj.toString();
	}

	/**
	 * @author Administrator
	 * @date 2014-12-18
	 * @return
	 * @Description 显示车辆详情 add by gwt
	 */
	public String viewForApp() throws Exception {
		String userId = getParameter("vehicleId");

		User users = null;
		JSONObject obj = new JSONObject();
		if (StringUtils.isNotEmpty(userId)) {
			users = userService.get(Integer.parseInt(userId));
		//	String coorInfo = jedisService.getUidInfo(users.getIdentifier());
//			if (coorInfo != null && coorInfo != "") {
//				JSONObject coorJson = new JSONObject(coorInfo);
//				obj.put("location", coorJson);
//			}
			obj.put("isok", true);
			obj.put("result", ResultManager.getBodyResult(users));
		} else {
			obj.put("isok", false);
			obj.put("result", ResultManager.getFaildResult("无效id"));
		}
		return obj.toString();
	}

	public String getGroupList() {
		String accountId = getParameter("accountId");
		// Account account = getSessionAccount();
		// if(account.getRole().getRoleId() != 2){
		// accountId = getCompanyId();
		// }else{
		// accountId = account.getAccountId().toString();
		// }
		JSONObject json = new JSONObject();
		boolean isok = false;
		try {
			String operatorId = "0";
			isok = true;
			json.put("array", teamService.getMapForApp(Integer
					.parseInt(accountId)));
		} catch (Exception e) {

		}
		try {
			json.put("isok", isok);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	public String getWaringList() {
		String userId = getParameter("userId");
		String type = getParameter("type");
		String time = getParameter("time");
		Object[] keywords = new Object[] { userId, time, null };
		String res = "";
		JSONObject obj = new JSONObject();
		try {
			List list = boxreminderService.getWarningForApp(keywords);

			obj.put("isok", true);
			obj.put("result", ResultManager.getBodyResult(list));
			res = obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			res = "{\"isok\":false}";
		} catch (Exception ee) {
			res = "{\"isok\":false}";
		}
		return res;
	}

	public String getWaringGroupList() {

		String accountId = getParameter("accountId");
		Account account=accountService.get(Integer.parseInt(accountId));
		String teamIds=teamService.getTeamIdStr(account);
		String time = getParameter("time");
		Object[] keywords = new Object[] { teamIds, time, "1,2,5,6,7,8,16" };
		String res = "";
		JSONObject obj = new JSONObject();
		try {
			List list = boxreminderService.getCountGroupWarn2App2(keywords);

			obj.put("isok", true);
			obj.put("result", ResultManager.getBodyResult(list));
			res = obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			res = "{\"isok\":false}";
		} catch (Exception ee) {
			res = "{\"isok\":false}";
		}
		return res;
	}

	
	public String addTeam() throws Exception{
		//update by zhangxin 2014-8-18
		//管理员或超级管理员权限才能操作
		//if(account.getParentId()!= 0){
//		if(account.getRole().getRoleId() == 3){
//			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"您无权执行该操作");
//		}
		String accountId=getParameter("accountId");
		String teamName = getParameter("teamName");
		String teamId = getParameter("teamId");
		String binds = getParameter("bindIds"); //12,12
//		String unbinds = getParameter("unbinds");//33,4
		String order = getParameter("order");
		int amount=0;
		Account account = accountService.get(Integer.parseInt(accountId));
		if(StringUtils.isNotBlank(binds)){
			amount  = binds.split(",").length;
		}
		if(StringUtils.isBlank(teamName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入车队名称");
		if(StringUtils.isBlank(teamId)){//add
			Team team = new Team();
			team.setAmount(amount);
			team.setTeamName(teamName);
			team.setCreateTime(new Date());
			team.setAccountId(account.getAccountId());
			team.setPid(account.getTeam().getTeamId());
			if(StringUtils.isNotBlank(order)){
				team.setOrder(Integer.parseInt(order));
			}
			teamId = teamService.save(team);
			if(StringUtils.isNotBlank(teamId)){
				return "{\"isok\":true}";
			}else{
				return "{\"isok\":false}";
			}
			 
		}else{//edit
			Team team =	teamService.get(Integer.parseInt(teamId));
			if(team==null){
				return "{\"isok\":false}";
			}
			team.setAmount(amount);
			team.setTeamName(teamName);
			team.setOrder(Integer.parseInt(order));
			if(teamService.saveTeam(team)){
				return "{\"isok\":true}";
			}else{
				return "{\"isok\":false}";
			}
		}
	}

	public String deleteTeam() throws Exception{
		String teamIds = getParameter("teamIds");
		if(StringUtils.isBlank(teamIds)){
			return ResultManager.getFaildResult("请输入需要删除的账号ID，以英文的逗号区分");
		}
		if(teamService.ifBind(teamIds)){
			return ResultManager.getFaildResult("请先解除该车队下绑定的车辆！");
		}
		Account account = getSessionAccount();
		teamService.deleteTeam(teamIds,account.getAccountId());
		opTeamService.deleteByTeam(Integer.parseInt(teamIds));
//		logService.save(EnumCommon.delete_log, userIds+"账户列表已经被删除");
		return ResultManager.getSuccResult();
	}
	
	
	/**
	 * 未绑定的司机
	 */
	public String unbindDrivers() {
		String plate = getParameter("driverName");
		String teamId = getParameter("teamId");
		Object[] keywords = new Object[] { teamId, plate };
		List list = driverService.getUnbindList(keywords);
		JSONObject json = new JSONObject();
		try {
			json.put("isok", true);
			json.put("result", ResultManager.getBodyResult(list));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	public String addCars() throws Exception {
		String accountId = getParameter("accountId");
		String identifier = getParameter("identifier");
		String plate = getParameter("plate");
		String type = getParameter("type");
		String driverId = getParameter("driverId");
		String discc = getParameter("discc");
		String teamId = getParameter("teamId");
		String ifRelay = getParameter("ifRelay");
		String cutpwd = getParameter("cutpwd");
		String ifMonitor = getParameterNoCheck("ifMonitor");
		String userId = getParameter("userId");

		JSONObject json = new JSONObject();

		// String accountId = getParameter("accountId");
		User user = null;
		if (StringUtils.isBlank(identifier)) {
			json.put("isok", false);
			json.put("result", "识别码必须填写");
			return json.toString();
		}
		if (!checkIdentifier(identifier)) {
			json.put("isok", false);
			json.put("result", "请输入正确的设备识别码");
			return json.toString();
		}
		if (StringUtils.isEmpty(plate)) {
			json.put("isok", false);
			json.put("result", "车牌必须填写");
			return json.toString();
		}
//		if (!StringUtils.isEmpty(discc)) {
//			String ss = userService.httpSave(identifier, discc);
//			JSONObject js = new JSONObject(ss);
//
//			if (js.has("code")) {
//				json.put("isok", false);
//				json.put("result", "服务器异常，上传排量失败");
//				return json.toString();
//			}
//		}
		
		if(StringUtils.isNotBlank(userId)){//表示更新
			user = userService.get(Integer.parseInt(userId));
			if(user == null) {
				json.put("isok", false);
				json.put("result", "系统出错");
				return json.toString();
			}
			user.setAccount(accountService.get(Integer.parseInt(accountId)));
			//user.setIdentifier(identifier);
			user.setPlate(plate);
			if(StringUtils.isNotBlank(type) ){
				user.setType(Integer.parseInt(type));
			}
			if(StringUtils.isNotBlank(driverId)){
				Driver driver = driverService.get(Integer.parseInt(driverId));
				//Driver driver = new Driver();
				//driver.setDriverId(Integer.parseInt(driverId));
				user.setDriver(driver);
			}else{
				user.setDriver(null);
			}
			if(StringUtils.isNotBlank(ifMonitor)){
				user.setIfMonitor(Short.parseShort(ifMonitor));
			}
			user.setUpTime(new Date());
			user.setIfRelay(Short.parseShort(ifRelay));
			if(cutpwd !=null && !"".equals(cutpwd)){
				user.setCutpwd(cutpwd);
			}else{
				user.setCutpwd(null);
			}
			
			if(!StringUtils.isEmpty(discc)){
				user.setDiscc(discc);
			}
//			if(!StringUtils.isEmpty(teamId) && !teamId.equals("0")){
//				Team team = teamService.get(Integer.parseInt(teamId));
//				user.setTeam(team);
//			}else{
//				user.setTeam(null);
//			}
			userService.save(user);
			
		} 
		else{//表示新增
		
		user = new User();
		

		if (StringUtils.isEmpty(identifier)) {
			json.put("isok", false);
			json.put("result", "识别码必须填写");
			return json.toString();
		}

		if (userService.ifExist(identifier, Integer.parseInt(accountId), null)) {
			json.put("isok", false);
			json.put("result", "该识别码已存在");
			return json.toString();
		}

		if (userService.ifExistPlate(plate, Integer.parseInt(accountId), null)) {
			json.put("isok", false);
			json.put("result", "该车牌已存在");
			return json.toString();
		}
		// user = userService.getPointUser(identifier,user);
		// if(user==null){
		// throw new CommonException("该车尚无位置点");
		// }
		user.setAccount(accountService.get(Integer.parseInt(accountId)));
		user.setIdentifier(identifier);
		user.setPlate(plate);
		if (StringUtils.isNotBlank(type)) {
			user.setType(Integer.parseInt(type));
		}

		if (StringUtils.isNotBlank(driverId)) {
			Driver driver = driverService.get(Integer.parseInt(driverId));
			//Driver driver = new Driver();
			//driver.setDriverId(Integer.parseInt(driverId));
			user.setDriver(driver);
		}
		if (StringUtils.isNotBlank(ifMonitor)) {
			user.setIfMonitor(Short.parseShort(ifMonitor));
		}
		user.setCreateTime(new Date());
		user.setIfDel((short) 0);
		user.setIfRelay(Short.parseShort(ifRelay));
		if (cutpwd != null && !"".equals(cutpwd)) {
			user.setCutpwd(cutpwd);
		} else {
			user.setCutpwd(null);
		}
		if (!StringUtils.isEmpty(discc)) {
			user.setDiscc(discc);
		}
		if (!StringUtils.isEmpty(teamId) && !teamId.equals("0")) {
			Team team = teamService.get(Integer.parseInt(teamId));
			user.setTeam(team);
		} else {
			user.setTeam(null);
		}
		userService.save(user);

	 }
		json.put("isok", true);
		return json.toString();
	}

	public String getExceptionList() throws Exception {
		String accountId = getParameter("accountId");
		String time = getParameter("time");
		Object[] keywords = new Object[] { accountId, time, null };
		String res = "";
		JSONObject obj = new JSONObject();
		try {
			List list = warnService.getCountGroupWarn2App(keywords);

			obj.put("isok", true);
			obj.put("result", ResultManager.getBodyResult(list));
			res = obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			res = "{\"isok\":false}";
		} catch (Exception ee) {
			ee.printStackTrace();
			res = "{\"isok\":false}";
		}
		return res;
	}

	public String getExceptionItemList() {
		String userId = getParameter("userId");
		String accountId = getParameter("accountId");
		Object[] keywords = new Object[] { accountId, userId, null };
		String res = "";
		JSONObject obj = new JSONObject();
		try {
			List list = warnService.getListWarn(keywords);

			obj.put("isok", true);
			obj.put("result", ResultManager.getBodyResult(list));
			res = obj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			res = "{\"isok\":false}";
		} catch (Exception ee) {
			res = "{\"isok\":false}";
		}
		return res;
	}

	/**
	 * @return 添加司机
	 * @throws Exception
	 */
	public String saveDriver() throws Exception{
		String teamId=getParameter("teamId");
		String driverName = getParameter("driverName");
		String driverTel = getParameter("driverTel"); //12,12
		String license = getParameter("license");//33,4
		String remark = getParameter("remark");
		String DriverId = getParameter("driverId");
		String zjcx = getParameter("zjcx");
		String loginName = getParameter("userName");
		String password = getParameter("pwd");
		String cardNo = getParameter("cardNo");
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(driverName)) {
			//return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入司机名称");
			json.put("isok", false);
			json.put("result", "请输入司机名称");
			return json.toString();
		}
		if(StringUtils.isBlank(DriverId)){//add
			//查询是否有重名,有重名添加序号后缀,序号为尾数依次加1
			List driverList = driverService.getExistNameList(loginName);
			if(driverList.size()>0) {
				//return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"司机登录名已存在");
				json.put("isok", false);
				json.put("result", "司机登录名已存在");
				return json.toString();
			}
			Driver driver = new Driver();
			driver.setTeamId(Integer.parseInt(teamId));//添加司机的teamId为添加人对应的teamId
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
			json.put("isok", true);
			json.put("result", "保存成功");
			return json.toString();
		}else{//edit
			Driver driver =	driverService.get(Integer.parseInt(DriverId));
			if(driver==null){
				//return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在此id！");
				json.put("isok", false);
				json.put("result", "不存在此id！");
				return json.toString();
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
			json.put("isok", true);
			json.put("result", "保存成功");
			return json.toString();
		}
	}

	/**
	 * 
	 * @return 获取司机列表
	 */
	public String diverList() {
		String driverName = getParameter("driverName");
//		String accountId = getParameter("accountId");
		String teamId=getParameter("teamId");
		Object[] keywords = new Object[] { teamId, driverName,1,null };
		List list = driverService.getList(keywords);
		JSONObject json = new JSONObject();
		try {
			json.put("isok", true);
			json.put("result", ResultManager.getBodyResult(list));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * @return删除司机
	 * @throws Exception
	 */
	public String deleteDriver() throws Exception {
		String driverId = getParameter("driverId");
		String accountId = getParameter("accountId");
		if (driverService.ifBind(driverId)) {
			driverService.unBindCar(Integer.parseInt(driverId));
		}
		boolean b = driverService.deleteDriver(driverId);
		if(b){
			return "{\"isok\":true}";
		}else{
			return "{\"isok\":false}";
		}
	}

	public String getCarDriver() {
		String driverId = getParameter("driverId");
		List list = driverService.getDriverBId(Integer.parseInt(driverId));
		JSONObject json = new JSONObject();
		try {
			if (list != null && list.size() > 0) {
				json.put("result", ResultManager.getBodyResult(list.get(0)));
				json.put("isok", true);
			} else {
				json.put("isok", false);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}
   public String getDriverById(){
	   String driverId = getParameter("driverId");
		Driver diver = driverService.get(Integer.parseInt(driverId));
		JSONObject json = new JSONObject();
		try {
			if (diver != null ) {
				json.put("result", ResultManager.getBodyResult(diver));
				json.put("isok", true);
			} else {
				json.put("isok", false);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString(); 
   }
   public String delCarById(){
	   String userId = getParameter("userId");
		String accountId = getParameter("accountId");
		boolean b = userService.fakeDelete(userId, Integer.parseInt(accountId));
		if(b){
			
			return "{\"isok\":true}";
		}else{
			return "{\"isok\":false}";
		}
   }
	public String setWarned() {
		String warnId = getParameter("warnId");
		String reader = getParameter("reader");
		boolean isok;
		try {
			isok = boxreminderService.setWarnedForApp(warnId, reader);
			JSONObject json = new JSONObject();
			json.put("isok", isok);
			return json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"isok\":false}";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "{\"isok\":false}";
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public String getOnlineList() {
		String accountId = getParameter("accountId");
		Account account=accountService.get(Integer.parseInt(accountId));
		int teamId=account.getTeam().getTeamId();
		String teamIds=teamService.getTeamIdStr(account);
		try {
			List list = userService.getList2(teamId, teamId+"");
			//keywords = new Object[]{null,null,teamids};
			List newList = userService.getOnlineList(new Object[]{null,null,teamIds});

//			}
			JSONObject json = new JSONObject();
			json.put("isok", true);
			json.put("result", ResultManager.getBodyResult(newList));
			return json.toString();
		} catch (Exception e) {
			
			e.printStackTrace();
			return "{\"isok\":false}";
		}
	}
	
	public String getLineWarn(){
		String accountId = getParameter("accountId");
		Account account=accountService.get(Integer.parseInt(accountId));
		String teamIds=teamService.getTeamIdStr(account);
		String time = getParameter("time");
		List list = guanhuiService.getWarnListToApp2(Integer.parseInt(accountId),Integer.parseInt(time),teamIds);
		Gson gs = new Gson();
		String s = gs.toJson(list);
		try {
			JSONObject json = new JSONObject();
			json.put("isok", true);
			json.put("result", s);
			return json.toString();
		} catch (Exception e) {
			return "{\"isok\":false}";
		}
	}

	protected boolean checkIdentifier(String identifier) {
		if ((identifier.length() == 5 || identifier.length() == 11)
				&& identifier.matches("[0-9]{1,}")) {
			return true;
		}
		return checkBoxIdentifier(identifier)
				|| checkNaviIdentifier(identifier);
	}

	/**
	 * @author Administrator
	 * @date Sep 12, 2013
	 * @param identifier
	 * @return
	 * @Description 检查是否是盒子用户
	 */
	private boolean checkBoxIdentifier(String identifier) {
		if (identifier == null || identifier.length() != 9)
			return false;
		int head = Character.getNumericValue(identifier.charAt(0));
		int i1 = Character.getNumericValue(identifier.charAt(1));
		int i2 = Character.getNumericValue(identifier.charAt(2));
		int i3 = Character.getNumericValue(identifier.charAt(3));
		int i4 = Character.getNumericValue(identifier.charAt(4));
		int i5 = Character.getNumericValue(identifier.charAt(5));
		int i6 = Character.getNumericValue(identifier.charAt(6));
		int i7 = Character.getNumericValue(identifier.charAt(7));
		int end = Character.getNumericValue(identifier.charAt(8));
		int total = i1 + i2 + i3 + i4 + i5 + i6 + i7;
		if (head != 5) {
			if ((total % 2) == 0) {
				if (head != 2)
					return false;
			} else {
				if (head != 1)
					return false;
			}
		}
		int exp = (head ^ i1 ^ i2 ^ i3 ^ i4 ^ i5 ^ i6 ^ i7) % 10;
		if (end != exp)
			return false;
		return true;
	}

	/**
	 * @author Administrator
	 * @date Sep 12, 2013
	 * @param identifier
	 * @return
	 * @Description 检查是否是导航用户
	 */
	private boolean checkNaviIdentifier(String identifier) {
		if (identifier == null || identifier.length() != 9)
			return false;
		int head = Character.getNumericValue(identifier.charAt(0));
		int i1 = Character.getNumericValue(identifier.charAt(1));
		int i2 = Character.getNumericValue(identifier.charAt(2));
		int i3 = Character.getNumericValue(identifier.charAt(3));
		int i4 = Character.getNumericValue(identifier.charAt(4));
		int i5 = Character.getNumericValue(identifier.charAt(5));
		int i6 = Character.getNumericValue(identifier.charAt(6));
		int i7 = Character.getNumericValue(identifier.charAt(7));
		int end = Character.getNumericValue(identifier.charAt(8));
		if ((i1 == i2 && i2 == i3) || (i2 == i3 && i3 == i4)
				|| (i3 == i4 && i4 == i5) || (i4 == i5 && i5 == i6)
				|| (i5 == i6 && i6 == i7))
			return false;
		int total = i1 + i2 + i3 + i4 + i5 + i6 + i7;
		if ((total % 2) == 0) {
			if (head != 4)
				return false;
		} else {
			if (head != 3)
				return false;
		}
		int exp = (head ^ i1 ^ i2 ^ i3 ^ i4 ^ i5 ^ i6 ^ i7) % 10;
		if (end != exp)
			return false;
		return true;
	}
}
