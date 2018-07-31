 package com.chanceit.http.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.service.IWarnService;

@SuppressWarnings("serial")
@Component("warnView")
public class WarnView extends BaseAction{
	@Autowired
	@Qualifier("warnService")
	private IWarnService warnService;
	
	public String getPagelist(Page page){
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{ 
			accountId = account.getAccountId().toString();
		}
		Object[] keywords = new Object[]{accountId, getSessionAccount()};
		page = warnService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	public String getlist(Page page) throws Exception{
		String vehicleId = getParameter("vehicleId");
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		if(StringUtils.isEmpty(vehicleId)) throw new CommonException("³µÁ¾ID±ØÐëÌîÐ´");
		String Date = getParameter("Date");
		Object[] keywords = new Object[]{accountId,vehicleId,Date,super.getSessionAccount().getAccountName()};
		return ResultManager.getBodyResult(warnService.getDetaiPagelList(page, keywords));
	}
	
	public String getAlarmCount(){
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		Object[] keywords = new Object[]{accountId, getSessionAccount()};
		return warnService.getAlarmCount(keywords);
	}

}
