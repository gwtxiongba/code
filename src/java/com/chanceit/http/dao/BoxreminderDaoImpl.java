package com.chanceit.http.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.dao.IBoxreminderDao;
import com.chanceit.http.pojo.Boxreminder;

@Component("boxreminderDao")
@Repository
public class BoxreminderDaoImpl extends HibernateService implements IBoxreminderDao{
	
	@Override
	public void save(Boxreminder boxreminder) {
		getSession().save(boxreminder);
	}
	
	@Override
	public boolean delete(List ids) {
		String hql = "delete from Boxreminder where warnId in (:ids)";
		Query query = super.getSession().createQuery(hql).setParameterList("ids",ids);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean update(String hql) {
		Query query = super.getSession().createQuery(hql);
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Boxreminder boxreminder) {
		getSession().delete(boxreminder);
	}

	@Override
	public void update(Boxreminder boxreminder) {
		getSession().clear();
		getSession().update(boxreminder);
	}

	@Override
	public Boxreminder get(int warnId) {
		return (Boxreminder) getSession().get(Boxreminder.class, warnId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getMapList(String hql,Object... objs) {
		Query  query = super.getQuery(hql, objs);
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	@Override
	public List getList(String hql,Object... objs) {
		Query  query = super.getQuery(hql, objs);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findMapPage(page, hql,values);
	}
	
	@Override
	public Page getPageList2(Page page , String sql ,Object... values) {
		return super.findSQLMapPage2(page, sql, values);
	}
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	
	@Override
	public Page getListPage(Page page , String hql ,Object... values) {
		return super.findListPage(page, hql,values);
	}
	
	//add by zhangxin 2014-07-15
	@Override
	public List getCmdList(String sql) {
		return super.findSQL(sql);
	}
}
