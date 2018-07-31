package com.chanceit.http.action;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.utils.Coder;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.JPushUtils;
import com.chanceit.framework.utils.JedisService;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Askleave;
import com.chanceit.http.pojo.Baoxiao;
import com.chanceit.http.pojo.Dept;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.Member;
import com.chanceit.http.pojo.Order;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IAskleaveService;
import com.chanceit.http.service.IBaoxiaoService;
import com.chanceit.http.service.IBrandService;
import com.chanceit.http.service.ICarBaseInfoService;
import com.chanceit.http.service.ICarDriverService;
import com.chanceit.http.service.ICarstyleService;
import com.chanceit.http.service.ICarusesService;
import com.chanceit.http.service.IDeptService;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.ILevelService;
import com.chanceit.http.service.IMemberService;
import com.chanceit.http.service.IOrderService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;

/**
 * @ClassName AccountView
 * @author Administrator
 * @date Jun 26, 2013 2:28:41 PM
 * @Description 账号管理相关接口
 */
@Component("govAppView")
public class GovAppView extends BaseAction {

	private String charSet = "UTF-8";// 字符集
	private int connectionTimeOut = 30000;// 连接超时时间
	private int readTimeOut = 40000;// 读取超时时间
	private String nodeJsUrl;
	private String mileUrl;
	private String slowTimeUrl;
	@Autowired
	@Qualifier("memberService")
	private IMemberService memberService;

	@Autowired
	@Qualifier("driverService")
	private IDriverService driverService;
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;

	@Autowired
	@Qualifier("deptService")
	private IDeptService deptService;

	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;

	@Autowired
	@Qualifier("levelService")
	private ILevelService levelService;
	@Autowired
	@Qualifier("askleaveService")
	private IAskleaveService askleaveService;

	private final int verificationCount = 4;

	@Autowired
	@Qualifier("jedisService")
	private JedisService jedisService;
	@Autowired
	@Qualifier("carDriverService")
	private ICarDriverService carDriverService;

	@Autowired
	@Qualifier("orderService")
	private IOrderService orderService;

	@Autowired
	@Qualifier("jpushUtils")
	private JPushUtils jpushUtils;

	@Autowired
	@Qualifier("carstyleService")
	private ICarstyleService carstyleService;

	@Autowired
	@Qualifier("carusesService")
	private ICarusesService carusesService;
	@Autowired
	@Qualifier("brandService")
	private IBrandService brandService;

	@Autowired
	@Qualifier("baoxiaoService")
	private IBaoxiaoService baoxiaoService;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("carBaseInfoService")
	private ICarBaseInfoService carBaseInfoService;

	/**
	 * 
	 * 
	 * @Description 账号登陆
	 */
	
	@SuppressWarnings("unchecked")
	public String LoginForGov() throws Exception {

		String type = getParameter("type");// 登录用户类型
		String username = getParameterNoCheck("username");
		String password = getParameterNoCheck("password");

		if ("0".equals(type)) {// 乘客
			Member member = memberService.getByUserName(username);
			if (member != null && member.getPwd().equals(password)) {
				Team team = teamService.get(member.getTeamId());
				Map map = new HashMap();
				if(member.getDeptId() != null){
				Dept dept = deptService.getDept(member.getDeptId());
				map.put("deptName", dept.getDeptName());
				}
				
				// JSONObject json = new JSONObject();
				map.put("id", member.getId());
				map.put("userName", member.getUserName());
				map.put("name", member.getName());
				map.put("tel", member.getTel());
				map.put("teamId", member.getTeamId());
				map.put("level", 1000);
				map.put("ifDel", member.getIfDel());
				map.put("status", member.getStatus());
				// map.put("createTime", member.getCreateTime());
				map.put("deptId", member.getDeptId());
				map.put("teamName", team.getTeamName());
				
				return ResultManager.getResultForApp(true, ResultManager.getBodyResult(map));
			}
		}

		if ("1".equals(type)) {// 司机
			Driver driver = driverService.getByUserName(username);
			if (driver != null && driver.getPwd().equals(password)) {
				Team team = teamService.get(driver.getTeamId());
				// Dept dept=deptService.getDept(driver.getDeptId());

				Map map = new HashMap();
				// JSONObject json = new JSONObject();
				map.put("id", driver.getDriverId());
				map.put("userName", driver.getUserName());
				map.put("name", driver.getDriverName());
				map.put("tel", driver.getDriverTel());
				map.put("teamId", driver.getTeamId());
				map.put("level", 1001);// 随便给的值
				map.put("ifDel", driver.getIfDel());
				;
				map.put("status", driver.getStatus());
				;
				// map.put("createTime", driver.getCreateTime());
				map.put("zjcx", driver.getZjcx());// 准驾车型
				// json.put("deptId", );
				map.put("teamName", team.getTeamName());
				// json.put("deptName", dept.getDeptName());
				String data = map.toString();
				return ResultManager.getResultForApp(true, ResultManager.getBodyResult(map));
			}
		}

		if ("2".equals(type)) {//
			Account account = accountService.getByName(username);
			if (account != null
					&& account.getAccountPwd().equals(
							Coder.encryptMD5(password))) {
				account.setVisitIp(getRequest().getRemoteAddr());
				account.setVisitTime(new Date());
				account.setLogintimes(account.getLogintimes() + 1);
				accountService.save(account);

				Map json = new HashMap();
				// JSONObject json = new JSONObject();
				json.put("id", account.getAccountId());
				json.put("userName", StringUtils.isBlank(account
						.getAccountName()) ? null : account.getAccountName());
				json.put("name", StringUtils.isBlank(account
						.getAccountRealName()) ? null : account
						.getAccountRealName());
				json.put("tel",
						StringUtils.isBlank(account.getAccountTel()) ? null
								: account.getAccountTel());
				json.put("teamId", account.getTeam().getTeamId());
				json.put("level", account.getLevel().getLevelId());
				json.put("ifDel", 0);
				;
				json.put("status", 1);
				;
				// json.put("createTime", account.getCreateTime());
				json.put("teamName", account.getTeam().getTeamName());
				json.put("deptId", account.getDept() == null ? null : account
						.getDept().getDeptId());
				json.put("deptName", account.getDept() == null ? null : account
						.getDept().getDeptName());

				return ResultManager.getResultForApp(true, ResultManager.getBodyResult(json));
			}
		}

		return ResultManager.getResultForApp(false, "账号名或者密码错误");
	}

	/**
	 * 乘客注册
	 * 
	 * @return
	 * @throws Exception
	 */
	public String memberRegister() throws Exception {
		String type = getParameter("type");// 登录用户类型
		String userName = getParameter("userName");
		String password = getParameter("password");
		String name = getParameter("name");
		String tel = getParameter("tel");
		String teamId = getParameter("teamId");
		String deptId = getParameter("deptId");
		String zjcx = getParameter("zjcx");
		String carno = getParameter("carno");
		String endTime = getParameter("endTime");
		String liesen = getParameter("liesen");
     if("0".equals(type)){
    	 Member member = new Member();
    	// 检查用户名是否存在
 		if (!StringUtils.isBlank(userName)) {
 			Member member2 = memberService.getByUserName(userName);
 			if (member2 != null) {
 				return ResultManager.getResultForApp(false, "用户名已存在");
 			} else {
 				member.setUserName(userName);
 			}
 		}

 		if (!StringUtils.isBlank(password)) {
 			member.setPwd(password);
 		}
 		if (!StringUtils.isBlank(name)) {
 			member.setName(name);
 		}
 		if (!StringUtils.isBlank(tel)) {
 			member.setTel(tel);
 		}
 		if (!StringUtils.isBlank(teamId)) {
 			member.setTeamId(Integer.parseInt(teamId));
 		}
 		if (!StringUtils.isBlank(deptId)) {
 			member.setDeptId(Integer.parseInt(deptId));
 		}
 		member.setLeavel(1000);
 		member.setIfDel(0);
 		member.setStatus(0);// 审批状态 0为待审批
 		member.setCreateTime(new Date());

 		memberService.saveMember(member);
 		jpushUtils.noticeNodeJsServer(12, Integer.parseInt(teamId), "");
     }else if("1".equals(type)){
    	 Driver driver = new Driver();
    	 if (!StringUtils.isBlank(userName)) {
 			if (driverService.ifExist(userName)) {
 				return ResultManager.getResultForApp(false, "用户名已存在");
 			} else {
 				driver.setUserName(userName);
 			}
 		}

 		if (!StringUtils.isBlank(password)) {
 			driver.setPwd(password);
 		}
 		if (!StringUtils.isBlank(name)) {
 			driver.setDriverName(name);
 		}
 		if (!StringUtils.isBlank(tel)) {
 			driver.setDriverTel(tel);
 		}
 		if (!StringUtils.isBlank(teamId)) {
 			driver.setTeamId(Integer.parseInt(teamId));
 		}
 		if (!StringUtils.isBlank(zjcx)) {
 			driver.setZjcx(zjcx);
 		}
 		if (!StringUtils.isBlank(carno)) {
 			driver.setCardNo(carno);
 		}
 		if (!StringUtils.isBlank(liesen)) {
 			driver.setLicense(liesen);
 		}
 		if (!StringUtils.isBlank(endTime)) {
 			driver.setEndTime(DateUtil.parseDate(endTime, "yyyy-MM-dd"));
 		}
 		// driver.setLevel(1001);
 		// driver.setIfDel(0);
 		driver.setStatus(0);// 审批状态 0为待审批
 		driver.setCreateTime(new Date());

 		driverService.saveDriver(driver); 
 		jpushUtils.noticeNodeJsServer(13, Integer.parseInt(teamId), "");
     }
		
		return ResultManager.getResultForApp(true, "");

	}

//	/**
//	 * 司机注册
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	public String driverRegister() throws Exception {
//		String username = getParameter("driverName");
//		String password = getParameter("password");
//		String drivername = getParameter("name");
//		String tel = getParameter("tel");
//		String teamId = getParameter("teamId");
//		String zjcx = getParameter("zjcx");
//		String carno = getParameter("carno");
//		String endTime = getParameter("endTime");
//		Driver driver = new Driver();
//
//		// 检查司机用户名是否存在
//		if (!StringUtils.isBlank(username)) {
//			if (driverService.ifExist(username)) {
//				return ResultManager.getResultForApp(false, "用户名已存在");
//			} else {
//				driver.setUserName(username);
//			}
//		}
//
//		if (!StringUtils.isBlank(password)) {
//			driver.setPwd(password);
//		}
//		if (!StringUtils.isBlank(drivername)) {
//			driver.setDriverName(drivername);
//		}
//		if (!StringUtils.isBlank(tel)) {
//			driver.setDriverTel(tel);
//		}
//		if (!StringUtils.isBlank(teamId)) {
//			driver.setTeamId(Integer.parseInt(teamId));
//		}
//		if (!StringUtils.isBlank(zjcx)) {
//			driver.setZjcx(zjcx);
//		}
//		if (!StringUtils.isBlank(carno)) {
//			driver.setCardNo(carno);
//		}
//		if (!StringUtils.isBlank(endTime)) {
//			driver.setEndTime(DateUtil.parseDate(endTime, "yyyy-MM-dd"));
//		}
//		// driver.setLevel(1);
//		// driver.setIfDel(0);
//		driver.setStatus(0);// 审批状态 0为待审批
//		driver.setCreateTime(new Date());
//
//		driverService.saveDriver(driver);
//		return ResultManager.getResultForApp(true, "");
//
//	}

	/**修改用户信息
	 * @return
	 */
	public String eiditUserInfo() {
		String type_s = getParameter("type");
		String uid = getParameter("uid");
		String username = getParameter("username");
		String password = getParameter("pwd");
		String zjcx = getParameter("zjcx");
		String tel = getParameter("tel");

		if ("0".equals(type_s)) {
			Member m = memberService.get(Integer.parseInt(uid));
			if (!StringUtils.isBlank(username)) {
             m.setName(username);
             
			}
			if (!StringUtils.isBlank(password)) {
				m.setPwd(password);
			}
			if (!StringUtils.isBlank(tel)) {
             m.setTel(tel);
			}
        memberService.update(m);
		} else if ("1".equals(type_s)) {
			Driver d = driverService.get(Integer.parseInt(uid));
			if (!StringUtils.isBlank(username)) {
	             d.setDriverName(username);
	             
				}
				if (!StringUtils.isBlank(password)) {
					d.setValPassword(password);
				}
				if (!StringUtils.isBlank(tel)) {
	             d.setDriverTel(tel);
				}
				if (!StringUtils.isBlank(zjcx)) {
		             d.setZjcx(zjcx);
					}
				driverService.update(d);
		} else {
			Account a = accountService.get(Integer.parseInt(uid));
			if (!StringUtils.isBlank(username)) {
	            a.setAccountRealName(username);
	             
				}
				if (!StringUtils.isBlank(password)) {
					try {
						a.setAccountPwd(Coder.encryptMD5(password));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					accountService.updatePswd(new Object[]{password, a.getAccountId()});
				}
				if (!StringUtils.isBlank(tel)) {
	             a.setAccountTel(tel);
				}
				accountService.save(a);
				
				//accountService.update(a);
		}
		return ResultManager.getResultForApp(true, "");
	}

	/**修改密码
	 * @return
	 */
	public String eiditUserPwd() {
		String type_s = getParameter("type");
		String uid = getParameter("uid");
		String password = getParameter("newpwd");
		String oldpassword = getParameter("oldpwd");
		if ("0".equals(type_s)) {
			Member m = memberService.get(Integer.parseInt(uid));
			if(!m.getPwd().equals(oldpassword)) return ResultManager.getResultForApp(false, "原密码不匹配");
			if (!StringUtils.isBlank(password)) {
				m.setPwd(password);
			}
        memberService.update(m);
		} else if ("1".equals(type_s)) {
			Driver d = driverService.get(Integer.parseInt(uid));
			if(!d.getPwd().equals(oldpassword)) return ResultManager.getResultForApp(false, "原密码不匹配");
				if (!StringUtils.isBlank(password)) {
					d.setPwd(password);
				}
				
				driverService.update(d);
		} else {
			Account a = accountService.get(Integer.parseInt(uid));
			try {
			if(!a.getAccountPwd().equals(Coder.encryptMD5(oldpassword))) return ResultManager.getResultForApp(false, "原密码不匹配");
				if (!StringUtils.isBlank(password)) {
				
						a.setAccountPwd(Coder.encryptMD5(password));
					
					accountService.updatePswd(new Object[]{password, a.getAccountId()});
				}
				accountService.save(a);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ResultManager.getResultForApp(true, "");
	}
	
	/**
	 * 获取所有公司
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTeams() throws Exception {
		Account account = new Account();
		account.setAccountId(6);
		Team t = new Team();
		t.setTeamId(36);
		account.setTeam(t);
		return teamService.getJson(account,
				Integer.parseInt(account.getAccountId().toString()), 1)
				.toString();
	}

	/**
	 * 获取部门
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDept() throws Exception {
		String teamId = getParameter("teamId");
		if (StringUtils.isEmpty(teamId)) {
			teamId = "0";
		}
		List list = deptService.getListByTeamId(Integer.parseInt(teamId));

		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	public String getOrders() {
		String type_s = getParameter("type");
		String status = getParameter("status");
		String uid = getParameter("uid");
		int type = Integer.parseInt(type_s);
		int userId = Integer.parseInt(uid);
		int st = Integer.parseInt(status);
		if (0 == type) {// 乘客
			type = 0;
			if (st == 0) {
				status = "0";
			} else if (st == 1) {
				status = "7,-1,-2,-3,-4,-5";
			} else {
				status = "0,1,2,3,4,5,6";
			}
		} else if (1 == type) {// 司机
			type = 1;
			if (st == 0) {
				status = "0";
			} else if (st == 1) {
				status = "7,-5";
			} else {
				status = "4,5,6";
			}
		} else {// 管理员，领导，车队长

			Account account = accountService.get(Integer.parseInt(uid));
			if (account.getLevel().getLevelId() == 3 || account.getLevel().getLevelId() == 5) { // 车队长
				type = 2;
				userId = account.getTeam().getTeamId();
				if (st == 0) {
					status = "0,1,2,3";
				} else if (st == 1) {
					status = "7,-1,-2,-3,-4,-5";
				} else {
					status = "4,5,6";
				}
			} else if (account.getLevel().getLevelId() == 4) { // 部门领导
				userId = account.getDept().getDeptId();
				type = 3;
				if (st == 0) {
					status = "0";
				} else if (st == 1) {
					status = "7,-1,-2,-3,-4,-5";
				} else {
					status = "1,2,3,4,5,6";
				}
			} else { // 管理员 公司领导
				userId = account.getTeam().getTeamId();
				type = 2;
				if (st == 0) {
					status = "0,3";
				} else if (st == 1) {
					status = "7,-1,-2,-3,-4,-5";
				} else {
					status = "1,2,4,5,6";
				}
			}
		}
		List list = orderService.getList(type, userId, status);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	/**
	 * 添加订单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addOrder() throws Exception {
		String type_s = getParameter("type");
		String uid = getParameter("uid");
		String beginTime = getParameter("begin_time");
		String endTime = getParameter("end_time");
		String car_user_id = getParameter("contact_id");
		String car_user_name = getParameter("car_user_name");
		String car_user_tel = getParameter("tel");
		String number = getParameter("num"); // 乘车人数
		String takes = getParameter("takes");// 货物
		String use_reason = getParameter("use_reason");// 用车事由
		String remark = getParameter("remark"); // 备注
		String cause = getParameter("cause"); //
		String adress0 = getParameter("begin_addr");// 起始地址
		String adress1 = getParameter("end_addr");// 结束地址
		String carStyleId = getParameter("carStyleId");// 结束地址
		String pos0_x = getParameter("from_lat");// 起始坐标
		String pos0_y = getParameter("from_lng");// 起始坐标
		String pos1_x = getParameter("to_lat");// 起始坐标
		String pos1_y = getParameter("to_lng");// 起始坐标

		String brand = getParameter("brandId");
		String carId = getParameter("carid");
		String driverId = getParameter("driverid");
		// if(StringUtils.isBlank(driverName)) return
		// ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"请输入司机名称");
		String accountId;
		int type = Integer.parseInt(type_s);
		int userId = Integer.parseInt(uid);
		int order_user_id = 0;
		String order_user_name = "";
		String order_user_tel = "";
		Order order = new Order();
		int teamId = 0;
		if (type == 0) {
			Member m = memberService.get(userId);
			teamId = m.getTeamId();
			order_user_id = m.getId();
			order_user_name = m.getName();
			order_user_tel = m.getTel();
			order.setStatus(0);
		} else {
			Account account = accountService.get(userId);
			order_user_id = account.getAccountId();
			order_user_name = account.getAccountRealName();
			order_user_tel = account.getAccountTel();
			if (account.getLevel().getLevelId() == 1) {
				order.setStatus(1);
			} else if (account.getLevel().getLevelId() == 2) {
				order.setStatus(2);
			} else if (account.getLevel().getLevelId() == 3 || account.getLevel().getLevelId() == 5) {
				order.setStatus(4);
			} else if (account.getLevel().getLevelId() == 4) {
				order.setStatus(3);
			}
			teamId = account.getTeam().getTeamId();
		}

		order.setOrderNum(String.valueOf(new Date().getTime()));
		order.setOrderUserId(order_user_id);
		order.setOrderUserName(order_user_name);
		order.setOrderUserTel(order_user_tel);
		order.setCarUserId(Integer.parseInt(car_user_id));
		order.setCarUserName(car_user_name);
		order.setCarUserTel(car_user_tel);
		order.setBeginTime(DateUtil.getDate(beginTime));
		order.setEndTime(DateUtil.getDate(endTime));
		order.setRemark(remark);
		order.setBeginAddr(adress0);
		order.setEndAddr(adress1);
		order.setBeginLat(Double.parseDouble(pos0_x));
		order.setBeginLng(Double.parseDouble(pos0_y));
		order.setEndLat(Double.parseDouble(pos1_x));
		order.setEndLng(Double.parseDouble(pos1_y));
		if (!StringUtils.isBlank(carId)) {
			order.setCarId(Integer.parseInt(carId));
			User u = userService.get(Integer.parseInt(carId));
			u.setOnline((short)2);
			userService.update(u);
		}
		if (!StringUtils.isBlank(driverId)) {
			order.setDriverId(Integer.parseInt(driverId));
		}
		if (!StringUtils.isBlank(carStyleId)) {
			order.setCarStyleId(Integer.parseInt(carStyleId));
		}
		if (!StringUtils.isBlank(brand)) {
			order.setBrandId(Integer.parseInt(brand));
		}
		if (!StringUtils.isBlank(cause)) {
			order.setCause(Integer.parseInt(cause));
		}
		order.setTakes(takes);
		order.setPnumber(Integer.parseInt(number));
		order.setCreateTime(new Date());
		// driver.setCreateTime(new Date());
		// driver.setAccount(accountService.get(Integer.parseInt(accountId)));//***注意
		// 操作员可添加
		// DriverId = driverService.save(driver);
		orderService.save(order);
		jpushUtils.noticeNodeJsServer(11,teamId, "new order");
		return ResultManager.getResultForApp(true, "");

	}

	public String getPlate() {
		String uid = getParameter("uid");
		String brand = getParameter("brand");
		String styleId = getParameter("carStyleId");
		String type = getParameter("type");
		String banche = getParameter("banche");
		String teamIds = "";
		if("1".equals(type)){
			Driver d = driverService.get(Integer.parseInt(uid));
			teamIds = d.getTeamId()+"";
		}else if("2".equals(type)){
			Account account = accountService.get(Integer.parseInt(uid));
			teamIds = account.getTeam().getTeamId()+"";
			if(account.getLevel().getLevelId() == 5){
				teamIds = teamService.getTeamIdStr(account);	
			}
		}else if("0".equals(type)){
			Member m = memberService.get(Integer.parseInt(uid));
			 teamIds = m.getTeamId()+"";
		}
		Object[] keywords = new Object[] { teamIds, styleId, brand,banche };
		List list = carBaseInfoService.getPlateList(keywords);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}
	public String getPlateBanche() {
		String uid = getParameter("uid");
		String brand = getParameter("brand");
		String styleId = getParameter("carStyleId");
		String type = getParameter("type");
		String teamIds = "";
		if("1".equals(type)){
			Driver d = driverService.get(Integer.parseInt(uid));
			teamIds = d.getTeamId()+"";
		}else if("2".equals(type)){
			Account account = accountService.get(Integer.parseInt(uid));
			 teamIds = account.getTeam().getTeamId()+"";
		}
		Object[] keywords = new Object[] { teamIds, styleId, brand };
		List list = carBaseInfoService.getPlateList(keywords);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}
	public String getDriver() {
		String uid = getParameter("uid");
		String status = getParameter("status");
		Account account = accountService.get(Integer.parseInt(uid));
		String teamIds = teamService.getTeamIdStr(account);
		if (account.getLevel().getLevelId() != 5 ) {
			teamIds = account.getTeam().getTeamId() + "";
		}
		Object[] keywords = new Object[] { teamIds, null,status,null};
		List list = driverService.getList(keywords);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	/**
	 * 操作订单
	 * 
	 * @return
	 */
	public String editOrder() {
		String orderId = getParameter("orderId");
		String reason = getParameter("reason");
		String carId = getParameter("carid");
		String driverId = getParameter("driverid");
		String type_s = getParameter("type");
		String status = getParameter("status");
		String uid = getParameter("uid");
		String glf = getParameter("glf");
		//

		Order order = orderService.get(Integer.parseInt(orderId));
		if (Integer.parseInt(status) == -1) { // app端拒绝
			order.setReason(reason);
			Account account = accountService.get(Integer.parseInt(uid));
			if (account.getLevel().getLevelId() == 3 || account.getLevel().getLevelId() == 5) {
				order.setStatus(-4);
			} else if (account.getLevel().getLevelId() == 1) {
				order.setStatus(-1);
			} else if (account.getLevel().getLevelId() == 2) {
				order.setStatus(-2);
			} else {
				order.setStatus(-3);
			}

		} else if (Integer.parseInt(status) == 1) { // app端同意
			order.setReason(reason);
			Account account = accountService.get(Integer.parseInt(uid));
			if (account.getLevel().getLevelId() == 3 || account.getLevel().getLevelId() == 5) {

				if (!StringUtils.isEmpty(carId)
						&& !StringUtils.isEmpty(driverId)) {
					order.setStatus(4);
					order.setCarId(Integer.parseInt(carId));
					order.setDriverId(Integer.parseInt(driverId));
					User u = userService.get(Integer.parseInt(carId));
					u.setOnline((short)2);
					userService.update(u);
				} else {
					return ResultManager.getResultForApp(false, "请为乘客配置车辆");
				}
			} else if (account.getLevel().getLevelId() == 1) {
				order.setStatus(1);
			} else if (account.getLevel().getLevelId() == 2) {
				order.setStatus(2);
			} else {
				order.setStatus(3);
			}
		} else if (Integer.parseInt(status) == -2) { // app端取消
			order.setReason(reason);
			order.setStatus(-5);
		} else if (Integer.parseInt(status) == 5) { // app端操作，发车
			order.setStatus(Integer.parseInt(status));
		} else if (Integer.parseInt(status) == 6) { // app端操作，上车
			order.setStatus(Integer.parseInt(status));
			order.setStartTime(new Date());
			User u = userService.get(order.getCarId());
			String url = mileUrl;
			String param = "uid:" + u.getIdentifier() + "/time:"
					+ (new Date().getTime() / 1000);
			String rec = crawlPost(url + param, "", "utf-8");
			if (!StringUtils.isBlank(rec)) {
				order.setMiles(Double.parseDouble(rec));
			}
			
			
		} else if (Integer.parseInt(status) == 7) { // app端操作，完成
			order.setStatus(Integer.parseInt(status));

			order.setOverTime(new Date());
			User u = userService.get(order.getCarId());
			String url = mileUrl;
			String param = "uid:" + u.getIdentifier() + "/time:"
					+ (new Date().getTime() / 1000);
			String rec = crawlPost(url + param, param, "utf-8");
			double mile = 0;
			if (!StringUtils.isBlank(rec)) {
				if (order.getMiles() != null && order.getMiles() != 0) {
					mile = Double.parseDouble(rec) - order.getMiles();
					if (mile > 0) {
						order.setMiles(mile);
//						if (u.getKmPrice() != null) {
//							double cost = u.getKmPrice() * mile;
//							if (u.getStartPrice() != null) {
//								cost += u.getStartPrice();
//							}
//						}
					}else{
						order.setMiles(0.01);
					}
				}

			}
			double cost = 0;
			if (u.getKmPrice() != null) {
				cost = u.getKmPrice() * mile / 1000;
				if (u.getStartPrice() != null) {
					cost += u.getStartPrice();
				}
			}
			String urlTime = slowTimeUrl;
			String param1 = "uid:" + u.getIdentifier() + "/time:"
					+ (order.getStartTime().getTime() / 1000)+"/time2:"+(order.getOverTime().getTime()/1000);
			String rec1 = crawlPost(urlTime + param1, "", "utf-8");
			if (!StringUtils.isBlank(rec1)) {
				if(u.getLowPrice()!=null){
					cost += (Double.parseDouble(rec1)/60)*u.getLowPrice();
				}
			}
			order.setCost(cost);
			User uu = userService.get(order.getCarId());
			uu.setOnline((short)0);
			userService.update(uu);
		}
		if (!StringUtils.isBlank(glf)) {
			order.setGlf(Double.parseDouble(glf));
		}
		
		Member u = memberService.get(order.getCarUserId());
        
		orderService.update(order);
		jpushUtils.sendMsgToMem(u.getUserName(), order.getStatus(), order.getReason());
		if(order.getStatus() == 4 || (order.getStatus() == -5 && order.getDriverId()!=null)){
			Driver d = driverService.get(order.getDriverId());
			jpushUtils.sendMsgToDriver(d.getUserName(), order.getStatus(), order.getReason());
		}
		return ResultManager.getResultForApp(true, "");
	}
	
	public String editFeedOrder() {
		String orderId = getParameter("orderId");
		String feedback = getParameter("content");
		String scor = getParameter("score");
		Order order = orderService.get(Integer.parseInt(orderId));
		if (!StringUtils.isBlank(feedback)) {
			order.setFeedback(feedback);
		}
		if (!StringUtils.isBlank(scor)) {
			order.setScore(Integer.parseInt(scor));
		}
		orderService.update(order);
		return ResultManager.getResultForApp(true, "");
	}

	public String editGlfOrder() {
		String orderId = getParameter("orderId");
		String glf = getParameter("glf");
		Order order = orderService.get(Integer.parseInt(orderId));
		if (!StringUtils.isBlank(glf)) {
			order.setGlf(Double.parseDouble(glf));
		}
		orderService.update(order);
		return ResultManager.getResultForApp(true, "");
	}

	/**
	 * 请假相关
	 */

	public String getLeaveList() {
		// String askleaveName = getParameter("askleaveName");
		String type_s = getParameter("type");
		// String status = getParameter("status");//0 待审批，1同意
		String uid = getParameter("uid");
		// if(Integer.parseInt(status)==0){
		// status="0";
		// }else
		// status="1,-1";

		int type = Integer.parseInt(type_s);
		int userId = Integer.parseInt(uid);
		// int st = Integer.parseInt(status);
		if (1 == type) {// 司机
			type = 1;
		} else { // 车队长
			Account account = accountService.get(userId);
			userId = account.getTeam().getTeamId();
		}

		List list = askleaveService.getList(type, userId, "");
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	public String addLeave() throws ParseException {
		String uid = getParameter("uid");
		String beginTime = getParameter("beginTime");
		String endTime = getParameter("endTime");
		String reason = getParameter("reason");

		if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
			return ResultManager.getResultForApp(false, "请选择时间");
		}
		if (StringUtils.isBlank(reason)) {
			return ResultManager.getResultForApp(false, "请说明请假事由");
		}
		Askleave askleave = new Askleave();
		askleave.setUserId(Integer.parseInt(uid));
		System.out.println(DateUtil.getDate(endTime));
		askleave.setEndTime(DateUtil.getDate(endTime));
		askleave.setStartTime(DateUtil.getDate(beginTime));
		askleave.setReason(reason);
		askleave.setStatus(0);
		askleave.setCreateTime(new Date());
		String res = askleaveService.save(askleave);
		if (res == null) {
			return ResultManager.getResultForApp(false, "系统异常");
		}
		return ResultManager.getResultForApp(true, "");
	}

	public String auditAskleave() throws Exception {
		String id = getParameter("id");
		String type = getParameter("status");
		String reason = getParameter("reason2");
		Askleave askleave = askleaveService.getAskleave(Integer.parseInt(id));
		if ("1".equals(type)) {
			askleave.setStatus(1);// 同意
		} else if ("-1".equals(type)) {
			askleave.setStatus(-1);// 拒绝
			askleave.setReason2(reason);
		}
		askleaveService.saveAskleave(askleave);

		return ResultManager.getResultForApp(true, "");

	}

	/**
	 * 车辆类型
	 * 
	 * @return
	 */
	public String getCarStyle() {
		List list = carstyleService.getList(null);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	/**
	 * 用车事由
	 * 
	 * @param page
	 * @return
	 */
	public String getCaurse() {

		List list = carusesService.getList(null);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	/**
	 * 品牌
	 * 
	 * @return
	 */
	public String getBrand() {
		List list = brandService.getList(null);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	/***************************************************************************
	 * 
	 * 报销相关接口
	 **************************************************************************/
	public String getBaoxiao() {
		// String baoxiaoName = getParameter("baoxiaoName");
		String uid = getParameter("uid");
		String type_s = getParameter("type");
		String statu = getParameter("status");// 0待审批报销 1报销列表
		int type = Integer.parseInt(type_s);
		int userId = Integer.parseInt(uid);
		String status = "";
		if (!StringUtils.isBlank(statu)) {
			if (Integer.parseInt(statu) == 0) {
				status = "0";
			} else {
				status = "1,-1";
			}
		}
		if (1 == type) {// 司机
			type = 1;
		} else { // 车队长
			Account account = accountService.get(userId);
			userId = account.getTeam().getTeamId();
		}

		List list = baoxiaoService.getList(type, userId, status);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	public String addBaoxiao() throws Exception {
		String type = getParameter("type");
		String userId = getParameter("uid");
		String carId = getParameter("carId");
		String time = getParameter("time");
		String jy = getParameter("jy");
		String xc = getParameter("xc");
		String by = getParameter("by");
		String wx = getParameter("wx");
		String lq = getParameter("lq");
		String tc = getParameter("tc");
		String nj = getParameter("nj");
		String bx = getParameter("bx");
		String qt = getParameter("qt");
		String gl = getParameter("gl");
		String info1 = getParameter("info1");
		if (StringUtils.isBlank(jy)) {
			jy = "0";
		}
		if (StringUtils.isBlank(xc)) {
			xc = "0";
		}
		if (StringUtils.isBlank(by)) {
			by = "0";
		}
		if (StringUtils.isBlank(wx)) {
			wx = "0";
		}
		if (StringUtils.isBlank(lq)) {
			lq = "0";
		}
		if (StringUtils.isBlank(tc)) {
			tc = "0";
		}
		if (StringUtils.isBlank(nj)) {
			nj = "0";
		}
		if (StringUtils.isBlank(bx)) {
			bx = "0";
		}
		if (StringUtils.isBlank(qt)) {
			qt = "0";
		}
		if (StringUtils.isBlank(gl)) {
			gl = "0";
		}

		Baoxiao baoxiao = new Baoxiao();
		baoxiao.setTime(DateUtil.getDate(time));
		// baoxiao.setTeamId(Integer.parseInt(teamId));
		baoxiao.setUserId(Integer.parseInt(userId));
		baoxiao.setCreateTime(new Date());
	    baoxiao.setCarId(Integer.parseInt(carId));
		baoxiao.setJyf(Double.parseDouble(jy));
		baoxiao.setXcf(Double.parseDouble(xc));
		baoxiao.setByf(Double.parseDouble(by));
		baoxiao.setWxf(Double.parseDouble(wx));
		baoxiao.setLqf(Double.parseDouble(lq));
		baoxiao.setTcf(Double.parseDouble(tc));
		baoxiao.setNjf(Double.parseDouble(nj));
		baoxiao.setBxf(Double.parseDouble(bx));
		baoxiao.setQtf(Double.parseDouble(qt));
		baoxiao.setGlf(Double.parseDouble(gl));
		baoxiao.setInfo1(info1);
		if ("1".equals(type)) {
			baoxiao.setStatus(0);// 设置状态为通过审批
		} else {

			baoxiao.setStatus(1);// 设置状态为通过审批
		}

		baoxiaoService.save(baoxiao);

		return ResultManager.getResultForApp(true, "");
	}

	public String auditBaoxiao() throws Exception {
		String id = getParameter("id");
		String type = getParameter("type");
		String reason = getParameter("reason");
		Baoxiao baoxiao = baoxiaoService.getBaoxiao(Integer.parseInt(id));
		if ("1".equals(type)) {
			baoxiao.setStatus(1);// 同意
			baoxiao.setInfo2(reason);
		} else if ("-1".equals(type)) {
			baoxiao.setStatus(-1);// 拒绝
			baoxiao.setInfo2(reason);
		}
		baoxiaoService.update(baoxiao);

		return ResultManager.getResultForApp(true, "");

	}

	public String getMember() throws Exception {
		String uid = getParameter("uid");
		String type = getParameter("type");
		String status = getParameter("status");
		int dtid = 0;
		Account account = null;
		//if ("2".equals(type)) {
			account = accountService.get(Integer.parseInt(uid));
			if (account.getLevel().getLevelId() == 4) {
				
				dtid = account.getDept().getDeptId();
			} else {
				dtid = account.getTeam().getTeamId();
			}
//		} else {
//			return ResultManager.getResultForApp(false, "您无权给别人申请");
//		}
		
		Object[] keywords = new Object[] { dtid,status,null};
		List list = memberService.getListSql(account.getLevel().getLevelId(),
				keywords);

		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));

	}
	
	
	
	public String editMemberDriver() throws Exception {
		String uid = getParameter("uid");
		String type = getParameter("type");
		String status = getParameter("status");
		if ("0".equals(type)) {
			Member m = memberService.get(Integer.parseInt(uid));
			if("-1".equals(status)){
				m.setStatus(-1);
			}else if("1".equals(status)){
				m.setStatus(1);
			}
			memberService.update(m);
		} else if("1".equals(type)){
			Driver d = driverService.get(Integer.parseInt(uid));
			if("-1".equals(status)){
				d.setStatus(-1);
			}else if("1".equals(status)){
				d.setStatus(1);
			}
			driverService.update(d);
		
		}else{
			return ResultManager.getResultForApp(false, "您无权给操作");	
		}

		return ResultManager.getResultForApp(true, "");

	}
	
	
	public String getDriverForapp() {
		String uid = getParameter("uid");
		String status = getParameter("status");
		Account account = accountService.get(Integer.parseInt(uid));
		String teamIds = teamService.getTeamIdStr(account);
		if (account.getLevel().getLevelId() != 1) {
			teamIds = account.getTeam().getTeamId() + "";
		}
		Object[] keywords = new Object[] { teamIds, null,status,null};
		List list = driverService.getList(keywords);
		return ResultManager.getResultForApp(true, ResultManager
				.getBodyResult(list));
	}

	public String crawlPost(String url, String para, String charset) {
		String content = "";
		BufferedReader reader = null;
		try {
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(connectionTimeOut);//
			connection.setReadTimeout(readTimeOut);//
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection
					.getOutputStream(), charset);
			out.write(para);
			out.flush();
			out.close();
			String sCurrentLine = "";
			reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(),
							charset == null ? this.charSet : charset));// 读取网页源代码，如果没有指定字符集，则使用默认的UTF-8
			while ((sCurrentLine = reader.readLine()) != null) {
				content += sCurrentLine;
			}
			// System.out.println(content);
		} catch (IOException e) {
			logger.error("访问" + url + "失败！原因:" + e.getMessage());
			close(reader);
		} finally {
			close(reader);
		}
		return content;
	}

	public static void close(Closeable c) {
		if (c != null)
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				c = null;
			}
	}

	public String getMile(String path) {
		URL url;
		URLConnection con;
		StringBuffer sb;
		String result = "";
		BufferedReader reader = null;
		try {
			url = new URL(path);
			con = url.openConnection();
			reader = new BufferedReader(new InputStreamReader(con
					.getInputStream()));
			sb = new StringBuffer();
			while (reader.read() != -1) {
				sb.append(reader.readLine());
			}
			result = new String(sb);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catlech block
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
				sb = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}
	


	public String getMileUrl() {
		return mileUrl;
	}

	public void setMileUrl(String mileUrl) {
		this.mileUrl = mileUrl;
	}

	public String getNodeJsUrl() {
		return nodeJsUrl;
	}

	public void setNodeJsUrl(String nodeJsUrl) {
		this.nodeJsUrl = nodeJsUrl;
	}

	public String getSlowTimeUrl() {
		return slowTimeUrl;
	}

	public void setSlowTimeUrl(String slowTimeUrl) {
		this.slowTimeUrl = slowTimeUrl;
	}

}
