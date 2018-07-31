/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Askleave;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IAskleaveService;
import com.chanceit.http.service.ITeamService;

/**
 * @ClassName AccountView
 * @author zhangxin 2014-08-13
 * @Description 账号管理相关接口
 */
@Component("askleaveView")
public class AskleaveView extends BaseAction {
	@Autowired
	@Qualifier("askleaveService")
	private IAskleaveService askleaveService;
	
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	
	
	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	
	public String list(Page page){
		//String askleaveName = getParameter("askleaveName");
		String status=getParameter("status");//0待审批请假  1请假列表
		String ss_name=getParameter("ss_name");
		if(Integer.parseInt(status)==0){
			status="0";
		}else
			status="1,-1";
		
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
//		Account accountAdmin = (Account)getSession().getAttribute("account");
//		int accountId;
//		if(accountAdmin.getParentId()> 0){
//			 accountId = accountAdmin.getParentId();
//		}else{
//			accountId = accountAdmin.getAccountId();
//		}
//		Object[] keywords = new Object[]{accountIDs,askleaveName};
		Object[] keywords = new Object[]{status,account.getTeam().getTeamId(),ss_name};
		page = askleaveService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}

	
	public String auditAskleave() throws Exception{
		String id=getParameter("id");
		String type=getParameter("type");
		String reason2=getParameter("reason2");
		Askleave askleave=askleaveService.getAskleave(Integer.parseInt(id));
		if("deal".equals(type)){
			askleave.setStatus(1);//同意
		}else if("refuse".equals(type)){
			askleave.setStatus(-1);//拒绝
			if(StringUtils.isNotBlank(reason2)){
				askleave.setReason2(reason2);
			}
		}
		askleaveService.saveAskleave(askleave);
		
		return ResultManager.getSuccResult();
		
	}
	
	
//	public String delete() throws Exception{
//		String AskleaveIds = getParameter("askleaveIds");
//		if(StringUtils.isBlank(AskleaveIds)){
//			return ResultManager.getFaildResult("请输入需要删除的账号ID，以英文的逗号区分");
//		}
//		Account account = getSessionAccount();
//		askleaveService.deleteAskleave(AskleaveIds,account.getAccountId());
//		return ResultManager.getSuccResult();
//	}

}
