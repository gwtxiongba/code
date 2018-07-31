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
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Askleave;
import com.chanceit.http.pojo.Weizhang;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IAskleaveService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IWeizhangService;

/**
 * @ClassName AccountView
 * @author zhangxin 2014-08-13
 * @Description 账号管理相关接口
 */
@Component("weizhangView")
public class WeizhangView extends BaseAction {
	@Autowired
	@Qualifier("weizhangService")
	private IWeizhangService weizhangService;
	
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
//		String ss_name=getParameter("ss_name");//搜索 ：维修人
		String ss_plate=getParameter("ss_plate");//搜索：车牌
		int teamId=getSessionAccount().getTeam().getTeamId();
		Object[] keywords = new Object[]{teamId,ss_plate};
		page = weizhangService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}

	
	public String saveWeizhang() throws Exception{
		String weizhangId=getParameter("id");
		String carId=getParameter("car_id");
		String foul_address=getParameter("foul_address");
		String foul_reason=getParameter("foul_reason");
		String foul_mark=getParameter("foul_mark");
		String foul_price=getParameter("foul_price");
		String driver_id=getParameter("driver_id");
		String foulTime_s=getParameter("foul_time");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date foul_time=dateFormat.parse(foulTime_s);
		
		if(StringUtils.isBlank(weizhangId)){//新增
			Weizhang weizhang=new Weizhang();
			weizhang.setCarId(Integer.parseInt(carId));
			weizhang.setDriverId(Integer.parseInt(driver_id));
			weizhang.setFourTime(foul_time);
			weizhang.setFourAddress(foul_address);
			weizhang.setFourReason(foul_reason);
			weizhang.setFourMark(Integer.parseInt(foul_mark));
			weizhang.setFourPrice(Double.parseDouble(foul_price));
			weizhangService.saveWeizhang(weizhang);
		}
		else{//修改
			Weizhang weizhang=weizhangService.get(Integer.parseInt(weizhangId));
			weizhang.setCarId(Integer.parseInt(carId));
			weizhang.setDriverId(Integer.parseInt(driver_id));
			weizhang.setFourTime(foul_time);
			weizhang.setFourAddress(foul_address);
			weizhang.setFourReason(foul_reason);
			weizhang.setFourMark(Integer.parseInt(foul_mark));
			weizhang.setFourPrice(Double.parseDouble(foul_price));
			weizhangService.saveWeizhang(weizhang);
		}
		
		return ResultManager.getSuccResult();
		
	}
	
	
	public String delete() throws Exception{
		String weizhangIds = getParameter("id");
		if(StringUtils.isBlank(weizhangIds)){
			return ResultManager.getFaildResult("请输入需要删除的账号ID，以英文的逗号区分");
		}
		weizhangService.deleteWeizhang(Integer.parseInt(weizhangIds));
		return ResultManager.getSuccResult();
	}

}
