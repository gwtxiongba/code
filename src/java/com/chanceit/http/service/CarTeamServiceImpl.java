package com.chanceit.http.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IBoxreminderDao;
import com.chanceit.http.dao.ICarTeamDao;
import com.chanceit.http.dao.IDeptDao;
import com.chanceit.http.pojo.CarTeam;
import com.chanceit.http.pojo.Dept;


@Transactional
@Component("carTeamService")
public class CarTeamServiceImpl implements ICarTeamService{
	@Autowired
	@Qualifier("carTeamDao")
	private ICarTeamDao carTeamDao;
	@Override
	public CarTeam getDept(int id) {
		// TODO Auto-generated method stub
		//deptDao.get(id);
		return carTeamDao.get(id);
	}

	@Override
	public Page getList(Page page,int teamId) {
		// TODO Auto-generated method stub
		String hql = "from CarTeam d where d.teamId=?";
		Object[] key = new Object[]{teamId};
		//deptDao.getList(hql, key);
		return carTeamDao.getPageList(page, hql, key);
	}
	@Override
	public List getListByTeamId(int teamId) {
		// TODO Auto-generated method stub
		String hql = "from CarTeam d where d.teamId=?";
		Object[] key = new Object[]{teamId};
		//deptDao.getList(hql, key);
		return carTeamDao.getList(hql, key);
	}
	@Override
	public void save(CarTeam dept) {
		// TODO Auto-generated method stub
		//deptDao.save(dept);
		carTeamDao.save(dept);
	}
	@Override
	public void edit(CarTeam dept) {
		// TODO Auto-generated method stub
		//deptDao.save(dept);
		carTeamDao.update(dept);
	}

	@Override
	public void delete(CarTeam dept) {
		// TODO Auto-generated method stub
		carTeamDao.delete(dept);
	}
}
