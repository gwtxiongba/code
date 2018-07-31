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
import com.chanceit.http.pojo.Caruses;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("carusesDao")
@Repository
public class CarusesDaoImpl extends HibernateService implements ICarusesDao {
	
	@Override
	public String save(Caruses caruses) {
		Serializable skey = getSession().save(caruses);
		return skey.toString();
	}
	@Override
	public void saveCaruses(Caruses caruses) {
		getSession().saveOrUpdate(caruses);
	}
	@Override
	public boolean delete(int id) {
		String hql = "delete from Caruses u where u.id = :id";
		Query query = super.getSession().createQuery(hql).setParameter("id",id);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Caruses caruses) {
		getSession().delete(caruses);
	}

	@Override
	public void update(Caruses caruses) {
		getSession().clear();
		getSession().update(caruses);
	}

	@Override
	public Caruses get(int carusesId) {
		return (Caruses) getSession().get(Caruses.class, carusesId);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List getList(String hql,Object[] objs) {
		return super.find(hql,objs);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Page getPageList(Page page , String hql ,Object... values) {
		return super.findListPage(page, hql,values);
	}
	
	
	@Override
	public List getListSql(String sql ,Object... values) {
		return super.findSQL(sql, values);
	}
	
}
