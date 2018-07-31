package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Team;


public interface ITeamDao {

	public String save(Team team);

	public boolean delete(List ids,int accountId);

	public void delete(Team team);

	public void update(Team team);

	public Team get(int teamId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);
	public boolean fakeDelete(List ids,int accountId);

	public void saveTeam(Team team);
	public List getSqlObj(String sql ,Class<?> ct); 
	
}