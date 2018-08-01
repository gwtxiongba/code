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
import com.chanceit.framework.utils.Coder;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.RandomNumUtil;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.framework.utils.StringUtil;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.PasswordLog;
import com.chanceit.http.pojo.Role;
import com.chanceit.http.pojo.Menu;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IMenuService;


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
	@Qualifier("menuService")
	private IMenuService menuService;
	
	private final int verificationCount=4;
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;
    public String gloabal() {
    	String key = getParameterNoCheck("key");
		Account account = null;
		account = getSessionAccount();
		if(account==null){
			return ResultManager.getFaildResult(102, "��δ��¼ϵͳ��ʱ��δ����");
		}
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		
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
			try {
				return menuService.getJson(account, Integer.parseInt(account.getAccountId().toString()),0).toString();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
			return "";
		
	}
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

		accountService.save(account);
		//�޸Ĺ�˾����ʱ����¶�Ӧ���ӵ����� ������ʾ
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
			
		
			accountService.save(account);
		    return ResultManager.getSuccResult();
		}else{
			Account account = accountService.get(Integer.parseInt(accountId));
			if(account == null) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS,"�˺Ų�����");
//			if(StringUtils.isEmpty(accountName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�������˺�");
			if(StringUtils.isBlank(companyName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����빫˾����");
			if(StringUtils.isBlank(tel)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"��������ϵ�绰");
			
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
//		if(account.getAccountPwd().equals(Coder.encryptMD5(oldPassword))){
//			account.setAccountPwd(Coder.encryptMD5(password));
//			accountService.save(account);
//			accountService.updatePswd(new Object[]{password, account.getAccountId()});
////			logService.save(EnumCommon.update_log, "�û�:"+getSessionAccount().getName()+","+account.getAccount()+"�˻��޸�����ɹ�");
//			return ResultManager.getSuccResult();
//		} 
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
			json2.put("id", account.getAccountId());
			json2.put("role", account.getRoleId()==3?1:0);
			json2.put("name", account.getAccountName());//����Ա���� �� ��˾��
			json2.put("account", account.getAccountName());
			json2.put("ctime",DateUtil.parseDate(account.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
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
//			}
			json.put("visitor",json2);
			json.put("appid",1);
			//��ȡ�ϴεļ��ʱ��
			json.put("vehCount", 0);
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
		if(account != null && account.getPassword().equals(Coder.encryptMD5(password)) 
				){
//			//δ������˺� 3����ֹ��½
//			Date date=DateUtil.getAfterDate(account.getCreateTime(), 3);
//			if("0".equals(account.getIsActivate())&&(date.getTime()-new Date().getTime())<0){
//				getSession().setAttribute("remindActivate", account);
//				return ResultManager.getFaildResult(-2,"�뼤����˺�");
//			}
			
			getSession().setAttribute("account", account);
			return viewown();
		}
		return ResultManager.getFaildResult(EnumCommon.EXCEPTION_LOGIN,"�˺��������������");
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
		Object[] keywords = new Object[]{accountAdmin.getAccountId(),name,sortF};
		page = accountService.getPageList(page, keywords);
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
				account.setPassword(Coder.encryptMD5(password));
				accountService.save(account);
				return ResultManager.getSuccResult();
			}
		}
		return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RESET,"��������ʧ��");
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
    
  
	
	public String resetAccount() throws Exception{
		String ids=getParameter("ids");
		accountService.resetAccount(ids);
		return ResultManager.getSuccResult();
	}
	
	public String saveMenu(){
		String menuName=getParameter("menuName");
		String url=getParameter("url");
		String parentId=getParameter("parentId");
		String buttons=getParameter("buttons");
		Menu m = new Menu();
		if("root".equals(parentId)){
			
			m.setMenuName(menuName);
			m.setParentId(1);
			m.setUrl(url);
			m.setCreateTime(new Date());
			m.setMadeAdmin("admin");
		}else{
			
			m.setMenuName(menuName);
			m.setParentId(Integer.parseInt(parentId));
			m.setUrl(url);
			m.setCreateTime(new Date());
			m.setMadeAdmin("admin");
		}
		menuService.saveMenu(m);
		return ResultManager.getSuccResult();
	}
    
}
