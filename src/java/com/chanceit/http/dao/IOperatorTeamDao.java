package com.chanceit.http.dao;

import java.util.List;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.OperatorTeam;
import com.chanceit.http.pojo.Team;


public interface IOperatorTeamDao {

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

	public void save(OperatorTeam opTeam);
	public Account getAccount(int operatorId);
	public void delete(int operatorId);
	public void deleteByTeam(int teamId);
	
	public boolean bindTeam(List ids,int operatorId);
	public boolean unBindTeam(List ids,int operatorId);
	
	public String getTeamIdsByOperatorId(String operatorIds);


	
}