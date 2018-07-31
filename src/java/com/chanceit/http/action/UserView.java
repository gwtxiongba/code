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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.ClientFactory;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.JedisService;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Driver;
import com.chanceit.http.pojo.SignInLog;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.pojo.User;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ISignInLogService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @ClassName AccountView
 * @author Administrator
 * @date Jun 26, 2013 2:28:41 PM
 * @Description �˺Ź�����ؽӿ�
 */
@Component("userView")
public class UserView extends BaseAction {
	private String charSet="UTF-8";// �ַ���
	private int connectionTimeOut = 30000;// ���ӳ�ʱʱ��
	private int readTimeOut = 40000;// ��ȡ��ʱʱ��
	
	 static double DEF_PI = 3.14159265359; // PI
     static double DEF_2PI= 6.28318530712; // 2*PI
     static double DEF_PI180= 0.01745329252; // PI/180.0
     static double DEF_R =6370693.5; // radius of earth
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	
	@Autowired
	@Qualifier("jedisService")
	private JedisService jedisService;
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	@Autowired
	@Qualifier("opTeamService")
	private IOperatorTeamService opTeamService;
	
	@Autowired
	@Qualifier("signInLogService")
	private ISignInLogService signInLogService;
	
	//�켣�ط�url
	private String pointUrl;
	
	//�г��ռ�url
	private String diaryUrl;
	
	//���Ͷϵ����url
	private String opCarUrl;
	private String ovspUrl;
	private String httpURL = "http://127.0.0.1:8080/shopHttp/api.action?";
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	public String golbal() throws Exception{
		String key = getParameterNoCheck("key");
		Account account = null;
		account = getSessionAccount();
		if(account==null){
			return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
		}
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		if("company".equals(key) || StringUtils.isBlank(key)){
			if(account.getParentId()==0){
				json1.put("id", account.getAccountId());
				json1.put("name", account.getCompanyName());
				json1.put("address", account.getAddress());
				json1.put("phone", account.getAccountTel());
				json1.put("email",  StringUtils.isBlank(account.getEmail())?"":account.getEmail());
				json.put("company",json1);
			}else{
				Account accountParent = accountService.get(account.getParentId());
				json1.put("id", account.getParentId());
				json1.put("name", accountParent.getCompanyName());
				json1.put("address", accountParent.getAddress());
				json1.put("phone", accountParent.getAccountTel());
				json1.put("email",  StringUtils.isBlank(accountParent.getEmail())?"":accountParent.getEmail());
				json.put("company",json1);
			}
		}
		if("teams".equals(key) || StringUtils.isBlank(key)){
			String accountId;
			//json2 = userService.getJson(accountId);
//			json.put("teams", teamService.getJson(Integer.parseInt(accountId)));
			//json.put("teams", teamService.getJson(Integer.parseInt(accountId)));
//			if(account.getRole().getRoleId() != 2){
//				accountId = getCompanyId();
//			}else{
				accountId = account.getAccountId().toString();
//			}
			//json.put("teams", teamService.getJson(account, Integer.parseInt(accountId)));
			return teamService.getJson(account, Integer.parseInt(account.getAccountId().toString()),0).toString();
		}
		if("uteams".equals(key) || StringUtils.isBlank(key)){
			String accountId;
			//json2 = userService.getJson(accountId);
//			json.put("teams", teamService.getJson(Integer.parseInt(accountId)));
			//json.put("teams", teamService.getJson(Integer.parseInt(accountId)));
//			if(account.getRole().getRoleId() != 2){
//				accountId = getCompanyId();
//			}else{
				accountId = account.getAccountId().toString();
//			}
			//json.put("uteams", teamService.getJson(account, Integer.parseInt(accountId),1));
			return teamService.getJson(account, Integer.parseInt(account.getAccountId().toString()),1).toString();	
		}
		return teamService.getJson(account, Integer.parseInt(account.getAccountId().toString()),0).toString();	
	}
	
	public String saveMonitor() throws Exception{
		String ifMonitor = getParameterNoCheck("ifMonitor");
		String vehicleIds = getParameter("vehicleIds");
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		if(StringUtils.isBlank(ifMonitor)){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"������ifMonitor");
		}
		if(StringUtils.isBlank(vehicleIds)){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"������vehicleIds");
		}
		if("1".equals(ifMonitor) || "0".equals(ifMonitor)){
			userService.saveMonitor(Integer.parseInt(ifMonitor), vehicleIds,Integer.parseInt(accountId));
			return ResultManager.getSuccResult(); 
		}else{
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"ifMonitor�����쳣");
		}
	}
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @throws Exception 
	 * @Description ������ҳ�б� listVehicle
	 */
	public String pageList(Page page) throws Exception{
		String teamId = getParameterNoCheck("teamId");
		String plate = getParameterNoCheck("plate");
		String identifier = getParameterNoCheck("identifier");
		String sortF = getParameterNoCheck("sortF");
		String flag = getParameterNoCheck("flag");
		String accountId;
		Account account = getSessionAccount();
		accountId = account.getAccountId().toString();
		Object[] keywords ;
//		if(account.getRole().getRoleId() == 3){
//		String sql=" select * from team  where FIND_IN_SET(team_id, getChildLst("+account.getTeam().getTeamId()+")) ";
//		List list=teamService.getListSql(sql, null);
		String teamIds="";
//		for(int i=0;i<list.size();i++){
//			Map m=(Map) list.get(i);
//			teamIds+=(Integer) m.get("team_id")+",";
//		}
		teamIds = teamService.getTeamIdStr(account);
//		for(Object obj: list){
//			Team opTeam = (Team)obj;
//				teamIds += opTeam.getTeamId() + ",";
//		}
//		if(!"".equals(teamIds)){
//			teamIds = teamIds.substring(0, teamIds.length()-1);
//		}
//					String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{account.getTeam().getTeamId(),plate,teamId,identifier,teamIds,sortF,flag};
//		}else{
//			keywords = new Object[]{accountId,plate,teamId,identifier,null};
//		}
		//Object[] keywords = new Object[]{accountId,plate,teamId,identifier};
		page = userService.getUsersPage(page, keywords);
		return ResultManager.getBodyResult(page);
	}

	public String getOnlineList() throws Exception{
//		String ifOnline = getParameterNoCheck("ifOnline");
		String accountId;
		Account account = getSessionAccount();
		accountId = account.getAccountId().toString();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
		Object[] keywords;
		//����ԱȨ��ֻ��ȡ������Ȩ�޵ĳ���ID
//		if(account.getRole().getRoleId() == 3){
//			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
//			keywords = new Object[]{accountId,null,teamIds};
//		}else{
		String teamids = teamService.getTeamIdStr(account);
			keywords = new Object[]{null,null,teamids};
//		}
		
		return ResultManager.getBodyResult(userService.getOnlineList(keywords));
	}

	public String getRelayList(Page page) throws Exception{
//		String ifOnline = getParameterNoCheck("ifOnline");
		String plate = getParameterNoCheck("plate");
		String identifier = getParameterNoCheck("identifier");
		String accountId;
		Account account = getSessionAccount();
		accountId = account.getAccountId().toString();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
//			accountId = account.getAccountId().toString();
//		}
		Object[] keywords;
		//����ԱȨ��ֻ��ȡ������Ȩ�޵ĳ���ID
		if(account.getRole().getRoleId() == 3){
			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{accountId,teamIds,plate,identifier};
		}else{
			keywords = new Object[]{accountId,null,plate,identifier};
		}
		page = userService.getRelayList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	/**
	 * @des ��صĳ����б�
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception{
		String teamId = getParameterNoCheck("teamId");
		String plate = getParameterNoCheck("plate");
		String accountId;
		String parentId;
		Account account = getSessionAccount();
		String teamids = teamService.getTeamIdStr(account);
//		if(account.getRole().getRoleId() == 3){
//			accountId = account.getAccountId().toString();
//			parentId = getCompanyId();
//			//account.getAccountId()
//			Object[] keywords = new Object[]{parentId,accountId,plate};
//			return ResultManager.getBodyResult(userService.getListForOperator(keywords));
//		}else{
//			if(account.getRole().getRoleId() != 2){
//				accountId = getCompanyId();
//			}else{
				accountId = account.getAccountId().toString();
//			}
			
			Object[] keywords = new Object[]{null,plate,teamids};
			return ResultManager.getBodyResult(userService.getList(keywords));
//		}
	}
	
	/**
	 * @des ��صĳ����б�
	 * @return
	 * @throws Exception
	 */
	public String monitorListForApp() throws Exception{
		String teamId = getParameterNoCheck("teamId");
		String plate = getParameterNoCheck("plate");
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() == 3){
			accountId = account.getAccountId().toString();
			//account.getAccountId()
			Object[] keywords = new Object[]{accountId,accountId,plate};
			String data = ResultManager.getBodyResult(userService.getListForOperator(keywords));
			return ResultManager.getResultForApp(true, data);
		}else{
			if(account.getRole().getRoleId() != 2){
				accountId = getCompanyId();
			}else{
				accountId = account.getAccountId().toString();
			}
			Object[] keywords = new Object[]{accountId,plate,teamId};
			String data = ResultManager.getBodyResult(userService.getList(keywords));
			return ResultManager.getResultForApp(true, data);
		}
	}
	
	/**
	 * @author Administrator
	 * @date Jun 26, 2013
	 * @return
	 * @throws Exception 
	 * @Description ������߸���
	 */
	public String save() throws Exception{
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		//update by zhangxin 2014-8-18
		//����Ա�򳬼�����ԱȨ�޲��ܲ���
		//if(account.getParentId()!= 0){
//		if(account.getRole().getRoleId() == 3){
//			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"����Ȩִ�иò���");
//		}
		Object[] keywords = new Object[]{accountId};
//		Long count = userService.getCount(keywords);
//		if(count > 1000) ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"�Ѿ������������");
		String cid = getParameter("vehicleId");
		String identifier = getParameter("identifier");
		String plate = getParameter("plate");
		String type = getParameter("type");
		String driverId = getParameter("driverId");
		String ifMonitor = getParameterNoCheck("ifMonitor");
		//String ifRelay = getParameter("ifRelay");
		String cutpwd = getParameter("cutpwd");
		String discc = getParameter("discc");
		String teamId = getParameter("teamId");
		String brand = getParameter("brand");
		String start_price = getParameter("startPrice");
		String km_price = getParameter("kmPrice");
		String low_price = getParameter("lowPrice");
		String reg_time = getParameter("regTime");
		String buy_time = getParameter("buyTime");
		String user_number = getParameter("userNumber");
		String online = getParameter("online");
		String tel=getParameter("tel");
		String iccid=getParameter("iccid");
		String carName=getParameter("carModel");
		String banche = getParameter("banche");
		String line = getParameter("line");
		User user = null;
		if(StringUtils.isBlank(identifier)) throw new CommonException("ʶ���������д");
		if(!checkIdentifier(identifier)) throw new CommonException("��������ȷ���豸ʶ����");
//		if(!StringUtils.isEmpty(discc)){
//			String param = "cmd=discc&userid=" + identifier + "&discc=" + discc;
//			String rec = crawlPost(httpURL, param, "utf-8");
//		  JSONObject js = new JSONObject(rec);
//		if(js.has("code")){
//			throw new CommonException("�������쳣���ϴ�����ʧ��");
//		}
//		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isNotBlank(cid)){//��ʾ����
			user = userService.get(Integer.parseInt(cid));
			if(user == null) throw new CommonException("����id��Ч");
			
			//�޸ĳ���״̬   ������onlineʱ�л�����״̬
			if(!StringUtils.isEmpty(online)){
				user.setOnline(Short.parseShort(online));
				userService.save(user);
				return ResultManager.getSuccResult();
			}
			
			if(StringUtils.isBlank(identifier)) throw new CommonException("ʶ���������д");
			if(!checkIdentifier(identifier)) throw new CommonException("��������ȷ���豸ʶ����");
			
			user.setAccount(accountService.get(Integer.parseInt(accountId)));
			if(userService.ifExist(identifier,Integer.parseInt(accountId),cid)){
				throw new CommonException("��ʶ�����Ѵ���");
			}
			if(userService.ifExistPlate(plate, Integer.parseInt(accountId), cid)){
				throw new CommonException("�ó����Ѵ���");
			}
			
			if(plate != null){
				if(StringUtils.isEmpty(plate)) throw new CommonException("���Ʊ�����д");
				user.setPlate(plate);
			}
			//user.setIdentifier(identifier);
			
			if(StringUtils.isNotBlank(type) ){
				user.setType(Integer.parseInt(type));
			}
			if(StringUtils.isNotBlank(driverId)){
				Driver driver = new Driver();
				driver.setDriverId(Integer.parseInt(driverId));
				user.setDriver(driver);
			}else{
				user.setDriver(null);
			}
			if(StringUtils.isNotBlank(ifMonitor)){
				user.setIfMonitor(Short.parseShort(ifMonitor));
			}
			user.setUpTime(new Date());
			user.setIfRelay((short)0);
			if(cutpwd !=null && !"".equals(cutpwd)){
				user.setCutpwd(cutpwd);
			}else{
				user.setCutpwd(null);
			}
			
			if(!StringUtils.isEmpty(discc)){
				user.setDiscc(discc);
			}
			if(!StringUtils.isEmpty(brand)){
				user.setBrand(brand);
			}
			if(!StringUtils.isEmpty(start_price)){
				user.setStartPrice(Double.parseDouble(start_price));
			}
			if(!StringUtils.isEmpty(km_price)){
				user.setKmPrice(Double.parseDouble(km_price));
			}
			if(!StringUtils.isEmpty(low_price)){
				user.setLowPrice(Double.parseDouble(low_price));
			}
			if(!StringUtils.isEmpty(user_number)&& !user_number.equals("0")){
				user.setUserNumber(Integer.parseInt(user_number));
			}
			if(!StringUtils.isEmpty(reg_time)){
				user.setRegTime(dateFormat.parse(reg_time+" 00:00:00"));
			}
			if(!StringUtils.isEmpty(buy_time)){
				user.setBuyTime(dateFormat.parse(buy_time+" 00:00:00"));
			}
			if(!StringUtils.isEmpty(teamId) && !teamId.equals("0")){
				Team team = teamService.get(Integer.parseInt(teamId));
				if(team == null)   throw new CommonException("��ѡ��������");
				user.setTeam(team);
			}else{
				user.setTeam(null);
			}
			//user.setTeam(null);
			
			if(!StringUtils.isEmpty(tel)){
				user.setTel(tel);
			}
			if(!StringUtils.isEmpty(carName)){
				user.setCarModel(carName);
			}
			if(!StringUtils.isEmpty(iccid)){
				user.setIccid(iccid);
			}
			if(!StringUtils.isEmpty(banche)){
				user.setLevel(1000);
				
				if(!StringUtils.isEmpty(line)){
					user.setLineGps(line);
				}
			}
//			if(!StringUtils.isEmpty(online)){
//				user.setOnline(Short.parseShort(online));
//			}else{
//				user.setOnline((short)0);	
//			}
			userService.save(user);
			
			//ͨ������ID��ȡ�ó����һ����־��Ϣ,�����ͬһ˾��,�򲻲�����Ϣ,��ֹƵ��ˢ���Ķ�β���������־
//			SignInLog signLog = signInLogService.getLastDriverByCarId(user.getVehicleId());
//			//�����˾��,��д����־
//			if(user.getDriver() != null && signLog != null && 
//					signLog.getDriver().getDriverId() != user.getDriver().getDriverId()){
//				SignInLog log = new SignInLog();
//				log.setCar(user);
//				log.setDriver(user.getDriver());
//				//1ΪȨ���û�
//				log.setIfLegal(1);
//				//0Ϊ����Ա����,1Ϊ˾���򿨲���
//				log.setFromType(0);
//				log.setSignInTime(new Date());
//				signInLogService.save(log);
//				//���ó����һ����Ϣ��ӽ��ʱ��
//				signLog.setUnbindTime(new Date());
//				signInLogService.save(signLog);
//			}else if(user.getDriver() == null && signLog != null ){//������˾��,����ӽ��ʱ��
//				//���ó����һ����Ϣ��ӽ��ʱ��
//				signLog.setUnbindTime(new Date());
//				signInLogService.save(signLog);
//			}else if(user.getDriver() != null && signLog == null){//����ó���һ�ΰ�˾����Ϊƽ̨�򿨣����һ����־��Ϣ
//				SignInLog log = new SignInLog();
//				log.setCar(user);
//				log.setDriver(user.getDriver());
//				//1ΪȨ���û�
//				log.setIfLegal(1);
//				//0Ϊ����Ա����,1Ϊ˾���򿨲���
//				log.setFromType(0);
//				log.setSignInTime(new Date());
//				signInLogService.save(log);
//			}
		} else {
//			if(userService.acountCar(account.getAccountId(),account.getLevel().getLevelId())){
//				return ResultManager.getFaildResult("����ӵĳ��������ѵ����ֵ��");
//			}
			user = new User();
			if(!checkIdentifier(identifier)) throw new CommonException("��������ȷ���豸ʶ����");
			if(StringUtils.isEmpty(plate)) throw new CommonException("���Ʊ�����д");
			if(StringUtils.isEmpty(identifier)) throw new CommonException("ʶ���������д");
			
			if(userService.ifExist(identifier,Integer.parseInt(accountId),null)){
				throw new CommonException("��ʶ�����Ѵ���");
			}
			
			if(userService.ifExistPlate(plate, Integer.parseInt(accountId), null)){
				throw new CommonException("�ó����Ѵ���");
			}
//			user = userService.getPointUser(identifier,user);
//			if(user==null){
//				throw new CommonException("�ó�����λ�õ�");
//			}
			user.setAccount(accountService.get(Integer.parseInt(accountId)));
			user.setIdentifier(identifier);
			user.setPlate(plate);
			if(StringUtils.isNotBlank(type) ){
				user.setType(Integer.parseInt(type));
			}
			
			if(StringUtils.isNotBlank(driverId) ){
				Driver driver = new Driver();
				driver.setDriverId(Integer.parseInt(driverId));
				user.setDriver(driver);
			}
			if(StringUtils.isNotBlank(ifMonitor)){
				user.setIfMonitor(Short.parseShort(ifMonitor));
			}
			user.setCreateTime(new Date());
			user.setIfDel((short)0);
			user.setIfRelay((short)0);
//			if(!StringUtils.isEmpty(discc)){
//				String param = "cmd=discc&userid=" + identifier + "&discc=" + discc;
//				String rec = crawlPost(httpURL, param, "utf-8");
//			  JSONObject js = new JSONObject(rec);
//			if(js.has("code")){
//				throw new CommonException("�������쳣���ϴ�����ʧ��");
//			}
//			}
			if(cutpwd !=null && !"".equals(cutpwd)){
				user.setCutpwd(cutpwd);
			}else{
				user.setCutpwd(null);
			}
			if(!StringUtils.isEmpty(discc)){
				user.setDiscc(discc);
			}
			
			if(!StringUtils.isEmpty(brand)){
				user.setBrand(brand);
			}
			if(!StringUtils.isEmpty(start_price)){
				user.setStartPrice(Double.parseDouble(start_price));
			}
			if(!StringUtils.isEmpty(km_price)){
				user.setKmPrice(Double.parseDouble(km_price));
			}
			if(!StringUtils.isEmpty(low_price)){
				user.setLowPrice(Double.parseDouble(low_price));
			}
			if(!StringUtils.isEmpty(user_number)&& !user_number.equals("0")){
				user.setUserNumber(Integer.parseInt(user_number));
			}
			if(!StringUtils.isEmpty(reg_time)){
				user.setRegTime(dateFormat.parse(reg_time+" 00:00:00"));
			}
			if(!StringUtils.isEmpty(buy_time)){
				user.setBuyTime(dateFormat.parse(buy_time+" 00:00:00"));
			}
			
			if(!StringUtils.isEmpty(teamId) && !teamId.equals("0")){
				Team team = teamService.get(Integer.parseInt(teamId));
				if(team == null) throw new CommonException("��ѡ��������");
				user.setTeam(team);
			}else{
				user.setTeam(null);
			}
			if(!StringUtils.isEmpty(tel)){
				user.setTel(tel);
			}
			if(!StringUtils.isEmpty(iccid)){
				user.setIccid(iccid);
			}
			if(!StringUtils.isEmpty(carName)){
				user.setCarModel(carName);
			}
			if(!StringUtils.isEmpty(banche)){
				user.setLevel(1000);
				if(!StringUtils.isEmpty(line)){
					user.setLineGps(line);
				}
			}
			userService.save(user);
			user.setOnline((short)0);
			//�����˾��,��д����־
//			if(user.getDriver() != null){
//				SignInLog log = new SignInLog();
//				log.setCar(user);
//				log.setDriver(user.getDriver());
//				//1ΪȨ���û�
//				log.setIfLegal(1);
//				//0Ϊ����Ա����,1Ϊ˾���򿨲���
//				log.setFromType(1);
//				log.setSignInTime(new Date());
//				signInLogService.save(log);
//			}
		}
	
		return ResultManager.getSuccResult(); 
	}
	
	/**
	 * �޸ĳ���teamId
	 * @return
	 * @throws Exception
	 */
	public String updateTeam() throws Exception{
		String uid=getParameter("uid");//����id
		String teamId=getParameter("teamId");//�������teamId
		if(!StringUtils.isBlank(uid)){
			User user=userService.get(Integer.parseInt(uid));
			if(!StringUtils.isBlank(teamId)){
				Team team = null;
				if("root".equals(teamId)){
					 team = getSessionAccount().getTeam();
				}else{
				 team = teamService.get(Integer.parseInt(teamId));
				}
				user.setTeam(team);
				userService.save(user);
			}else{
				throw new CommonException("�뽫�����϶�����Ӧ�Ļ���");
			}
		}else{
			throw new CommonException("�ó���������");
		}
		return ResultManager.getSuccResult();
	}
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @Description ɾ��
	 */
	public String delete(){
		String accountId;
		Account account = getSessionAccount();
			accountId = account.getAccountId().toString();
		String userIds = getParameter("vehicleIds");
		if(StringUtils.isEmpty(userIds)){
			return ResultManager.getFaildResult("��������Ҫɾ�����˺�ID����Ӣ�ĵĶ�������");
		}
		userService.fakeDelete(userIds,Integer.parseInt(accountId));
		return ResultManager.getSuccResult();
	}
	
	/**
	 * @des ��ǰ���ӷ��鳵������ δ���鳵�� �б�
	 * @return
	 * @throws Exception
	 */
	public String getList() throws Exception {
		String teamId = getParameterNoCheck("teamId");
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		if(StringUtils.isBlank(teamId)) {
			return ResultManager.getBodyResult(userService.getList(accountId,0)).replace("null", "0");
		}else{
			return ResultManager.getBodyResult(userService.getList(accountId,Integer.parseInt(teamId))).replace("null", "0");
		}
	}
	
	public String listBrandBySytle() throws Exception{
		String styleId = getParameter("styleId");
		List list = userService.listBrand(getSessionAccount(), Integer.parseInt(styleId));
		return ResultManager.getBodyResult(list);
	}
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @Description ��ʾ����
	 */
	public String view(){
		String userId = getParameter("vehicleId");
		User users = null;
		if(StringUtils.isNotEmpty(userId)){
			users = userService.get(Integer.parseInt(userId));
		} else {
			return ResultManager.getFaildResult("��Чid");
		}
		return ResultManager.getBodyResult(users);
	}
	public String driverDetail(){
		String driverId = getParameter("driverId");
		if(StringUtils.isBlank(driverId)) return ResultManager.getFaildResult("������driverId");
		Object[] keywords = new Object[]{driverId};
		List list = userService.getDriverDetailed(keywords);
		return ResultManager.getBodyResult(list);
	}
	
	
	public String vehiclesOnlineCount()  throws Exception{
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
//		return "{\"total\":123,\"onlines\":23}";
		return userService.getOnlineAmount(getSessionAccount(), accountId).toString();// {"total":123,"onlines":23}
	}
	
	public String vehiclesRelayCount()  throws Exception{
		String accountId;
		Account account = getSessionAccount();
		accountId = account.getAccountId().toString();
		Object[] keywords;
		if(account.getRole().getRoleId() == 3){
			String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
			keywords = new Object[]{getSessionAccount(),teamIds,accountId};
		}else{
			keywords = new Object[]{getSessionAccount(),null,accountId};
		}
		return userService.getRelayAmount(keywords).toString();// {"total":123,"onlines":23}
	}
	
	
	/**
	 * @author Administrator
	 * @date Aug 26, 2013
	 * @param identifier
	 * @return
	 * @Description �豸ʶ�����У�鷽��
	 */
	protected boolean checkIdentifier(String identifier){
		if((identifier.length()==5 || identifier.length()==11) && identifier.matches("[0-9]{1,}")){
			return true;
		}
		return checkBoxIdentifier(identifier) || checkNaviIdentifier(identifier);
	}
	
	/**
	 * @author Administrator
	 * @date Sep 12, 2013
	 * @param identifier
	 * @return 
	 * @Description ����Ƿ��Ǻ����û�
	 */
	private boolean checkBoxIdentifier(String identifier){
		if(identifier == null || identifier.length() != 9) return false;
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
		if(head != 5){
			if((total % 2) == 0){
				if(head != 2) return false;
			} else {
				if(head != 1) return false;
			}
		}
		int exp = (head^i1^i2^i3^i4^i5^i6^i7)%10;
		if(end != exp) return false;
		return true;
	}
	
	/**
	 * @author Administrator
	 * @date Sep 12, 2013
	 * @param identifier
	 * @return
	 * @Description ����Ƿ��ǵ����û�
	 */
	private boolean checkNaviIdentifier(String identifier){
		if(identifier == null || identifier.length() != 9) return false;
		int head = Character.getNumericValue(identifier.charAt(0));
		int i1 = Character.getNumericValue(identifier.charAt(1));
		int i2 = Character.getNumericValue(identifier.charAt(2));
		int i3 = Character.getNumericValue(identifier.charAt(3));
		int i4 = Character.getNumericValue(identifier.charAt(4));
		int i5 = Character.getNumericValue(identifier.charAt(5));
		int i6 = Character.getNumericValue(identifier.charAt(6));
		int i7 = Character.getNumericValue(identifier.charAt(7));
		int end = Character.getNumericValue(identifier.charAt(8));
		if((i1 == i2 && i2 == i3)|| (i2 == i3 && i3 == i4)|| (i3 == i4 && i4 == i5)|| 
				(i4 == i5 && i5 == i6)|| (i5 == i6 && i6 == i7))
			return false;
		int total = i1 + i2 + i3 + i4 + i5 + i6 + i7;
		if((total % 2) == 0 ){
			if(head != 4) return false;
		} else {
			if(head != 3) return false;
		}
		int exp = (head^i1^i2^i3^i4^i5^i6^i7)%10;
		if(end != exp) return false;
		return true;
	}

	public String mapdata() throws Exception{
		// TODO Auto-generated method stub
		String identifier = getParameter("identifier");
		String callback = getParameter("callback");
		User user = userService.getByName(identifier);
		JSONObject json = new JSONObject();
		String coorInfo = jedisService.getUidInfo(identifier);
		if(user==null || coorInfo==null){
			json.put("status", 1);
			json.put("total", 0);
			json.put("size", 0);
			JSONArray jsonAry = new JSONArray();
			json.put("contents",jsonAry);
		}else{
			json.put("status", 0);
			json.put("total", 1);
			json.put("size", 1);
			JSONArray jsonAry = new JSONArray();
			JSONObject subJson = new JSONObject();
			JSONObject coorJson;
			coorJson = new JSONObject(coorInfo);
//				double[] coordinate = new double[2];
//				coordinate[0] = (Double)coorJson.get("lng");
//				coordinate[1] = (Double)coorJson.get("lat");
			subJson.put("location", "["+coorJson.get("bdx").toString()+","+coorJson.get("bdy").toString()+"]");//"online":0,"angle":0,"time":"2014-02-08 14:34:00","speed":0,
			subJson.put("online", coorJson.get("online"));
			subJson.put("angle", coorJson.get("angle"));
			subJson.put("time", coorJson.get("time"));
			subJson.put("speed", coorJson.get("speed"));
			
			JSONObject carJson = new JSONObject(ResultManager.getBodyResult(user));
			subJson.put("vehicle", carJson);
			jsonAry.put(subJson);
			json.put("contents",jsonAry);
		}
		
		if(StringUtils.isNotBlank(callback)){
			String rs = callback+"&&"+callback +"("+json.toString()+")";
			return rs.replace("\"[", "[").replace("]\"", "]");
		}else{
			String  result = json.toString().replace("\"[", "[").replace("]\"", "]");
			System.out.println(result);
			return json.toString().replace("\"[", "[").replace("]\"", "]");
		}
	}
	
	
	/**
	 * ��ȡλ�õ���Ϣ
	 * add by zhangxin 2014-07-16
	 * @return
	 * @throws Exception
	 */
	public String getPoint() throws Exception{
		String vehicleId = getParameter("vehicleId");
		User user = userService.get(Integer.parseInt(vehicleId));
		String identifier = user.getIdentifier();
		
		String strDate = getParameter("date"); 
		String endDate = getParameter("date1"); 
		//String endDate = strDate.replaceAll("-", "");
		//String beginDate = beginDateStr + "0000";
		long t1 = getT(strDate);
		long t2 = getT(endDate);
		if((t2-t1)>3600000*24*3){
		  return	ResultManager.getFaildResult(1, "ֻ�ܲ�ѯ3�����ڵĹ켣");	
		}
		//String endDateStr = addDate(strDate, 1);
		//String endDate = endDateStr.replaceAll("-", "") + "0000";
		
		//String uid = "153206344";
		//String uid = "253039860";
		//String url = "http://localhost:88/testmysql.php?";
		String url = pointUrl;
		String param = "uid=" + identifier + 
				   	   "&getBeginDate=" + strDate + 
				       "&getEndDate=" + endDate;
		String rec = crawlPost(url, param, "utf-8");
		//����json�ַ���
		JSONObject obj = new JSONObject(rec);
		boolean isOK = (Boolean)obj.get("isok");
		int tt = 0;
		List gps = new ArrayList();
		Map<Integer,Integer> flagMap = new HashMap<Integer, Integer>();
		
		if(isOK){
			JSONArray pointArray = (JSONArray)obj.get("points");
			//System.out.println(pointArray.length());
			//gps = new Object[1][pointArray.length()];
			int flag1 = 0;
			int flag2 = 0;
			for(int i = 0;i < pointArray.length();i++){  
				JSONObject jo = pointArray.getJSONObject(i);
				int t11 =  jo.getInt("tt");	
				flagMap.put(t11, 0);
			}
		    for(int i = 0;i < pointArray.length()-1;i++){  
		    	JSONObject jo = pointArray.getJSONObject(i);
		    	JSONObject jo2 = pointArray.getJSONObject(i+1);
		    	String x = "";
		    	String y = "";
		    	String x2 = "";
		    	String y2 = "";
		    	boolean base64 = (Boolean)obj.get("base64");
		    	//�ж��Ƿ����,������ܾͽ��н��ܴ���
		    	if(base64){
		    		byte[] deX = Base64.decode(jo.getString("x")); 
		    		byte[] deY = Base64.decode(jo.getString("y")); 
		    		x = new String(deX);
		    		y = new String(deY);
		    		byte[] deX2 = Base64.decode(jo2.getString("x")); 
		    		byte[] deY2 = Base64.decode(jo2.getString("y")); 
		    		x2 = new String(deX2);
		    		y2 = new String(deY2);
		    	}else{
		    		x = jo.getString("x");
		    		y = jo.getString("y");
		    		x2 = jo2.getString("x");
		    		y2 = jo2.getString("y");
		    	}
		    	
		    	String time = jo.getString("t");
		    	
		    	int speed = 0;
		    	int speed2 = 0;
		    	int azimuth = 0;
		    	//�ж��������Ƿ��з�λ��
		    	if(jo.get("speed") != null){
		    		speed = jo.getInt("speed");
		    	}
		    	if(jo2.get("speed") != null){
		    		speed2 = jo2.getInt("speed");
		    	}
		    	if(jo.get("azimuth") != null){
		    		//��λ�ǳ���10  Ȼ����������ȡ��
		    		Double azi = jo.getDouble("azimuth")/10;
		    		BigDecimal bd = new BigDecimal(azi).setScale(0, BigDecimal.ROUND_HALF_UP);
		    		azimuth = bd.intValue();
		    	}
//		    	if(i == 0){
//		    		tt =  jo.getInt("tt");
//		    	}else{
//		    	if((jo.getInt("tt") - tt)<10){
//		    		double dis = ;
//		    		if(dis > 1){
//		    			continue;
//		    		}
//		    	}
		    	tt =  jo.getInt("tt");	
		    	int tt2 = jo2.getInt("tt");	
//		    	}
		    	int ft = tt2-tt;
		    	//if(ft<10){
		    	//System.out.println(x+"/"+y+"/"+x2+"/"+y2);
		    	double dis=GetShortDistance(Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(x2),Double.parseDouble(y2));
		    	 double ds = ((speed+speed2)/2)*ft*(1000/3600.0)+30;
//		    	
		    	if(dis>ds && dis>ft*67){
		    		//System.out.println(dis+"///"+ds+"//"+tt+"///"+time);
		    		flagMap.put(tt,flagMap.get(tt)+1);
		    		flagMap.put(tt2,flagMap.get(tt2)+1);
		    		//break;
		    	}else{
		    		flagMap.put(tt,flagMap.get(tt)-1);
		    		flagMap.put(tt2,flagMap.get(tt2)-1);
		    	}
		    	Object[] pointObj = new Object[]{x,y,time,speed,azimuth,tt};
		    	gps.add(pointObj);
		    		
		    }
		}
		List gps2 = new ArrayList();
		for (int j = 0; j < gps.size(); j++) {
			Object[] pointObj = (Object[]) gps.get(j);
			if(flagMap.get(pointObj[5])<0){
				gps2.add(gps.get(j));
			}
		}
//		System.out.println("-----" +gps.size() +"----");
//		System.out.println("-----" +gps2.size() +"----");
		//Object[] pointObj = (Object[]) gps2.get(0);
		return ResultManager.getBodyResult(gps2);
	}


	public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // �Ƕ�ת��Ϊ����
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // ���Ȳ�
        dew = ew1 - ew2;
        // ���綫��������180 �ȣ����е���
        if (dew > DEF_PI)
        dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
        dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // �������򳤶�(��γ��Ȧ�ϵ�ͶӰ����)
        dy = DEF_R * (ns1 - ns2); // �ϱ����򳤶�(�ھ���Ȧ�ϵ�ͶӰ����)
        // ���ɶ�����б�߳�
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
	
	/**
	 * ��ȡ�г��켣
	 * add by zhangxin 2014-07-18
	 * @return
	 * @throws Exception
	 */
	public String getDiaryPoint() throws Exception{
		String vehicleId = getParameter("vehicleId");
		User user = userService.get(Integer.parseInt(vehicleId));
		String in0 = user.getIdentifier();
		String date = getParameter("date"); 
 		String in1 = date.substring(0,7) + "-01";
 		
 		String param="cmd=getDataByMonth_and&identifier="+in0+"&month="+in1;
		String rec=crawlPost(ovspUrl, param, "utf-8");
		//����json�ַ���
		JSONObject obj = new JSONObject(rec);
		if(obj.has("reason")){
			String reason=obj.getString("reason");
			return ResultManager.getFaildResult(reason);
		}
		return rec;
 		
 		
// 		Service service = new Service();
// 		Call call = (Call) service.createCall();
// 		call.setTargetEndpointAddress(new URL(diaryUrl));
// 		call.setOperationName("getDataByMonth_and");
// 		String rec = (String) call.invoke(new Object[]{in0, in1});
// 		return rec;

	}
	
	/**
	 * ��ȡ�г��켣
	 * add by zhangxin 2014-07-18
	 * @return
	 * @throws Exception
	 */
	public String getYearDiaryPoint() throws Exception{
		String vehicleId = getParameter("vehicleId");
		User user = userService.get(Integer.parseInt(vehicleId));
		String in0 = user.getIdentifier();
		String in1 = getParameter("date"); 
		
// 		Service service = new Service();
// 		Call call = (Call) service.createCall();
// 		call.setTargetEndpointAddress(new URL(diaryUrl));
// 		call.setOperationName("getDataByYear");
// 		String rec = (String) call.invoke(new Object[]{in0, in1});
 		
 		String param="cmd=getDataByYear&identifier="+in0+"&dateTime="+in1;
		String rec=crawlPost(ovspUrl, param, "utf-8");
 		return rec;
	}
	
	/**
	 * ���� �� ����
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
		// ���ڼ�һ��Ϊ��������
		ca.add(Calendar.DATE, days);
		
		Date eDate = ca.getTime();
		String addDate = dft.format(eDate);
		return addDate;
	}
	
	public static long getT(String strDate) throws ParseException{
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = dft.parse(strDate);
		
		
		return date.getTime();
	}
	public String  getTeams(){
		Account account = getSessionAccount();
		if(account==null){
			return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
		}
		String accountId = "";
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		List list = teamService.getTeamsList(account, Integer.parseInt(accountId));
		Gson gson = new Gson();
		gson.toJson(list);
		return gson.toJson(list).toString();
	}
	
	/**
	 * ��ȡ�豸����5��ͷ����Ϣ�����ڶϵ�����
	 * add by shaozheng 2014-10-10
	 * @return
	 */
	public String getPhone(){
		String identifier = getParameter("identifier");
		return "";
	}
		
	/**
	 * node���������䳵����ع�����Ϣ����
	 * add by zhangxin 2014-10-14
	 * @return
	 * @throws JSONException 
	 */
	public String getCarInfoState() throws Exception{
		Logger logger = Logger.getLogger(UserView.class);
		String identifier = getParameter("identifier");
		String result = getParameter("result");
		User car = userService.getByName(identifier);
		System.out.println("powerOff---->" + result);
		JSONObject json = new JSONObject(result);
		String uid = json.getString("uid");
		if(!identifier.equals(uid)){
			return ResultManager.getFaildResult("�豸�벻һ��");
		}
		JSONObject json_msg = new JSONObject(json.get("msg").toString());	
		JSONObject json_msg1 = new JSONObject(json_msg.get("msgBody").toString());
		int opcode = json_msg1.getInt("opCode2");
		int tempInt = json_msg1.getInt("tmpInt"); 
		
		if(car != null){
		    //����node��������ˢ��
		    Account account = car.getAccount();
		    int companyId;
		    if(account.getRole().getRoleId() == 3){
		    	companyId = Integer.parseInt(getCompanyId());
		    }else{
		    	companyId = account.getAccountId();
		    }
		    logger.debug("companyId===>" + companyId);
		
			logger.info("-------start------");
		    logger.info("result======>" + result);
		    logger.info("op======>" + opcode);
		    logger.info("------end-------");
			
//			if(((opcode >> 30) & 0x3 )== 2){ 
//				switch(tempInt){ 
//					case 1: 
//						mc.noticeNodeJsServer1(5, companyId, "����������ʻ,ִ��ָ��ʧ��!" );
//						logger.info("------����������ʻ,ִ��ָ��ʧ��------");
//						break; 
//					case 2: 
//						mc.noticeNodeJsServer1(5, companyId, "Ӳ������,ִ��ָ��ʧ��!");
//						logger.info("------Ӳ������,ִ��ָ��ʧ��------");
//						break; 
//					case 3: 
//						mc.noticeNodeJsServer1(5, companyId, "ǰһ��ָ���ȡ��!");
//						logger.info("------ǰһ��ָ���ȡ��------");
//						break; 
//					case 4: 
//						mc.noticeNodeJsServer1(5, companyId, "�������ִ��ָ�ʱ!");
//						logger.info("------�������ִ��ָ�ʱ------");
//						break; 
//					default: 
//						mc.noticeNodeJsServer1(5, companyId, "δ֪����,ִ������ʧ��!");
//						logger.info("------δ֪����,ִ������ʧ��------");
//						break; 
//				} 
//			} else if (((opcode >> 30) & 0x3 ) == 1) { 
//				mc.noticeNodeJsServer1(4, companyId, "ִ������ɹ�!");
//				logger.info("------ִ������ɹ�------");
//			} 
		}
		//op = 1 Ϊ�Ѿ����Ͷϵ�  op = 0Ϊδ���Ͷϵ�
		/*int op = (opcode&(int)Math.pow(2, 8))/(int)Math.pow(2, 8);
		
		System.out.println("op==>" + op);
		logger.info("-------start------");
	    logger.info("result======>" + result);
	    logger.info("op======>" + op);
	    logger.info("------end-------");
	    
	    if(car != null){
		    //����node��������ˢ��
		    Account account = car.getAccount();
		    int companyId;
		    if(account.getRole().getRoleId() == 3){
		    	companyId = Integer.parseInt(getCompanyId());
		    }else{
		    	companyId = account.getAccountId();
		    }
		    logger.debug("companyId===>" + companyId);
		    
		    if((opcode&(int)Math.pow(2, 30))/(int)Math.pow(2, 30) == 1){ 
		    	mc.noticeNodeJsServer1(4, companyId);
		    	 //op = 1 Ϊ�Ѿ����Ͷϵ�  op = 0Ϊδ���Ͷϵ�
				userService.setOff(car.getVehicleId(),op);
				logger.info("------ִ������ɹ�------");
			}else if((opcode & ((int) Math.pow(2, 31) + 1)) / ((int) Math.pow(2, 31) + 1) == 1) { 
				mc.noticeNodeJsServer1(5, companyId);
				logger.info("------ִ������ʧ��------");
			}else{ 
				mc.noticeNodeJsServer1(6, companyId);
				logger.info("------ִ�����ʱ-----");
			}
		    
	    }
	    */
		return "success";
	}
	
	/**
	 * �ͻ���������Ͷϵ�
	 * add by zhangxin 2014-10-14
	 * @return
	 * @throws IOException 
	 */
	public String powerOffAndFuelCut() throws IOException{
		URL url;
		URLConnection con;
		StringBuffer sb;
		String identifier = getParameter("identifier");
		String opPwd = getParameter("opPwd");
		String state = getParameter("state");
		//��֤�Ƿ���������Ƿ���ȷ,����ȷ������ʾ
		Boolean isOk = userService.checkOffPwd(identifier, opPwd);
		if(!isOk){
			return ResultManager.getFaildResult("�������벻��ȷ,����������");
		}
		//uid:ʶ����
		//op:1���Ͷϵ� ���͹���      0�ǲ�ѯ����״̬  
		//s:1�Ƕ��Ͷϵ�   0 �ǻָ�����
		String path = opCarUrl + "/uid:" + identifier + "/op:1/s:" + state;	
		url = new URL(path);
		con = url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		sb=new StringBuffer();
		while(reader.read() != -1){
			sb.append(reader.readLine());
		}
		String openOrClose = new String("{"+sb);
		return openOrClose;
	}
	
	//���ó��ٱ������ٶ�
	public String overSpeed() throws Exception{
		String identifier = getParameter("identifier");
		String overSpeed=getParameter("overSpeed");
//		String[] ids=identifier.split(",");
//		for(int i=0;i<ids.length;i++){
//			System.out.println(i+":"+ids[i]);
//		}
		// http://www.chanceit.cn:8081/api.action?cmd=overSpd&identifier=153117990&overSpd=129
		String param="cmd=overSpd&identifier="+identifier+"&overSpd="+overSpeed;
		String rec=crawlPost(ovspUrl, param, "utf-8");
		//����json�ַ���
		JSONObject obj = new JSONObject(rec);
		if(obj.has("reason")){
			String reason=obj.getString("reason");
			return ResultManager.getFaildResult(reason);
		}
		boolean isOK = (Boolean)obj.get("isok");
		return identifier;
	}
	
	//λ�õ��ϴ�ʱ����
	public String posTime() throws Exception{
		String identifier = getParameter("identifier");
		String pos_time=getParameter("pos_time");
//		String[] ids=identifier.split(",");
//		for(int i=0;i<ids.length;i++){
//			System.out.println(i+":"+ids[i]);
//		}
		// http://www.chanceit.cn:8081/api.action?cmd=overSpd&identifier=153117990&overSpd=129
		//String url="http://127.0.0.1:8080/api.action?cmd=overSpd&";
		String param="cmd=overSpd&identifier="+identifier+"&pos_time="+pos_time;
		String rec=crawlPost(ovspUrl, param, "utf-8");
		//����json�ַ���
		JSONObject obj = new JSONObject(rec);
		if(obj.has("reason")){
			String reason=obj.getString("reason");
			return ResultManager.getFaildResult(reason);
		}
		boolean isOK = (Boolean)obj.get("isok");
//		if(!isOK){
//			boolean code=obj.getBoolean("code");
//			String reason=obj.getString("reason");
//			return ResultManager.getFaildResult(reason);
//		}
		return identifier;
	}
	
	public String getLocationByUids(){
		Account account = getSessionAccount();
		JSONArray resArray = new JSONArray();
		String uidss = "";
		Map map = new HashMap();
		List<User> listUser = null;
		int toatal = 0;
		int onlineCount = 0;
		JSONObject json_res = new JSONObject();
		String teamids = teamService.getTeamIdStr(account);
		try {
			
//			if(account.getRole().getRoleId() == 3){
//				String teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
//				 listUser = userService.getList2(teamIds);
//			}else{
			
		 listUser = userService.getList2(account.getTeam().getTeamId(),teamids);
//			}
		for(int i = 0;i<listUser.size();i++){
			User u = listUser.get(i);
			if(u != null){
			 map.put(u.getIdentifier(), u);	
			}
			if(i==0){
				uidss = u.getIdentifier();
			}else{
				uidss += ","+u.getIdentifier();
			}
		}
		String res = jedisService.getLocationByUidsMe(uidss);
		//System.out.println("*********"+res);
		if(res != null && res != ""){
		
			JSONObject json = new JSONObject(res);
			String uids = json.getString("uids");
//			List<User> list = userService.getUserListToMap(uids);
//			for(int i=0;i<list.size();i++){
//				User u = list.get(i);
//				if(u != null){
//				 map.put(u.getIdentifier(), u);	
//				}
//			}
			JSONArray array = (JSONArray) json.get("res");
			toatal =listUser.size();
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject json_tem = array.getJSONObject(i);
				JSONObject tempJson = new JSONObject();
				JSONObject gps = new JSONObject();
				double x = json_tem.getDouble("bdx");
				double y = json_tem.getDouble("bdy");
			    int speed = json_tem.getInt("speed");
			    int angle = json_tem.getInt("angle");
			    //int time = json_tem.getInt("time");
			    int online = json_tem.getInt("online");
			    if(online == 1){
			    	onlineCount += 1;
			    }
			    long time = Long.parseLong(String.valueOf(json_tem.get("time")));
			    gps.put("x", x);
			    gps.put("y", y);
			    gps.put("angle", angle/10);
			    gps.put("speed", speed);
			    gps.put("time", DateUtil.getDateByMills(time*1000));
			    String uid = json_tem.getString("uid");
			    tempJson.put("identifier", uid);
			    tempJson.put("gps", gps);
			    if(map.get(uid) == null){
			     tempJson.put("vehicle",ResultManager.getBodyResult(map.get(uid)));
			    }else{
			    tempJson.put("vehicle", new JSONObject(ResultManager.getBodyResult(map.get(uid))));
			    }
			    tempJson.put("online", online);
			    resArray.put(tempJson);
			    json_res.put("onlineCount", onlineCount);
				json_res.put("total", toatal);
				json_res.put("res", resArray);
			}
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json_res.toString();
	}
	
	
	public String getPointUrl() {
		return pointUrl;
	}

	public void setPointUrl(String pointUrl) {
		this.pointUrl = pointUrl;
	}

	public String getDiaryUrl() {
		return diaryUrl;
	}

	public void setDiaryUrl(String diaryUrl) {
		this.diaryUrl = diaryUrl;
	}

	public String getOpCarUrl() {
		return opCarUrl;
	}

	public void setOpCarUrl(String opCarUrl) {
		this.opCarUrl = opCarUrl;
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
					charset == null ? this.charSet : charset));// ��ȡ��ҳԴ���룬���û��ָ���ַ�������ʹ��Ĭ�ϵ�UTF-8
			while ((sCurrentLine = reader.readLine()) != null) {
				content += sCurrentLine;
			}
			//System.out.println(content);
		} catch (IOException e) {
			logger.error("����"+url + "ʧ�ܣ�ԭ��:"+e.getMessage());
			close(reader);
		}finally {
			close(reader);
			System.out.println("///////////////////getPoint close");
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

	public String getOvspUrl() {
		return ovspUrl;
	}

	public void setOvspUrl(String ovspUrl) {
		this.ovspUrl = ovspUrl;
	}

	public String getHttpURL() {
		return httpURL;
	}

	public void setHttpURL(String httpURL) {
		this.httpURL = httpURL;
	}
	
}
  