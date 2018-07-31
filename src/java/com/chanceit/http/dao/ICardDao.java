package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Card;


public interface ICardDao {
	public void save(Card card);
	public Page getPageList(Page page, String hql, Object... values);
	public Card get(int cardId);
	public boolean deleteAll(List ids);
	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);
}