package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.MessageStats;

public interface IMessageStatsDao {

	public String save(MessageStats MessageStats);

	public boolean delete(List ids);

	public void delete(MessageStats MessageStats);

	public void update(MessageStats MessageStats);

	public MessageStats get(String id);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	

}