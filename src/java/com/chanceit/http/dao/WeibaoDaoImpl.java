package com.chanceit.http.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.chanceit.framework.utils.HibernateService;
import com.chanceit.framework.utils.Page;
import com.chanceit.http.pojo.Weibao;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("weibaoDao")
@Repository
public class WeibaoDaoImpl extends HibernateService implements IWeibaoDao {
	
	@Override
	public String save(Weibao weibao) {
		Serializable skey = getSession().save(weibao);
		return skey.toString();
	}
	@Override
	public void saveWeibao(Weibao weibao) {
		getSession().saveOrUpdate(weibao);
	}
	@Override
	public boolean delete(int id) {
		String hql = "delete from Weibao u where u.id = :id";
		Query query = super.getSession().createQuery(hql).setParameter("id",id);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Weibao weibao) {
		getSession().delete(weibao);
	}

	@Override
	public void update(Weibao weibao) {
		getSession().clear();
		getSession().update(weibao);
	}

	@Override
	public Weibao get(int weibaoId) {
		return (Weibao) getSession().get(Weibao.class, weibaoId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findSQLMapPage(page, hql,values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	
}
