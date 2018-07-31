package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Boxreminder;

public interface IBoxreminderDao {

	public void save(Boxreminder Boxreminder);

	public boolean delete(List ids);

	public void delete(Boxreminder boxreminder);

	public void update(Boxreminder boxreminder);

	public Boxreminder get(int warnId);
	
	public List getMapList(String hql,Object... objs) ;

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	public boolean update(String hql);
	public Page getPageList2(Page page , String hql ,Object... values) ;
	public List getListSql(String sql ,Object... values);
	public Page getListPage(Page page , String hql ,Object... values);
	
	//add by zhangxin 2014-07-15
	public List getCmdList(String sql);

}