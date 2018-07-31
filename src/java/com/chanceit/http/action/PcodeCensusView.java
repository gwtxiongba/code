/**
 * Copyright (c) 2011-2012 Chanceit Technology Company LTD.
 * All rights reserved.
 * 
 * Created on Jun 26, 2013
 * Id: AccountView.java,v 1.0 Jun 26, 2013 2:28:41 PM Administrator
 */
package com.chanceit.http.action;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.service.IOperatorTeamService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IpcodeCensusService;

/**
 * @ClassName ProstoreMsg
 * @author zhangxin
 * @date 2014-8-18
 * @Description 故障码统计
 */
@Component("pcodeCensusView")
public class PcodeCensusView extends BaseAction {
	
	private static final long serialVersionUID = 1657519070056147590L;

	@Autowired
	@Qualifier("pcodeCensusService")
	private IpcodeCensusService pcodeCensusService;
	
	@Autowired
	@Qualifier("teamService")
	private ITeamService teamService;
	
	@Autowired
	@Qualifier("opTeamService")
	private IOperatorTeamService opTeamService;
	
	public String start() throws JSONException{
		//return pcodeCensusService.getCensusList();
		return "true";
	}
	
	/**
	 * 获取统计列表
	 * @return
	 * @throws JSONException
	 */
	public String getCensusList() throws JSONException{
		String date = getParameter("date");
		return pcodeCensusService.getCensusList(date);
	}
	
	/**
	 * 获取时间戳
	 * @return
	 * @throws JSONException
	 */
	public String getPcodeDates() throws JSONException{
		return pcodeCensusService.getPcodeDates();
	}
	
	/**
	 * 获取检测信息
	 * @return
	 * @throws JSONException
	 */
	public String getDetectionInfo() throws Exception{
		Account account = getSessionAccount();
		String teamIds;
		//操作员获取其权限下车队,管理员获取所有车队
//		if(account.getRole().getRoleId() == 3){
//			teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
//		}else{
			//teamIds = teamService.getTeamIdsByAccountId(account.getAccountId());
//		}
			teamIds = teamService.getTeamIdStr(account);
		//获取车队下所有识别码
		String identifiers = teamService.getIdentifiersByTeamIds(teamIds);
		return pcodeCensusService.getDetectionInfo(identifiers, account);
	}
	
	/**
	 * 获取检测日志
	 * @return
	 * @throws Exception
	 */
	public String getDetectionDiary() throws Exception{
		String date = getParameter("date");
		Account account = getSessionAccount();
		String teamIds;
		//操作员获取其权限下车队,管理员获取所有车队
//		if(account.getRole().getRoleId() == 3){
//			teamIds = opTeamService.getTeamIdsByOperatorId(account.getAccountId() + "");
//		}else{
//			teamIds = teamService.getTeamIdsByAccountId(account.getAccountId());
//		}
		teamIds = teamService.getTeamIdStr(account);
		//获取车队下所有识别码
		String identifiers = teamService.getIdentifiersByTeamIds(teamIds);
		return pcodeCensusService.getDetectionDiary(identifiers, date);
	}
}
