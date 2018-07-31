package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Warn;

public interface IWarnDao {

	public void save(Warn work);

	public boolean delete(List ids);

	public void delete(Warn work);

	public void update(Warn work);

	public Warn get(int workId);
	
	public List getMapList(String hql,Object... objs) ;

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	public boolean update(String hql);
	public Page getPageList2(Page page , String hql ,Object... values) ;
	public List getListSql(String sql ,Object... values);
	public Page getListPage(Page page , String hql ,Object... values);

}