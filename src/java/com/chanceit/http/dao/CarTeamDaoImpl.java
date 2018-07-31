package com.chanceit.http.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.CarTeam;
import com.chanceit.http.pojo.Dept;
@Component("carTeamDao")
@Repository
public class CarTeamDaoImpl extends HibernateService implements ICarTeamDao{

	@Override
	public boolean delete(List ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(CarTeam dept) {
		// TODO Auto-generated method stub
		getSession().delete(dept);
	}

	@Override
	public CarTeam get(int id) {
		// TODO Auto-generated method stub
		return (CarTeam) getSession().get(CarTeam.class, id);
		//return null;
	}

	@Override
	public List getList(String hql, Object[] objs) {
		// TODO Auto-generated method stub
		return super.find(hql,objs);
	}

	@Override
	public List getListSql(String sql, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getMapList(String hql, Object... objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getPageList(Page page, String hql, Object... values) {
		// TODO Auto-generated method stub
		return super.findListPage(page, hql,values);
	}

	@Override
	public void save(CarTeam dept) {
		// TODO Auto-generated method stub
		 getSession().saveOrUpdate(dept);
		
	}

	@Override
	public void update(CarTeam dept) {
		// TODO Auto-generated method stub
		
	}

}
