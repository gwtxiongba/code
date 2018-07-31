/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IUserService;

/**
 * @ClassName AccountView
 * @author Administrator
 * @date Jun 26, 2013 2:28:41 PM
 * @Description �˺Ź�����ؽӿ�
 */
@Component("teamView")
public class TeamView extends BaseAction {
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	
	@Autowired
	@Qualifier("opTeamService")
	private IOperatorTeamService opTeamService;
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	public String saveTeam() throws Exception{
		Account account = (Account)getSession().getAttribute("account");
		
		//update by zhangxin 2014-8-18
		//����Ա�򳬼�����ԱȨ�޲��ܲ���
		//if(account.getParentId()!= 0){
//		if(account.getRole().getRoleId() == 3){
//			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_RIGHT,"����Ȩִ�иò���");
//		}
		String teamName = getParameter("teamName");
		String binds = getParameter("bindIds"); //12,12
//		String unbinds = getParameter("unbinds");//33,4
		String order = getParameter("order");
		String teamId = getParameter("teamId");
		int amount=0;
		if(StringUtils.isNotBlank(binds)){
			amount  = binds.split(",").length;
		}
		if(StringUtils.isBlank(teamName)) return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����복������");
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
//				if(StringUtils.isNotBlank(binds)){
//					userService.bindTeam(binds,account.getAccountId(),Integer.parseInt(teamId));
////					return ResultManager.getSuccResult();
////				}else if(StringUtils.isNotBlank(unbinds)){
//					userService.unBindTeam(binds,account.getAccountId(),Integer.parseInt(teamId));
//				}
//				userService.synRedisTeamId(account.getAccountId(),Integer.parseInt(teamId));
				return ResultManager.getSuccResult();
			}else{
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "ϵͳ�쳣��");
			}
			 
		}else{//edit
			Team team =	teamService.get(Integer.parseInt(teamId));
			if(team==null){
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_PARA,"�����ڴ�id��");
			}
			team.setAmount(amount);
			team.setTeamName(teamName);
			team.setOrder(Integer.parseInt(order));
			if(teamService.saveTeam(team)){
//				if(StringUtils.isNotBlank(binds)){
//					userService.bindTeam(binds,account.getAccountId(),Integer.parseInt(teamId));
//					userService.unBindTeam(binds,account.getAccountId(),Integer.parseInt(teamId));
//				}else{
//					userService.unBindTeamAll(account.getAccountId(), Integer.parseInt(teamId));
//				}
//				userService.synRedisTeamId(account.getAccountId(),Integer.parseInt(teamId));
				return ResultManager.getSuccResult();
			}else{
				return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "ϵͳ�쳣��");
			}
		}
	}
	
	
	public String delete() throws Exception{
		String teamIds = getParameter("teamIds");
		if(StringUtils.isBlank(teamIds)){
			return ResultManager.getFaildResult("��������Ҫɾ�����˺�ID����Ӣ�ĵĶ�������");
		}
		if(teamService.ifBind(teamIds)){
			return ResultManager.getFaildResult("���Ƚ���ó����°󶨵ĳ�����");
		}
		Account account = getSessionAccount();
		boolean  t = teamService.ifParent(Integer.parseInt(teamIds));
		if(!t){
			teamService.deleteTeam(teamIds,account.getAccountId());
		}else{
			return ResultManager.getFaildResult("�����¼������򳵶ӵİ�");
		}
		
		//opTeamService.deleteByTeam(Integer.parseInt(teamIds));
//		logService.save(EnumCommon.delete_log, userIds+"�˻��б��Ѿ���ɾ��");
		return ResultManager.getSuccResult();
	}
	
	public String list(Page page){
		String teamName = getParameter("teamName");
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		Object[] keywords = new Object[]{account.getTeam().getTeamId(),teamName};
		page = teamService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
}
