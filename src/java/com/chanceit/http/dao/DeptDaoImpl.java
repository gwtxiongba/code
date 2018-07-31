package com.chanceit.http.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Account;
import com.chanceit.http.pojo.Dept;
@Component("deptDao")
@Repository
public class DeptDaoImpl extends HibernateService implements IDeptDao{

	@Override
	public boolean delete(List ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(Dept dept) {
		// TODO Auto-generated method stub
		getSession().delete(dept);
	}

	@Override
	public Dept get(int id) {
		// TODO Auto-generated method stub
		return (Dept) getSession().get(Dept.class, id);
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
	public void save(Dept dept) {
		// TODO Auto-generated method stub
		 getSession().saveOrUpdate(dept);
		
	}

	@Override
	public void update(Dept dept) {
		// TODO Auto-generated method stub
		
	}

}
