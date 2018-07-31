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
import com.chanceit.http.pojo.Carstyle;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("carstyleDao")
@Repository
public class CarstyleDaoImpl extends HibernateService implements ICarstyleDao {
	
	@Override
	public String save(Carstyle carstyle) {
		Serializable skey = getSession().save(carstyle);
		return skey.toString();
	}
	@Override
	public void saveCarstyle(Carstyle carstyle) {
		getSession().saveOrUpdate(carstyle);
	}
	@Override
	public boolean delete(int id) {
		String hql = "delete from Carstyle u where u.id = :id";
		Query query = super.getSession().createQuery(hql).setParameter("id",id);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Carstyle carstyle) {
		getSession().delete(carstyle);
	}

	@Override
	public void update(Carstyle carstyle) {
		getSession().clear();
		getSession().update(carstyle);
	}

	@Override
	public Carstyle get(int carstyleId) {
		return (Carstyle) getSession().get(Carstyle.class, carstyleId);
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
