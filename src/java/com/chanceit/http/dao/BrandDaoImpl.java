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
import com.chanceit.http.pojo.Brand;


/**
 * 
 * @author dj
 * @description 几乎包含所有常用的Dao函数，增删改查，分页等等。
 */
@Component("brandDao")
@Repository
public class BrandDaoImpl extends HibernateService implements IBrandDao {
	
	@Override
	public String save(Brand brand) {
		Serializable skey = getSession().save(brand);
		return skey.toString();
	}
	@Override
	public void saveBrand(Brand brand) {
		getSession().saveOrUpdate(brand);
	}
	@Override
	public boolean delete(int id) {
		String hql = "delete from Brand u where u.id = :id";
		Query query = super.getSession().createQuery(hql).setParameter("id",id);  
		int rs = query.executeUpdate();  
		if(rs==0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void delete(Brand brand) {
		getSession().delete(brand);
	}

	@Override
	public void update(Brand brand) {
		getSession().clear();
		getSession().update(brand);
	}

	@Override
	public Brand get(int brandId) {
		return (Brand) getSession().get(Brand.class, brandId);
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
