package com.chanceit.http.dao;

import java.util.List;
import java.util.Map;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Brand;
import com.chanceit.http.pojo.User;

public interface IBrandDao {

	public String save(Brand brand);
	
	public void saveBrand(Brand brand) ;

	public boolean delete(int id);

	public void delete(Brand brand);

	public void update(Brand brand);

	public Brand get(int brandId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);
	
	public List getListSql(String sql ,Object... values) ;
	
}