package com.chanceit.http.service;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Message;

public interface IMessageCenterService {
	public Page getMsgList(Page page, Object[] values);
	public Page getMsgInforList(Page page,Object[] values);
	public boolean save(List<Message> msgList);
	public boolean save(Message msg);
}