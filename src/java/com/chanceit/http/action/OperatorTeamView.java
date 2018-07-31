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
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.OperatorTeam;
import com.chanceit.http.pojo.Team;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.IUserService;

/**
 * @ClassName AccountView
 * @author zhangxin 2014-08-25
 * @Description 账号管理相关接口
 */
@Component("opTeamView")
public class OperatorTeamView extends BaseAction {
	
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
	
	public String list(){
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		Object[] keywords = new Object[]{accountId};
//		page = opTeamService.getPageList(page, keywords);
		return opTeamService.getPageList(page, keywords);
	}
	
	public String upDateOpTeam(){
		String operatorId = getParameter("operatorId"); 
		String teamId = getParameter("bindIds");//12,12
		int opId = Integer.parseInt(operatorId);
		String[] ids = teamId.split(",");
		
		Account account = opTeamService.getAccount(opId);
		if(StringUtils.isNotBlank(teamId)){
			  opTeamService.delete(opId);
			  for(String id : ids ){
				  System.out.println(id);
				  Team team = opTeamService.get(Integer.parseInt(id));
				  
				  OperatorTeam operatorTeam = new OperatorTeam();
				  operatorTeam.setAccount(account);
				  operatorTeam.setCreateTime(new Date());
				  operatorTeam.setTeam(team);
				  opTeamService.save(operatorTeam);
			  }
			  opTeamService.bindTeam(teamId, opId);
			  opTeamService.unBindTeam(teamId, opId);
			  return ResultManager.getSuccResult();
		}else if(StringUtils.isBlank(teamId)){
			opTeamService.delete(opId);
			OperatorTeam operatorTeam = new OperatorTeam();
			operatorTeam.setAccount(account);
			opTeamService.save(operatorTeam);
			opTeamService.unBindTeam(teamId, opId);
			return ResultManager.getSuccResult();
		}
		return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "系统异常！");
	}
	
	/**
	 * @des 当前操作员分组车队，与 未分组车队 列表
	 * @return
	 * @throws Exception
	 */
	public String getGroupList() throws Exception {
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		String operatorId = getParameterNoCheck("operatorId");
		return opTeamService.getGroupList(accountId,Integer.parseInt(operatorId)).replace("null", "0");
	}

}
