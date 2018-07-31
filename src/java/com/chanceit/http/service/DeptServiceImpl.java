package com.chanceit.http.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IBoxreminderDao;
import com.chanceit.http.dao.IDeptDao;
import com.chanceit.http.pojo.Dept;


@Transactional
@Component("deptService")
public class DeptServiceImpl implements IDeptService{
	@Autowired
	@Qualifier("deptDao")
	private IDeptDao deptDao;
	@Override
	public Dept getDept(int id) {
		// TODO Auto-generated method stub
		//deptDao.get(id);
		return deptDao.get(id);
	}

	@Override
	public Page getList(Page page,int teamId) {
		// TODO Auto-generated method stub
		String hql = "from Dept d where d.teamId=?";
		Object[] key = new Object[]{teamId};
		//deptDao.getList(hql, key);
		return deptDao.getPageList(page, hql, key);
	}
	@Override
	public List getListByTeamId(int teamId) {
		// TODO Auto-generated method stub
		String hql = "from Dept d where d.teamId=?";
		Object[] key = new Object[]{teamId};
		//deptDao.getList(hql, key);
		return deptDao.getList(hql, key);
	}
	@Override
	public void save(Dept dept) {
		// TODO Auto-generated method stub
		//deptDao.save(dept);
		 deptDao.save(dept);
	}
	@Override
	public void edit(Dept dept) {
		// TODO Auto-generated method stub
		//deptDao.save(dept);
		 deptDao.update(dept);
	}

	@Override
	public void delete(Dept dept) {
		// TODO Auto-generated method stub
		deptDao.delete(dept);
	}
}
