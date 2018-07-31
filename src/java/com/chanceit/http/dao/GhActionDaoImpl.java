package com.chanceit.http.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.http.pojo.GhAction;
@Component("ghactionDao")
@Repository
public class GhActionDaoImpl extends HibernateService implements IGhActionDao{

	@Override
	public void add(GhAction action) {
		// TODO Auto-generated method stub
		getSession().saveOrUpdate(action);
	}

	@Override
	public void del(GhAction action) {
		// TODO Auto-generated method stub
		getSession().delete(action);
	}

	@Override
	public void del(int pid) {
		// TODO Auto-generated method stub
		String sql = "delete from ghaction where pid="+pid;
		excuteSQL(sql);
		
	}
	@Override
	public List<GhAction> getById(int id){
		String hql = " from GhAction where id=?";
		List<GhAction> list = find(hql, new Object[]{id});
		return list;
	}
	
	@Override
	public List<GhAction> getByUidAndPid(int pid){
		String hql = " from GhAction where pid=?";
		List<GhAction> list = find(hql, new Object[]{pid});
		return list;
	}
	@Override
	public void save(String sql){
		excuteSQL(sql);
	}

}
