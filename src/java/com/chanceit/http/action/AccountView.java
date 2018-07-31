/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.mail.SmtpMailSend;
import com.chanceit.framework.utils.Coder;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.RandomNumUtil;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.ConfigInfo;
import com.chanceit.http.pojo.Dept;
import com.chanceit.http.pojo.Level;
import com.chanceit.http.pojo.OperatorTeam;
import com.chanceit.http.pojo.PasswordLog;
import com.chanceit.http.pojo.Role;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IDeptService;
import com.chanceit.http.service.ILevelService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;
import com.chanceit.http.service.IpcodeCensusService;
import com.chanceit.http.service.TeamService;

/**
 * @ClassName AccountView
 * @author Administrator
 * @date Jun 26, 2013 2:28:41 PM
 * @Description �˺Ź�����ؽӿ�
 */
@Component("accountView")
public class AccountView extends BaseAction {
	
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
	@Qualifier("teamService")
	private ITeamService teamService;
	@Autowired
	@Qualifier("deptService")
	private IDeptService deptService;
	@Autowired
	@Qualifier("pcodeCensusService")
	private IpcodeCensusService pcodeCensusService;
	
	private final int verificationCount=4;
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	public String edit() throws Exception{
//		String accountId = getParameter("companyId");
		String companyName = getParameter("name");
		String shortName = getParameter("shortName");
		String address = getParameter("address");
		String tel = getParameter("phone");
		String email = getParameter("email");
		String gps = getParameter("gps");

		//if(StringUtils.isBlank(accountId)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS,"id����Ϊ��");
		Account account = null;
//		if(StringUtils.isBlank(accountId)){
			account =  (Account)getSession().getAttribute("account");
//		}else{
//			account = accountService.get(Integer.parseInt(accountId));
//		}
		
		if(account == null) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS,"�˺Ų�����");
//			if(StringUtils.isEmpty(accountName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�������˺�");
		if(StringUtils.isBlank(companyName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����빫˾����");
		if(StringUtils.isBlank(tel)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������ϵ�绰");
		account.setCompanyName(companyName);
		//System.out.println("jjj"+account.getTeam().getTeamId()+account.getTeam().getTeamName());
		//account.getTeam().setTeamName(companyName);
		account.setAccountTel(tel);

		if(StringUtils.isNotBlank(email))
		account.setEmail(email);
		if(StringUtils.isNotBlank(address))
		account.setAddress(address);
		accountService.save(account);
		//�޸Ĺ�˾����ʱ����¶�Ӧ���ӵ����� ������ʾ
		Team t = teamService.getTeam(account.getTeam().getTeamId());
		t.setTeamName(companyName);
		teamService.update(t);
		return ResultManager.getSuccResult();
	}
	/**
	 * @author Administrator
	 * @date Jun 26, 2013
	 * @return
	 * @throws Exception 
	 * @Description ������߸����˺�
	 */
	public String save() throws Exception{
		String accountId = getParameter("accountId");
		String companyName = getParameter("name");
		String shortName = getParameter("shortName");
		String address = getParameter("address");
		String tel = getParameter("phone");
		String email = getParameter("email");
		String gps = getParameter("gps");

		if(StringUtils.isBlank(accountId)){
			Account account = null;
			String accountName = getParameter("account");
			String accountPwd = "123456";//getParameter("password");
			
			account = new Account();
			if(StringUtils.isEmpty(accountName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�������˺�");
			if(StringUtils.isEmpty(accountPwd)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������");
			if(StringUtils.isEmpty(companyName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����빫˾����");
//			if(StringUtils.isEmpty(address)) throw new CommonException("��ַ������д");
			if(StringUtils.isEmpty(tel)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������ϵ�绰");
//			if(StringUtils.isEmpty(email)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������ʼ�");
			
			String mobile = getParameter("mobile");
			
			Level level = new Level();
			level.setLevelId(2);
			account.setLevel(level);
			Role role = new Role();
			role.setRoleId(2); //����Ա
			account.setRole(role);
			if(accountService.ifExist(accountName)){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�˺������Ѵ���");
			}
			if(accountService.ifExistEmail(email,0)){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����Ѵ���");
			}
			account.setAccountName(accountName);
			account.setAccountPwd(Coder.encryptMD5(accountPwd));
			account.setCompanyNick(shortName);
			account.setEmail(email);
			account.setCompanyName(companyName);
			account.setAddress(address);
			account.setCoordinate(gps);
			account.setAccountTel(tel);
			account.setCreateIp(getRequest().getRemoteAddr());
			account.setVisitIp(getRequest().getRemoteAddr());
			account.setCreateTime(new Date());
			account.setVisitTime(new Date());
			account.setParentId(0);
			account.setLogintimes(0);
			accountService.save(account);
		    return ResultManager.getSuccResult();
		}else{
			Account account = accountService.get(Integer.parseInt(accountId));
			if(account == null) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS,"�˺Ų�����");
//			if(StringUtils.isEmpty(accountName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�������˺�");
			if(StringUtils.isBlank(companyName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����빫˾����");
			if(StringUtils.isBlank(tel)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������ϵ�绰");
			account.setCompanyName(companyName);
			account.setAccountTel(tel);
	
			if(StringUtils.isNotBlank(email))
			account.setEmail(email);
			if(StringUtils.isNotBlank(address))
			account.setAddress(address);
			accountService.save(account);
			return ResultManager.getSuccResult();
		}
		    
	}
	
	public String logout(){
		getSession().invalidate();
		return ResultManager.getSuccResult(); 
	}
	
	/**
	 * @author Administrator
	 * @date Jul 2, 2013
	 * @return
	 * @throws Exception 
	 * @Description �˻������޸�
	 */
	public String set() throws Exception{
		String oldPassword = getParameter("oldpwd");
		String password = getParameter("newpwd");
		Account account = getSessionAccount();
		if(StringUtils.isEmpty(oldPassword)){
			return ResultManager.getFaildResult("������ԭ����");
		}
		if(StringUtils.isEmpty(password)){
			return ResultManager.getFaildResult("������������");
		}
		if(account.getAccountPwd().equals(Coder.encryptMD5(oldPassword))){
			account.setAccountPwd(Coder.encryptMD5(password));
			accountService.save(account);
			accountService.updatePswd(new Object[]{password, account.getAccountId()});
//			logService.save(EnumCommon.update_log, "�û�:"+getSessionAccount().getName()+","+account.getAccount()+"�˻��޸�����ɹ�");
			return ResultManager.getSuccResult();
		} 
		return ResultManager.getFaildResult("������֤ʧ��");
	}
	

	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @Description ��ʾ�˺�����
	 */
	public String view(){
		String accountId = getParameter("accountId");
		Account accounts = null;
		if(StringUtils.isNotEmpty(accountId)){
			accounts = accountService.get(Integer.parseInt(accountId));
		} else {//���accountId�����ڣ���Ĭ�ϲ��ҵ�ǰ��¼�˺�����
			accounts = getSessionAccount();
		}
		return ResultManager.getBodyResult(accounts);
	}
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @Description ��ʾ��¼�˺�����
	 */
	public String viewown(){
		try {
			Account account = null;
			account = getSessionAccount();
			if(account==null){
				return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
			}
			JSONObject json = new JSONObject();
			JSONObject json1 = new JSONObject();
			JSONObject json2 = new JSONObject();
			//update by zhangxin 2014-08-18
			Team t = teamService.getTeam(account.getTeam().getTeamId());
			
			if(t == null) return ResultManager.getFaildResult(1, "������¼��˾������");
			//if(account.getParentId() == 1 || account.getParentId() == 0){
				json1.put("id", t.getTeamId());
				json1.put("name", t.getTeamName());
				json1.put("address", account.getAddress());
				json1.put("phone", account.getAccountTel());
				if(account.getLevel().getLevelId()==4){  //�����쵼��Ӳ���id
				json1.put("deptId", account.getDept().getDeptId());
				}
				json1.put("email",  StringUtils.isBlank(account.getEmail())?"":account.getEmail());
				json.put("company",json1);
//			}else{
//				Account accountParent = accountService.get(account.getParentId());
//				if(accountParent.getParentId() != 0){
//				json1.put("id", account.getParentId());
//				json1.put("name", accountParent.getCompanyName());
//				json1.put("address", accountParent.getAddress());
//				json1.put("phone", accountParent.getAccountTel());
//				json1.put("email",  StringUtils.isBlank(accountParent.getEmail())?"":accountParent.getEmail());
//				json.put("company",json1);
//				}else{
//					json1.put("id", account.getAccountId());
//					json1.put("name", account.getCompanyName());
//					json1.put("address", account.getAddress());
//					json1.put("phone", account.getAccountTel());
//					json1.put("email",  StringUtils.isBlank(account.getEmail())?"":account.getEmail());
//					json.put("company",json1);
//				}
//			}
			json2.put("id", account.getAccountId());
			json2.put("levelId", account.getLevel().getLevelId());
			json2.put("levelName", account.getLevel().getLevelName());
			json2.put("ifAdmin", account.getRole().getRoleId()==1?1:0);
			json2.put("role", account.getRole().getRoleId()==3?1:0);
			json2.put("name", account.getCompanyName());//����Ա���� �� ��˾��
			json2.put("account", account.getAccountName());
			json2.put("ctime",DateUtil.parseDate(account.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			json2.put("lasttime", DateUtil.parseDate(account.getVisitTime(),"yyyy-MM-dd HH:mm:ss"));
			//����ԱȨ�޻�ȡ��������ID
			
			List teamList = new ArrayList();
//			if(account.getRole().getRoleId() == 3){
//				List oplist = opTeamService.getOpTeamByIds(account.getAccountId() + "");
//				for(int i = 0;i < oplist.size();i++){
//					OperatorTeam opTeam = (OperatorTeam)oplist.get(i);
//					if(opTeam.getTeam() != null){
//						teamList.add(opTeam.getTeam().getTeamId());
//					}
//				}
//				json2.put("teamIds", teamList);
//			}else{
				//json2.put("teamIds", teamList.toArray());
				json2.put("teamIds", teamList);
//			}
			json.put("visitor",json2);
			json.put("appid",1);
			//��ȡ�ϴεļ��ʱ��
			ConfigInfo config = pcodeCensusService.getConfigInfo(account.getAccountId());
			if(config != null){
				json.put("scanDate",config.getScanDate());
			}else{
				json.put("scanDate","");
			}
			//��ӻ�ȡ��������
			String accountId = "";
			if(account.getRole().getRoleId() != 2){
				accountId = getCompanyId();
			}else{
				accountId = account.getAccountId().toString();
			}
			String teamIds = teamService.getTeamIdStr(account);
			Object[] keywords = new Object[]{teamIds};
			Long count = userService.getCount(keywords);
			System.out.println("///////////////count"+count);
//			JSONObject json3 = new JSONObject();
//			JSONArray jsonAry = new JSONArray();
			json.put("vehCount", count);
			System.out.println(json.toString());
			return json.toString();//ResultManager.getBodyResult(account);
		} catch (JSONException e) {
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "ϵͳ�����쳣");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "ϵͳ�����쳣");
		}
	}
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @Description ��֤�˺����Ƿ��ظ�
	 */
	public String check() throws Exception{
		String accountName = getParameter("accountName");
		if(StringUtils.isEmpty(accountName)){
			return ResultManager.getFaildResult("�˻�������Ϊ��");
		}
		if(accountService.ifExist(accountName)){
			return ResultManager.getFaildResult("�˺��Ѵ���");
		} else {
			return ResultManager.getSuccResult();
		}
	}
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @throws Exception 
	 * @Description �˺ŵ�½
	 */
	public String login() throws Exception{

		String accountName = getParameterNoCheck("account");
		String password = getParameterNoCheck("pwd");
		String mobile = getParameter("mobile");
		if(StringUtils.isEmpty(accountName)){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�������˺�");
		}
		if(StringUtils.isEmpty(password)){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������");
		}
		
		Account account = accountService.getByName(accountName);
		String code = getParameter("vcode");
		
		Object random = getSession().getAttribute("random");//��ȡ��������֤��
		
		if(StringUtils.isEmpty(mobile) || !"mobile".equals(mobile))
			if(random == null || !random.toString().equalsIgnoreCase(code)) 
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_VCODE,"��֤����дʧ��");
//		
		if(account != null && account.getAccountPwd().equals(Coder.encryptMD5(password)) 
				){
//			//δ������˺� 3����ֹ��½
//			Date date=DateUtil.getAfterDate(account.getCreateTime(), 3);
//			if("0".equals(account.getIsActivate())&&(date.getTime()-new Date().getTime())<0){
//				getSession().setAttribute("remindActivate", account);
//				return ResultManager.getFaildResult(-2,"�뼤����˺�");
//			}
			account.setVisitIp(getRequest().getRemoteAddr());
			account.setVisitTime(new Date());
			account.setLogintimes(account.getLogintimes()+1);
			accountService.save(account);
			getSession().setAttribute("account", account);
			getSession().setAttribute("levelId", account.getLevel().getLevelId());
			getSession().setAttribute("levelName", account.getLevel().getLevelName());
			getSession().setAttribute("limitNum", account.getLevel().getLimitNum());
			return viewown();
		}
		return ResultManager.getFaildResult(EnumCommon.EXCEPTION_LOGIN,"�˺��������������");
	}
	
	/**
	 * add by zhangxin 2014-09-19
	 * @Description �˺ŵ�½
	 */
	public String loginForApp() throws Exception{

		String accountName = getParameterNoCheck("account");
		String password = getParameterNoCheck("pwd");
		String mobile = getParameter("mobile");
//		if(StringUtils.isEmpty(accountName)){
//			return ResultManager.getResultForApp(false,"�������˺�");
//		}
//		if(StringUtils.isEmpty(password)){
//			return ResultManager.getResultForApp(false,"����������");
//		}
		
		Account account = accountService.getByName(accountName);
		String code = getParameter("vcode");
		
		if(account != null && account.getAccountPwd().equals(Coder.encryptMD5(password))){
			account.setVisitIp(getRequest().getRemoteAddr());
			account.setVisitTime(new Date());
			account.setLogintimes(account.getLogintimes()+1);
			accountService.save(account);
			getSession().setAttribute("account", account);
			getSession().setAttribute("levelId", account.getLevel().getLevelId());
			getSession().setAttribute("levelName", account.getLevel().getLevelName());
			getSession().setAttribute("limitNum", account.getLevel().getLimitNum());
			String data = viewown();
			return ResultManager.getResultForApp(true, data);
		}
		return ResultManager.getResultForApp(false, "�˺��������������");
	}
	
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @Description �˺��б�
	 */
	public String list(Page page){
		Account accountAdmin = (Account)getSession().getAttribute("account");
		String name=getParameter("name");//����������Ա����
		String sortF = getParameter("sortF");
		//update by zhangxin 2014-8-18
		//����Ա�򳬼�����ԱȨ�޲��ܲ���
		//if(accountAdmin.getParentId()!= 0){
//		if(accountAdmin.getRole().getRoleId() == 3){
//			 return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"����Ȩִ�иò���");
//		}
		//��ѯ����Ա
		String teamIds = teamService.getTeamIdStr(accountAdmin);
		Object[] keywords = new Object[]{accountAdmin.getAccountId(), teamIds,name,sortF};
		page = accountService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @returnlogin
	 * @Description �˺��б�
	 */
	public String adminList(Page page){
		Account accountAdmin = (Account)getSession().getAttribute("account");
		//update by zhangxin 2014-8-18
		//����Ա�򳬼�����ԱȨ�޲��ܲ���
		//if(accountAdmin.getParentId()!= 0){
		if(accountAdmin.getRole().getRoleId() == 3){
			 return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"����Ȩִ�иò���");
		}
		//��ѯ����Ա
		Object[] keywords = new Object[]{accountAdmin.getAccountId(), 2};
		page = accountService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	public String listCompany(Page page){
		page = accountService.getPageListCompany(page);
		return ResultManager.getBodyResult(page);
	}
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @Description ɾ���˺�
	 */
	public String delete(){
		String accountIds = getParameter("accountIds");
		if(StringUtils.isBlank(accountIds)){
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������Ҫɾ�����˺�ID����Ӣ�ĵĶ�������");
		}
		List list = opTeamService.getOpTeamByIds(accountIds);
		if(list.size() > 1){
			return ResultManager.getFaildResult(EnumCommon.WEBSERVICE_OTHER,"���ֲ���Ա�복�Ӱ󶨹�ϵδɾ��,����ɾ���������Ա�복�Ӱ󶨹�ϵ");
		}
		if(list.size() == 1){
			OperatorTeam opTeam = (OperatorTeam)list.get(0);
			if(opTeam.getTeam() != null){
				return ResultManager.getFaildResult(EnumCommon.WEBSERVICE_OTHER,"���ֲ���Ա�복�Ӱ󶨹�ϵδɾ��,����ɾ���������Ա�복�Ӱ󶨹�ϵ");
			}
		}
		accountService.delete(accountIds);
//		logService.save(EnumCommon.delete_log, accountIds+"�˻��б��Ѿ���ɾ��");
		return ResultManager.getSuccResult();
	}
	
	/**
	 * @author Administrator
	 * @date Jul 1, 2013
	 * @return
	 * @throws Exception 
	 * @Description �б�
	 */
/*	public void listCar() throws Exception{
		Account account = super.getSessionAccount();
		Object[] keywords = new Object[]{account.getAccountId()};
//		page = userService.getPageList(page, keywords);
		List list = userService.getList(account.getAccountId().toString());
		getSession().setAttribute("list", list);
	}*/
	
	
	/**
	 * 
	 * @author Administrator
	 * @date Jan 15, 2014
	 * @return
	 * @throws Exception 
	 * @Description ��֤�˺���Ϣ
	 */
	public String checkAccountName() throws Exception{
		String accountName=getParameter("accountName");
		if(StringUtils.isEmpty(accountName)){return ResultManager.getFaildResult("�˺����Ʋ����ǿ�");}
		String mobile = getParameter("mobile");
		String code = getParameter("code");
		Object random = getSession().getAttribute("random");//��ȡ��������֤��
//		if(StringUtils.isEmpty(mobile) || !"mobile".equals(mobile))
//			if(random == null || !random.toString().equalsIgnoreCase(code)) 
//				return ResultManager.getFaildResult(ResultManager.EXCEPTION_LOGIN,"��֤����дʧ��");
		
		Account account=accountService.getByName(accountName);
		if(account!=null){
			return ResultManager.getBodyResult(account);
		}
		return ResultManager.getFaildResult("�����˺Ų�����");
	}
	/**
	 * 
	 * @author Administrator
	 * @date Jan 20, 2014
	 * @return
	 * @throws Exception
	 * @Description ��֤������֤��
	 */
	public String checkVerificationCode()throws Exception{
		String code=getParameter("ecode");
		String email=getParameter("emails");
		if(StringUtils.isEmpty(code)){
			return ResultManager.getFaildResult("����д��֤��");
		}
		if(StringUtils.isNotEmpty(email)){
			Account account=accountService.getByEmail(email);
			if(account!=null){
				if(Integer.parseInt(account.getVerificationCount())<verificationCount){
					Date date=account.getSendTime();
					date=DateUtil.getAfterHoursDate(date,1);
					if(date.getTime()-new Date().getTime()<0){
						account.setRandomCode("");
						account.setSendTime(null);
						account.setVerificationCount("0");
						accountService.save(account);
						return ResultManager.getFaildResult(-1,"��֤��ʧЧ�������»�ȡ");
					}
					if(account.getRandomCode().equals(code)){
						account.setRandomCode("");
						account.setSendTime(null);
						account.setVerificationCount("0");
						accountService.save(account);
						return ResultManager.getSuccResult();		
					}else{
						String count=account.getVerificationCount();
						account.setVerificationCount(Integer.parseInt(count)+1+"");
						accountService.save(account);
						return ResultManager.getFaildResult(-2,"��֤�����,������"+(verificationCount-Integer.parseInt(count))+"����");
					}
				}else{
					account.setRandomCode("");
					account.setSendTime(null);
					account.setVerificationCount("0");
					accountService.save(account);
				    return ResultManager.getFaildResult(-1,"��֤��ʧЧ�������»�ȡ");
				}
			}
		}
		return ResultManager.getFaildResult("��֤ʧ��");
	}


	/**
	 * 
	 * @author Administrator
	 * @date Jan 15, 2014
	 * @return
	 * @throws Exception
	 * @Description �������� �޸�
	 */
	public String resetPassword() throws Exception{
		String password="123456";//getParameter("password");
		String accountName=getParameter("account");
		if(StringUtils.isNotEmpty(accountName)&&StringUtils.isNotEmpty(password)){
			Account account=accountService.getByName(accountName);
			if(account!=null){
				account.setAccountPwd(Coder.encryptMD5(password));
				accountService.save(account);
				return ResultManager.getSuccResult();
			}
		}
		return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RESET,"��������ʧ��");
	}
	/**
	 * 
	 * @author Administrator
	 * @date Jan 6, 2014
	 * @return
	 * @throws Exception
	 * @Description ���������ʼ�����
	 */
	public String forgetPasswordMail() throws Exception{
		String accountName=getParameter("accountNames");
		Account account=accountService.getByName(accountName);
		String email=getParameter("emails");
		if(StringUtils.isEmpty(email)){
			return ResultManager.getFaildResult("�����������");
		}
		if(account!=null&&StringUtils.isNotEmpty(account.getEmail())){
			if(!account.getEmail().equals(email)){
				return ResultManager.getFaildResult("�����������ע�����䲻һ�£�����������");
			}
			int randomId=RandomNumUtil.getRandomPassword();	
			String content="���ã���л��ʹ�ó��Ӽ��ƽ̨�����ڽ���������֤�������������֤��Ϊ��"+randomId+"��Ϊ�˱������ʺŵİ�ȫ�ԣ�����1Сʱ�������֤����";
			if(SmtpMailSend.sendSMTPMail("���ӷ���ƽ̨���������֤",content,account.getEmail(),null)){	
				account.setRandomCode(randomId+"");
			    account.setSendTime(new Date());
			    account.setVerificationCount("0");
			    accountService.save(account);
			    return ResultManager.getSuccResult();  
			}
		       return ResultManager.getFaildResult("����ʧ��");	
		}
		return ResultManager.getFaildResult("���˺ŵ�����Ϊ��");
	}
	/**
	 * 
	 * @author Administrator
	 * @date Jan 6, 2014
	 * @return
	 * @Description ���伤���˺�
	 */
	public String hrefAccountActivate()throws Exception{
		String activateId=getParameter("activateId");
		String code=getParameter("code");
		//get
		Account account=accountService.get(Integer.parseInt(activateId));
		HashMap map=new HashMap();
		HttpSession session=getSession();
		boolean flag=false;
		String msg="";
		if(account!=null){
			if("1".equals(account.getIsActivate())){
				msg="���˺��ѱ��������ʧ��";
			}else
			if(Integer.parseInt(account.getVerificationCount())<=verificationCount){
				if(code.equals(account.getRandomCode())){
					//�����ֶθ��� �ÿ������
					account.setIsActivate("1");
					account.setRandomCode("");
					account.setVerificationCount("0");	
					accountService.save(account);
					flag=true;
					msg="��ϲ�������伤��ɹ�!";
				}else{
					account.setVerificationCount(Integer.parseInt(account.getVerificationCount())+1+"");
					accountService.save(account);
					msg="��֤����󣬼���ʧ�ܣ������»�ȡ��֤��";
				}
			}else {
				account.setIsActivate("0");
				account.setRandomCode("");
				account.setVerificationCount("0");	
				accountService.save(account);
				msg="����������࣬����ʧ�ܣ������»�ȡ�����ʼ�";
				//return ResultManager.getFaildResult(ResultManager.EXCEPTION_LOGIN, "����������������»�ȡ�����ʼ�");
			}
		}
		map.put("flag", flag);
		map.put("reason", msg);
        map.put("account", account);
        session.setAttribute("regDone", map);
		getResponse().sendRedirect("regDone.jsp");
		return ResultManager.getSuccResult();
	}
	/**
	 * 
	 * @author Administrator
	 * @date Jan 23, 2014
	 * @return
	 * @throws Exception 
	 * @Description ��������
	 */
	public String updateEmail() throws Exception{
		String accountId=getParameter("accountId");
		String password=getParameter("password");
		String email=getParameter("email");
		if(StringUtils.isEmpty(password)) {return  ResultManager.getFaildResult("����������");}
		if(StringUtils.isEmpty(email))return  ResultManager.getFaildResult("�������µ�����");
		//if(accountService.ifExistEmail(email))return ResultManager.getFaildResult("�������Ѵ���");
		Account account=accountService.get(Integer.parseInt(accountId));
		if(account!=null){
			if(account.getAccountPwd().equals(Coder.encryptMD5(password))){
				account.setEmail(email);
				String md5Name=UUID.randomUUID().toString();
		           String href=getRequest().getRequestURL()+"?cmd=href_account_activate&activateId="+account.getAccountId()+"&code="+md5Name;
		           if(SmtpMailSend.sendSMTPMail("���Ӽ��ƽ̨�˺ż���", "��ӭʹ�ó��Ӽ��ƽ̨�����������˻����Ӽ����˻�<a href='"+href+"'>������</a>���������µļ�������Ϊ׼��", account.getEmail(), "html")){
		        	   accountService.save(account);
					   getSession().setAttribute("regAccount", account);
					   return ResultManager.getSuccResult();	
				}
			}
			return ResultManager.getFaildResult("�����������");
		}
		return ResultManager.getFaildResult("��ȡ�˺���Ϣʧ��");
	}
	/**
	 * 
	 * @author Administrator
	 * @date Jan 15, 2014
	 * @return
	 * @throws Exception
	 * @Description �����˺ż����ʼ�
	 */
	public String sendActivateMail() throws Exception{
		String accountId=getParameter("accountId");
		//String email=getParameter("email");
		Account account=accountService.get(Integer.parseInt(accountId));
		if(account==null){
			return ResultManager.getFaildResult("��ȡ�˺���Ϣʧ��");
		}
		if((verificationCount+"").equals(account.getVerificationCount())){
			return ResultManager.getFaildResult("��������������࣬����ʧ��");
		}
		if(StringUtils.isNotEmpty(accountId)&&StringUtils.isNotEmpty(account.getEmail())){
			String md5Name=UUID.randomUUID().toString();
           String href=getRequest().getRequestURL()+"?cmd=href_account_activate&activateId="+account.getAccountId()+"&code="+md5Name;
           //if(true){
            if(SmtpMailSend.sendSMTPMail("���Ӽ��ƽ̨�˺ż���", "��ӭʹ�ó��Ӽ��ƽ̨�����������˻����Ӽ����˻�<a href='"+href+"'>������</a>���������µļ�������Ϊ׼��", account.getEmail(), "html")){
		      account.setIsActivate("0");
		      account.setRandomCode(md5Name);
		      if(StringUtils.isNotEmpty(account.getVerificationCount())){
		    	  account.setVerificationCount(Integer.parseInt(account.getVerificationCount())+1+"");
		      }else{
		      account.setVerificationCount("0");
		      }
		      getSession().invalidate();
		      getSession().setAttribute("activateAccount", account);
		      accountService.save(account);
		      return ResultManager.getSuccResult();
		      } 
		}	
		return ResultManager.getFaildResult("�����˺ż����ʼ�ʧ��");
	}
	/**
	 * 
	 * @author Administrator
	 * @date Jan 22, 2014
	 * @return
	 * @Description �첽��֤����Ψһ
	 */
    public String checkEmail(){
    	String email=getParameter("email");
    	if(StringUtils.isEmpty(email)){
    		return ResultManager.getFaildResult( "�����ַ�����ǿ�");
    	}
    	Pattern p = Pattern.compile("^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$");
    	Matcher m = p.matcher(email);
    	String result=ResultManager.getSuccResult();
    	if(!m.find()){
			return ResultManager.getFaildResult( "��������ȷ�������ַ");
		}
    	if(accountService.ifExistEmail(email,0)){
    		return ResultManager.getFaildResult(ResultManager.EXCEPTION_COMMON, "�������Ѿ�����");
    	}
    	return result;
    }
    public String checkAccName()throws Exception{
    	String accountName=getParameter("accountName");
    	Pattern p=Pattern.compile("^[a-zA-Z0-9_@.]{5,10}$");
    	Matcher m=p.matcher(accountName);
    	String result=ResultManager.getSuccResult();
    	if(!m.find()){
    		result=ResultManager.getFaildResult("�˺��ɳ���5-10��Ӣ�ġ����֡��»����Լ�@���");
    	}
    	if(accountService.ifExist(accountName)){
    		result=ResultManager.getFaildResult(ResultManager.EXCEPTION_COMMON, "���˺����Ѿ�����");
    	}
    	return result;
    }
    
	public String saveOperator() throws Exception {
		Account accountAdmin = (Account)getSession().getAttribute("account");
		//update by zhangxin 2014-8-18
		//����Ա�򳬼�����ԱȨ�޲��ܲ���
		//if(accountAdmin.getParentId()!= 0){()
		int roleID = accountAdmin.getRole().getRoleId();
//		if(){
//			
//		}
//		if(accountAdmin.getRole().getRoleId() == 3){
//			 return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"����Ȩִ�иò���");
//		}
		String accountId = getParameter("accountId");
		String name = getParameter("realname");
		String address = getParameter("address");
		String tel = getParameter("phone");
		String email = getParameter("email");
		String accountName = getParameter("account");
		String accountRealName = getParameter("accountRealName");
		String accountPwd = getParameter("pwd");
		String teamId = getParameter("teamId");
		String levelId = getParameter("levelId");
		String deptId = getParameter("deptId");
		//��������Ա
		String ifAdmin = getParameter("ifAdmin");

	//	String levelId = getParameter("levelId");
		if(StringUtils.isBlank(accountId)){
			Account account = null;
			account = new Account();
			if(StringUtils.isEmpty(accountName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�������˺�");
			if(StringUtils.isEmpty(accountPwd)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������");
			if(StringUtils.isEmpty(name)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������");
	//			if(StringUtils.isEmpty(address)) throw new CommonException("��ַ������д");
//			if(StringUtils.isEmpty(tel)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������ϵ�绰");
	//			if(StringUtils.isEmpty(email)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������ʼ�");
	//			String mobile = getParameter("mobile");
			Level level = new Level();
			level.setLevelId(Integer.parseInt(levelId));
			account.setLevel(level);
			Role role = new Role();
			if("1".equals(ifAdmin)){
				role.setRoleId(2); //����Ա
			}else{
				role.setRoleId(2); //����Ա
			}
			account.setRole(role);
			if(accountService.ifExist(accountName)){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�˺������Ѵ���");
			}
//			if(accountService.ifExistEmail(email,0)){
//				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����Ѵ���");
//			}
			if(!StringUtils.isEmpty(deptId)){
				Dept dept = deptService.getDept(Integer.parseInt(deptId));
				if(dept != null){
					account.setDept(dept);
				}
				//account.setDept_id(Integer.parseInt(deptId));
			}
			
			account.setAccountName(accountName);
			account.setAccountRealName(accountRealName);
			account.setAccountPwd(Coder.encryptMD5(accountPwd));
			account.setEmail(email);
			account.setCompanyName(name);
			account.setAddress(address);
			account.setAccountTel(tel);
			account.setCreateIp(getRequest().getRemoteAddr());
			account.setVisitIp(getRequest().getRemoteAddr());
			account.setCreateTime(new Date());
			account.setVisitTime(new Date());
			account.setParentId(accountAdmin.getAccountId());
			account.setLogintimes(0);
			//account.setTeamId(Integer.parseInt(teamId));
			Team t = teamService.getTeam(Integer.parseInt(teamId));
			account.setTeam(t);
			accountService.save(account);
			
			//�����뱣�浽��־�� ���ڲ�����
			PasswordLog pwdLog = new PasswordLog();
			pwdLog.setAccountId(account.getAccountId());
			pwdLog.setPassword(accountPwd);
			accountService.saveOrUpdatePswd(pwdLog);
			
			//�������Ӳ���Ա,�����һ������Ա�복�ӹ�ϵ�б�Ŀ�����
//			if(!"1".equals(ifAdmin)){
//				OperatorTeam opTeam = new OperatorTeam();
//				opTeam.setAccount(account);
//				opTeam.setCreateTime(new Date());
//				opTeamService.saveOpTeam(opTeam);
//			}
		    return ResultManager.getSuccResult();
		}else{
			Account account = accountService.get(Integer.parseInt(accountId));
			if(account == null) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS,"�˺Ų�����");
			if(StringUtils.isBlank(name)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������");
			if(StringUtils.isBlank(tel)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������ϵ�绰");
			Level level = new Level();
			level.setLevelId(Integer.parseInt(levelId));
			account.setLevel(level);
			
			Role role = new Role();
			if("1".equals(ifAdmin)){
				role.setRoleId(2); //����Ա
			}else{
				role.setRoleId(2); //����Ա
			}
			account.setRole(role);
			if(!StringUtils.isEmpty(deptId)){
				Dept dept = deptService.getDept(Integer.parseInt(deptId));
				if(dept != null){
					account.setDept(dept);
				}
			}
//			if(accountService.ifExist(accountName)){
//				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�˺������Ѵ���");
//			}
//			if(accountService.ifExistEmail(email,Integer.parseInt(accountId))){
//				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����Ѵ���");
//			}
//			account.setAccountName(accountName);
//			account.setAccountPwd(Coder.encryptMD5(accountPwd));
			account.setEmail(email);
			account.setCompanyName(name);
			account.setAccountRealName(accountRealName);
			account.setAddress(address);
			account.setAccountTel(tel);
			account.setCreateIp(getRequest().getRemoteAddr());
			account.setVisitTime(new Date());
			account.setParentId(accountAdmin.getAccountId());
			Team t = teamService.getTeam(Integer.parseInt(teamId));
			account.setTeam(t);
			//account.setTeamId(Integer.parseInt(teamId));
			accountService.save(account);
		    return ResultManager.getSuccResult();
		}
}
    
	public String editOperator() throws Exception {
		Account accountAdmin = (Account)getSession().getAttribute("account");
		//update by zhangxin 2014-8-18
		//����Ա�򳬼�����ԱȨ�޲��ܲ���
		//if(accountAdmin.getParentId()!= 0){
//		if(accountAdmin.getRole().getRoleId() == 3){
//			 return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"����Ȩִ�иò���");
//		}
		String accountId = getParameter("accountId");
		String name = getParameter("name");
		String address = getParameter("address");
		String tel = getParameter("phone");
		String email = getParameter("email");
//		String accountName = getParameter("account");
		String accountPwd = getParameter("pwd");

		if(StringUtils.isBlank(accountId)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS,"�˺�id����Ϊ��");
		Account account = null;
		account = accountService.get(Integer.parseInt(accountId));
		if(account == null) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS,"�˺Ų�����");
		if(StringUtils.isBlank(name)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"����������");
		if(StringUtils.isBlank(tel)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������ϵ�绰");
			Level level = new Level();
			level.setLevelId(2);
			account.setLevel(level);
			
			Role role = new Role();
			role.setRoleId(2); //����Ա
			account.setRole(role);
//			if(accountService.ifExist(accountName)){
//				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�˺������Ѵ���");
//			}
			if(accountService.ifExistEmail(email,Integer.parseInt(accountId))){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����Ѵ���");
			}
//			account.setAccountName(accountName);
			account.setAccountPwd(Coder.encryptMD5(accountPwd));
			account.setEmail(email);
			account.setCompanyName(name);
			account.setAddress(address);
			account.setAccountTel(tel);
			account.setCreateIp(getRequest().getRemoteAddr());
			account.setVisitTime(new Date());
			account.setParentId(accountAdmin.getAccountId());
			accountService.save(account);
		    return ResultManager.getSuccResult();
	}
	
	public String getLeavels(){
		List<Level> list = levelService.getList("");
		return ResultManager.getBodyResult(list);
	}
	
	public String resetAccount() throws Exception{
		String ids=getParameter("ids");
		accountService.resetAccount(ids);
		return ResultManager.getSuccResult();
	}
    
}
