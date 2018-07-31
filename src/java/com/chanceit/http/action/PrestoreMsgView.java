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
import com.chanceit.framework.enums.EnumCommon;
import com.chanceit.framework.utils.Page;
import com.chanceit.framework.utils.ResultManager;
import com.chanceit.http.pojo.PreStoreMessage;
import com.chanceit.http.service.IpreStoreMsgService;

/**
 * @ClassName ProstoreMsg
 * @author zhangxin
 * @date 2014-6-27
 * @Description 预存消息相关接口
 */
@Component("prestoreMsgView")
public class PrestoreMsgView extends BaseAction {
	
	private static final long serialVersionUID = 1657519070056147590L;

	@Autowired
	@Qualifier("preStoreMsgService")
	private IpreStoreMsgService preStoreMsgService;
	
	/**
	 * 保存预存信息对象
	 * @throws Exception
	 */
	public String save() throws Exception {
		String accountId = getCompanyId();
		String msgContent = getParameter("preMsgContent");
		//组装预存消息对象并保存
		PreStoreMessage psMsg = new PreStoreMessage();
		psMsg.setPreMsgContent(msgContent);
		if(accountId != null && !"".equals(accountId)){
			psMsg.setAccountId(Integer.parseInt(accountId));
		}
		boolean success = preStoreMsgService.save(psMsg);
		if(success){
			return ResultManager.getSuccResult();
		}else{
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "系统异常！");
		}
	}
	
	/**
	 * 通过accountId查询预存信息列表
	 * @return 预存信息列表
	 */
	public String list(Page page) {
		String accountId = getCompanyId();
		Object[] keywords = new Object[]{accountId};
		page = preStoreMsgService.getMsgList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	/**
	 * 删除预存信息
	 * @throws Exception
	 */
	public String delete() throws Exception{
		String preMsgIds = getParameter("preMsgIds");
		if(StringUtils.isBlank(preMsgIds)){
			return ResultManager.getFaildResult("请输入需要删除的账号ID，以英文的逗号区分");
		}
		preStoreMsgService.deleteMsg(preMsgIds);
		return ResultManager.getSuccResult();
	}
}
