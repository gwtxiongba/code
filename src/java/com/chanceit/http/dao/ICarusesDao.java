package com.chanceit.http.dao;

import java.util.List;
import java.util.Map;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Caruses;
import com.chanceit.http.pojo.User;

public interface ICarusesDao {

	public String save(Caruses caruses);
	
	public void saveCaruses(Caruses caruses) ;

	public boolean delete(int id);

	public void delete(Caruses caruses);

	public void update(Caruses caruses);

	public Caruses get(int carusesId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	public List getListSql(String sql ,Object... values) ;
	
}