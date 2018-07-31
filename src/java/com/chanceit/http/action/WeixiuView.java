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
import com.chanceit.http.pojo.Weixiu;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IAskleaveService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IWeixiuService;

/**
 * @ClassName AccountView
 * @author zhangxin 2014-08-13
 * @Description 账号管理相关接口
 */
@Component("weixiuView")
public class WeixiuView extends BaseAction {
	@Autowired
	@Qualifier("weixiuService")
	private IWeixiuService weixiuService;
	
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
		//String weixiuName = getParameter("weixiuName");
		String status=getParameter("status");//0待审批维修  1维修列表
		String ss_name=getParameter("ss_name");//搜索 ：维修人
		String ss_plate=getParameter("ss_plate");//搜索：车牌
		if(Integer.parseInt(status)==0){
			status="0";
		}else
			status="1,-1";
		
		Account account = getSessionAccount();
//		Object[] keywords = new Object[]{accountIDs,weixiuName};
		Object[] keywords = new Object[]{status,account.getTeam().getTeamId(),ss_name,ss_plate};
		page = weixiuService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}

	
	public String auditWeixiu() throws Exception{
		String id=getParameter("id");
		String type=getParameter("type");
		String reason2=getParameter("reason2");
		Weixiu weixiu=weixiuService.getWeixiu(Integer.parseInt(id));
		if("deal".equals(type)){
			weixiu.setStatus(1);//同意
		}else if("refuse".equals(type)){
			weixiu.setStatus(-1);//拒绝
			if(StringUtils.isNotBlank(reason2)){
				weixiu.setReason2(reason2);
			}
		}
		weixiuService.saveWeixiu(weixiu);
		
		return ResultManager.getSuccResult();
		
	}
	
	
//	public String delete() throws Exception{
//		String AskleaveIds = getParameter("weixiuIds");
//		if(StringUtils.isBlank(AskleaveIds)){
//			return ResultManager.getFaildResult("请输入需要删除的账号ID，以英文的逗号区分");
//		}
//		Account account = getSessionAccount();
//		weixiuService.deleteAskleave(AskleaveIds,account.getAccountId());
//		return ResultManager.getSuccResult();
//	}

}
