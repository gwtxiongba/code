package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Menu;


public interface IMenuDao {

	public String save(Menu team);

	public boolean delete(List ids);

	public void delete(Menu team);

	public void update(Menu team);

	public Menu get(int menuId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);
	public void saveTeam(Menu team);
	public List getSqlObj(String sql ,Class<?> ct); 
	
}