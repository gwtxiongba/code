package com.chanceit.http.dao;

import java.util.List;
import java.util.Map;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Carstyle;
import com.chanceit.http.pojo.User;

public interface ICarstyleDao {

	public String save(Carstyle carstyle);
	
	public void saveCarstyle(Carstyle carstyle) ;

	public boolean delete(int id);

	public void delete(Carstyle carstyle);

	public void update(Carstyle carstyle);

	public Carstyle get(int carstyleId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	public List getListSql(String sql ,Object... values) ;
	
}