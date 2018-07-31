package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.BaseInfo;

public interface ICarBaseInfoDao {
	public void save(BaseInfo baseInfo);

	public boolean delete(List ids,int baseInfo);

	public void delete(BaseInfo baseInfo);

	public void update(BaseInfo baseInfo);

	public BaseInfo get(int baseInfoId);
	
	public Page getPageList(Page page, String hql, Object... values);
	
	public List getListSql(String sql ,Object... values);
	
	public boolean updateUser(int userId,int baseId);
	
	public List getList(String hql,Object[] objs);
	
}
