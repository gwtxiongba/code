/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.exceptions.CommonException;
import com.chanceit.framework.utils.Converter;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.service.IBoxreminderService;

/**
 * @ClassName AccountView
 * @author Administrator
 * @date Jun 26, 2013 2:28:41 PM
 * @Description 账号管理相关接口
 */
@Component("boxWarnView")
public class BoxWarnView extends BaseAction {
	
	@Autowired
	@Qualifier("boxreminderService")
	private IBoxreminderService boxreminderService;
	
	@Autowired
	@Qualifier("converter")
	private Converter converter;
	
	public String getPagelist(Page page){
		String accountId;
		String type = getParameter("type");
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		//type = "2,8";
		if(StringUtils.isBlank(type)){
			return ResultManager.getFaildResult("请选择类型");
		}
		Object[] keywords = new Object[]{accountId, getSessionAccount(),type};
		page = boxreminderService.getPageList(page, keywords);
		
		return ResultManager.getBodyResult(page);
	}
	
	public String getlist(Page page) throws Exception{
		String vehicleId = getParameter("vehicleId");
		String type = getParameter("type");
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		if(StringUtils.isEmpty(vehicleId)) throw new CommonException("车辆ID必须填写");
		if(StringUtils.isBlank(type)){
			return ResultManager.getFaildResult("请选择类型");
		}
		String Date = getParameter("Date");
		Object[] keywords = new Object[]{accountId,vehicleId,Date,super.getSessionAccount().getAccountName(),type};
		return ResultManager.getBodyResult(boxreminderService.getDetaiPagelList(page, keywords));
	}
	
	public String getAlarmCount(){
		String accountId;
		Account account = getSessionAccount();
//		if(account.getRole().getRoleId() != 2){
//			accountId = getCompanyId();
//		}else{
			accountId = account.getAccountId().toString();
//		}
		Object[] keywords = new Object[]{accountId, getSessionAccount()};
		return boxreminderService.getAlarmCount(keywords);
	}
	
	//add by zhangxin 2014-07-14
	/**
	 * @des 监控的车辆列表
	 * @return
	 * @throws Exception
	 */
	public String cmdlist() throws Exception{
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		Date date=new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastTime = fmt.format(new Date(date.getTime() - 24*60*60*1000));
		Object[] keywords = new Object[]{accountId, lastTime};
		return ResultManager.getBodyResult(boxreminderService.getCmdList(account, keywords));
	}

	public String getDateEvent() {
		String accountId;
		Account account = getSessionAccount();
		if(account.getRole().getRoleId() != 2){
			accountId = getCompanyId();
		}else{
			accountId = account.getAccountId().toString();
		}
		String vehicleId = getParameter("vehicleId");
		if(StringUtils.isEmpty(vehicleId)) throw new CommonException("车辆ID必须填写");
		String date = getParameter("date");
		if(StringUtils.isBlank(date)){
			date = DateUtil.parseDate(new Date(), "yyyy-MM-dd");
		}
		Object[] keywords = new Object[]{accountId,vehicleId,date};
		return ResultManager.getBodyResult(boxreminderService.getDateEvent(keywords));
	}

}
