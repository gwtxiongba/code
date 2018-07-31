package com.chanceit.http.dao;

import java.util.List;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.CarTeam;
import com.chanceit.http.pojo.Dept;


public interface ICarTeamDao {

	public void save(CarTeam carTeam);

	public boolean delete(List ids);

	public void delete(CarTeam carTeam);

	public void update(CarTeam carTeam);

	public CarTeam get(int levelId);

	@SuppressWarnings("unchecked")
	public List getList(String hql, Object[] objs);

	@SuppressWarnings("unchecked")
	public List getMapList(String hql, Object... objs);

	@SuppressWarnings("unchecked")
	public Page getPageList(Page page, String hql, Object... values);

	public List getListSql(String sql, Object... values);

}