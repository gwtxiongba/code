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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.chanceit.framework.action.BaseAction;
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Askleave;
import com.chanceit.http.pojo.Weibao;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IAskleaveService;
import com.chanceit.http.service.ITeamService;
import com.chanceit.http.service.IWeibaoService;

/**
 * @ClassName AccountView
 * @author zhangxin 2014-08-13
 * @Description �˺Ź�����ؽӿ�
 */
@Component("weibaoView")
public class WeibaoView extends BaseAction {
	@Autowired
	@Qualifier("weibaoService")
	private IWeibaoService weibaoService;
	
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
//		String ss_name=getParameter("ss_name");//���� ��ά����
		String ss_plate=getParameter("ss_plate");//����������
		String flag = getParameter("flag");//����������
		int teamId=getSessionAccount().getTeam().getTeamId();
		Object[] keywords = new Object[]{teamId,ss_plate,flag};
		page = weibaoService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}

	
	public String saveWeibao() throws Exception{
		String weibaoId=getParameter("id");
		String carId=getParameter("car_id");
		String yearTime_s=getParameter("year_time");
		String keepTime_s=getParameter("keep_time");
		String safeTime_s=getParameter("safe_time");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date yearTime=dateFormat.parse(yearTime_s);
		Date keepTime=dateFormat.parse(keepTime_s);
		Date safeTime=dateFormat.parse(safeTime_s);
		
		if(StringUtils.isBlank(weibaoId)){//����
			Weibao weibao=new Weibao();
			weibao.setCarId(Integer.parseInt(carId));
			weibao.setYearTime(yearTime);
			weibao.setKeepTime(keepTime);
			weibao.setSafeTime(safeTime);
			weibaoService.saveWeibao(weibao);
		}
		else{//�޸�
			Weibao weibao=weibaoService.get(Integer.parseInt(weibaoId));
			weibao.setCarId(Integer.parseInt(carId));
			weibao.setYearTime(yearTime);
			weibao.setKeepTime(keepTime);
			weibao.setSafeTime(safeTime);
			weibaoService.saveWeibao(weibao);
		}
		
		return ResultManager.getSuccResult();
		
	}
	
	
	public String delete() throws Exception{
		String weibaoIds = getParameter("id");
		if(StringUtils.isBlank(weibaoIds)){
			return ResultManager.getFaildResult("��������Ҫɾ�����˺�ID����Ӣ�ĵĶ�������");
		}
		weibaoService.deleteWeibao(Integer.parseInt(weibaoIds));
		return ResultManager.getSuccResult();
	}
	
	public String getCount(){
		String ss_plate=getParameter("ss_plate");//����������
		String flag = getParameter("flag");//����������
		int teamId=getSessionAccount().getTeam().getTeamId();
		Object[] keywords = new Object[]{teamId,ss_plate,"1"};
		List list = weibaoService.getListEnd(keywords);
		JSONObject json = new JSONObject();
		try {
			json.put("total", list.size());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}
	

}
