package com.chanceit.http.service;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.PreStoreMessage;

public interface IpreStoreMsgService {
	public Page getMsgList(Page page, Object[] values);
	public boolean save(PreStoreMessage preStoreMessage);
	public PreStoreMessage getPsMsg(int psMsgId);
	public boolean deleteMsg(String psMsgIds) ;
}