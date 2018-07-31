/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Coder;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.CarDriver;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.SignInLog;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.ICarDriverService;
import com.chanceit.http.service.ICardService;
import com.chanceit.http.service.IDriverService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ISignInLogService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;

/**
 * @ClassName AccountView
 * @author Administrator
 * @date Jun 26, 2013 2:28:41 PM
 * @Description 账号管理相关接口
 */
@Component("carDriverView")
public class CarDriverView extends BaseAction {
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
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
	@Qualifier("carDriverService")
	private ICarDriverService carDriverService;
	
	@Autowired
	@Qualifier("cardService")
	private ICardService cardService;
	
	@Autowired
	@Qualifier("signInLogService")
	private ISignInLogService signInLogService;
	
	@Autowired
	@Qualifier("opTeamService")
	private IOperatorTeamService opTeamService;
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	/**
	 * 保存或修改车辆司机规则
	 */
	public String saveCarDriver() throws Exception{
		String carDriverId = getParameter("carDriverId");
		String vehicleId = getParameter("vehicleId");
		String driverId = getParameter("driverId");
		String startTime = getParameter("startTime");
		String endTime = getParameter("endTime");
		
		User car = new User();
		car.setVehicleId(Integer.parseInt(vehicleId));
		
		Driver driver = new Driver();
		driver.setDriverId(Integer.parseInt(driverId));
		
		if(StringUtils.isBlank(carDriverId)){//add
			CarDriver carDriver = new CarDriver();
			carDriver.setCar(car);
			carDriver.setDriver(driver);
			carDriver.setStartTime(DateUtil.parseDate(startTime, "yyyy-MM-dd"));
			carDriver.setEndTime(DateUtil.parseDate(endTime, "yyyy-MM-dd"));
		
			carDriverService.save(carDriver);
			return ResultManager.getSuccResult();
		}else{//edit
			//通过ID查询车辆司机规则信息
			CarDriver carDriver = carDriverService.get(Integer.parseInt(carDriverId));
			if(carDriver == null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"不存在此id！");
			}
			carDriver.setCar(car);
			carDriver.setDriver(driver);
			carDriver.setStartTime(DateUtil.parseDate(startTime, "yyyy-MM-dd"));
			carDriver.setEndTime(DateUtil.parseDate(endTime, "yyyy-MM-dd"));
			carDriverService.save(carDriver);
			return ResultManager.getSuccResult();
		}
			 
	}
	
	/**
	 * 删除车辆司机规则
	 */
	public String delete() throws Exception{
		String carDriverIds = getParameter("carDriverIds");
		if(StringUtils.isBlank(carDriverIds)){
			return ResultManager.getFaildResult("请输入需要删除的账号ID，以英文的逗号区分");
		}
		Account account = getSessionAccount();
		carDriverService.deleteAll(carDriverIds);
		return ResultManager.getSuccResult();
	}
	
	/**
	 * 获取车辆司机规则列表
	 */
	public String list(Page page){
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		String vehicleId = getParameter("vehicleId");
		//操作员权限只获取自身有权限的车队ID
		Object[] keywords ;
		if(account.getRole().getRoleId() == 3){
			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{vehicleId,accountId,teamIds};
		}else{
			keywords = new Object[]{vehicleId,accountId,null};
		}
		page = carDriverService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}


	public String unbindDrivers(){
		String plate = getParameter("driverName");
		Account accountAdmin = (Account)getSession().getAttribute("account");
		int accountId;
		if(accountAdmin.getParentId()> 0){
			 accountId = accountAdmin.getParentId();
		}else{
			accountId = accountAdmin.getAccountId();
		}
		Object[] keywords = new Object[]{accountId,plate};
		List list = driverService.getUnbindList(keywords);
		return ResultManager.getBodyResult(list);
	}
	
	/**
	 * 验证写卡接口
	 * @return
	 * @throws Exception
	 */
	public String writeCard() throws Exception{
		String loginName = getParameter("loginName");
		String password = getParameter("password");
		String valName = getParameter("valName");
		String cardNo = getParameter("cardNo");
		//写卡是否成功标识符
		Account account = accountService.getByName(loginName);
		//验证用户是否拥有权限
		if(account != null && account.getAccountPwd().equals(Coder.encryptMD5(password))) {
			Driver driver = driverService.getDriverByValName(valName);
			if(driver != null){
				//卡号不同的情况下将司机和卡号绑定
				driverService.updateDriverByCondition(new Object[]{cardNo, valName});
				return ResultManager.getFaildResult(EnumCommon.WRITECARD_SUCCESS, "写卡成功！");
			}else{
				return ResultManager.getFaildResult(EnumCommon.WRITECARD_FAILE, "司机名不存在！");
			}
		}else{
			return ResultManager.getFaildResult(EnumCommon.WRITECARD_FAILE, "用户名或密码错误！");
		}
	}
	
	/**
	 * 写卡接口
	 * @return
	 * @throws Exception
	 */
	/*public String writeCard() throws Exception{
		String valName = getParameter("valName");
		String cardNo = getParameter("cardNo");
		try{
			//卡号不同的情况下将司机和卡号绑定
			driverService.updateDriverByCondition(new Object[]{cardNo, valName});
			return ResultManager.getFaildResult(EnumCommon.WRITECARD_SUCCESS, "绑定成功！");
		}catch(Exception e){
			return ResultManager.getFaildResult(EnumCommon.WRITECARD_FAILE, "绑定失败！");
		}
	}*/
	
	/**
	 * 司机签到接口
	 * @return
	 * @throws Exception
	 */
	public Boolean valSignIn(String identifier, String cardNo, String valName,
			Date signInTime) throws Exception{
		//String identifier = getParameter("identifier");
		//String cardNo = getParameter("cardNo");
		//String valName = getParameter("valName");
		User car = userService.getByName(identifier);
		//刷卡是否合法标识符
		int ifLegal = 0;
		//刷卡是否成功标识符
		boolean isOk = false;
		//通过登录名和卡号来获取司机信息
		Driver driver = driverService.getDriverByCondition(new Object[]{valName, cardNo});
		if(driver != null){
			
			Integer driverId = driver.getDriverId();
			Integer carId = car.getVehicleId();
			//通过司机id和车辆id来获取相对应的车辆司机验证规则
			CarDriver carDriver = carDriverService.getInfoByBothId(driverId, carId);
			//如果有相对应的验证规则或者司机权限为1(车站管理员时),将司机与车辆关联,如果没有则提示无权限
			if(carDriver != null || (driver.getLevel() != null && driver.getLevel() == 1)){
				
				Driver bindDriver = car.getDriver();
				//如果车辆未绑定司机,则绑定,如果已绑定就判断是否更换司机
				if(bindDriver == null){
					Driver newDriver = new Driver();
					newDriver.setDriverId(driverId);
					car.setDriver(newDriver);
					userService.save(car);
				}else{
					Integer bindId = bindDriver.getDriverId();
					if(driverId != bindId){
						Driver newDriver = new Driver();
						newDriver.setDriverId(driverId);
						car.setDriver(newDriver);
						userService.save(car);
					}
				}
				//设置合法用户
				ifLegal = 1;
				isOk = true;
			}
		}else{
			driver = driverService.getDriverByValName(valName);
		}
		
		//通过车的ID获取该车最后一条日志信息,如果是同一司机,则不插入信息,防止频繁刷卡的多次插入冗余日志
		SignInLog signLog = signInLogService.getLastDriverByCarId(car.getVehicleId());
		if(signLog != null && signLog.getDriver().getDriverId() != driver.getDriverId()){
			//添加日志信息
			SignInLog log = new SignInLog();
			log.setCar(car);
			log.setDriver(driver);
			//1为权限用户
			log.setIfLegal(ifLegal);
			//0为管理员操作,1为司机打卡操作
			log.setFromType(1);
			log.setSignInTime(signInTime);
			//插入日志
			signInLogService.save(log);
			//给该车最后一条信息添加解绑时间
			signLog.setUnbindTime(signInTime);
			signInLogService.save(signLog);
		}
		/*if(isOk){
			return ResultManager.getFaildResult(EnumCommon.READCARD_SUCCESS, "刷卡成功！");
		}else{
			return ResultManager.getFaildResult(EnumCommon.READCARD_FAILE, "刷卡失败, 车辆与司机未绑定关系！");
		}*/
		if(isOk){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 手机端司机签到接口(暂时预留,功能未实现)
	 * @return
	 * @throws Exception
	 */
	public String valSignInForApp() throws Exception{
		String loginName = getParameter("loginName");
		String loginPassword = getParameter("loginPassword");
		//通过登录名来获取司机信息
		
		return null;
	}
	
	
	public String cardString() throws Exception{
		String identifier = "194515836";
		String valName = "cssj1000";
		String cardNo = "e5286eb2";
		String uuid = "879879798789798";
		String bk = "";
		
		
 		String diaryUrl = "http://192.168.0.137:8081/shopHttp/services/UserOBDInfoService";
 		Service service = new Service();
 		Call call = (Call) service.createCall();
 		call.setTargetEndpointAddress(new URL(diaryUrl));
 		call.setOperationName("swipeCard");
 		String rec = (String) call.invoke(new Object[]{identifier, valName, cardNo, uuid, bk});
		return rec;
	}
}
