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
 * @Description Ԥ����Ϣ��ؽӿ�
 */
@Component("prestoreMsgView")
public class PrestoreMsgView extends BaseAction {
	
	private static final long serialVersionUID = 1657519070056147590L;

	@Autowired
	@Qualifier("preStoreMsgService")
	private IpreStoreMsgService preStoreMsgService;
	
	/**
	 * ����Ԥ����Ϣ����
	 * @throws Exception
	 */
	public String save() throws Exception {
		String accountId = getCompanyId();
		String msgContent = getParameter("preMsgContent");
		//��װԤ����Ϣ���󲢱���
		PreStoreMessage psMsg = new PreStoreMessage();
		psMsg.setPreMsgContent(msgContent);
		if(accountId != null && !"".equals(accountId)){
			psMsg.setAccountId(Integer.parseInt(accountId));
		}
		boolean success = preStoreMsgService.save(psMsg);
		if(success){
			return ResultManager.getSuccResult();
		}else{
			return ResultManager.getFaildResult(EnumCommon.EXCEPTION_SYS, "ϵͳ�쳣��");
		}
	}
	
	/**
	 * ͨ��accountId��ѯԤ����Ϣ�б�
	 * @return Ԥ����Ϣ�б�
	 */
	public String list(Page page) {
		String accountId = getCompanyId();
		Object[] keywords = new Object[]{accountId};
		page = preStoreMsgService.getMsgList(page, keywords);
		return ResultManager.getBodyResult(page);
	}
	
	/**
	 * ɾ��Ԥ����Ϣ
	 * @throws Exception
	 */
	public String delete() throws Exception{
		String preMsgIds = getParameter("preMsgIds");
		if(StringUtils.isBlank(preMsgIds)){
			return ResultManager.getFaildResult("��������Ҫɾ�����˺�ID����Ӣ�ĵĶ�������");
		}
		preStoreMsgService.deleteMsg(preMsgIds);
		return ResultManager.getSuccResult();
	}
}
