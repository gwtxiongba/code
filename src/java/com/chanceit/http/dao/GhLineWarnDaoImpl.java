package com.chanceit.http.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.GhLineWarn;
@Component("ghlineDao")
@Repository
public class GhLineWarnDaoImpl extends HibernateService implements IGhLineWarnDao{

	@Override
	public void add(GhLineWarn glw) {
		// TODO Auto-generated method stub
		getSession().saveOrUpdate(glw);
	}

	@Override
	public void del(GhLineWarn glw) {
		// TODO Auto-generated method stub
		getSession().delete(glw);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String sql ,Object... values) {
		return super.findSQLMapPage3(page, sql,values);
	}
	@Override
	public void update(String sql){
		excuteSQL(sql);
	}
	@Override
	public List getCount(String sql){
		return getSqlList(sql);
	}
	@Override
	public List getLimiList(String sql){
		return getSqlList(sql);
	}
	@Override
	public List getWarnList(String sql){
		return findSQLList(sql);
	}
}
