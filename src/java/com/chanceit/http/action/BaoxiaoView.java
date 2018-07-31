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
import com.chanceit.framework.utils.DateUtil;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Baoxiao;
import com.chanceit.http.service.IAccountService;
import com.chanceit.http.service.IBaoxiaoService;
import com.chanceit.http.service.ITeamService;

@Component("baoxiaoView")
public class BaoxiaoView extends BaseAction {
	@Autowired
	@Qualifier("baoxiaoService")
	private IBaoxiaoService baoxiaoService;



	/**
	 * @Fields serialVersionUID aaa
	 */
	private static final long serialVersionUID = -2658614164790204780L;

	public String list(Page page) {
		// String baoxiaoName = getParameter("baoxiaoName");
		String status = getParameter("status");// 0待审批报销 1报销列表
		String ss_name=getParameter("ss_name");//搜索 ：报销人姓名
		if (Integer.parseInt(status) == 0) {
			status = "0";
		} else
			status = "1,-1";

		String accountId;
		Account account = getSessionAccount();
		accountId = account.getAccountId().toString();

		Object[] keywords = new Object[] { status,
				account.getTeam().getTeamId(),ss_name };
		page = baoxiaoService.getPageList(page, keywords);
		return ResultManager.getBodyResult(page);
	}

	public String addBaoxiao() throws Exception {
		//String teamId = getParameter("teamId");
		int teamId = getSessionAccount().getTeam().getTeamId();
		String userId = getParameter("userId");
		String carId = getParameter("carId");
		String time = getParameter("time") + ":00";
		String jy = getParameter("jy");
		String xc = getParameter("xc");
		String by = getParameter("by");
		String wx = getParameter("wx");
		String lq = getParameter("lq");
		String tc = getParameter("tc");
		String nj = getParameter("nj");
		String bx = getParameter("bx");
		String qt = getParameter("qt");
		String gl = getParameter("gl");
		if (StringUtils.isBlank(jy)) {
			jy = "0";
		}
		if (StringUtils.isBlank(xc)) {
			xc = "0";
		}
		if (StringUtils.isBlank(by)) {
			by = "0";
		}
		if (StringUtils.isBlank(wx)) {
			wx = "0";
		}
		if (StringUtils.isBlank(lq)) {
			lq = "0";
		}
		if (StringUtils.isBlank(tc)) {
			tc = "0";
		}
		if (StringUtils.isBlank(nj)) {
			nj = "0";
		}
		if (StringUtils.isBlank(bx)) {
			bx = "0";
		}
		if (StringUtils.isBlank(qt)) {
			qt = "0";
		}
		if (StringUtils.isBlank(gl)) {
			gl = "0";
		}
		Baoxiao baoxiao = new Baoxiao();
		baoxiao.setTime(DateUtil.getDate(time));
		baoxiao.setTeamId(teamId);
		baoxiao.setUserId(Integer.parseInt(userId));
		baoxiao.setCarId(Integer.parseInt(carId));
		baoxiao.setJyf(Double.parseDouble(jy));
		baoxiao.setXcf(Double.parseDouble(xc));
		baoxiao.setByf(Double.parseDouble(by));
		baoxiao.setWxf(Double.parseDouble(wx));
		baoxiao.setLqf(Double.parseDouble(lq));
		baoxiao.setTcf(Double.parseDouble(tc));
		baoxiao.setNjf(Double.parseDouble(nj));
		baoxiao.setBxf(Double.parseDouble(bx));
		baoxiao.setQtf(Double.parseDouble(qt));
		baoxiao.setGlf(Double.parseDouble(gl));
		baoxiao.setStatus(1);// 设置状态为通过审批

		baoxiaoService.save(baoxiao);

		return ResultManager.getSuccResult();
	}

	public String auditBaoxiao() throws Exception {
		String id = getParameter("id");
		String type = getParameter("type");
		String info2 = getParameter("info2");
		Baoxiao baoxiao = baoxiaoService.getBaoxiao(Integer.parseInt(id));
		if ("deal".equals(type)) {
			baoxiao.setStatus(1);// 同意
			baoxiao.setInfo2(info2);
		} else if ("refuse".equals(type)) {
			baoxiao.setStatus(-1);// 拒绝
			baoxiao.setInfo2(info2);
		}
		baoxiaoService.update(baoxiao);

		return ResultManager.getSuccResult();

	}


}
