package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Driver;


public interface IDriverDao {

	public String save(Driver driver);

	public boolean delete(List ids);

	public void delete(Driver driver);

	public void update(Driver driver);

	public Driver get(int driverId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);
	public boolean fakeDelete(List ids,int accountId);

	public void saveDriver(Driver driver);
	public List getExistNameList(String loginName);
	public void updateDriver(String hql);
	public boolean  unBindDriver(String sql) ;
	public boolean resetDriver(List ids);
}