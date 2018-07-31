package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.CarDriver;


public interface ICarDriverDao {
	public void save(CarDriver carDriver);
	public Page getPageList(Page page , String hql ,Object... values);
	public CarDriver get(int carDriverId);
	public boolean deleteAll(List ids);
	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);
	public List getByDriverId(String sql,Object[] values);
}