package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.PreStoreMessage;


public interface IpreStoreMsgDao {
	public Page getMsgList(Page page, Object... values);
	public boolean delete(List ids);
	public void save(PreStoreMessage preStoreMessage);
}