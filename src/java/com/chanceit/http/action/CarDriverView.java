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
 * @Description �˺Ź�����ؽӿ�
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
	 * ������޸ĳ���˾������
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
			//ͨ��ID��ѯ����˾��������Ϣ
			CarDriver carDriver = carDriverService.get(Integer.parseInt(carDriverId));
			if(carDriver == null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����ڴ�id��");
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
	 * ɾ������˾������
	 */
	public String delete() throws Exception{
		String carDriverIds = getParameter("carDriverIds");
		if(StringUtils.isBlank(carDriverIds)){
			return ResultManager.getFaildResult("��������Ҫɾ�����˺�ID����Ӣ�ĵĶ�������");
		}
		Account account = getSessionAccount();
		carDriverService.deleteAll(carDriverIds);
		return ResultManager.getSuccResult();
	}
	
	/**
	 * ��ȡ����˾�������б�
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
		//����ԱȨ��ֻ��ȡ������Ȩ�޵ĳ���ID
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
	 * ��֤д���ӿ�
	 * @return
	 * @throws Exception
	 */
	public String writeCard() throws Exception{
		String loginName = getParameter("loginName");
		String password = getParameter("password");
		String valName = getParameter("valName");
		String cardNo = getParameter("cardNo");
		//д���Ƿ�ɹ���ʶ��
		Account account = accountService.getByName(loginName);
		//��֤�û��Ƿ�ӵ��Ȩ��
		if(account != null && account.getAccountPwd().equals(Coder.encryptMD5(password))) {
			Driver driver = driverService.getDriverByValName(valName);
			if(driver != null){
				//���Ų�ͬ������½�˾���Ϳ��Ű�
				driverService.updateDriverByCondition(new Object[]{cardNo, valName});
				return ResultManager.getFaildResult(EnumCommon.WRITECARD_SUCCESS, "д���ɹ���");
			}else{
				return ResultManager.getFaildResult(EnumCommon.WRITECARD_FAILE, "˾���������ڣ�");
			}
		}else{
			return ResultManager.getFaildResult(EnumCommon.WRITECARD_FAILE, "�û������������");
		}
	}
	
	/**
	 * д���ӿ�
	 * @return
	 * @throws Exception
	 */
	/*public String writeCard() throws Exception{
		String valName = getParameter("valName");
		String cardNo = getParameter("cardNo");
		try{
			//���Ų�ͬ������½�˾���Ϳ��Ű�
			driverService.updateDriverByCondition(new Object[]{cardNo, valName});
			return ResultManager.getFaildResult(EnumCommon.WRITECARD_SUCCESS, "�󶨳ɹ���");
		}catch(Exception e){
			return ResultManager.getFaildResult(EnumCommon.WRITECARD_FAILE, "��ʧ�ܣ�");
		}
	}*/
	
	/**
	 * ˾��ǩ���ӿ�
	 * @return
	 * @throws Exception
	 */
	public Boolean valSignIn(String identifier, String cardNo, String valName,
			Date signInTime) throws Exception{
		//String identifier = getParameter("identifier");
		//String cardNo = getParameter("cardNo");
		//String valName = getParameter("valName");
		User car = userService.getByName(identifier);
		//ˢ���Ƿ�Ϸ���ʶ��
		int ifLegal = 0;
		//ˢ���Ƿ�ɹ���ʶ��
		boolean isOk = false;
		//ͨ����¼���Ϳ�������ȡ˾����Ϣ
		Driver driver = driverService.getDriverByCondition(new Object[]{valName, cardNo});
		if(driver != null){
			
			Integer driverId = driver.getDriverId();
			Integer carId = car.getVehicleId();
			//ͨ��˾��id�ͳ���id����ȡ���Ӧ�ĳ���˾����֤����
			CarDriver carDriver = carDriverService.getInfoByBothId(driverId, carId);
			//��������Ӧ����֤�������˾��Ȩ��Ϊ1(��վ����Աʱ),��˾���복������,���û������ʾ��Ȩ��
			if(carDriver != null || (driver.getLevel() != null && driver.getLevel() == 1)){
				
				Driver bindDriver = car.getDriver();
				//�������δ��˾��,���,����Ѱ󶨾��ж��Ƿ����˾��
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
				//���úϷ��û�
				ifLegal = 1;
				isOk = true;
			}
		}else{
			driver = driverService.getDriverByValName(valName);
		}
		
		//ͨ������ID��ȡ�ó����һ����־��Ϣ,�����ͬһ˾��,�򲻲�����Ϣ,��ֹƵ��ˢ���Ķ�β���������־
		SignInLog signLog = signInLogService.getLastDriverByCarId(car.getVehicleId());
		if(signLog != null && signLog.getDriver().getDriverId() != driver.getDriverId()){
			//�����־��Ϣ
			SignInLog log = new SignInLog();
			log.setCar(car);
			log.setDriver(driver);
			//1ΪȨ���û�
			log.setIfLegal(ifLegal);
			//0Ϊ����Ա����,1Ϊ˾���򿨲���
			log.setFromType(1);
			log.setSignInTime(signInTime);
			//������־
			signInLogService.save(log);
			//���ó����һ����Ϣ��ӽ��ʱ��
			signLog.setUnbindTime(signInTime);
			signInLogService.save(signLog);
		}
		/*if(isOk){
			return ResultManager.getFaildResult(EnumCommon.READCARD_SUCCESS, "ˢ���ɹ���");
		}else{
			return ResultManager.getFaildResult(EnumCommon.READCARD_FAILE, "ˢ��ʧ��, ������˾��δ�󶨹�ϵ��");
		}*/
		if(isOk){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * �ֻ���˾��ǩ���ӿ�(��ʱԤ��,����δʵ��)
	 * @return
	 * @throws Exception
	 */
	public String valSignInForApp() throws Exception{
		String loginName = getParameter("loginName");
		String loginPassword = getParameter("loginPassword");
		//ͨ����¼������ȡ˾����Ϣ
		
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
